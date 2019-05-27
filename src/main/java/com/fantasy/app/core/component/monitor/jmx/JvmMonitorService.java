package com.fantasy.app.core.component.monitor.jmx;

import com.fantasy.app.core.component.monitor.jmx.vo.JvmMonitor;
import com.fantasy.app.core.component.monitor.jmx.vo.OsMonitor;
import com.fantasy.app.core.component.monitor.jmx.vo.RuntimeMonitor;
import com.fantasy.app.core.exception.JmxException;


/**
 * jvm监控服务
 * 通过调用jmx接口获取jvm参数信息
 * 内存使用情况
 * @author Administrator
 *
 */
public interface JvmMonitorService {

	/**
	 * 监控当前的jvm
	 * @return
	 */
	public JvmMonitor monitorJvm() throws JmxException;
	
	/**
	 * 监控当前的操作系统
	 * @return
	 */
	public OsMonitor monitorOs() throws JmxException;
	
	
	public RuntimeMonitor monitorRuntime() throws JmxException;
	
}
