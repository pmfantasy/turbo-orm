package com.fantasy.app.core.base;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.ColumnMapRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ParameterizedPreparedStatementSetter;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SingleColumnRowMapper;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.SqlParameterValue;
import org.springframework.jdbc.core.SqlTypeValue;
import org.springframework.jdbc.core.StatementCreatorUtils;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.object.GenericStoredProcedure;
import org.springframework.jdbc.support.lob.LobHandler;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.util.StringUtils;

import com.fantasy.app.core.annotation.WsdColumn;
import com.fantasy.app.core.annotation.WsdNotDbColumn;
import com.fantasy.app.core.base.log.LogType;
import com.fantasy.app.core.component.db.DateValue;
import com.fantasy.app.core.component.db.DbTypeEnum;
import com.fantasy.app.core.component.db.MysqlFeature;
import com.fantasy.app.core.component.db.OracleFeature;
import com.fantasy.app.core.component.db.PageDataHandler;
import com.fantasy.app.core.component.db.PostgreSQLFeature;
import com.fantasy.app.core.component.db.SingleTransationCircleWithOutResult;
import com.fantasy.app.core.component.db.SingleTransationCircleWithResult;
import com.fantasy.app.core.component.db.jdbcpool.JdbcPoolHandler;
import com.fantasy.app.core.component.db.orm.BatchSqlArg;
import com.fantasy.app.core.component.db.orm.ModuleDmlCreator;
import com.fantasy.app.core.component.db.orm.ModuleFeatureBeanRowWapper;
import com.fantasy.app.core.component.db.orm.SqlArg;
import com.fantasy.app.core.component.db.orm.SqlCreator;
import com.fantasy.app.core.component.db.orm.WhereCondition;
import com.fantasy.app.core.component.db.plsql.In;
import com.fantasy.app.core.component.db.plsql.Out;
import com.fantasy.app.core.component.db.tablesharding.TableShardingParser;
import com.fantasy.app.core.component.log.Log;
import com.fantasy.app.core.component.pager.Pager;
import com.fantasy.app.core.component.pager.PagerData;
import com.fantasy.app.core.component.pager.Pg;
import com.fantasy.app.core.exception.DaoException;
import com.fantasy.app.core.para.CorePara.CoreInitCtx;
import com.fantasy.app.core.util.CollectionUtil;
import com.fantasy.app.core.util.StrUtil;
import com.fantasy.app.core.util.SystemOutCollectionUtil;

/**
 * 通用数据库操作类
 *
 * @日期：2013-6-6下午7:37:40
 * @作者：公众号：18岁fantasy
 */
public abstract class Dao implements JdbcPoolHandler {

    public Logger logger = Log.getLogger(LogType.DB);

    private static final String DEFAULT_DESC = "数据库操作";
    private static final String ERROR_SUFFIX = " 错误";
    private static final String SUCCESS_SUFFIX = " 成功";
    private static final String DEFAULT_SUCCESS_DESC = DEFAULT_DESC
            + SUCCESS_SUFFIX;
    private static final String DEFAULT_ERROR_DESC = DEFAULT_DESC
            + ERROR_SUFFIX;

    private JdbcTemplate jdbcTemplate;
    private LobHandler lobHandler;
    private DataSourceTransactionManager transactionManager;
    private ModuleDmlCreator moduleDmlCreator;

    public DataSource getDataSource() {
        return jdbcTemplate.getDataSource();
    }

    private int dml(String desc, String sql, Object[] args)
            throws DaoException {
        try {
            args = warpSqlArgType(args);
            printSql(desc, sql, args);
            int num = 0;
            num = jdbcTemplate.update(sql, args);
            logger.info(getSuccessDesc(desc) + " 操作记录数" + num);
            return num;
        } catch (Exception e) {
            String errorDesc = getErrorDesc(desc);
            logger.error(errorDesc, e);
            throw new DaoException(errorDesc, e);
        }
    }

    public String[] getJdbcProperties() {
        return null;//jdbcTemplate.getDataSource().getConnection().getMetaData()
    }

    /**
     * 执行ddl eg：create drop truncat alter ...
     *
     * @param desc
     * @param sql  ddl 语句
     * @throws DaoException
     */
    public void ddl(String desc, String sql) throws DaoException {
        try {
            printSql(desc, sql, new Object[]{});
            jdbcTemplate.execute(sql);
            logger.info(getSuccessDesc(desc));
        } catch (Exception e) {
            String errorDesc = getErrorDesc(desc);
            logger.error(errorDesc, e);
            throw new DaoException(errorDesc, e);
        }
    }

    /**
     * @param desc
     * @param sql
     * @param log  是否记录日志
     * @throws DaoException
     */
    public void execute(String desc, String sql, boolean log) throws DaoException {
        try {
            if (log) {
                printSql(desc, sql, new Object[]{});
            }
            jdbcTemplate.execute(sql);
            if (log) {
                logger.info(getSuccessDesc(desc));
            }
        } catch (Exception e) {
            String errorDesc = getErrorDesc(desc);
            if (log) {
                logger.error(errorDesc, e);
            }
            throw new DaoException(errorDesc, e);
        }
    }

    /**
     * 只是测试连接
     *
     * @param desc
     * @param sql
     * @param log  是否记录日志
     * @throws DaoException
     */
    public ResultVo<?> testExecuteStatus(String desc, String sql) throws DaoException {
        ResultVo<?>  status = new ResultVo<Object>();
        try {
            jdbcTemplate.execute(sql);
            status.setSuccess(true);
        } catch (Exception e) {
            String errorDesc = desc;
            status.setSuccess(false);
            status.setMsg(errorDesc + ",[sql:" + sql + "]" + e.getMessage());
        }
        return status;
    }

    /**
     * 添加
     *
     * @param desc 操作说明
     * @param sql  sql语句
     * @param args 参数
     * @return 成功数
     * @throws DaoException
     */
    public int insert(String desc, String sql, Object[] args)
            throws DaoException {
        return dml(desc, sql, args);
    }

    /**
     * eg************:
     * 拼接sql语句之前的语句部分: preWhereSql: UPDATE  FROM tablename WHERE name=?
     * 用于拼sql语句的条件 parameter: SearchParameter parameter = new SearchParameter();
     * parameter.addEqual("c1", "123");
     * 拼接sql语句之前的语句部分的参数(也就是name的参数值) preArgs：    new Object[]{"joe"}
     * 最终sql语句为：UPDATE  FROM tablename WHERE name=? AND c1=?
     * 参数为   new Object[]{"joe","123"}
     * **************
     * 提供拼接查询sql语句的分页列表查询
     *
     * @param desc        操作说明
     * @param preWhereSql 拼接sql语句之前的语句部分
     * @param parameter   用于拼sql语句的条件
     * @param preArgs     拼接sql语句之前的语句部分的参数
     * @return
     * @throws DaoException
     */
    public int insert(String desc, String preWhereSql, WhereCondition parameter, Object[] preArgs)
            throws DaoException {
        SqlArg sqlArg = getSqlArg(preWhereSql, parameter);
        sqlArg.addArgBefore(preArgs);
        return insert(desc, sqlArg);
    }

