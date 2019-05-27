package com.fantasy.app.core.component.db.orm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

import com.fantasy.app.core.annotation.WsdColumn;
import com.fantasy.app.core.base.BaseDao;
import com.fantasy.app.core.base.ModuleFeatureBean;
import com.fantasy.app.core.component.db.LobValue;
import com.fantasy.app.core.component.db.tablesharding.TableShardingParser;
import com.fantasy.app.core.util.CollectionUtil;
import com.fantasy.app.core.util.HashSelect;
import com.fantasy.app.core.util.UUIDGenerator;

/**
 * 系统提供的正对module的dml处理类
 *
 * @日期：2012-12-14下午11:17:48
 * @作者：公众号：18岁fantasy
 */
public class DefaultDmlCreateor implements ModuleDmlCreator {

    NullColumnHander nullColumnHander = new DefaultNullColumnHander();

    @Override
    public SqlArg createInsert(ModuleFeatureBean modulefeatureBean, String[] executeFields) throws Exception {

        Class<? extends ModuleFeatureBean> clazz = modulefeatureBean.getClass();
        TableInfo tableInfo = ModuleParser.getModuleTable(clazz);
        Map<String, ColumnInfo> columnInfos = ModuleParser.getModuleColumn(clazz);

        StringBuilder insertStatement = new StringBuilder();

        Object[] args = new Object[columnInfos.size()];

        insertStatement.append("Insert Into ");
        if (tableInfo != null) {
            if (tableInfo.isSharding()) {
                insertStatement.append(BaseDao.WSD_TABLE(tableInfo.getSchema(), TableShardingParser.getShardingTableName(modulefeatureBean, tableInfo.getTable())));
            } else {
                insertStatement.append(BaseDao.WSD_TABLE(tableInfo.getSchema(), tableInfo.getTable()));
            }
        }
        insertStatement.append(" (");
        int columnCount = 0;
        for (Iterator<ColumnInfo> iterator = columnInfos.values().iterator(); iterator.hasNext(); ) {
            ColumnInfo columnInfo = (ColumnInfo) iterator.next();
            if (!columnInfo.isDbColumn()) continue;
            ReflectionUtils.makeAccessible(columnInfo.getField());
            Object obj = ReflectionUtils.getField(columnInfo.getField(), modulefeatureBean);
            //hander null
            if (obj == null) {
                if (columnInfo.isNullable()) {
                    obj = nullColumnHander.warpNull(columnInfo);
                } else {
                    continue;
                }
                if (columnInfo.isId() && columnInfo.isUuid()) {
                    obj = UUIDGenerator.getUUID();
                }
            }
            //hander blob
            if (obj instanceof byte[]) {
                obj = new LobValue((byte[]) obj);
            }
            //hander clob
            if (obj instanceof String && WsdColumn.TYPE.CLOB == columnInfo.getType()) {
                obj = new LobValue((String) obj);
            }

            args[columnCount] = obj;

            columnCount++;
            if (columnCount > 1) {
                insertStatement.append(", ");
            }
            insertStatement.append(columnInfo.getColumnName());
        }


        insertStatement.append(") Values(");
        if (columnCount < 1) {
            throw new InvalidDataAccessApiUsageException("Unable to locate columns for table '" +
                    tableInfo.getTable() + "' so an insert statement can't be generated");
        }
        for (int i = 0; i < columnCount; i++) {
            if (i > 0) {
                insertStatement.append(", ");
            }
            insertStatement.append("?");
        }
        insertStatement.append(")");

        Object[] theEndArg = Arrays.copyOf(args, columnCount);

        return new SqlArg(insertStatement.toString(), theEndArg);
    }


