package com.fantasy.app.core.base;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.fantasy.app.core.component.pager.MemoryPagerData;
import com.fantasy.app.core.component.pager.Pager;

import java.util.Arrays;
import java.util.LinkedHashMap;


/**
 * 服务层操作基类，如果有自定义的服务类，请继承此方法
 * @日期：2012-12-14下午11:06:35
 * @作者：公众号：18岁fantasy
 */
@Component
@Lazy
public class BaseService {
	@Autowired
	private BaseDao baseDao;

	public Dao getDao() {
		return CmCommon.getDao();
	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	/**
	 * 根据内存数据进行分页显示
	 * @param desc 
	 * @param memoryData LinkedHashMap 数据源
	 * @param pager 
	 * @return
	 */
	public static MemoryPagerData getPagerListFromMemoryData(String desc, LinkedHashMap memoryData, Pager pager) {
		MemoryPagerData pageData = new MemoryPagerData();
		if (memoryData == null || memoryData.isEmpty()) {
			pageData.setDatas(new LinkedHashMap(0));
			pageData.setTotal(0);
			return pageData;
		}
		int total = memoryData.size();
		int offset = pager.getPg().getOffset();
		int pagerSize = pager.getPg().getPagesize();
		int pagesize = pager.getPg().getPagesize() == 0 ? 1 : pager.getPg().getPagesize();
		int totalpage = (Integer) (total % pagesize) == 0 ? (total / pagesize) : (total / pagesize + 1);
		if (total < (offset + 1)) {
			if (pager.getPg().getOffset() != 0) {
				pager.getPg().setOffset((totalpage - 1) * pagesize);
			}
		}

		Object[] keys = memoryData.keySet().toArray();
		int to = offset + pagerSize;
		if (to > (total - 1))
			to = total - 1;
		Object[] returnKeys = Arrays.copyOfRange(keys, offset, to);

		LinkedHashMap returnDatas = new LinkedHashMap(pagerSize);
		for (int i = 0; i < returnKeys.length; i++) {
			returnDatas.put(returnKeys[i], memoryData.get(returnKeys[i]));
		}
		pageData.setTotal(memoryData.size());
		pageData.setDatas(returnDatas);
		return pageData;
	}

	public BaseDao getBaseDao() {
		return baseDao;
	}

	public static String WSD_TABLE(String tableName) {
		return BaseDao.WSD_TABLE(tableName);
	}
	/**
	 * DML(insert update delete)表拆分的支持 目前只支持表后缀为_数字的，拆分
	 * @param rawTableName
	 * @param id
	 * @return
	 */
	public static String  SHARDING_TABLE_DML(String rawTableName,String id){
		return BaseDao.SHARDING_TABLE_DML(rawTableName, id);
	}
	/**
	 * 查询，当没有给定id时，为全表查询
	 * @param rawTableName
	 * @param id
	 * @return
	 */
	public static String SHARDING_TABLE_SEL(String rawTableName,String id){
		return BaseDao.SHARDING_TABLE_SEL(rawTableName, id);
	}


}
