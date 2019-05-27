package com.fantasy.app.core.component.monitor.jmx;

import java.util.Set;

import javax.management.ObjectName;

import com.fantasy.app.core.component.monitor.jmx.vo.WebServerMonitor;
import com.fantasy.app.core.exception.JmxException;


public class TomcatMonitorService extends AbstractMonitorService{

	
	
	@Override
	public WebServerMonitor monitorServer() throws JmxException {
		checkConn();
		WebServerMonitor monitor=null; 
		/*Set<ObjectName> s = getMBeanNames("Catalina:type=Manager,*");
		for (ObjectName objectName : s) {
			//只获取当前项目的线程池数据
			if (objectName.getCanonicalKeyPropertyListString().contains("/turbo") || objectName.getCanonicalKeyPropertyListString().contains("/adapter") ) {
				int maxAllowed=(Integer) getAttribute(objectName.getCanonicalName(), "maxActiveSessions");//允许的最大连接数
				int maxActive=(Integer) getAttribute(objectName.getCanonicalName(), "maxActive");//到目前为止最大连接数
				int count=(Integer) getAttribute(objectName.getCanonicalName(), "sessionCounter");//到目前为止总共的连接会话数
				int active=(Integer) getAttribute(objectName.getCanonicalName(), "activeSessions");//此时此刻活动的连接会话数
				monitor=new WebServerMonitor(maxAllowed, maxActive, count, active);
			}
		}*/
		
		/**
		 * 目前是写死的，应该根据项目所在的连接 动态的获取出http-端口号，
		 * 但是这个目前不好获取到，因为无法准确的知道，该项目部署后访问路径是什么
		 */
		Set<ObjectName> s = getMBeanNames("Catalina:type=ThreadPool,*");
		for (ObjectName objectName : s) {
			//只获取当前项目的线程池数据
			if (objectName.getCanonicalKeyPropertyListString().contains("http-")) {
				int maxAllowed=(Integer) getAttribute(objectName.getCanonicalName(), "maxThreads");//最大线程数
				int active=(Integer) getAttribute(objectName.getCanonicalName(), "currentThreadCount");//此时此刻活动的线程数
				int busy=(Integer) getAttribute(objectName.getCanonicalName(), "currentThreadsBusy");//此时此刻正在处理请求的线程
				monitor=new WebServerMonitor(maxAllowed, active,busy);
			}
		}
		return monitor;
	}

}
