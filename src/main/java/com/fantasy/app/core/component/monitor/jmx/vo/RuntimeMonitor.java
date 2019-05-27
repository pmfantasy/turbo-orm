package com.fantasy.app.core.component.monitor.jmx.vo;

import java.util.Date;

/**
 * 运行环境监控
 * 主要有：jdk版本信息，启动时间等
 * @author 公众号：18岁fantasy
 *
 */
public class RuntimeMonitor {

	private String name;//vm名称
	private String version;//vm版本
	private String vendor;//厂商
	private Date start;//启动时间
	
	public RuntimeMonitor() {
		super();
		// TODO Auto-generated constructor stub
	}
	public RuntimeMonitor(String name, String version, String vendor, Date start) {
		super();
		this.name = name;
		this.version = version;
		this.vendor = vendor;
		this.start = start;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getVendor() {
		return vendor;
	}
	public void setVendor(String vendor) {
		this.vendor = vendor;
	}
	public Date getStart() {
		return start;
	}
	public void setStart(Date start) {
		this.start = start;
	}
	@Override
	public String toString() {
		return "RuntimeMonitor [name=" + name + ", version=" + version
				+ ", vendor=" + vendor + ", start=" + start + "]";
	}
}