    public SqlArg createUpdate(ModuleFeatureBean modulefeatureBean, WhereCondition updateParameter, boolean executeAllNull, String[] executeFields) throws Exception {
        String noUpdateParameterErrorMsg = " update 没有where 参数！难道是批量全表修改？不支持此危险操作！";
        if (updateParameter == null || updateParameter.getWhereCondition().isEmpty())
            throw new Exception(noUpdateParameterErrorMsg);
        SqlArg where = SqlCreator.createWhere(updateParameter, true);
        if (where == null || !StringUtils.hasText(where.getSql())) throw new Exception(noUpdateParameterErrorMsg);

        Class<? extends ModuleFeatureBean> clazz = modulefeatureBean.getClass();
        TableInfo tableInfo = ModuleParser.getModuleTable(clazz);
        Map<String, ColumnInfo> columnInfos = ModuleParser.getModuleColumn(clazz);
        if (columnInfos.isEmpty()) throw new Exception("无 set 参数！");
        StringBuilder updateStatement = new StringBuilder();

        Object[] args = new Object[columnInfos.size()];

        updateStatement.append("Update ");
        if (tableInfo.isSharding()) {
            updateStatement.append(TableShardingParser.getShardingTableName(modulefeatureBean, tableInfo.getTable()));
        } else {
            updateStatement.append(BaseDao.WSD_TABLE(tableInfo.getSchema(), tableInfo.getTable()));
        }
        int columnCount = 0;
        List<String> ingnoreColumns = arrayToList(executeFields, true, false);
        for (Iterator<ColumnInfo> iterator = columnInfos.values().iterator(); iterator.hasNext(); ) {
            ColumnInfo columnInfo = (ColumnInfo) iterator.next();
            //如果在ignore列表里面就不更新此字段
            if (ingnoreColumns.contains(columnInfo.getColumnName().toUpperCase()) ||
                    !columnInfo.isDbColumn()) {
                continue;
            }
            ReflectionUtils.makeAccessible(columnInfo.getField());
            Object obj = ReflectionUtils.getField(columnInfo.getField(), modulefeatureBean);
            //hander null
            if (obj == null) {
                if (executeAllNull) {
                    continue;
                } else {
                    if (columnInfo.isNullable()) {
                        obj = nullColumnHander.warpNull(columnInfo);
                    } else {
                        continue;
                    }
                }

            }
            //hander blob
            if (obj instanceof byte[]) {
                obj = new LobValue((byte[]) obj);
            }
            //hander clob
            if (obj instanceof String && WsdColumn.TYPE.CLOB == columnInfo.getType()) {
                obj = new LobValue((String) obj);
            }

            args[columnCount] = obj;

            columnCount++;
            if (columnCount > 1) {
                updateStatement.append(", ");
            }
            if (columnCount == 1) {
                updateStatement.append(" Set ");
            }
            updateStatement.append(columnInfo.getColumnName())
                    .append(" =? ");
        }

        Object[] theEndArg = Arrays.copyOf(args, columnCount);
        updateStatement.append(where.getSql());

        List<Object> allArgs = new ArrayList<Object>();
        allArgs.addAll(Arrays.asList(theEndArg));
        allArgs.addAll(Arrays.asList(where.getArgs()));
        return new SqlArg(updateStatement.toString(), allArgs.toArray());
    }

    /**
     * 将数组装换为list
     *
     * @param executeFields
     * @param allUper
     * @param allLower
     * @return empty list or arraylist
     */
    private List<String> arrayToList(String[] executeFields, boolean allUper, boolean allLower) {
        List<String> newList = new ArrayList<String>();
        if (executeFields == null || executeFields.length == 0) return newList;
        for (int i = 0; i < executeFields.length; i++) {
            String ele = executeFields[i];
            if (allUper) ele = ele.toUpperCase();
            if (allLower) ele = ele.toLowerCase();
            newList.add(ele);
        }
        return newList;
    }


