package com.fantasy.app.core.component.monitor.jmx;

import com.fantasy.app.core.component.monitor.jmx.vo.WebServerMonitor;
import com.fantasy.app.core.exception.JmxException;

/**
 * 监控web服务器
 * @author Administrator
 *
 */
public interface WebServerMonitorService {

	public WebServerMonitor monitorServer() throws JmxException;
}
