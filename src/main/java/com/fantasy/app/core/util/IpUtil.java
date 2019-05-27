package com.fantasy.app.core.util;


import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;
/**
 * ip获取工具
 * @author 公众号：18岁fantasy
 * @date 2019年5月5日 下午1:18:29
 */
public class IpUtil {
	
	/**
	 * 获取本机器IP
	 * @param request
	 * @return
	 */
	public static String getServerIpAddr() {
		String ip = "";
		try {
			InetAddress addr = InetAddress.getLocalHost();
			ip = addr.getHostAddress().toString();
		} catch (Exception e) {
			//ignored...
		}
		return ip;
	}
	

	/**
	 * 获取客户端真实Ip
	 * @param request
	 * @return
	 */
	public static String getRemoteIpAddr(HttpServletRequest request) {
		String ip = "";
		try{
			ip = request.getHeader("x-forwarded-for");
			if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
				ip = request.getHeader("Proxy-Client-IP");
			}
			if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
				ip = request.getHeader("WL-Proxy-Client-IP");
			}
			if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
				ip = request.getRemoteAddr();
			}
			if(ip.equals("0:0:0:0:0:0:0:1")) {
			  ip = "服务器本机";
			}
		}catch(Exception e){
			//ignored...
		}
		return ip;
	}
	
	
	/**
	 * 获取Webservice客户端真实Ip
	 * @param request
	 * @return
	 */
	public static String getRemoteIpAddr(WebServiceContext wsc) {
		String ip = "";
		try{
			MessageContext mc = wsc.getMessageContext();
			HttpServletRequest request = (HttpServletRequest) (mc.get(MessageContext.SERVLET_REQUEST));
			ip = getRemoteIpAddr(request);
		}catch(Exception e){
			//ignored...
		}
		return ip;
	}
	
	/**
	 * 获取本机器IP，用于获取多网卡IP列表
	 * @return
	 */
	public static ArrayList<String> getLoclIpList(){
		ArrayList<String> ipList = new ArrayList<String>();
		try {
			Enumeration allNetInterfaces = NetworkInterface.getNetworkInterfaces();
			InetAddress ip = null;
			while (allNetInterfaces.hasMoreElements()) {
				NetworkInterface netInterface = (NetworkInterface) allNetInterfaces
						.nextElement();
				Enumeration addresses = netInterface.getInetAddresses();
				while (addresses.hasMoreElements()) {
					ip = (InetAddress) addresses.nextElement();
					if (ip != null && ip instanceof Inet4Address) {
						ipList.add(ip.getHostAddress());
					}
				}
			}
		} catch (Exception e) {
			
		}
		return ipList;
	}
}
