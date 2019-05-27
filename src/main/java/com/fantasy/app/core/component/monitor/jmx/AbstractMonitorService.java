package com.fantasy.app.core.component.monitor.jmx;

import java.lang.management.MemoryUsage;
import java.util.Date;
import java.util.Set;

import javax.management.MBeanInfo;
import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import javax.management.openmbean.CompositeData;

import com.fantasy.app.core.component.monitor.jmx.JmxConnectionFactory.JmxType;
import com.fantasy.app.core.component.monitor.jmx.vo.JvmMemory;
import com.fantasy.app.core.component.monitor.jmx.vo.JvmMonitor;
import com.fantasy.app.core.component.monitor.jmx.vo.OsMonitor;
import com.fantasy.app.core.component.monitor.jmx.vo.RuntimeMonitor;
import com.fantasy.app.core.exception.JmxException;


/**
 * 抽象jmx类，实现 JvmMonitorService,WebServerMonitorService接口
 * @author Administrator
 *
 */
public abstract class AbstractMonitorService implements JvmMonitorService,WebServerMonitorService{
	
	private MBeanServerConnection conn;

	public AbstractMonitorService() {
		super();
	}
	
	
	public void initJmxConn(JmxType type,Object... args) throws JmxException{
		
		if (type==null) {
			throw new JmxException("JmxType 不能为空");
		}
		this.conn=JmxConnectionFactory.getConnection(type,args);
	}
	
	
	
	protected Object getMBean(String objectName) throws JmxException {
		try {
			return this.conn.getMBeanInfo(new ObjectName(objectName));
		} catch (Exception e) {
			throw new JmxException("获取MBean["+objectName+"]异常",e);
		}
	}
	
	protected Set<ObjectName> getMBeanNames(String objectName) throws JmxException {
		try {
			return this.conn.queryNames(new ObjectName(objectName),null);
		} catch (Exception e) {
			throw new JmxException("获取MBean["+objectName+"]异常",e);
		}
	}
	
	protected MBeanInfo getMBeanInfo(String objectName) throws JmxException {
		try {
			return this.conn.getMBeanInfo(new ObjectName(objectName));
		} catch (Exception e) {
			throw new JmxException("获取MBean["+objectName+"]信息异常",e);
		}
	}
	
	/**
	 * 模糊查询某个域的属性
	 * @param objectName
	 * @param attribute
	 * @return
	 * @throws JmxException
	 */
	protected Object getQueryAttribute(String objectName,String attribute) throws JmxException {
		try {
			Set<ObjectName> set=getMBeanNames(objectName);
			if (set!=null && !set.isEmpty()) {
				for (ObjectName object : set) {
					return this.conn.getAttribute(object,attribute);
				}
			}
		    return getAttribute(objectName, attribute);
		} catch (Exception e) {
			throw new JmxException("获取MBean["+objectName+"]属性["+attribute+"]异常",e);
		}
	}
	
	protected Object getAttribute(String objectName,String attribute) throws JmxException {
		try {
			return this.conn.getAttribute(new ObjectName(objectName),attribute);
		} catch (Exception e) {
			throw new JmxException("获取MBean["+objectName+"]属性["+attribute+"]异常",e);
		}
	}
	
	protected  void checkConn() throws JmxException{
		if (this.conn==null) {
			throw new JmxException("必须调用initConnJmx()方法完成初始化");
		}
	}
	
	
	@Override
	public RuntimeMonitor monitorRuntime() throws JmxException {
		checkConn();
		//jdk名称
		String name=(String) this.getAttribute("java.lang:type=Runtime", "VmName");
		//jdk版本--这个获取不到，因为jdk不是必须的,只有通过VM版本反推出对应的jdk版本
		//String version=System.getProperty("java.version");//(String) this.getAttribute("java.lang:type=Runtime", "VmVersion");
		String version=(String) this.getAttribute("java.lang:type=Runtime", "VmVersion");
		 if(version.startsWith("20.")){
			version="1.6(VM"+version+")";
		}else if(version.startsWith("24.")){
			version="1.7(VM"+version+")";
		}else if (version.startsWith("25.")) {
			version="1.8(VM"+version+")";
		}
		//厂商
		String vendor=(String) this.getAttribute("java.lang:type=Runtime", "VmVendor");
		//启动时间
		Long start=(Long) this.getAttribute("java.lang:type=Runtime", "StartTime");
		return new RuntimeMonitor(name, version, vendor, new Date(start));
	}
	
