package core.dao.vo;

import com.fantasy.app.core.annotation.WsdColumn;

public abstract class BaseVo {

  @WsdColumn(name="c_name")
  private String name;
  public String getName() {
    return name;
  }
  public void setName(String name) {
    this.name = name;
  }
  
  
}
