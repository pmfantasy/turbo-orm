package core;

import com.fantasy.app.core.base.CmCommon;
import com.fantasy.app.core.base.Dao;

/**
 * 服务获取池，在这里面定义出框架支持之外的服务
 * @author 公众号：18岁fantasy
 * @date 2019年5月9日 上午9:09:44
 */
public class CM extends CmCommon{


  private static final String PG_DBNAME = "pg";
  /**
   * 获另外的的数据源操作类
   */
  public static Dao getPGDao() {
    
    return getDao(PG_DBNAME);
    
  }
}