	@Override
	public JvmMonitor monitorJvm() throws JmxException {
		checkConn();
		JvmMonitor monitor=new JvmMonitor();
		//jvm中线程信息
		//当前线程信息
		int threadCount=(Integer) this.getAttribute("java.lang:type=Threading", "ThreadCount");
		//线程峰值
		int peakThreadCount=(Integer) this.getAttribute("java.lang:type=Threading", "PeakThreadCount");
		//后台进程
		int daemonThreadCount=(Integer) this.getAttribute("java.lang:type=Threading", "DaemonThreadCount");
		
		monitor.setThreadCount(threadCount);
		monitor.setPeakThreadCount(peakThreadCount);
		monitor.setDaemonThreadCount(daemonThreadCount);
			
		//获取堆内存  objectname 属性名--返回的是一个CompositeData复合类型
		MemoryUsage heapMemoryUsage=MemoryUsage.from((CompositeData) this.getAttribute("java.lang:type=Memory", "HeapMemoryUsage"));
		long committed=heapMemoryUsage.getCommitted()/1024/1024;//已经分配的内存
		long used=heapMemoryUsage.getUsed()/1024/1024;//已经使用的内存
		long init=heapMemoryUsage.getInit()/1024/1024;//最小内存
		long max=heapMemoryUsage.getMax()/1024/1024;//最大内存
		
		//获取系统配置堆参数
		String[] args=(String[]) getAttribute("java.lang:type=Runtime", "InputArguments");
		StringBuilder xm=new StringBuilder();
		StringBuilder pg=new StringBuilder();
		for (int i = 0; i < args.length; i++) {
			if (args[i].contains("-Xms") || args[i].contains("-Xmx")) {
				xm.append(args[i]+" ");
			}
			if (args[i].contains("-XX:PermSiz") || args[i].contains("-XX:MaxPermSiz") || args[i].contains("-XX:MetaspaceSize") || args[i].contains("-XX:MaxMetaspaceSize")) {
				pg.append(args[i]+" ");
			}
		}
		monitor.setHeap(new JvmMemory(xm.toString(), committed, used, init, max));
		
		
		//获取非堆内存，持久内存的大小--objectname，属性名
		//筛选出jdk1.6 1.8不同版本的objectname
		ObjectName objName=null;
		JvmMemory permGen=null;
		Set<ObjectName> set=getMBeanNames("java.lang:type=MemoryPool,name=*");
		if (set!=null &&!set.isEmpty()) {
			for (ObjectName objectName : set) {
				//如果包含perm 1.8版本Metaspace  1.6版本
				if (objectName.getKeyProperty("name").endsWith("Metaspace") || objectName.getKeyProperty("name").endsWith("Perm Gen")) {
					objName=objectName;
					break;
				}
			}
			MemoryUsage permGenUsage = MemoryUsage.from((CompositeData) getAttribute(objName.getCanonicalName(), "Usage"));
			long committedPermGen=permGenUsage.getCommitted()/1024/1024;//已经分配的内存
			long usedPermGen=permGenUsage.getUsed()/1024/1024;//已经使用的内存
			long initPermGen=permGenUsage.getInit()/1024/1024;//最小内存
			long maxPermGen=permGenUsage.getMax()/1024/1024;//最大内存
			permGen=new JvmMemory(pg.toString(), committedPermGen, usedPermGen, initPermGen, maxPermGen);
		}
		monitor.setPermGen(permGen);
		return monitor;
	}
	
	@Override
	public OsMonitor monitorOs() throws JmxException {
		checkConn();
		//操作系统名称
		String name=(String) getAttribute("java.lang:type=OperatingSystem", "Name");
		//操作系统版本
		String arch=(String) getAttribute("java.lang:type=OperatingSystem", "Arch");
		
		//cpu核心数
		int processors=(Integer) getAttribute("java.lang:type=OperatingSystem", "AvailableProcessors");
		//系统负载
		double osla=(Double) getAttribute("java.lang:type=OperatingSystem", "SystemLoadAverage");
		//虚拟机分配的内存大小
		long commitedVmSize=(Long) getAttribute("java.lang:type=OperatingSystem", "CommittedVirtualMemorySize")/1024/1024;
		//总共的物理内存大小
		long totalPmSize=(Long) getAttribute("java.lang:type=OperatingSystem", "TotalPhysicalMemorySize")/1024/1024;
		//空闲的物理内存大小
		long freePmSize=(Long) getAttribute("java.lang:type=OperatingSystem", "FreePhysicalMemorySize")/1024/1024;
		return new OsMonitor(name+"-"+arch,processors, osla, commitedVmSize, totalPmSize, freePmSize);
	}
	
}
