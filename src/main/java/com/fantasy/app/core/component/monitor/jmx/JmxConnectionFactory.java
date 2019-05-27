package com.fantasy.app.core.component.monitor.jmx;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.util.Map;

import javax.management.MBeanServerConnection;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

import com.fantasy.app.core.exception.JmxException;

public class JmxConnectionFactory {
	
	public static enum JmxJ2eeType{
		TOMCAT,
		TONGWEB,
		WEBLOGIC;
	}
	
	public static enum JmxType {
		SAME(0,"监控应用与被监控应用位于同一JVM"),
		REMOTE(1,"监控应用与被监控应用不位于同一JVM"),
		LOCAL(2,"监控应用与被监控应用不位于同一JVM但在同一物理主机上");
		private int type;
		private String desc;
		private JmxType(int type, String desc) {
			this.type = type;
			this.desc = desc;
		}
		public int getType() {
			return type;
		}
		public void setType(int type) {
			this.type = type;
		}
		public String getDesc() {
			return desc;
		}
		public void setDesc(String desc) {
			this.desc = desc;
		}
		
	}
	
	/**
	 * 使用方式
	 * 1.监控应用与被监控应用位于同一JVM
	 *     JmxConnectionFactory.getConnection(JmxType.SAME)
	 *     
	 * 2.监控应用与被监控应用不位于同一JVM
	 *    JmxConnectionFactory.getConnection(JmxType.LOCAL,"service:jmx:rmi:///jndi/rmi://192.168.2.105:8000/jmxrmi")
	 *    
	 * 3.监控应用与被监控应用不位于同一JVM但在同一物理主机上(2的特化情况，通过进程Attach)
	 *   JmxConnectionFactory.getConnection(JmxType.LOCAL,"org.apache.catalina.startup.Bootstrap") 将会删选出本机tomcat启动的jvm
	 *   JmxConnectionFactory.getConnection(JmxType.LOCAL,"com.tongweb.server.PELaunch") 将会删选出本机tongweb启动的jvm
	 *   JmxConnectionFactory.getConnection(JmxType.LOCAL,"weblogic.Server") 将会删选出本机weblogic启动的jvm
	 * 
	 * @param type
	 * @param args
	 * @return
	 * @throws JmxException 
	 * @throws IOException
	 * @throws AttachNotSupportedException
	 */
	public static MBeanServerConnection getConnection(JmxType type,Object... args) throws JmxException{
		try {
			if (type==JmxType.SAME) {
				return ManagementFactory.getPlatformMBeanServer();
			}else if(type==JmxType.REMOTE){
				if (args==null || args.length<1 || args[0]==null || "".equals(args[0])) {
					throw new RuntimeException("必须要有一个jmx连接地址");
				}
				JMXServiceURL url = new JMXServiceURL(args[0].toString());
				JMXConnector connector =null;
				if (args.length>=2) {
					Map<String, ?> env=args[1] instanceof Map? (Map) args[1]:null;
					connector=JMXConnectorFactory.connect(url,env);
				}else{
					connector=JMXConnectorFactory.connect(url);
				}
				return connector.getMBeanServerConnection();
			}else{//通过本地进程连接
				if (args==null || args.length<1 || args[0]==null || "".equals(args[0])) {
					throw new RuntimeException("必须要有一个provider用于获取要连接的vm");
				}
				/*List<VirtualMachineDescriptor> vms = VirtualMachine.list();
				for (VirtualMachineDescriptor vmd : vms) {
					String provider=vmd.displayName();
					if (provider.startsWith(args[0].toString())) {
						//连接到虚拟机
					   VirtualMachine vm = VirtualMachine.attach(vmd);
					   Properties props = vm.getAgentProperties();
					   //获取到本地连接地址
					   String connectorAddress = props.getProperty("com.sun.management.jmxremote.localConnectorAddress");
					   //连接到服务器中的MBean
					   JMXServiceURL url = new JMXServiceURL(connectorAddress);
					   JMXConnector connector = JMXConnectorFactory.connect(url);
					   return connector.getMBeanServerConnection();
					}
				}*/
			}
		} catch (Exception e) {
			throw new JmxException("获取jmx连接异常",e);
		}
		return null;
	}
}
