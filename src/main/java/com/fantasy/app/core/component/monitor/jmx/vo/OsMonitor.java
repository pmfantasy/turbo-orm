package com.fantasy.app.core.component.monitor.jmx.vo;


/**
 * 操作系统相关信息统计
 * @author 公众号：18岁fantasy
 *
 */
public class OsMonitor {

	private String name;//操作系统名称
	private int processors;//处理器个数
	//加载平均值在某些平台上可能是不可用的，在这些平台上实现此方法代价太高。
    //返回：系统加载平均值；如果加载平均值不可用，则返回负值。
	private double osla;//SystemLoadAverage 操作系统负载
	
	private long commitedVmSize;//已经分配的虚拟机内存大小，单位M
	
	private long totalPmSize;//机器总共的物理内存大小,TotalPhysicalMemorySize 单位M
	private long freePmSize;//机器可用的物理内存大小，PhysicalMemorySize
	public int getProcessors() {
		return processors;
	}
	public void setProcessors(int processors) {
		this.processors = processors;
	}
	public double getOsla() {
		return osla;
	}
	public void setOsla(double osla) {
		this.osla = osla;
	}
	public long getCommitedVmSize() {
		return commitedVmSize;
	}
	public void setCommitedVmSize(long commitedVmSize) {
		this.commitedVmSize = commitedVmSize;
	}
	public long getTotalPmSize() {
		return totalPmSize;
	}
	public void setTotalPmSize(long totalPmSize) {
		this.totalPmSize = totalPmSize;
	}
	public long getFreePmSize() {
		return freePmSize;
	}
	public void setFreePmSize(long freePmSize) {
		this.freePmSize = freePmSize;
	}
	public OsMonitor(String name,int processors, double osla, long commitedVmSize,
			long totalPmSize, long freePmSize) {
		super();
		this.name=name;
		this.processors = processors;
		this.osla = osla;
		this.commitedVmSize = commitedVmSize;
		this.totalPmSize = totalPmSize;
		this.freePmSize = freePmSize;
	}
	
	
	public OsMonitor() {
		super();
		// TODO Auto-generated constructor stub
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@Override
	public String toString() {
		return "OsMonitor [processors=" + processors + ", osla=" + osla
				+ ", commitedVmSize=" + commitedVmSize + ", totalPmSize="
				+ totalPmSize + ", freePmSize=" + freePmSize + "]";
	}
	
	
}
