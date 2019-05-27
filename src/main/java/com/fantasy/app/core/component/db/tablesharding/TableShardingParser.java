package com.fantasy.app.core.component.db.tablesharding;

import java.io.File;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.ServletContext;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

import com.fantasy.app.core.base.BaseDao;
import com.fantasy.app.core.base.ModuleFeatureBean;
import com.fantasy.app.core.component.db.orm.ColumnInfo;
import com.fantasy.app.core.component.db.orm.ModuleParser;
import com.fantasy.app.core.component.db.orm.TableInfo;
import com.fantasy.app.core.util.CollectionUtil;
import com.fantasy.app.core.util.HashSelect;

/**
 * 表拆分服务，只支持主键为字符串的表
 * @author 公众号：18岁fantasy
 * @2015-4-20 @下午4:47:45
 */
public class TableShardingParser {
	
	public static TableShardingParser INSTANCE = new TableShardingParser();
//	private Logger logger = Log.getDetachLogger(LOGTYPE.R);
	private TableShardingParser() {
	}

	public static TableShardingParser getInstance() {
		return INSTANCE;
	}
	private static ConcurrentHashMap<String, Integer>  TABLE_SHARDING_CACHE = new ConcurrentHashMap<String, Integer>();
	private static final String TABLE_SHARDING_XML = "table_sharding.xml";

	public static String  SHARDING_TABLE_DML(String rawTableName,String[] idValues){
		return SHARDING_TABLE_DML(null, rawTableName, idValues);
	}
	public static String SHARDING_TABLE_SEL(String rawTableName,String[] idValues){
		return SHARDING_TABLE_SEL(null, rawTableName,idValues);
	}
	
	public static String  SHARDING_TABLE_DML(String rawTableName,String idValue){
		return SHARDING_TABLE_DML(rawTableName, new String[]{idValue});
	}
	public static String SHARDING_TABLE_SEL(String rawTableName,String idValue){
		if(idValue==null){
			return SHARDING_TABLE_SEL(rawTableName,new String[]{}); 
		}else{
			return SHARDING_TABLE_SEL(rawTableName,new String[]{idValue});
		}
	}
	
	public static  String getShardingTableName(ModuleFeatureBean modulefeatureBean,String rawTableName) throws Exception {
		Class<? extends ModuleFeatureBean> clazz = modulefeatureBean.getClass();
		
		Map<String,ColumnInfo> columnInfos = ModuleParser.getModuleIdColumn(clazz);
		if(!CollectionUtil.hasElement(columnInfos)){
			throw new Exception("未发现id类型的字段，请确保使用了@SiColumn(isId=true)");
		}
		String[] idValues  = new String[columnInfos.size()];
		int i = 0;
		for (Iterator<ColumnInfo> iterator = columnInfos.values().iterator(); iterator.hasNext();) {
			ColumnInfo columnInfo = (ColumnInfo) iterator.next();
			ReflectionUtils.makeAccessible(columnInfo.getField());
			Object obj = ReflectionUtils.getField(columnInfo.getField(), modulefeatureBean);
			if(obj!=null&&!(obj instanceof String)){
				throw new Exception("水平拆分表只支持主键为字符串的字段");
			}
			idValues[i] = String.valueOf(obj);
			i++;
		}
		return rawTableName+"_"+HashSelect.target(idValues, getTableShardingNum(rawTableName));
	}
	public static String  SHARDING_TABLE_DML(String scheamName,String rawTableName,String[] idValues){
		return BaseDao.WSD_TABLE(scheamName,rawTableName+"_"+HashSelect.target(idValues, getTableShardingNum(rawTableName)));
	}
	public static String SHARDING_TABLE_SEL(String scheamName,String rawTableName,String[] idValues){
		if(idValues!=null&&idValues.length!=0){
			return SHARDING_TABLE_DML(scheamName,rawTableName, idValues);
		}else{
			return getAllShardingTableUnion(scheamName,rawTableName);
		}
	}
	public static String getFirstShardingTable(String rawTableName){
		return rawTableName+"_"+0;
	}
	public static String getAllShardingTableUnion(String scheamName,String rawTableName){
		StringBuffer buffer = new StringBuffer();
		buffer.append(" (");
		int shardingNum = getTableShardingNum(rawTableName);
		for (int i = 0; i < shardingNum; i++) {
			buffer.append(" select * from ");
			buffer.append(BaseDao.WSD_TABLE(scheamName,rawTableName+"_"+i));
			if(i!=shardingNum-1){
				buffer.append(" union all ");
			}else{
				buffer.append(")");
			}
		}
		return buffer.toString();
	}
	public static int getTableShardingNum(String tableName){
		if(tableIsSharding(tableName)){
			return TABLE_SHARDING_CACHE.get(tableName.toUpperCase());
		}
		return 0;
	}
	public static boolean tableIsSharding(Class<? extends ModuleFeatureBean> clazz){
		TableInfo tableInfo = ModuleParser.getModuleTable(clazz);
		Integer num = TABLE_SHARDING_CACHE.get(tableInfo.getTable().toUpperCase());
		if(num==null)return false;
		return true;
	}
	public static boolean tableIsSharding(String tableName){
		Integer num = TABLE_SHARDING_CACHE.get(tableName.toUpperCase());
		if(num==null)return false;
		return true;
	}
	public static void putTableSharding(String tableName ,int shardingNum){
		TABLE_SHARDING_CACHE.put(tableName.toUpperCase(), shardingNum);
	}
	
	public void parase(ServletContext servletContext) {
		SAXReader saxReader = new SAXReader();
		try {
			String mqFile = TableShardingParser.class.getClassLoader().getResource(TABLE_SHARDING_XML).getFile();
			if(StringUtils.hasText(mqFile)){
				
				File f = new File(mqFile);
				if(f.exists()){
					if(servletContext!=null){
						servletContext.log("开始解析水平拆表信息...");
					}
					//修改之前的new File(mqFile)为上面已定义的f
					Document mqDoc = saxReader.read(f);
					parserTableSharding(mqDoc,servletContext);
					if(servletContext!=null){
						servletContext.log("解析水平拆表信息成功");
					}
				}
			}
		} catch (Exception e) {
			if(servletContext!=null){
				servletContext.log("解析" + TABLE_SHARDING_XML + "出错", e);
			}
		}
	}
	@SuppressWarnings("unchecked")
	private void parserTableSharding(Document mqDoc,ServletContext servletContext ) {
		List<Element> tableShardings = mqDoc.selectNodes("/table_sharding/table");
		if(tableShardings!=null){
			for (Iterator<Element> iterator = tableShardings.iterator(); iterator
					.hasNext();) {
				Element tableSharding = (Element) iterator.next();
				String rawTable = tableSharding.attributeValue("name");
				String num = tableSharding.attributeValue("sharding_num");
				Integer shardingNum = 0;
				if(StringUtils.hasText(rawTable)){
					try {
						shardingNum = Integer.parseInt(num);
					} catch (Exception e) {
						//
					}
				}
				if(shardingNum==0)continue;
				putTableSharding(rawTable.toUpperCase(), shardingNum);
				if(servletContext!=null){
					servletContext.log("发现表拆分："+rawTable+",拆分个数"+shardingNum);
				}
			}
		}
	}
//	public static void main(String[] args) {
//		String mqFile = TableShardingParser.class.getClassLoader().getResource(TABLE_SHARDING_XML).getFile();
//		System.out.println(mqFile);
//	}
}
