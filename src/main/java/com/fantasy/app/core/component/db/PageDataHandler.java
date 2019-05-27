package com.fantasy.app.core.component.db;

import java.util.List;
/**
 * 处理分页数据
 * @author joe
 * @time  2016-2-1 下午3:54:46
 */
public interface PageDataHandler<T> {

	public void handerPagerData(List<T> datas);
}
