package com.fantasy.app.core.component.filesplit;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

/**
 * 文件包
 * @author 公众号：18岁fantasy
 * 2017-5-25 下午3:37:17
 */
@XStreamAlias("packet")
public class Packet {
	@XStreamImplicit(itemFieldName = "packetfile")
	private List<String> packetfile;

	public List<String> getPacketfile() {
		return packetfile;
	}

	public void setPacketfile(List<String> packetfile) {
		this.packetfile = packetfile;
	}

	@Override
	public String toString() {
		return "Packet [packetfile=" + packetfile + "]";
	}


}
