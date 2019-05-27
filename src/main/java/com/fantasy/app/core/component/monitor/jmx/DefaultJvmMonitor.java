package com.fantasy.app.core.component.monitor.jmx;

import com.fantasy.app.core.component.monitor.jmx.vo.WebServerMonitor;
import com.fantasy.app.core.exception.JmxException;

public class DefaultJvmMonitor extends AbstractMonitorService{

	@Override
	public WebServerMonitor monitorServer() throws JmxException {
		throw new JmxException("DefaultJvmMonitor只监控jvm,不支持监控应用服务器");
	}

}
