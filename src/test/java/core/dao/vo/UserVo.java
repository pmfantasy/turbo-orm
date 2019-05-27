package core.dao.vo;

import java.util.Arrays;
import java.util.Date;

import com.fantasy.app.core.annotation.WsdColumn;
import com.fantasy.app.core.annotation.WsdColumn.TYPE;
import com.fantasy.app.core.annotation.WsdNotDbColumn;
import com.fantasy.app.core.annotation.WsdTable;
import com.fantasy.app.core.base.ModuleFeatureBean;

@WsdTable(name="T_USER")
public class UserVo extends ModuleFeatureBean{
  
    /**
     * 用户id
     */
    @WsdColumn(isId=true)
	private String id;
	/**
	 * 用户名
	 */
    private String name;
    
    /**
     * 用户邮箱
     */
    private String email;
    
    /**
     * 简历
     */
    @WsdColumn(type=TYPE.CLOB)
    private String  resume;
    /**
     * 头像图片
     */
    @WsdColumn(type=TYPE.BLOB,name="head_sculpture")
    private byte[] headSculpture;
    
    /**
     * 时间戳
     */
    private Date ts;
    
    /**
     * 接收界面传递的值用于server的属性，不往数据库插入
     */
    @WsdNotDbColumn
    private String token;
    
    public String getId() {
      return id;
    }
    public void setId(String id) {
      this.id = id;
    }
    public String getName() {
      return name;
    }
    public void setName(String name) {
      this.name = name;
    }
    public String getEmail() {
      return email;
    }
    public void setEmail(String email) {
      this.email = email;
    }
    public byte[] getHeadSculpture() {
      return headSculpture;
    }
    public void setHeadSculpture(byte[] headSculpture) {
      this.headSculpture = headSculpture;
    }
    
    public String getResume() {
      return resume;
    }
    public void setResume(String resume) {
      this.resume = resume;
    }
    
    public Date getTs() {
      return ts;
    }
    public void setTs(Date ts) {
      this.ts = ts;
    }
    @Override
    public int hashCode() {
      return super.hashCode();
    }
    @Override
    public boolean equals(Object obj) {
      return super.equals(obj);
    }
    @Override
    protected Object clone() throws CloneNotSupportedException {
      return super.clone();
    }
    @Override
    public String toString() {
      return "UserVo [id=" + id + ", name=" + name + ", email=" + email + ", resume=" + resume
          + ", headSculpture=" + Arrays.toString(headSculpture) + ", ts=" + ts + ", token=" + token
          + "]";
    }
	
}
