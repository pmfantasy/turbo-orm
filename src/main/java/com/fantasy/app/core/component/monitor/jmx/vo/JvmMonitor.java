package com.fantasy.app.core.component.monitor.jmx.vo;


/**
 * jvm监控对象
 * 
 * @author 公众号：18岁fantasy
 *
 */
public class JvmMonitor {
	private int threadCount;//当前活动的线程    返回活动线程的当前数目，包括守护线程和非守护线程。
	private int peakThreadCount;//峰值   java 虚拟机启动或峰值重置以来峰值活动线程计数。
	private int daemonThreadCount;//后台进程  活动守护线程的当前数目。
	private JvmMemory heap;//堆内存
	private JvmMemory permGen;//持久内存
	public JvmMemory getHeap() {
		return heap;
	}
	public void setHeap(JvmMemory heap) {
		this.heap = heap;
	}
	public JvmMemory getPermGen() {
		return permGen;
	}
	public void setPermGen(JvmMemory permGen) {
		this.permGen = permGen;
	}
	public int getThreadCount() {
		return threadCount;
	}
	public void setThreadCount(int threadCount) {
		this.threadCount = threadCount;
	}
	public int getPeakThreadCount() {
		return peakThreadCount;
	}
	public void setPeakThreadCount(int peakThreadCount) {
		this.peakThreadCount = peakThreadCount;
	}
	public int getDaemonThreadCount() {
		return daemonThreadCount;
	}
	public void setDaemonThreadCount(int daemonThreadCount) {
		this.daemonThreadCount = daemonThreadCount;
	}
	@Override
	public String toString() {
		return "JvmMonitor [heap=" + heap + ", permGen=" + permGen + "]";
	}
	public JvmMonitor() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	
}
