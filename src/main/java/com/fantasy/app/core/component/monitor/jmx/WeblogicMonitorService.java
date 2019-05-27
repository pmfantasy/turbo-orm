package com.fantasy.app.core.component.monitor.jmx;

import java.io.IOException;
import java.util.Set;

import javax.management.MBeanServerConnection;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;

import com.fantasy.app.core.component.monitor.jmx.JmxConnectionFactory.JmxType;
import com.fantasy.app.core.component.monitor.jmx.vo.WebServerMonitor;
import com.fantasy.app.core.exception.JmxException;

/**
 * WeblogicMonitorService
 * @author 公众号：18岁fantasy
 * 2017-5-25 下午3:39:07
 */
public class WeblogicMonitorService extends AbstractMonitorService {

	
	/**
	 * 目前只监控AdminServer服务器，如果项目部署到了一个受管服务器下，那就不支持了。做起来比较麻烦
	 * @throws JmxException 
	 */
	@Override
	public WebServerMonitor monitorServer() throws JmxException {
		checkConn();
		Set<ObjectName> set=getMBeanNames("com.bea:Name=*,Type=Server");
		ObjectName objName=null;
		//选择 "com.bea:Name=AdminServer,Type=Server"
		if (set!=null && !set.isEmpty()) {
			if (set.size()==1) {//如果只有一个,那么就取第一个
				for (ObjectName objectName : set) {
					objName=objectName;
					break;
				}
			}else{
				for (ObjectName objectName : set) {
					if ("com.bea:Name=AdminServer,Type=Server".equals(objectName.getCanonicalName())) {//选择主服务
						objName=objectName;
						break;
					}
				}
				//如果还没有,那就随便取一个
				for (ObjectName objectName : set) {
					objName=objectName;
					break;
				}
			}
		}
		if (objName==null) {
			return null;
		}
	    //
		String defaultNameProperty=objName.getKeyProperty("Name");//获取name属性
		System.out.println("获取的objectName["+objName.getCanonicalName()+"]");
		//线程配置相关(在server下)
		//服务器最小线程数据，默认值是1，最大可以设置65534
		int minThreads=(Integer) getAttribute("com.bea:Name="+defaultNameProperty+",Type=Server", "SelfTuningThreadPoolSizeMin");
		//服务器最大线程数量，默认是400，最大可以设置65534
		int maxThreads=(Integer) getAttribute("com.bea:Name="+defaultNameProperty+",Type=Server", "SelfTuningThreadPoolSizeMax");
				
		
		//线程池相关信息(在ServerRuntime-下的ThreadPoolRuntime)
		ObjectName threadPoolRuntime= (ObjectName) getAttribute("com.bea:Name="+defaultNameProperty+",Type=ServerRuntime", "ThreadPoolRuntime");
		int total=(Integer) getAttribute(threadPoolRuntime.getCanonicalName(), "ExecuteThreadTotalCount");//线程总数
		int idle=(Integer) getAttribute(threadPoolRuntime.getCanonicalName(), "ExecuteThreadIdleCount");//空闲线程
		int hogging=(Integer) getAttribute(threadPoolRuntime.getCanonicalName(), "HoggingThreadCount");//独占线程数
		int standby=(Integer) getAttribute(threadPoolRuntime.getCanonicalName(), "StandbyThreadCount");//备用线程数
				//总线程数=total  活动线程数=idle+hogging+standby
		//总线程数=total  total=活动的线程数+standby  正在执行任务的线程数=活动的线程数-idle
		//当通过控制台登陆到console页面后，本身就会占用一个Active Thread
		//总线程数=备用线程+活动线程
		//活动线程=空闲线程+忙碌线程
		//忙碌线程=独占线程+非独占线程
		//非独占线程=阻塞线程+非阻塞独占线程
		WebServerMonitor monitor=new WebServerMonitor(maxThreads,(total-standby),(total-standby-idle));
		return monitor;
	}

	public static void main(String[] args) throws JmxException, MalformedObjectNameException, NullPointerException, IOException {
		String jmxUrl="service:jmx:rmi:///jndi/rmi://"+"192.168.2.104"+":"+"7200"+"/jmxrmi";
		WeblogicMonitorService weblogic=new WeblogicMonitorService();
		/*weblogic.initJmxConn(JmxType.REMOTE, jmxUrl);
		System.out.println(weblogic.monitorJvm());
		System.out.println(weblogic.monitorOs());
		System.out.println(weblogic.monitorRuntime());
		System.out.println(weblogic.monitorServer());*/
		MBeanServerConnection conn=JmxConnectionFactory.getConnection(JmxType.REMOTE, jmxUrl);
		Set<ObjectName> beans=conn.queryNames(null,null);
		System.out.println("bens的个数::"+beans.size());
		for (ObjectName objectName : beans) {
			System.out.println(objectName.getCanonicalName());
		}
	}
}
