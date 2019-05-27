package com.fantasy.app.core.component.monitor.jmx;

import com.fantasy.app.core.component.monitor.jmx.vo.ClassLoaderVo;
import com.fantasy.app.core.component.monitor.jmx.vo.DiskVo;
import com.fantasy.app.core.component.monitor.jmx.vo.GcVo;
import com.fantasy.app.core.component.monitor.jmx.vo.JvmInfoVo;
import com.fantasy.app.core.component.monitor.jmx.vo.OperatingSystemVo;
import com.fantasy.app.core.component.monitor.jmx.vo.SessionVo;
import com.fantasy.app.core.component.monitor.jmx.vo.TableSpaceVo;
import com.fantasy.app.core.component.monitor.jmx.vo.ThreadVo;
/**
 * 监控接口
 * @author joe
 *
 */
public interface MonitorHandler {

	public OperatingSystemVo getOperatingSystemInfo(Object args);
	
	public JvmInfoVo getJvmInfo(Object args);
	
	public ClassLoaderVo getClassLoaderInfo(Object args);
	
	public GcVo getGcInfo(Object args);
	
	public ThreadVo getThreadInfo(Object args);
	
	public DiskVo getDiskInfo(Object args);
	
	public TableSpaceVo getTableSpaceInfo(Object args);
	
	public SessionVo getDbSessionInfo(Object args);
}