    @Override
    public SqlArg createQuery(Class<? extends ModuleFeatureBean> classToQuery,
                              WhereCondition searchParameter) throws Exception {
        Class<? extends ModuleFeatureBean> clazz = classToQuery;
        TableInfo tableInfo = ModuleParser.getModuleTable(clazz);
        Map<String, ColumnInfo> columnInfos = ModuleParser.getModuleColumn(clazz);
        if (columnInfos == null || columnInfos.isEmpty()) {
            throw new RuntimeException(clazz + " is not in " + ModuleParser.MODULE_PACKAGE);
        }
        StringBuilder queryStatement = new StringBuilder();

        queryStatement.append("SELECT ");

        int columnCount = 0;
        for (Iterator<ColumnInfo> iterator = columnInfos.values().iterator(); iterator.hasNext(); ) {
            ColumnInfo columnInfo = (ColumnInfo) iterator.next();
            if (!columnInfo.isDbColumn()) continue;
            columnCount++;
            if (columnCount > 1) {
                queryStatement.append(", ");
            }
            queryStatement.append(columnInfo.getColumnName());
        }

        queryStatement.append(" FROM ");
        if (tableInfo.isSharding()) {
            queryStatement.append(TableShardingParser.SHARDING_TABLE_SEL(tableInfo.getSchema(), tableInfo.getTable(), null));
        } else {
            queryStatement.append(BaseDao.WSD_TABLE(tableInfo.getSchema(), tableInfo.getTable()));
        }

        //Object[] theEndArg = Arrays.copyOf(args, columnCount);
        List<Object> allArgs = new ArrayList<Object>();
        //allArgs.addAll(Arrays.asList(theEndArg));
        if (searchParameter != null && !searchParameter.getWhereCondition().isEmpty()) {
            SqlArg where = SqlCreator.createWhere(searchParameter, true);
            if (where != null && StringUtils.hasText(where.getSql())) {
                queryStatement.append(where.getSql());
            }
            allArgs.addAll(Arrays.asList(where.getArgs()));
        }
        return new SqlArg(queryStatement.toString(), allArgs.toArray());
    }


    @Override
    public SqlArg createDelete(Class<? extends ModuleFeatureBean> classToQuery,
                               WhereCondition deleteParameter) throws Exception {

        String noUpdateParameterErrorMsg = " delete 没有where 参数！难道是批量全表删除？不支持此危险操作！";
        if (deleteParameter == null || deleteParameter.getWhereCondition().isEmpty())
            throw new Exception(noUpdateParameterErrorMsg);
        SqlArg where = SqlCreator.createWhere(deleteParameter, true);
        if (where == null || !StringUtils.hasText(where.getSql())) throw new Exception(noUpdateParameterErrorMsg);

        Class<? extends ModuleFeatureBean> clazz = classToQuery;
        TableInfo tableInfo = ModuleParser.getModuleTable(clazz);

        StringBuilder deleteStatement = new StringBuilder();

        deleteStatement.append("Delete From ");
        if (tableInfo.isSharding()) {
            deleteStatement.append(BaseDao.WSD_TABLE(tableInfo.getSchema(), TableShardingParser.SHARDING_TABLE_SEL(tableInfo.getSchema(), tableInfo.getTable(), null)));
        } else {
            deleteStatement.append(BaseDao.WSD_TABLE(tableInfo.getSchema(), tableInfo.getTable()));
        }

        deleteStatement.append(where.getSql());

        return new SqlArg(deleteStatement.toString(), where.getArgs());
    }

