package core.dao;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.fantasy.app.core.base.BaseTest;
import com.fantasy.app.core.base.Dao;
import com.fantasy.app.core.component.db.SingleTransationCircleWithOutResult;
import com.fantasy.app.core.component.db.factory.DaoFactory;
import com.fantasy.app.core.component.db.jdbcpool.DataSourceProperty;
import com.fantasy.app.core.component.db.orm.WhereCondition;
import com.fantasy.app.core.component.db.orm.valuebean.BetweenValue;
import com.fantasy.app.core.component.pager.Pager;
import com.fantasy.app.core.component.pager.PagerData;
import com.fantasy.app.core.exception.DaoException;
import com.fantasy.app.core.util.CollectionUtil;
import com.fantasy.app.core.util.FileUtil;
import com.fantasy.app.core.util.UUIDGenerator;

import core.CM;
import core.dao.vo.UserVo;

/**
 * dao单元测试
 * 
 * @author 公众号：18岁fantasy 2019-5-08 下午5:24:01
 */
public class DaoTest extends BaseTest {

  /**
   * 测试插入对象
   */
  @Test
  public void testInsert() {
    try {
      UserVo u = new UserVo();
      u.setId(UUIDGenerator.getUUID());
      u.setName("n_n");
      u.setEmail("email@163.com");
      u.setHeadSculpture(FileUtil.readFileToByteArray(new File("d:\\hp.png")));
      u.setResume("简历内容");
      u.setTs(new Date());
      CM.getDao().insertModule("测试插入对象", u);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * 测试批量insert
   * 
   * @throws IOException
   */
  @Test
  public void testBatchInsert() throws IOException {
    try {
      List<UserVo> users = new ArrayList<UserVo>();
      for (int i = 0; i < 50; i++) {
        UserVo u = new UserVo();
        u.setId(UUIDGenerator.getUUID());
        u.setName("n_" + i);
        u.setEmail("email@" + i + ".com");
        u.setHeadSculpture(FileUtil.readFileToByteArray(new File("d:\\hp.png")));
        u.setResume("简历内容" + i);
        u.setTs(new Date());
        users.add(u);
      }
      CM.getDao().batchInsertModule("测试批量插入对象", users);
    } catch (DaoException e) {
      e.printStackTrace();
    }
  }

  /**
   * 测试列表查询
   */
  @Test
  public void testListModule() {
    try {

      List<UserVo> users = CM.getDao().listModule("测试基于对象查询列表", UserVo.class, null);
      CollectionUtil.printCollection(users);
    } catch (DaoException e) {
      e.printStackTrace();
    }
  }

  /**
   * 测试根据id查询对象
   */
  @Test
  public void testGetById() {
    try {

      String[] idValuds = {"3f1d35b12ab544fdb73badc750e534e2"};
      UserVo user = CM.getDao().getModuleById("根据id获取对象", UserVo.class, idValuds);
      FileUtil.writeByteArrayToFile(new File("d:\\hp2.png"), user.getHeadSculpture());
      System.out.println(user);

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * 测试根据条件查询
   */
  @Test
  public void listModuleByWhereCondition() {
    try {
      WhereCondition condition = new WhereCondition();
      condition.where().eq("name", "n_21");
      List<UserVo> users = CM.getDao().listModule("测试查询", UserVo.class, condition);
      CollectionUtil.printCollection(users);
    } catch (DaoException e) {
      e.printStackTrace();
    }
  }

  /**
   * 测试多条件查询
   */
  @Test
  public void listModuleByMultWhereCondition() {
    try {
      WhereCondition condition = new WhereCondition();
      condition.where1Eq1().andLike("email", "%email%")
          .andGreaterThanOrEq("ts", "2019-5-8 11:08:43").and().isNotNull("resume");
      List<UserVo> users = CM.getDao().listModule("测试查询", UserVo.class, condition);
      CollectionUtil.printCollection(users);
    } catch (DaoException e) {
      e.printStackTrace();
    }
  }

  /**
   * 测试sql语句查询对象
   */
  @Test
  public void listModuleBySql() {
    try {
      WhereCondition condition = new WhereCondition();
      condition.where().greaterThanOrEq("ts", "2019-5-8 14:08:43");
      List<UserVo> users =
          CM.getDao().listModule("测试查询", "select * from t_user  ", UserVo.class, condition);
      CollectionUtil.printCollection(users);
    } catch (DaoException e) {
      e.printStackTrace();
    }
  }

  /**
   * 测试分页查询
   */
  @Test
  public void listPaginationModule() {
    try {
      WhereCondition condition = new WhereCondition();
      condition.where().greaterThanOrEq("ts", "2019-5-8 14:08:43");
      Pager pager = new Pager(0, 10);
      PagerData<UserVo> users =
          CM.getDao().getPagerModuleList("测试分页查询", UserVo.class, condition, pager);
      System.out.println("总条数：" + users.getTotal());
      CollectionUtil.printCollection(users.getRows());
    } catch (DaoException e) {
      e.printStackTrace();
    }
  }

  /**
   * 测试根据sql和条件混合语句分页查询
   */
  @Test
  public void listPaginationModuleBySql() {
    try {
      WhereCondition condition = new WhereCondition();
      condition.andLike("email", "%email%").orderBy("email desc");
      Pager pager = new Pager(0, 10);
      PagerData<UserVo> users = CM.getDao().getPagerModuleList("测试分页查询",
          "select id,name,email,ts from t_user where ts > ?", UserVo.class,
          new Object[] {"2019-5-8 11:41:23"}, condition, pager);
      System.out.println("总条数：" + users.getTotal());
      CollectionUtil.printCollection(users.getRows());
    } catch (DaoException e) {
      e.printStackTrace();
    }
  }


  /**
   * 测试条件删除
   */
  @Test
  public void testDelete() {
    WhereCondition condition = new WhereCondition();
    condition.where().isNotNull("ts").andBetween("ts",
        new BetweenValue("2019-5-8 11:17:51", "2019-5-8 16:17:51", true, true));
    try {
      CM.getDao().deleteModule("测试删除", UserVo.class, condition);
    } catch (DaoException e) {
      e.printStackTrace();
    }
  }

  /**
   * 测试根据id删除
   */
  @Test
  public void testDeleteById() {
    try {
      CM.getDao().deleteModuleById("测试删除", UserVo.class,
          new Object[] {"06e773fb69404bf7b64a156bada11846"});
    } catch (DaoException e) {
      e.printStackTrace();
    }
  }

  /**
   * 测试根据id删除
   */
  @Test
  public void testDeleteByBeanId() {
    try {
      UserVo user = new UserVo();
      user.setId("3f9f00ec99c04cabae099b292f4d9d4e");
      CM.getDao().deleteModuleById("测试删除", user);
    } catch (DaoException e) {
      e.printStackTrace();
    }
  }

  /**
   * 测试批更新
   */
  @Test
  public void testBatchUpdate() {
    try {
      UserVo user = new UserVo();
      user.setName("new_name");
      
      WhereCondition condition = new WhereCondition();
      condition.where().lessThan("Ts", "2019-05-08 15:47:11");
      
      CM.getDao().updateModule("测试删除", user, condition, new String[] {"headSculpture"});//可设置哪些字段不更新
    } catch (DaoException e) {
      e.printStackTrace();
    }
  }
  
  /**
   * 测试根据id更新
   */
  @Test
  public void testUpdateById() {
    try {
      UserVo user = new UserVo();
      user.setId("3cccc60b170848b1a53e3db9d519bd48");
      user.setName("new_name");
      
      CM.getDao().updateModuleById("测试根据id更新", user, new String[] {"headSculpture"});//可设置哪些字段不更新
    } catch (DaoException e) {
      e.printStackTrace();
    }
  }
  /**
   * 测试根据id更新不包含值为空的对象
   */
  @Test
  public void updateModuleByIdExecuteNull() {
    try {
      UserVo user = new UserVo();
      user.setId("bc17b776523241479354f6a4e6a2e26e");
      user.setName("new_name");
      
      CM.getDao().updateModuleByIdExecuteNull("测试根据id更新不包含值为空的对象", user);
    } catch (DaoException e) {
      e.printStackTrace();
    }
  }
  
  /**
   * save or update
   */
  @Test
  public void saveOrUpdateModuleById() {
    try {
      UserVo user = new UserVo();
      user.setId("bc17b776523241479354f6a4e6a2e261");
      user.setName("new_name");
      
      CM.getDao().saveOrUpdateModuleById("save or update", user, new String[] {"headSculpture"});
    } catch (DaoException e) {
      e.printStackTrace();
    }
  }
  /**
   * 事务操作
   */
  @Test
  public void doInSingleTransationCircle() {
    try {
     
      UserVo user = new UserVo();
      user.setId("bc17b776423241479354f6a4e6a2e261");
      user.setName("new_name");
      
      Dao dao =  CM.getDao();
      
      CM.getDao().doInSingleTransationCircle("事务操作", new SingleTransationCircleWithOutResult() {
        
        @Override
        public void actionInCircle() throws RuntimeException {
          try {
            dao.insertModule("插入", user);
            
            user.setEmail("email@1.1");
            WhereCondition condition = new WhereCondition();
            condition.where().lessThan("Ts", "2019-05-08 15:47:11");
            dao.updateModule("修改", user, condition);
            
            dao.deleteModuleById("删除", user);
          } catch (DaoException e) {
            throw new RuntimeException(e);
          }
        }
      });
      
    } catch (DaoException e) {
      e.printStackTrace();
    }
  }
  
  @Test
  public void multDataSource() {
    try {
     
      //默认数据源
      CM.getDao().listModule("测试基于对象查询列表", UserVo.class, null);
      //另外的数据源
      CM.getPGDao().listModule("测试基于对象查询列表", UserVo.class, null);
      
    } catch (DaoException e) {
      e.printStackTrace();
    }
  }
  
  /**
   * 可在非spring环境下使用各种方法。常用场景如，比如要动态添加一个对数据库对其进行监控
   */
  public static void main(String[] args) throws Exception {
    
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
