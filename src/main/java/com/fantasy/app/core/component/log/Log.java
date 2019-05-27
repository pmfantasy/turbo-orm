package com.fantasy.app.core.component.log;

import java.io.File;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.fantasy.app.core.para.CorePara;
/**
 * 获取日志记录七，为了防止和log4j的日志记录器冲突。这里起名为Log
 * @日期：2012-12-14下午11:22:10
 * @作者：公众号：18岁fantasy
 */
public class Log {

  //log4j.properties默认的日志目录
  private static String default_log4j_config="log4j.properties";
  //log4j.properties用来指定日志目录key的key
  private static String default_log_path_key="log_path";
  static{
      setLog4jHomePath();
  }
  
  /**
   * 设置日志根目录
   * @param servletContext
   */
  private static void setLog4jHomePath(){
      String sysLogPro = System.getProperty(default_log_path_key);
      if(sysLogPro==null||"".equalsIgnoreCase(sysLogPro)){
          String log4jHomePath = CorePara.CoreInitCtx.ZLWORKDIR+File.separator+"logs";
          System.setProperty(default_log_path_key, log4jHomePath);
      }
        try {
            PropertyConfigurator.configure(Log.class.getClassLoader().getResource(default_log4j_config));
        } catch (Exception e) {
            e.printStackTrace();
        }
     
  }
	/**
	 * 获取日志记录器,
	 * @param logType 记录类型
	 * @return
	 */
	public static Logger getLogger(String type) {
		return getLoggerStrategy(type);
	}
	/**
	 * 返回封装后的logger
	 * @param type
	 * @return
	 */
	private  static Logger getLoggerStrategy(String type) {
	    
		Logger logger = org.apache.log4j.Logger.getLogger(type);//com.zlxd.app.core.component.log.Logger.getLogger(type);
		return logger;
	}
	
	public static String getTraceInfoCurrent() {
      StringBuffer sb = new StringBuffer();
      StackTraceElement[] stacks = new Throwable().getStackTrace();
      sb.append("class: ").append(stacks[1].getClassName())
              .append("; method: ").append(stacks[1].getMethodName())
              .append("; number: ").append(stacks[1].getLineNumber());
      return sb.toString();
  }
  public static String getPathInfo(){  
      StringBuffer sb = new StringBuffer(); 
      
      StackTraceElement[] stacks = Thread.currentThread().getStackTrace();   
      if (null == stacks || stacks.length < 4){
          return sb.toString();
      }
      StackTraceElement cus = stacks[3];
      sb.append(cus.getClassName()+"."+cus.getMethodName()+"("+cus.getFileName()+":"+cus.getLineNumber()+")" ); 
      return sb.toString();
  }
   
}