    /**
     * 慎用，除非对数据库数据库（类型，空值等有所了解）
     * 单表维护使用
     * 添加ModuleFeatureBean
     *
     * @param desc 操作说明
     * @param sql  sql语句
     * @param args 参数
     * @return 成功数
     * @throws DaoException
     */
    public int insertModule(String desc, ModuleFeatureBean modulefeatureBean)
            throws DaoException {
        try {
            return insert(desc, moduleDmlCreator.createInsert(modulefeatureBean, null));
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

    /**
     * 批量添加
     *
     * @param desc 描述
     * @param sql  sql语句
     * @param args 参数
     * @return 成功数
     * @throws DaoException
     */
    public int batchInsert(String desc, String sql, List<Object[]> args)
            throws DaoException {
        return batchDml(desc, sql, args);
    }

    /**
     * 批量插入实体
     *
     * @param desc
     * @param modulefeatureBeans 统一实体类型的多个对象
     * @return
     * @throws DaoException
     */
    public int batchInsertModule(String desc, List<? extends ModuleFeatureBean> modulefeatureBeans)
            throws DaoException {
        BatchSqlArg batchSqlArg = moduleDmlCreator.createBatchInsert(modulefeatureBeans, null);
        return batchInsert(desc, batchSqlArg.getSql(), batchSqlArg.getBatchArgs());
    }

    /**
     * 添加
     *
     * @param desc 操作说明
     * @param sql  sql语句
     * @param args 参数
     * @return 成功数
     * @throws DaoException
     */
    private int insert(String desc, SqlArg sqlArg)
            throws DaoException {
        return dml(desc, sqlArg.getSql(), sqlArg.getArgs());
    }

    /**
     * 修改
     *
     * @param desc 操作说明
     * @param sql  sql语句
     * @param args 参数
     * @return 成功数
     * @throws DaoException
     */
    public int update(String desc, String sql, Object[] args)
            throws DaoException {
        return dml(desc, sql, args);
    }

    /**
     * eg************:
     * 拼接sql语句之前的语句部分: preWhereSql: UPDATE  FROM tablename WHERE name=?
     * 用于拼sql语句的条件 parameter: SearchParameter parameter = new SearchParameter();
     * parameter.addEqual("c1", "123");
     * 拼接sql语句之前的语句部分的参数(也就是name的参数值) preArgs：    new Object[]{"joe"}
     * 最终sql语句为：UPDATE  FROM tablename WHERE name=? AND c1=?
     * 参数为   new Object[]{"joe","123"}
     * **************
     * 提供拼接查询sql语句的分页列表查询
     *
     * @param desc        操作说明
     * @param preWhereSql 拼接sql语句之前的语句部分
     * @param parameter   用于拼sql语句的条件
     * @param preArgs     拼接sql语句之前的语句部分的参数
     * @return
     * @throws DaoException
     */
    public int update(String desc, String preWhereSql, WhereCondition parameter, Object[] preArgs)
            throws DaoException {
        SqlArg sqlArg = getSqlArg(preWhereSql, parameter);
        sqlArg.addArgBefore(preArgs);
        return dml(desc, sqlArg.getSql(), sqlArg.getArgs());
    }

    /**
     * 修改，慎用，除非对数据库数据库（类型，空值等有所了解）
     * 单表维护使用
     * 修改ModuleFeatureBean
     *
     * @param desc          操作说明
     * @param sql           sql语句
     * @param args          参数
     * @param executeFields 不更新的字段。
     * @return 成功数
     * @throws DaoException
     */
    public int updateModule(String desc, ModuleFeatureBean modulefeatureBean, WhereCondition updateParameter, String[] executeFields)
            throws DaoException {
        SqlArg sqlArg = null;
        try {
            sqlArg = moduleDmlCreator.createUpdate(modulefeatureBean, updateParameter, false, executeFields);
        } catch (Exception e) {
            logger.error(e);
            throw new DaoException(e);
        }
        return update(desc, sqlArg.getSql(), sqlArg.getArgs());
    }

    /**
     * 修改，慎用，除非对数据库数据库（类型，空值等有所了解）
     * 单表维护使用
     * 修改ModuleFeatureBean
     *
     * @param desc 操作说明
     * @param sql  sql语句
     * @param args 参数
     * @return 成功数
     * @throws DaoException
     */
    public int updateModule(String desc, ModuleFeatureBean modulefeatureBean, WhereCondition updateParameter)
            throws DaoException {
        return updateModule(desc, modulefeatureBean, updateParameter, null);
    }

    /**
     * 修改，慎用，除非对数据库数据库（类型，空值等有所了解）
     * 单表维护使用
     * 修改ModuleFeatureBean
     *
     * @param desc          操作说明
     * @param sql           sql语句
     * @param args          参数
     * @param executeFields 不更新的字段。
     * @return 成功数
     * @throws DaoException
     */
    public int updateModuleExecuteNull(String desc, ModuleFeatureBean modulefeatureBean, WhereCondition updateParameter)
            throws DaoException {
        SqlArg sqlArg = null;
        try {
            sqlArg = moduleDmlCreator.createUpdate(modulefeatureBean, updateParameter, true, null);
        } catch (Exception e) {
            logger.error(e);
            throw new DaoException(e);
        }
        return update(desc, sqlArg.getSql(), sqlArg.getArgs());
    }

    /**
     * 根据主键修改对象 {@link com.fantasy.app.core.annotation.WsdColumn#isId()}
     * 单表维护使用
     * 修改ModuleFeatureBean
     *
     * @param desc          操作说明
     * @param sql           sql语句
     * @param args          参数
     * @param executeFields 不更新的字段。
     * @return 成功数
     * @throws DaoException
     */
    public int updateModuleById(String desc, ModuleFeatureBean modulefeatureBean, String[] executeFields)
            throws DaoException {
        SqlArg sqlArg = null;
        try {
            WhereCondition byIdCondition = moduleDmlCreator.createByIdCondition(modulefeatureBean, executeFields, true);
            sqlArg = moduleDmlCreator.createUpdate(modulefeatureBean, byIdCondition, false, executeFields);
        } catch (Exception e) {
            logger.error(e);
            throw new DaoException(e);
        }
        return update(desc, sqlArg.getSql(), sqlArg.getArgs());
    }

    /**
     * 根据主键修改对象 {@link com.fantasy.app.core.annotation.WsdColumn#isId()}
     * 单表维护使用
     * 修改ModuleFeatureBean
     *
     * @param desc          操作说明
     * @param executeFields 不更新的字段。
     * @return 成功数
     * @throws DaoException
     */
    public int updateModuleByIdExecuteNotNull(String desc, ModuleFeatureBean modulefeatureBean)
            throws DaoException {
        List<String> executeFields = new ArrayList<>();
        try {
            Field[] fields = modulefeatureBean.getClass().getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                if (field.get(modulefeatureBean) == null) {
                    if (field.isAnnotationPresent(WsdColumn.class)) {
                        WsdColumn wsdColumn = field.getAnnotation(WsdColumn.class);
                        executeFields.add(wsdColumn.name());
                    } else {
                        executeFields.add(field.getName());
                    }
                } else {
                    if (field.isAnnotationPresent(WsdColumn.class)) {
                        WsdColumn wsdColumn = field.getAnnotation(WsdColumn.class);
                        if (wsdColumn.UUID())
                            if (!StringUtils.isEmpty(wsdColumn.name()))
                                executeFields.add(wsdColumn.name());
                            else
                                executeFields.add(field.getName());
                    }
                }
            }
        } catch (IllegalAccessException e) {
            logger.error(e);
            throw new DaoException(e);
        }
        return updateModuleById(desc, modulefeatureBean, executeFields.toArray(new String[]{}));
    }

    /**
     * 验证数据库连接状态，根据数据库类型不同而不同
     *
     * @return
     * @throws DaoException
     */
    public ResultVo<?> dbIsAvaliable() {
        DbTypeEnum dbTypeEnum = getDbType();
        String checkSql = dbTypeEnum.getValidataSql();
        ResultVo<?> status = new ResultVo<Object>();
        try {
            int result = queryForInt("验证数据库连接状态", checkSql, new Object[]{});
            status.setSuccess(result == 1 ? true : false);
            status.setMsg(result == 1 ? "数据链接正常" : "数据链接异常");
        } catch (Exception e) {
            status.setSuccess(false);
            status.setMsg("数据链接异常，" + e.getMessage());
        }
        return status;
    }

    /**
     * 根据主键修改对象 {@link com.fantasy.app.core.annotation.WsdColumn#isId()}
     * 单表维护使用
     * 修改ModuleFeatureBean
     *
     * @param desc          操作说明
     * @param sql           sql语句
     * @param args          参数
     * @param executeFields 不更新的字段。
     * @return 成功数
     * @throws DaoException
     */
    public int updateModuleByIdExecuteNull(String desc, ModuleFeatureBean modulefeatureBean)
            throws DaoException {
        SqlArg sqlArg = null;
        try {
            WhereCondition byIdCondition = moduleDmlCreator.createByIdCondition(modulefeatureBean, null, true);
            sqlArg = moduleDmlCreator.createUpdate(modulefeatureBean, byIdCondition, true, null);
        } catch (Exception e) {
            logger.error(e);
            throw new DaoException(e);
        }
        return update(desc, sqlArg.getSql(), sqlArg.getArgs());
    }

    /**
     * 根据主键修改对象 {@link com.fantasy.app.core.annotation.WsdColumn#isId()},首先判断是否存在
     * 单表维护使用
     * 修改ModuleFeatureBean
     *
     * @param desc          操作说明
     * @param sql           sql语句
     * @param args          参数
     * @param executeFields 不更新的字段。
     * @return 成功数
     * @throws DaoException
     */
    public int saveOrUpdateModuleByIdExecuteNull(String desc, ModuleFeatureBean modulefeatureBean)
            throws DaoException {
        try {
            WhereCondition byIdCondition = moduleDmlCreator.createByIdCondition(modulefeatureBean, null, true);
            if (getModule("", modulefeatureBean.getClass(), byIdCondition) == null) {
                return insertModule("", modulefeatureBean);
            } else {
                return updateModuleByIdExecuteNull(desc, modulefeatureBean);
            }
        } catch (Exception e) {
            logger.error(e);
            throw new DaoException(e);
        }
    }

    /**
     * 根据主键修改对象 {@link com.fantasy.app.core.annotation.WsdColumn#isId()},首先判断是否存在
     * 单表维护使用
     * 修改ModuleFeatureBean
     *
     * @param desc          操作说明
     * @param sql           sql语句
     * @param args          参数
     * @param executeFields 不更新的字段。
     * @return 成功数
     * @throws DaoException
     */
    public int saveOrUpdateModuleById(String desc, ModuleFeatureBean modulefeatureBean, String[] executeFields)
            throws DaoException {
        try {
            WhereCondition byIdCondition = moduleDmlCreator.createByIdCondition(modulefeatureBean, executeFields, true);
            if (getModule("", modulefeatureBean.getClass(), byIdCondition) == null) {
                return insertModule("", modulefeatureBean);
            } else {
                return updateModuleById(desc, modulefeatureBean, executeFields);
            }
        } catch (Exception e) {
            logger.error(e);
            throw new DaoException(e);
        }
    }

    /**
     * 批量更新
     *
     * @param desc 描述
     * @param sql  sql语句
     * @param args 参数
     * @return 成功数
     * @throws DaoException
     */
    public int batchUpdate(String desc, String sql, List<Object[]> args)
            throws DaoException {
        return batchDml(desc, sql, args);
    }

    /**
     * 执行存储过程
     *
     * @param desc 操作说明
     * @param sql  sql语句
     * @param args 参数
     * @return 成功数
     * @throws DaoException
     */
    public Map<String, Object> callProcedure(String desc, final String procedureNme, LinkedList<In> inParms, LinkedList<Out> outParms)
            throws DaoException {
        GenericStoredProcedure procedure = new GenericStoredProcedure();
        procedure.setJdbcTemplate(jdbcTemplate);
        procedure.setSql(procedureNme);
        procedure.setFunction(false);
        Object[] inValues = null;
        if (CollectionUtil.hasElement(inParms)) {
            inValues = new Object[inParms.size()];
            int i = 0;
            for (Iterator<In> iterator = inParms.iterator(); iterator.hasNext(); ) {
                In inp = iterator.next();
                procedure.declareParameter(new SqlParameter(inp.getName(), inp.getType().getType()));
                inValues[i++] = inp.getValue();
            }
        }
        if (CollectionUtil.hasElement(outParms)) {
            for (Iterator<Out> iterator = outParms.iterator(); iterator.hasNext(); ) {
                Out outp = iterator.next();
                SqlOutParameter outParameter = null;
                if (StringUtils.hasText(outp.getPlsTypeName())) {
                    outParameter = new SqlOutParameter(outp.getName(), outp.getType().getType(), outp.getPlsTypeName());
                } else {
                    outParameter = new SqlOutParameter(outp.getName(), outp.getType().getType(), outp.getRowMapper());
                }
                procedure.declareParameter(outParameter);
            }
        }
        Map<String, Object> map = procedure.execute(inValues);
        return handleReturnValue(map, outParms);
    }

    /**
     * 处理存储过程调用返回结果,只针对基本数据类型的转换 BigDecimal ,java.sql.Date,java.sql.Timestamp
     *
     * @param map
     * @param outParms
     * @return
     */
    private Map<String, Object> handleReturnValue(Map<String, Object> map,
                                                  LinkedList<Out> outParms) {
        if (CollectionUtil.hasElement(map) && CollectionUtil.hasElement(outParms)) {
            for (Iterator<Out> iterator = outParms.iterator(); iterator.hasNext(); ) {
                Out out = iterator.next();
                String name = out.getName();
                Class<?> caseType = out.getCaseType();
                Object value = map.get(name);
                if (value == null) continue;
                if (caseType != null && value instanceof BigDecimal) {
                    BigDecimal new_name = (BigDecimal) value;
                    if (caseType == Integer.class) {
                        map.put(name, new_name.intValue());
                    } else if (caseType == Long.class) {
                        map.put(name, new_name.longValue());
                    } else if (caseType == Double.class) {
                        map.put(name, new_name.doubleValue());
                    } else {
                        //other not support
                    }
                } else if (value instanceof java.sql.Date) {
                    java.sql.Date new_date = (java.sql.Date) value;
                    map.put(name, new Date(new_date.getTime()));
                } else if (value instanceof java.sql.Timestamp) {
                    java.sql.Timestamp new_date = (java.sql.Timestamp) value;
                    map.put(name, new Date(new_date.getTime()));
                } else {
                    //other not support
                }
            }
        }
        SystemOutCollectionUtil.printMap(map);
        return map;
    }


    /**
     * 删除
     *
     * @param desc 操作说明
     * @param sql  sql语句
     * @param args 参数
     * @return 成功数
     * @throws DaoException
     */
    public int delete(String desc, String sql, Object[] args)
            throws DaoException {
        return dml(desc, sql, args);
    }

    /**
     * eg************:
     * 拼接sql语句之前的语句部分: preWhereSql: DELETE  FROM tablename WHERE name=?
     * 用于拼sql语句的条件 parameter: SearchParameter parameter = new SearchParameter();
     * parameter.addEqual("c1", "123");
     * 拼接sql语句之前的语句部分的参数(也就是name的参数值) preArgs：    new Object[]{"joe"}
     * 最终sql语句为： DELETE  FROM tablename WHERE name=? AND c1=?
     * 参数为   new Object[]{"joe","123"}
     * **************
     * 提供拼接查询sql语句的分页列表查询
     *
     * @param desc        操作说明
     * @param preWhereSql 拼接sql语句之前的语句部分
     * @param parameter   用于拼sql语句的条件
     * @param preArgs     拼接sql语句之前的语句部分的参数
     * @return
     * @throws DaoException
     */
    public int delete(String desc, String preWhereSql, WhereCondition parameter, Object[] preArgs)
            throws DaoException {
        SqlArg sqlArg = getSqlArg(preWhereSql, parameter);
        sqlArg.addArgBefore(preArgs);
        return dml(desc, sqlArg.getSql(), sqlArg.getArgs());
    }

    /**
     * 删除
     *
     * @param desc 操作说明
     * @param sql  sql语句
     * @param args 参数
     * @return 成功数
     * @throws DaoException
     */
    private int deleteShardingModule(String desc, ModuleFeatureBean moduleFeatureBean, WhereCondition deleteParameter)
            throws DaoException {
        SqlArg sqlArg = null;
        try {
            sqlArg = moduleDmlCreator.createShardingDelete(moduleFeatureBean, deleteParameter);
        } catch (Exception e) {
            logger.error(e);
            throw new DaoException(e);
        }
        return dml(desc, sqlArg.getSql(), sqlArg.getArgs());
    }

    /**
     * 删除
     *
     * @param desc 操作说明
     * @param sql  sql语句
     * @param args 参数
     * @return 成功数
     * @throws DaoException
     */
    public int deleteModule(String desc, Class<? extends ModuleFeatureBean> classToQuery, WhereCondition deleteParameter)
            throws DaoException {
        SqlArg sqlArg = null;
        try {
            sqlArg = moduleDmlCreator.createDelete(classToQuery, deleteParameter);
        } catch (Exception e) {
            logger.error(e);
            throw new DaoException(e);
        }
        return dml(desc, sqlArg.getSql(), sqlArg.getArgs());
    }

    /**
     * 根据id删除对象
     *
     * @param desc 操作说明
     * @param sql  sql语句
     * @param args 参数
     * @return 成功数
     * @throws DaoException
     */
    public int deleteModuleById(String desc, Class<? extends ModuleFeatureBean> classToQuery, Object[] idValuds)
            throws DaoException {
        WhereCondition byIdCondition = null;
        try {
            byIdCondition = moduleDmlCreator.createByIdCondition(classToQuery, idValuds, true);
        } catch (Exception e) {
            logger.error(e);
            throw new DaoException(e);
        }
        return deleteModule(desc, classToQuery, byIdCondition);
    }

    /**
     * 根据 给定的对象删除，要求对象里面需要有id值
     *
     * @param desc 操作说明
     * @param sql  sql语句
     * @param args 参数
     * @return 成功数
     * @throws DaoException
     */
    public int deleteModuleById(String desc, ModuleFeatureBean modulefeatureBean)
            throws DaoException {
        WhereCondition byIdCondition = null;
        try {
            byIdCondition = moduleDmlCreator.createByIdCondition(modulefeatureBean, null, true);
        } catch (Exception e) {
            logger.error(e);
            throw new DaoException(e);
        }
        if (TableShardingParser.tableIsSharding(modulefeatureBean.getClass())) {
            return deleteShardingModule(desc, modulefeatureBean, byIdCondition);
        } else {
            return deleteModule(desc, modulefeatureBean.getClass(), byIdCondition);
        }
    }

    private static final int DEFAUT_BATCHSIZE = 100;

    private int batchDml(final String desc, final String sql, final List<Object[]> args)
            throws DaoException {
        try {
            int[][] num = {{0}};
            printSql(desc, sql, args);
            num = doInSingleTransationCircle(desc, new SingleTransationCircleWithResult<int[][]>() {
                @Override
                public int[][] actionInCircle() throws RuntimeException {
                    try {
                        return jdbcTemplate.batchUpdate(sql, args, DEFAUT_BATCHSIZE,
                                new ParameterizedPreparedStatementSetter<Object[]>() {
                                    public void setValues(PreparedStatement ps,
                                                          Object[] argument) throws SQLException {
                                        for (int i = 0; i < argument.length; i++) {
                                            doSetValue(ps, i + 1, argument[i]);
                                        }
                                    }
                                });
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            });
            int numResult = computeCount(num);
            //TODO spring 有bug 总是返回负数
            logger.info(getSuccessDesc(desc) + " 操作记录数：" + numResult);
            return numResult;
        } catch (Exception e) {
            String errorDesc = getErrorDesc(desc);
            logger.error(errorDesc, e);
            throw new DaoException(errorDesc, e);
        }
    }

    private int computeCount(int[][] num) {
        int numResult = 0;
        for (int i = 0; i < num.length; i++) {
            for (int j = 0; j < num[i].length; j++) {
                numResult += num[i][j];
            }
        }
        if (numResult < 0) numResult = 0;
        return numResult;
    }

    /**
     * 批量删除
     *
     * @param desc 描述
     * @param sql  sql语句
     * @param args 参数
     * @return 成功数
     * @throws DaoException
     */
    public int batchDelete(String desc, String sql, List<Object[]> args)
            throws DaoException {
        return batchDml(desc, sql, args);
    }

    /**
     * 在同一个事务里面进行多个操作，无返回值
     *
     * @param circleWithOutResult
     * @throws DaoException
     */
    public void doInSingleTransationCircle(String desc,
                                           final SingleTransationCircleWithOutResult circleWithOutResult)
            throws DaoException {
        TransactionTemplate template = new TransactionTemplate(
                transactionManager);
        template.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        try {
            template.execute(new TransactionCallbackWithoutResult() {
                @Override
                protected void doInTransactionWithoutResult(
                        TransactionStatus status) {
                    circleWithOutResult.actionInCircle();
                }
            });
            logger.info(getSuccessDesc(desc));
        } catch (Exception e) {
            String errorDesc = getErrorDesc(desc);
            logger.error(errorDesc, e);
            throw new DaoException(errorDesc, e);
        }
    }

    /**
     * 在同一个事务里面进行多个操作，有返回值
     *
     * @param circleWithOutResult {@link SingleTransationCircleWithResult}
     * @throws DaoException
     */
    public <T> T doInSingleTransationCircle(String desc,
                                            final SingleTransationCircleWithResult<T> circleWithResult)
            throws DaoException {
        TransactionTemplate template = new TransactionTemplate(
                transactionManager);
        template.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        T returnObj = null;
        try {
            returnObj = template.execute(new TransactionCallback<T>() {
                public T doInTransaction(TransactionStatus status) {
                    return circleWithResult.actionInCircle();
                }
            });
        } catch (Exception e) {
            String errorDesc = getErrorDesc(desc);
            logger.error(errorDesc, e);
            throw new DaoException(errorDesc, e);
        }
        logger.info(getSuccessDesc(desc));
        return returnObj;
    }

    /**
     * 将查询结果 通过rowMapper 进行封装，并返回
     *
     * @param desc      操作说明
     * @param sql       sql语句
     * @param rowMapper 结果处理对象
     * @param args      参数
     * @return 返回单条记录，如果查询记录为0 返回null，查询到多个将抛出异常
     * @throws DaoException
     */
    public <T> T get(String desc, String sql, RowMapper<T> rowMapper, Object[] args) throws DaoException {
        T obj = null;
        args = warpSqlArgType(args);
        printSql(desc, sql, args);
        try {
            obj = jdbcTemplate.queryForObject(sql, args, rowMapper);
        } catch (EmptyResultDataAccessException e) {
            logger.warn(desc + " 查询到0 条");
        } catch (Exception e) {
            String errorDesc = getErrorDesc(desc);
            logger.error(errorDesc, e);
            throw new DaoException(errorDesc, e);
        }
        logger.info(getSuccessDesc(desc));

        return obj;
    }

    /**
     * 将查询结果 通过rowMapper 进行封装，并返回
     *
     * @param desc            操作说明
     * @param sql             sql语句
     * @param classToQuery    返回类型
     * @param searchParameter 参数
     * @return 返回单条记录，如果查询记录为0 返回null，查询到多个将抛出异常
     * @throws DaoException
     */
    public <T extends ModuleFeatureBean> T getModule(String desc, Class<T> classToQuery, WhereCondition searchParameter) throws DaoException {
        SqlArg sqlArg = null;
        try {
            sqlArg = moduleDmlCreator.createQuery(classToQuery, searchParameter);
        } catch (Exception e) {
            logger.error(e);
            throw new DaoException(e);
        }
        return get(desc, sqlArg.getSql(), new ModuleFeatureBeanRowWapper<T>(classToQuery), sqlArg.getArgs());
    }

    /**
     * 将查询结果 通过rowMapper 进行封装，并返回
     *
     * @param desc            操作说明
     * @param sql             sql语句
     * @param classToQuery    返回类型
     * @param searchParameter 参数
     * @return 返回单条记录，如果查询记录为0 返回null，查询到多个将抛出异常
     * @throws DaoException
     */
    public <T extends ModuleFeatureBean> T getModule(String desc, String sql, Class<T> classToQuery, Object[] args) throws DaoException {
        return get(desc, sql, new ModuleFeatureBeanRowWapper<T>(classToQuery), args);
    }

    /**
     * 根据主键获取对象 @see {@link com.fantasy.app.core.annotation.WsdColumn#isId()}
     * 单表维护使用
     *
     * @param desc         操作说明
     * @param classToQuery 返回类型
     * @return 成功数
     * @throws DaoException
     */
    public <T extends ModuleFeatureBean> T getModuleById(String desc, Class<T> classToQuery, Object[] idValuds)
            throws DaoException {
        WhereCondition byIdCondition = null;
        try {
            byIdCondition = moduleDmlCreator.createByIdCondition(classToQuery, idValuds, true);
        } catch (Exception e) {
            logger.error(e);
            throw new DaoException(e);
        }
        return getModule(desc, classToQuery, byIdCondition);
    }

    /**
     * eg************:
     * 拼接sql语句之前的语句部分: preWhereSql: SELECT *  FROM tablename WHERE name=?
     * 用于拼sql语句的条件 parameter: SearchParameter parameter = new SearchParameter();
     * parameter.addEqual("c1", "123");
     * 拼接sql语句之前的语句部分的参数(也就是name的参数值) preArgs：    new Object[]{"joe"}
     * 最终sql语句为： SELECT *  FROM tablename WHERE name=? AND c1=?
     * 参数为   new Object[]{"joe","123"}
     * **************
     *
     * @param desc        操作说明
     * @param preWhereSql 拼接sql语句之前的语句部分
     * @param rowMapper   结果处理对象
     * @param parameter   用于拼sql语句的条件
     * @param preArgs     拼接sql语句之前的语句部分的参数
     * @return
     * @throws DaoException
     */
    public <T> T get(String desc, String preWhereSql, RowMapper<T> rowMapper,
                     WhereCondition parameter, Object[] preArgs) throws DaoException {
        SqlArg sqlArg = getSqlArg(preWhereSql, parameter);
        sqlArg.addArgBefore(preArgs);
        return get(desc, sqlArg.getSql(), rowMapper, sqlArg.getArgs());
    }

    /**
     * 将查询结果以map的形式返回
     *
     * @param desc      操作说明
     * @param sql       sql语句
     * @param rowMapper 结果处理对象
     * @param args      参数
     * @return 返回单条记录，如果查询记录为0 返回null，查询到多个将抛出异常
     * @throws DaoException
     */
    public Map<String, Object> getMap(String desc, String sql, Object[] args) throws DaoException {
        return get(desc, sql, new ColumnMapRowMapper(), args);
    }

    /**
     * 将查询结果以map的形式返回
     *
     * @param desc      操作说明
     * @param sql       sql语句
     * @param rowMapper 结果处理对象
     * @param args      参数
     * @return 返回单条记录，如果查询记录为0 返回null，查询到多个将抛出异常
     * @throws DaoException
     */
    public Map<String, Object> getMap(String desc, String preWhereSql, WhereCondition parameter, Object[] preArgs) throws DaoException {
        return get(desc, preWhereSql, new ColumnMapRowMapper(), parameter, preArgs);
    }

    /**
     * 将查询结果 通过rowMapper 进行封装，并返回list
     *
     * @param desc      操作说明
     * @param sql       sql语句
     * @param rowMapper 结果处理对象
     * @param args      参数
     * @return 列表或者null
     * @throws DaoException
     */
    public <T> List<T> list(String desc, String sql, RowMapper<T> rowMapper, Object[] args) throws DaoException {
        List<T> results = null;
        args = warpSqlArgType(args);
        printSql(desc, sql, args);
        try {
            results = jdbcTemplate.query(sql, args, rowMapper);
        } catch (Exception e) {
            String errorDesc = getErrorDesc(desc);
            logger.error(errorDesc, e);
            throw new DaoException(errorDesc, e);
        }
        logger.info(getSuccessDesc(desc));

        return results;
    }

    /**
     * 递归查询 模拟oracle START WITH s.id=? " +
     * " CONNECT BY prior  s.pid=s.id " +
     * " ORDER BY s.type";
     * 将查询结果 通过rowMapper 进行封装，并返回list
     *
     * @param desc      操作说明
     * @param sql       sql语句
     * @param rowMapper 结果处理对象
     * @param args      参数
     * @return 列表或者null
     * @throws DaoException
     */
    public void listRecursion(String desc, List<String> results, String preWhereSql, final String pidColumnName, final String cidColumnName, final boolean startWithC, WhereCondition parameter, Object[] preArgs, final int startValueIndex) throws DaoException {
        SqlArg sqlArg = getSqlArg(preWhereSql, parameter);
        sqlArg.addArgBefore(preArgs);
        try {
            String next_arg = null;
            List<String> next_cs = new ArrayList<String>();
            try {
                if (startWithC) {
                    next_arg = jdbcTemplate.queryForObject(sqlArg.getSql(), sqlArg.getArgs(), new RowMapper<String>() {
                        @Override
                        public String mapRow(ResultSet rs, int rowNum)
                                throws SQLException {
                            return startWithC ? rs.getString(pidColumnName) : rs.getString(cidColumnName);
                        }
                    });
                } else {
                    next_cs = list(desc, sqlArg.getSql(), new RowMapper<String>() {
                        @Override
                        public String mapRow(ResultSet rs, int rowNum)
                                throws SQLException {
                            return startWithC ? rs.getString(pidColumnName) : rs.getString(cidColumnName);
                        }
                    }, sqlArg.getArgs());
                }
            } catch (EmptyResultDataAccessException e) {
            }
            if (StrUtil.isNotBank(next_arg)) {
                results.add(next_arg);
                preArgs[startValueIndex] = next_arg;
                listRecursion(desc, results, preWhereSql, pidColumnName, cidColumnName, startWithC, parameter, preArgs, startValueIndex);
            }
            if (!next_cs.isEmpty()) {
                for (Iterator<String> iterator = next_cs.iterator(); iterator.hasNext(); ) {
                    String next_arg_c = (String) iterator.next();
                    results.add(next_arg_c);
                    preArgs[startValueIndex] = next_arg_c;
                    listRecursion(desc, results, preWhereSql, pidColumnName, cidColumnName, startWithC, parameter, preArgs, startValueIndex);
                }
            }
        } catch (Exception e) {
            String errorDesc = getErrorDesc(desc);
            logger.error(errorDesc, e);
            throw new DaoException(errorDesc, e);
        }
        logger.info(getSuccessDesc(desc));
    }

    /**
     * eg************:
     * 拼接sql语句之前的语句部分: preWhereSql: SELECT *  FROM tablename WHERE name=?
     * 用于拼sql语句的条件 parameter: SearchParameter parameter = new SearchParameter();
     * parameter.addEqual("c1", "123");
     * 拼接sql语句之前的语句部分的参数(也就是name的参数值) preArgs：    new Object[]{"joe"}
     * 最终sql语句为： SELECT *  FROM tablename WHERE name=? AND c1=?
     * 参数为   new Object[]{"joe","123"}
     * **************
     *
     * @param desc        操作说明
     * @param preWhereSql 拼接sql语句之前的语句部分
     * @param rowMapper   结果处理对象
     * @param parameter   用于拼sql语句的条件
     * @param preArgs     拼接sql语句之前的语句部分的参数
     * @return
     * @throws DaoException
     */
    public <T> List<T> list(String desc, String preWhereSql, RowMapper<T> rowMapper,
                            WhereCondition parameter, Object[] preArgs) throws DaoException {
        SqlArg sqlArg = getSqlArg(preWhereSql, parameter);
        sqlArg.addArgBefore(preArgs);
        return list(desc, sqlArg.getSql(), rowMapper, sqlArg.getArgs());
    }

    /**
     * 将查询结果 以map的形式返回
     *
     * @param desc      操作说明
     * @param sql       sql语句
     * @param rowMapper 结果处理对象
     * @param args      参数
     * @return 返回记录列表，如果查询记录为0 返回null
     * @throws DaoException
     */
    public List<Map<String, Object>> listMap(String desc, String sql, Object[] args) throws DaoException {
        return list(desc, sql, new ColumnMapRowMapper(), args);
    }

    /**
     * eg************:
     * 拼接sql语句之前的语句部分: preWhereSql: SELECT *  FROM tablename WHERE name=?
     * 用于拼sql语句的条件 parameter: SearchParameter parameter = new SearchParameter();
     * parameter.addEqual("c1", "123");
     * 拼接sql语句之前的语句部分的参数(也就是name的参数值) preArgs：    new Object[]{"joe"}
     * 最终sql语句为： SELECT *  FROM tablename WHERE name=? AND c1=?
     * 参数为   new Object[]{"joe","123"}
     * **************
     *
     * @param desc        操作说明
     * @param preWhereSql 拼接sql语句之前的语句部分
     * @param rowMapper   结果处理对象
     * @param parameter   用于拼sql语句的条件
     * @param preArgs     拼接sql语句之前的语句部分的参数
     * @return
     * @throws DaoException
     */
    public List<Map<String, Object>> listMap(String desc, String preWhereSql,
                                             WhereCondition parameter, Object[] preArgs) throws DaoException {
        SqlArg sqlArg = getSqlArg(preWhereSql, parameter);
        sqlArg.addArgBefore(preArgs);
        return listMap(desc, sqlArg.getSql(), sqlArg.getArgs());
    }

    /**
     * 获取单个字符字段列表
     *
     * @param desc
     * @param preWhereSql
     * @param parameter
     * @param preArgs
     * @return
     * @throws DaoException
     */
    public List<String> listSignleStringColumn(String desc, String preWhereSql,
                                               WhereCondition parameter, Object[] preArgs) throws DaoException {
        SqlArg sqlArg = getSqlArg(preWhereSql, parameter);
        sqlArg.addArgBefore(preArgs);
        return list(desc, sqlArg.getSql(), new SingleColumnRowMapper<String>(), sqlArg.getArgs());
    }

    /**
     * 将查询结果 通过rowMapper 进行封装，并返回list
     *
     * @param desc      操作说明
     * @param rowMapper 结果处理对象
     * @param args      参数
     * @return 列表或者null
     * @throws DaoException
     */
    public <T extends ModuleFeatureBean> List<T> listModule(String desc, Class<T> classToQuery, WhereCondition searchParameter) throws DaoException {
        SqlArg sqlArg = null;
        try {
            sqlArg = moduleDmlCreator.createQuery(classToQuery, searchParameter);
        } catch (Exception e) {
            logger.error(e);
            throw new DaoException(e);
        }

        return list(desc, sqlArg.getSql(), new ModuleFeatureBeanRowWapper<T>(classToQuery), sqlArg.getArgs());
    }

    /**
     * 将查询结果 通过rowMapper 进行封装，并返回list
     *
     * @param desc            操作说明
     * @param sql             sql语句
     * @param classToQuery    结果处理对象
     * @param searchParameter sql 后面部分的拼接参数
     * @return 列表或者null
     * @throws DaoException
     */
    public <T extends ModuleFeatureBean> List<T> listModule(String desc, String preWhereSql, Class<T> classToQuery, WhereCondition searchParameter) throws DaoException {
        return list(desc, preWhereSql, new ModuleFeatureBeanRowWapper<T>(classToQuery), searchParameter, null);
    }

    /**
     * 将查询结果 通过rowMapper 进行封装，并返回list
     *
     * @param desc         操作说明
     * @param sql          sql语句
     * @param classToQuery 结果处理对象
     * @param args         参数
     * @return 列表或者null
     * @throws DaoException
     */
    public <T extends ModuleFeatureBean> List<T> listModule(String desc, String sql, Class<T> classToQuery, Object[] args) throws DaoException {
        return list(desc, sql, new ModuleFeatureBeanRowWapper<T>(classToQuery), args);
    }


    /**
     * 查询对应的数目
     *
     * @param desc
     * @param classToQuery
     * @param searchParameter
     * @return
     * @throws DaoException
     */
    public <T extends ModuleFeatureBean> int countModule(String desc, Class<T> classToQuery, WhereCondition searchParameter) throws DaoException {
        try {
            SqlArg sqlArg = moduleDmlCreator.createQuery(classToQuery, searchParameter);
            String total_sql = totalWrapSql(sqlArg.getSql());
            int total = queryForInt(desc + " 总数", total_sql, sqlArg.getArgs());
            return total;
        } catch (Exception e) {
            logger.error(e);
            throw new DaoException(e);
        }
    }

    /**
     * 将查询结果 通过rowMapper 进行封装，并返回list
     *
     * @param desc      操作说明
     * @param sql       sql语句
     * @param rowMapper 结果处理对象
     * @param args      参数
     * @return 列表或者null
     * @throws DaoException
     */
    public <T extends ModuleFeatureBean> PagerData<T> getPagerModuleList(String desc, Class<T> classToQuery, WhereCondition searchParameter, Pager pager) throws DaoException {
        SqlArg sqlArg = null;
        try {
            sqlArg = moduleDmlCreator.createQuery(classToQuery, searchParameter);
        } catch (Exception e) {
            logger.error(e);
            throw new DaoException(e);
        }
        return getPagerList(desc, sqlArg.getSql(), pager, new ModuleFeatureBeanRowWapper<T>(classToQuery), sqlArg.getArgs());
    }

    /**
     * 将查询结果 通过rowMapper 进行封装，并返回list
     *
     * @param desc      操作说明
     * @param sql       sql语句
     * @param rowMapper 结果处理对象
     * @param args      参数
     * @return 列表或者null
     * @throws DaoException
     */
    public <T extends ModuleFeatureBean> PagerData<T> getPagerModuleList(String desc, String preSql, Class<T> classToQuery, Object[] preArgs, WhereCondition searchParameter, Pager pager) throws DaoException {
        return getPagerList(desc, preSql, new ModuleFeatureBeanRowWapper<T>(classToQuery), pager, searchParameter, preArgs);
    }

    /**
     * 将查询结果 通过rowMapper 进行封装，同时获取总数，并返回分页类型对象
     *
     * @param desc      操作说明
     * @param sql       sql语句
     * @param pager     分页参数
     * @param rowMapper 结果处理对象
     * @param args      参数
     * @return 返回记录列表，如果查询记录为0 返回null
     * @throws DaoException
     */
    public <T> PagerData<T> getPagerList(String desc, String sql, Pager pager, RowMapper<T> rowMapper, Object[] args)
            throws DaoException {

        PagerData<T> pageData = new PagerData<T>();
        try {
            args = warpSqlArgType(args);
            String total_sql = totalWrapSql(sql);
            int total = queryForInt(desc + " 总数", total_sql, args);
            if (total != 0) {//总数不为0才去查具体数据
                //解决用户传递的offset超过实际总数的问题
                int offset = pager.getPg().getOffset();
                int pagesize = pager.getPg().getPagesize() == 0 ? 1 : pager.getPg().getPagesize();
                int totalpage = (Integer) (total % pagesize) == 0 ? (total / pagesize)
                        : (total / pagesize + 1);
                if (total < (offset + 1)) {
                    if (pager.getPg().getOffset() != 0) {
                        pager.getPg().setOffset((totalpage - 1) * pagesize);
                    }
                }
                String pg_sql = pagerWrapSql(sql, pager.getPg().getOffset(), pager.getPg().getPagesize());
                List<T> datas = list(desc, pg_sql, rowMapper, args);
                pageData.setRows(datas);
            }
            pageData.setPg(pager.getPg());
            pageData.setTotal(total);
        } catch (Exception e) {
            String errorDesc = getErrorDesc(desc);
            logger.error(errorDesc, e);
            throw new DaoException(errorDesc, e);
        }
        return pageData;
    }

    /**
     * 将查询结果 通过map进行封装，同时获取总数，并返回分页类型对象
     *
     * @param desc  操作说明
     * @param sql   sql语句
     * @param pager 分页参数
     * @param args  参数
     * @return 返回记录列表，如果查询记录为0 返回null
     * @throws DaoException
     */
    public PagerData<Map<String, Object>> getPagerMapList(String desc, String sql, Pager pager, Object[] args)
            throws DaoException {
        return getPagerList(desc, sql, pager, new ColumnMapRowMapper(), args);
    }

    /**
     * eg************:
     * 拼接sql语句之前的语句部分: preWhereSql: SELECT *  FROM tablename WHERE name=?
     * 用于拼sql语句的条件 parameter: SearchParameter parameter = new SearchParameter();
     * parameter.addEqual("c1", "123");
     * 拼接sql语句之前的语句部分的参数(也就是name的参数值) preArgs：    new Object[]{"joe"}
     * 最终sql语句为： SELECT *  FROM tablename WHERE name=? AND c1=?
     * 参数为   new Object[]{"joe","123"}
     * **************
     * 提供拼接查询sql语句的分页列表查询
     *
     * @param desc        操作说明
     * @param preWhereSql 拼接sql语句之前的语句部分
     * @param rowMapper   结果处理对象
     * @param pager       分页对象
     * @param parameter   用于拼sql语句的条件
     * @param preArgs     拼接sql语句之前的语句部分的参数
     * @return
     * @throws DaoException
     */
    public <T> PagerData<T> getPagerList(String desc, String preWhereSql, RowMapper<T> rowMapper, Pager pager,
                                         WhereCondition parameter, Object[] preArgs) throws DaoException {
        SqlArg sqlArg = getSqlArg(preWhereSql, parameter);
        sqlArg.addArgBefore(preArgs);
        return getPagerList(desc, sqlArg.getSql(), pager, rowMapper, sqlArg.getArgs());
    }

    /**
     * 后台分页查询，并处理分页对象数据。
     *
     * @param desc
     * @param sql
     * @param total
     * @param pager
     * @param rowMapper
     * @param args
     * @return
     * @throws DaoException
     */
    public <T extends ModuleFeatureBean> void handleModulePagerListInner(String desc, String sql, Class<T> classToQuery, Object[] args, int pagesize, PageDataHandler<T> pagerDataHandler)
            throws DaoException {
        handlePagerListInner(desc, sql, new ModuleFeatureBeanRowWapper<T>(classToQuery), args, pagesize, pagerDataHandler);
    }

    /**
     * 后台分页查询，并处理分页map数据。
     *
     * @param desc
     * @param sql
     * @param total
     * @param pager
     * @param rowMapper
     * @param args
     * @return
     * @throws DaoException
     */
    public void handleMapPagerListInner(String desc, String sql, Object[] args, int pagesize, PageDataHandler<Map<String, Object>> pagerDataHandler)
            throws DaoException {
        handlePagerListInner(desc, sql, new ColumnMapRowMapper(), args, pagesize, pagerDataHandler);
    }

    /**
     * 后台分页查询，并处理分页数据。
     *
     * @param desc
     * @param sql
     * @param total
     * @param pager
     * @param rowMapper
     * @param args
     * @return
     * @throws DaoException
     */
    public <T> void handlePagerListInner(String desc, String sql, RowMapper<T> rowMapper, Object[] args, int pagesize, PageDataHandler<T> pagerDataHandler)
            throws DaoException {
        try {
            args = warpSqlArgType(args);
            Pager pager = new Pager(0, pagesize);
            for (int i = 0; i < Integer.MAX_VALUE; i++) {
                String pg_sql = pagerWrapSql(sql, pager.getPg().getOffset(), pager.getPg().getPagesize());
                List<T> datas = list(desc, pg_sql, rowMapper, args);
                if (datas == null || datas.isEmpty()) break;
                pagerDataHandler.handerPagerData(datas);
                pager.setPg(new Pg(pager.getPg().getOffset() + pagesize, pagesize));
            }
        } catch (Exception e) {
            String errorDesc = getErrorDesc(desc);
            logger.error(errorDesc, e);
            throw new DaoException(errorDesc, e);
        }
    }

    /**
     * 不分页查全部，返回结果集，用户自己处理
     *
     * @param desc
     * @param sql
     * @param args
     * @throws DaoException
     */
    public <T> void queryForRowSet(String desc, String sql, Object[] args, final RowCallbackHandler callbackHandler) throws DaoException {
        args = warpSqlArgType(args);
        printSql(desc, sql, args);
        try {
            jdbcTemplate.query(sql, args, callbackHandler);
        } catch (Exception e) {
            String errorDesc = getErrorDesc(desc);
            logger.error(errorDesc, e);
            throw new DaoException(errorDesc, e);
        }
        logger.info(getSuccessDesc(desc));
    }

    /**
     * 返回int类型的数据
     *
     * @param desc 操作说明
     * @param sql  sql语句
     * @param args 参数
     * @return int数值or null
     * @throws DaoException
     */
    public <T> T queryForSingleColumnRow(String desc, String sql, Class<T> clazz, Object[] args) throws DaoException {
        return get(desc, sql, new SingleColumnRowMapper<T>(clazz), args);
    }

    /**
     * 返回int类型的数据
     *
     * @param desc 操作说明
     * @param sql  sql语句
     * @param args 参数
     * @return int数值or null
     * @throws DaoException
     */
    public Integer queryForInt(String desc, String sql, Object[] args) throws DaoException {
        return get(desc, sql, new SingleColumnRowMapper<Integer>(Integer.class), args);
    }

    /**
     * 返回int类型的数据
     *
     * @param desc 操作说明
     * @param sql  sql语句
     * @param args 参数
     * @return int数值or null
     * @throws DaoException
     */
    public Integer queryForInt(String desc, String preWhereSql, WhereCondition parameter, Object[] preArgs) throws DaoException {
        return get(desc, preWhereSql, new SingleColumnRowMapper<Integer>(Integer.class), parameter, preArgs);
    }

    /**
     * 返回String类型的数据
     *
     * @param desc 操作说明
     * @param sql  sql语句
     * @param args 参数
     * @return String or null
     * @throws DaoException
     */
    public String queryForString(String desc, String sql, Object[] args) throws DaoException {
        return get(desc, sql, new SingleColumnRowMapper<String>(String.class), args);
    }

    /**
     * 返回String类型的数据
     *
     * @param desc 操作说明
     * @param sql  sql语句
     * @param args 参数
     * @return String or null
     * @throws DaoException
     */
    public String queryForString(String desc, String preWhereSql, WhereCondition parameter, Object[] preArgs) throws DaoException {
        return get(desc, preWhereSql, new SingleColumnRowMapper<String>(String.class), parameter, preArgs);
    }

    /**
     * 返回long类型的数据
     *
     * @param desc 操作说明
     * @param sql  sql语句
     * @param args 参数
     * @return long数值 or null
     * @throws DaoException
     */
    public Long queryForLong(String desc, String sql, Object[] args) throws DaoException {
        return get(desc, sql, new SingleColumnRowMapper<Long>(Long.class), args);
    }

    /**
     * 返回long类型的数据
     *
     * @param desc 操作说明
     * @param sql  sql语句
     * @param args 参数
     * @return long数值 or null
     * @throws DaoException
     */
    public Long queryForLong(String desc, String preWhereSql, WhereCondition parameter, Object[] preArgs) throws DaoException {
        return get(desc, preWhereSql, new SingleColumnRowMapper<Long>(Long.class), parameter, preArgs);
    }

    /**
     * 获取clob数据
     *
     * @param rs
     * @param columnName
     * @return
     * @throws SQLException
     */
    public String getClob(ResultSet rs, String columnName) throws SQLException {
        return lobHandler.getClobAsString(rs, columnName);
    }

    public String getClob(ResultSet rs, int columnIndex) throws SQLException {
        return lobHandler.getClobAsString(rs, columnIndex);
    }

    public byte[] getBlob(ResultSet rs, String columnName) throws SQLException {
        return lobHandler.getBlobAsBytes(rs, columnName);
    }

    public byte[] getBlob(ResultSet rs, int columnIndex) throws SQLException {
        return lobHandler.getBlobAsBytes(rs, columnIndex);
    }

    public String getSuccessDesc(String desc) {
        return StringUtils.hasText(desc) ? desc + SUCCESS_SUFFIX : DEFAULT_SUCCESS_DESC;
    }

    public String getErrorDesc(String desc) {
        return StringUtils.hasText(desc) ? desc + ERROR_SUFFIX : DEFAULT_ERROR_DESC;
    }

    // ------------------------------------------------------------------------------------
    protected void doSetValue(PreparedStatement ps, int parameterPosition,
                              Object argValue) throws SQLException {
        if (argValue instanceof SqlParameterValue) {
            SqlParameterValue paramValue = (SqlParameterValue) argValue;
            StatementCreatorUtils.setParameterValue(ps, parameterPosition,
                    paramValue, paramValue.getValue());
        } else {
            StatementCreatorUtils.setParameterValue(ps, parameterPosition,
                    SqlTypeValue.TYPE_UNKNOWN, argValue);
        }
    }

    /**
     * 返回分页语句封装
     *
     * @param sql
     * @param offset
     * @param pagerSize
     * @return
     */
    public String pagerWrapSql(String sql, int offset, int pagerSize) {
        DbTypeEnum dbTypeEnum = getDbType();
        if (dbTypeEnum == DbTypeEnum.ORACLE) {
            return OracleFeature.pagerWrapSql(sql, offset, pagerSize);
        } else if (dbTypeEnum == DbTypeEnum.MYSQL) {
            return MysqlFeature.pagerWrapSql(sql, offset, pagerSize);
        } else if (dbTypeEnum == DbTypeEnum.POSTGRESQL) {
            return PostgreSQLFeature.pagerWrapSql(sql, offset, pagerSize);
        } else {
            throw new RuntimeException("不支持的数据类型...");
        }
    }

    /**
     * 组装查询总数的语句
     *
     * @param sql
     * @param offset
     * @param pagerSize
     * @return
     */
    private static String totalWrapSql(String sql) {
        //int idx = sql.toUpperCase().indexOf(" FROM ");
        //int f = sql.indexOf("from");
        //int min = Math.min(F, f);
        //int idx = min<0?Math.max(F, f):min;
        return "SELECT count(*) FROM (" + sql + ") count_ ";
    }

    /**
     * 打印语句
     *
     * @param sql
     */
    public void printSql(String desc, String sql, Object[] args) {
        StringBuffer trace = new StringBuffer();
        trace.append("--sql print:").append(desc).append("\r\n");
        trace.append("sql:[").append(sql).append("]\r\nargs:").append(Arrays.toString(args));
        if (CoreInitCtx.PRINTSQL) {
            logger.info(trace);
        }
    }

    public abstract DbTypeEnum getDbType();

    public boolean isMysql() {
        return getDbType() == DbTypeEnum.MYSQL;
    }

    public boolean isOracle() {
        return getDbType() == DbTypeEnum.ORACLE;
    }

    public boolean isSqlserver() {
        return getDbType() == DbTypeEnum.SQLSERVER;
    }

    /**
     * 打印语句
     *
     * @param sql
     */
    public void printSql(String desc, String sql, List<Object[]> args) {
        StringBuffer trace = new StringBuffer();
        trace.append("--sql print:").append(desc).append("\r\n");
        trace.append("sql:[").append(sql).append("]\r\n");
        trace.append("args:");
        if (args != null && !args.isEmpty()) {

            for (Iterator<Object[]> iterator = args.iterator(); iterator.hasNext(); ) {
                Object[] objects = iterator.next();
                trace.append(Arrays.toString(objects));
            }
        } else {
            trace.append("[]");
        }
        if (CoreInitCtx.PRINTSQL) {
            logger.info(trace);
        }
    }

    public SqlArg getSqlArg(String preWhereSql, WhereCondition parameter) {
        SqlArg sqlArg = SqlCreator.createWhere(parameter, true);
        String sql = preWhereSql + sqlArg.getSql();
        sqlArg.setSql(sql);
        return sqlArg;
    }

    public LobHandler getLobHandler() {
        return lobHandler;
    }

    protected ModuleDmlCreator getModuleDmlCreator() {
        return moduleDmlCreator;
    }

    protected void setModuleDmlCreator(ModuleDmlCreator moduleDmlCreator) {
        this.moduleDmlCreator = moduleDmlCreator;
    }

    protected JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    protected void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    protected DataSourceTransactionManager getTransactionManager() {
        return transactionManager;
    }

    protected void setTransactionManager(
            DataSourceTransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }

    protected void setLobHandler(LobHandler lobHandler) {
        this.lobHandler = lobHandler;
    }

    public Object[] warpSqlArgType(Object[] rawArg) {
        // 请在给参数的时候自行设置,如果数据库为date 则使用new DateValue() 如果是timestemp 则使用 new TimeStampValue
        //如果不设置 spring讲统一按照timestemp处理。这样如果数据库类型个设置的类型不一致。将会出现不经过索引的情况，将会有效率问题。
        if (rawArg == null) return null;
        Object[] newTypeArg = new Object[rawArg.length];
        for (int i = 0; i < rawArg.length; i++) {
            Object val = rawArg[i];
            if (val instanceof java.util.Date) {
                //这里默认都转换为date处理,如果数据库是timestemp，请使用new TimeStampValue代替
                val = new DateValue(val, this);
            }
            newTypeArg[i] = val;
        }
        return newTypeArg;
    }

    /**
     * 根据实体拼接查询条件异常
     *
     * @param t
     * @author 颜培轩
     * @date 2017/12/07
     */
    public WhereCondition createWhereConditionByVo(Object t) throws DaoException {
        WhereCondition whereCondition = new WhereCondition().where1Eq1();
        Field[] fields = t.getClass().getDeclaredFields();
        for (Field field : fields) {
            try {
                WsdNotDbColumn wsdNotDbColumn = field.getAnnotation(WsdNotDbColumn.class);
                if (wsdNotDbColumn != null) continue;
                WsdColumn wsdColumn = field.getAnnotation(WsdColumn.class);
                String columnName;
                if (wsdColumn != null && !StringUtils.isEmpty(wsdColumn.name())) {
                    columnName = wsdColumn.name();
                } else {
                    columnName = field.getName();
                }
                PropertyDescriptor pd = new PropertyDescriptor(field.getName(), t.getClass());
                Method getMethod = pd.getReadMethod();
                whereCondition.andEq(columnName, getMethod.invoke(t));
            } catch (Exception e) {
                throw new DaoException("根据实体拼接查询条件异常", e);
            }
        }
        return whereCondition;
    }
}