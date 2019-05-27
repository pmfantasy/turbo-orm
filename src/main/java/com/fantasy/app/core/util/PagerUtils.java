package com.fantasy.app.core.util;

import com.fantasy.app.core.component.pager.Pager;
import com.fantasy.app.core.component.pager.Pg;
/**
 * 
* @ClassName: PagerUtils
* @Description: 分页工具
* @author Json工具类
* @date 2017年8月9日 下午2:18:35
* Update Logs:
* ****************************************************
* Name:
* Date:
* Description:
******************************************************
*
 */
public class PagerUtils {
	
	/**
	 * 
	* @Title: getPager
	* @Description: 使用当前页和页容量  获取分页对象
	* @author 王明盛
	* @date  2017年8月9日 下午2:18:32
	* @param rows 页容量
	* @param page 当前页
	* @return Pager对象
	 */
	public static Pager getPager(Integer pageSize,Integer currentPage) {
		Pager pager = new Pager();
		Pg pg = new Pg();
		pg.setCurrentPage(currentPage==null?1:currentPage);
		pg.setPagesize(pageSize==null?1:pageSize);
		pager.setPg(pg);
		return pager;
	}
	
}
