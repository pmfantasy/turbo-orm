package com.fantasy.app.core.component.monitor.jmx;

import java.util.Set;

import javax.management.MBeanAttributeInfo;
import javax.management.MBeanInfo;
import javax.management.ObjectName;

import com.fantasy.app.core.component.monitor.jmx.vo.WebServerMonitor;
import com.fantasy.app.core.exception.JmxException;



/**
 * tongweb容器监控
 * @author Administrator
 *
 */
public class TongWebMonitorService extends AbstractMonitorService {

	@Override
	public WebServerMonitor monitorServer() throws JmxException {
		checkConn();
		//查找request-processing 还是写死的,tongweb返回的该属性值是string类型--这个对应的是请求处理的配置
		//int maxThreads=Integer.parseInt((String) getAttribute("com.tongtech.tongweb:type=request-processing,config=server,category=config", "thread-count"));
		
		//从Selector中查找，对应的http-listener-1中的配置 排除9060端口
		Set<ObjectName> beans=getMBeanNames("com.tongtech.tongweb:name=http*,type=Selector");
		System.out.println("beans大小:"+beans.size());
		WebServerMonitor monitor=null;
		for (ObjectName objectName : beans) {
			  MBeanInfo info=getMBeanInfo(objectName.getCanonicalName());
			  MBeanAttributeInfo[] attrs= info.getAttributes();
			  System.out.println("*************************"+objectName.getCanonicalName()+"*******************************");
			  for (MBeanAttributeInfo in : attrs) {
				System.out.println(in.getName());
			}
			if (!objectName.getCanonicalKeyPropertyListString().contains("9060")) {
				int maxThreads=(Integer) getAttribute(objectName.getCanonicalName(), "maxThreadsStats");//最大允许的线程数
				int active=(Integer) getAttribute(objectName.getCanonicalName(), "currentThreadCountStats");//当前存在的线程数
				int busy=(Integer) getAttribute(objectName.getCanonicalName(), "currentThreadsBusyStats");//正处于工作的线程数
				monitor=new WebServerMonitor(maxThreads, active, busy);
				System.out.println("打印:"+monitor);
			}
		}
		return monitor;
	}

}
