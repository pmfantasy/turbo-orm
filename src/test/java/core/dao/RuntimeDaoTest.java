package core.dao;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

import com.fantasy.app.core.base.Dao;
import com.fantasy.app.core.component.db.factory.DaoFactory;
import com.fantasy.app.core.component.db.jdbcpool.DataSourceProperty;
import com.fantasy.app.core.exception.DaoException;
import com.fantasy.app.core.util.CollectionUtil;

import core.dao.vo.UserVo;

/**
 * 测试无spring的dao
 * @author 公众号：18岁fantasy
 * 2017-5-11 下午3:56:33
 */
public class RuntimeDaoTest {

	public static void main(String[] args) throws DaoException, ParseException {
	  
	    //数据源参数
		DataSourceProperty dataSourceProperty = new DataSourceProperty();
		dataSourceProperty.setJdbcUrl("jdbc:mysql://localhost:3306/test");
		dataSourceProperty.setUsername("root");
		dataSourceProperty.setPassword("root");
		dataSourceProperty.setInitialSize(1);
		dataSourceProperty.setMaxActive(1);
		
		Dao dmDao = DaoFactory.createDao("动态数据源数据源", dataSourceProperty);
		List<Map<String, Object>> users = dmDao.listMap("查询", "select *  from t_user where ts<?", new Object[] {"2019-5-8 15:08:43"});
	    CollectionUtil.printCollection(users);
	}
}
