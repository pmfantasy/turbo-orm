package com.fantasy.app.core.component.monitor.jmx.vo;
/**
 * WebServerMonitor
 * @author 公众号：18岁fantasy
 * 2017-5-25 下午3:40:08
 */
public class WebServerMonitor {

	
	private int maxAllowed;//最大允许的连接数
	private int active;//当前活动的线程数
	private int busy;//当前正在处理请求的线程数，正在忙碌的线程
	public WebServerMonitor(int maxAllowed, int active, int busy) {
		super();
		this.maxAllowed = maxAllowed;
		this.active = active;
		this.busy = busy;
	}
	public int getMaxAllowed() {
		return maxAllowed;
	}
	public void setMaxAllowed(int maxAllowed) {
		this.maxAllowed = maxAllowed;
	}
	public int getActive() {
		return active;
	}
	public void setActive(int active) {
		this.active = active;
	}
	public int getBusy() {
		return busy;
	}
	public void setBusy(int busy) {
		this.busy = busy;
	}
	@Override
	public String toString() {
		return "WebServerMonitor [maxAllowed=" + maxAllowed + ", active="
				+ active + ", busy=" + busy + "]";
	}
	public WebServerMonitor() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	
	
}