    @Override
    public BatchSqlArg createBatchInsert(
            List<? extends ModuleFeatureBean> modulefeatureBeans, String[] executeFields) {

        if (!CollectionUtil.hasElement(modulefeatureBeans)) return null;

        Class<? extends ModuleFeatureBean> clazz = modulefeatureBeans.get(0).getClass();
        TableInfo tableInfo = ModuleParser.getModuleTable(clazz);
        Map<String, ColumnInfo> columnInfos = ModuleParser.getModuleColumn(clazz);

        StringBuilder insertStatement = new StringBuilder();
        insertStatement.append("Insert Into ");
        if (tableInfo != null) {
            if (tableInfo.isSharding()) {
                //批量无法使用bean模式的sharding，只能使用sql语句
                insertStatement.append(BaseDao.WSD_TABLE(tableInfo.getSchema(), TableShardingParser.getFirstShardingTable(tableInfo.getTable())));
            } else {
                insertStatement.append(BaseDao.WSD_TABLE(tableInfo.getSchema(), tableInfo.getTable()));
            }
        }
        insertStatement.append(" (");
        int columnCount = 0;
        List<Object[]> argsList = new ArrayList<Object[]>(modulefeatureBeans.size());
        //计算用
        List<List<Object>> argsListMid = new ArrayList<List<Object>>();
        for (Iterator<ColumnInfo> iterator = columnInfos.values().iterator(); iterator.hasNext(); ) {
            ColumnInfo columnInfo = (ColumnInfo) iterator.next();
            if (!columnInfo.isDbColumn()) continue;
            ReflectionUtils.makeAccessible(columnInfo.getField());
            int beanIndex = 0;
            for (Iterator<? extends ModuleFeatureBean> iterator2 = modulefeatureBeans.iterator(); iterator2.hasNext(); ) {
                ModuleFeatureBean modulefeatureBean = iterator2.next();

                if (columnCount == 0) {
                    List<Object> beanArg = new ArrayList<Object>();
                    argsListMid.add(beanIndex, beanArg);
                }
                Object obj = ReflectionUtils.getField(columnInfo.getField(), modulefeatureBean);
                //hander null
                if (obj == null) {
                    obj = nullColumnHander.warpNull(columnInfo);
                }
                //hander blob
                if (obj instanceof byte[]) {
                    obj = new LobValue((byte[]) obj);
                }
                //hander clob
                if (obj instanceof String && WsdColumn.TYPE.CLOB == columnInfo.getType()) {
                    obj = new LobValue((String) obj);
                }
                List<Object> beanArg = argsListMid.get(beanIndex);
                beanArg.add(columnCount, obj);
                beanIndex++;
            }
            columnCount++;
            if (columnCount > 1) {
                insertStatement.append(", ");
            }
            insertStatement.append(columnInfo.getColumnName());
        }
        insertStatement.append(") Values(");
        if (columnCount < 1) {
            throw new InvalidDataAccessApiUsageException("Unable to locate columns for table '" +
                    tableInfo.getTable() + "' so an insert statement can't be generated");
        }
        for (int i = 0; i < columnCount; i++) {
            if (i > 0) {
                insertStatement.append(", ");
            }

            insertStatement.append("?");
        }
        for (int i = 0; i < modulefeatureBeans.size(); i++) {
            argsList.add(argsListMid.get(i).toArray());
        }
        insertStatement.append(")");
        /*System.out.println(insertStatement.toString());
        CollectionUtil.printCollection(argsList);*/
        return new BatchSqlArg(insertStatement.toString(), argsList);
    }

    @Override
    public WhereCondition createByIdCondition(ModuleFeatureBean modulefeatureBean,
                                              String[] executeFields, boolean withWhere) throws Exception {
        Class<? extends ModuleFeatureBean> clazz = modulefeatureBean.getClass();

        WhereCondition whereCondition = new WhereCondition();

        Map<String, ColumnInfo> columnInfos = ModuleParser.getModuleIdColumn(clazz);
        if (!CollectionUtil.hasElement(columnInfos)) {
            throw new Exception("未发现id类型的字段，请确保使用了@SiColumn(isId=true)");
        }
        if (withWhere) {
            whereCondition.where();
        }
        for (Iterator<ColumnInfo> iterator = columnInfos.values().iterator(); iterator.hasNext(); ) {
            ColumnInfo columnInfo = (ColumnInfo) iterator.next();
            ReflectionUtils.makeAccessible(columnInfo.getField());
            Object obj = ReflectionUtils.getField(columnInfo.getField(), modulefeatureBean);
            //hander null
            if (obj == null) {
                continue;
            }
            //hander blob
            if (obj instanceof byte[]) {
                obj = new LobValue((byte[]) obj);
            }
            //hander clob
            if (obj instanceof String && WsdColumn.TYPE.CLOB == columnInfo.getType()) {
                obj = new LobValue((String) obj);
            }
            whereCondition.eq(columnInfo.getColumnName(), obj);
        }

        return whereCondition;
    }

