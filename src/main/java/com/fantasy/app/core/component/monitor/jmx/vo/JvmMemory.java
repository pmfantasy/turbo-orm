package com.fantasy.app.core.component.monitor.jmx.vo;


/**
 * 单位都是M
 * @author 公众号：18岁fantasy
 *
 */
public class JvmMemory {

	private String arg;
	private long committed;//已分配
	private long used;//已使用
	private long init;//最小值
	private long max;//最大值
	
	public String getArg() {
		return arg;
	}
	public void setArg(String arg) {
		this.arg = arg;
	}
	
	public JvmMemory() {
		super();
		// TODO Auto-generated constructor stub
	}
	public JvmMemory(String arg, long committed, long used, long init, long max) {
		super();
		this.arg=arg;
		this.committed = committed;
		this.used = used;
		this.init = init;
		this.max = max;
	}
	public long getCommitted() {
		return committed;
	}
	public void setCommitted(long committed) {
		this.committed = committed;
	}
	public long getUsed() {
		return used;
	}
	public void setUsed(long used) {
		this.used = used;
	}
	public long getInit() {
		return init;
	}
	public void setInit(long init) {
		this.init = init;
	}
	public long getMax() {
		return max;
	}
	public void setMax(long max) {
		this.max = max;
	}
	@Override
	public String toString() {
		return "JvmMemory [arg=" + arg + ", committed=" + committed + ", used="
				+ used + ", init=" + init + ", max=" + max + "]";
	}
	
	
	
	
	
}
