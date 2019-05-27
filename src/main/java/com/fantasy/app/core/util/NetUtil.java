package com.fantasy.app.core.util;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

import javax.servlet.http.HttpServletRequest;

/**
 * 网络相关工具
 * @author 公众号：18岁fantasy
 * @2014-9-3 @上午11:34:38
 */
public class NetUtil {

	/**
	 * ip,端口连接测试
	 * @param ip
	 * @param port
	 * @return
	 */
	public static boolean canConnect(String ip,int port){
		Socket connect = new Socket();  
        try {  
            connect.connect(new InetSocketAddress(ip, port),3000);  
            return  connect.isConnected();  
        } catch (IOException e) {  
        }finally{  
            try {  
                connect.close();  
            } catch (IOException e) {  
            }  
        }  
        return false;
	}
	/**
	 * ip测试
	 * @param ip
	 * @param port
	 * @return
	 */
	public static boolean canConnect(String ip){
        try {  
        	InetAddress address = InetAddress.getByName(ip);
            return  address.isReachable(5000);
        } catch (IOException e) {  
        } 
        return false;
	}
	/**
	 * 获取本机ip地址
	 * @return
	 */
	public static String getLocalHostIp(){
		try {
			return java.net.InetAddress.getLocalHost().getHostAddress();
		 } catch (Exception e) {
			return "";
	     }
	}
	/**
	 * 获取本机主机名
	 * @return
	 */
	public static String getLocalHostName(){
		try {
			return java.net.InetAddress.getLocalHost().getHostName();
		 } catch (Exception e) {
			return "";
	     }
	}
	/**
	 * 获取客户端ip
	 * @param request
	 * @return
	 */
	public static String getRemoteIp(HttpServletRequest request){
		 // 获取请求主机IP地址,如果通过代理进来，则透过防火墙获取真实IP地址  
		if(request==null)return null;
        String ip = request.getHeader("X-Forwarded-For");  //通过负载均衡获取真实客户端IP
  
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
                ip = request.getHeader("Proxy-Client-IP");  
            }  
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
                ip = request.getHeader("WL-Proxy-Client-IP");  
            }  
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
                ip = request.getHeader("HTTP_CLIENT_IP");  
            }  
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
                ip = request.getHeader("HTTP_X_FORWARDED_FOR");  
            }  
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
                ip = request.getRemoteAddr();  //非负载均衡下直接获取
            }  
        } else if (ip.length() > 15) {  
            String[] ips = ip.split(",");  
            for (int index = 0; index < ips.length; index++) {  
                String strIp = (String) ips[index];  
                if (!("unknown".equalsIgnoreCase(strIp))) {  
                    ip = strIp;  
                    break;  
                }  
            }  
        }  
        return ip;  
	}
//	public static void main(String[] args) {
//		System.out.println(getLocalHostIp()+"/"+getLocalHostName());
//	}
}