    @Override
    public WhereCondition createByIdCondition(
            Class<? extends ModuleFeatureBean> classToQuery, Object[] idValues, boolean withWhere)
            throws Exception {

        WhereCondition whereCondition = new WhereCondition();

        Map<String, ColumnInfo> columnInfos = ModuleParser.getModuleIdColumn(classToQuery);
        if (!CollectionUtil.hasElement(columnInfos)) {
            throw new Exception("未发现id类型的字段，请确保使用了@WsdColumn(isId=true)");
        }
        if (idValues == null || idValues.length == 0) {
            throw new Exception("没有给id赋值");
        }
        if (withWhere) {
            whereCondition.where();
        }
        int order = 0;
        for (Iterator<ColumnInfo> iterator = columnInfos.values().iterator(); iterator.hasNext(); ) {
            ColumnInfo columnInfo = (ColumnInfo) iterator.next();
            ReflectionUtils.makeAccessible(columnInfo.getField());
            Object obj = idValues[order];
            if (order > 0) {
                whereCondition.and();//添加上and
            }
            order++;
            //hander null
            if (obj == null) {
                continue;
            }
            //hander blob
            if (obj instanceof byte[]) {
                obj = new LobValue((byte[]) obj);
            }
            //hander clob
            if (obj instanceof String && WsdColumn.TYPE.CLOB == columnInfo.getType()) {
                obj = new LobValue((String) obj);
            }
            whereCondition.eq(columnInfo.getColumnName(), obj);
        }

        return whereCondition;
    }


    @Override
    public Object[] createByIdConditionAndShardingTable(
            ModuleFeatureBean modulefeatureBean, String[] executeFields,
            boolean withWhere) throws Exception {

        Class<? extends ModuleFeatureBean> clazz = modulefeatureBean.getClass();
        TableInfo tableInfo = ModuleParser.getModuleTable(clazz);
        String rawTableName = tableInfo.getTable();
        WhereCondition whereCondition = new WhereCondition();

        Map<String, ColumnInfo> columnInfos = ModuleParser.getModuleIdColumn(clazz);
        if (!CollectionUtil.hasElement(columnInfos)) {
            throw new Exception("未发现id类型的字段，请确保使用了@SiColumn(isId=true)");
        }
        if (withWhere) {
            whereCondition.where();
        }
        String[] idValues = new String[columnInfos.size()];
        int i = 0;
        for (Iterator<ColumnInfo> iterator = columnInfos.values().iterator(); iterator.hasNext(); ) {
            ColumnInfo columnInfo = (ColumnInfo) iterator.next();
            ReflectionUtils.makeAccessible(columnInfo.getField());
            Object obj = ReflectionUtils.getField(columnInfo.getField(), modulefeatureBean);
            if (obj != null && !(obj instanceof String)) {
                throw new Exception("水平拆分表只支持主键为字符串的字段");
            }
            idValues[i] = String.valueOf(obj);
            i++;
            //hander null
            if (obj == null) {
                continue;
            }
            //hander blob
            if (obj instanceof byte[]) {
                obj = new LobValue((byte[]) obj);
            }
            //hander clob
            if (obj instanceof String && WsdColumn.TYPE.CLOB == columnInfo.getType()) {
                obj = new LobValue((String) obj);
            }
            whereCondition.eq(columnInfo.getColumnName(), obj);
        }
        String shardingTable = rawTableName + "_" + HashSelect.target(idValues, TableShardingParser.getTableShardingNum(rawTableName));
        return new Object[]{shardingTable, whereCondition};
    }


    @Override
    public SqlArg createShardingDelete(ModuleFeatureBean moduleFeatureBean,
                                       WhereCondition deleteParameter) throws Exception {
        String noUpdateParameterErrorMsg = " delete 没有where 参数！难道是批量全表删除？不支持此危险操作！";
        if (deleteParameter == null || deleteParameter.getWhereCondition().isEmpty())
            throw new Exception(noUpdateParameterErrorMsg);
        SqlArg where = SqlCreator.createWhere(deleteParameter, true);
        if (where == null || !StringUtils.hasText(where.getSql())) throw new Exception(noUpdateParameterErrorMsg);

        Class<? extends ModuleFeatureBean> clazz = moduleFeatureBean.getClass();
        TableInfo tableInfo = ModuleParser.getModuleTable(clazz);

        StringBuilder deleteStatement = new StringBuilder();

        deleteStatement.append("Delete From ");
        deleteStatement.append(BaseDao.WSD_TABLE(tableInfo.getSchema(), TableShardingParser.getShardingTableName(moduleFeatureBean, tableInfo.getTable())));

        deleteStatement.append(where.getSql());

        return new SqlArg(deleteStatement.toString(), where.getArgs());
    }
}
