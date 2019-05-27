package com.fantasy.app.core.util;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;

import org.apache.cxf.endpoint.Client;
import org.apache.cxf.frontend.ClientProxy;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.apache.cxf.transport.http.HTTPConduit;
import org.apache.cxf.transports.http.configuration.HTTPClientPolicy;

import com.fantasy.app.core.exception.ServiceException;


/**
 * WebService客户端工具
 * @author 公众号：18岁fantasy
 * @date 2019年5月5日 下午1:19:57
 */
public class WebserviceUtil {
	/**
	 * 缓存池
	 */
	private static Map<String, Object> cache = new ConcurrentHashMap<String, Object>();

	/**
	 * 获取服务端口
	 * @param wsdl
	 * @param clazz
	 * @return
	 * 注意：这个服务端口汇报缓存
	 */
	public static Object getServicePort(String wsdl, Class<?> clazz) {
		Object service = cache.get(wsdl);
		if (service == null) {
			JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();
			factory.setServiceClass(clazz);
			factory.setAddress(wsdl);
			service = factory.create();
			cache.put(wsdl, service);
		}
		//设置客户端的配置信息，超时等.
		   Client proxy = ClientProxy.getClient(service);
		   HTTPConduit conduit = (HTTPConduit) proxy.getConduit();
		   HTTPClientPolicy policy = new HTTPClientPolicy();
		   policy.setConnectionTimeout(10000); //连接超时时间
		   policy.setReceiveTimeout(120000);//请求超时时间.
		   policy.setCookie("");    
		   conduit.setClient(policy);
		return service;
	}
	/**
	 * 
	 * @param wsdl
	 * @param namespaceURI
	 * @param localPart
	 * @param clazz
	 * @return
	 * @throws MalformedURLException
	 */
	public static Object getServicePort(String wsdl, String namespaceURI,  String localPart,Class<?> clazz) throws MalformedURLException {
		Service service = Service.create(new URL(wsdl), new QName(namespaceURI,localPart));
//		System.out.println("******************超时时间设置start*******************");
		//设置客户端的配置信息，超时等.
		   Client proxy = ClientProxy.getClient(service);
		   HTTPConduit conduit = (HTTPConduit) proxy.getConduit();
		   HTTPClientPolicy policy = new HTTPClientPolicy();
		   policy.setConnectionTimeout(10000); //连接超时时间
		   policy.setReceiveTimeout(120000);//请求超时时间.
		   conduit.setClient(policy);
//		   System.out.println("******************超时时间设置 end*******************");
		return service.getPort(clazz);
	}

	/**
	 * 获取平台调用服务
	 * @return
	 * @throws ServiceException 
	 */
    @SuppressWarnings("rawtypes")
	public static Object getDataExchRecvService(String wsdl,Class clz) throws ServiceException{
		Object service;
		try {
			service = getServicePort(wsdl,"http://service.unstructured.adapter.turbo.dhcc.com.cn/","DataExchRecvServiceImplService",clz);
		} catch (MalformedURLException e) {
			throw new ServiceException("调用交换平台接口错误",e);
		}
		return service;
    }
    
    
	/**
	 * 清理wsdl，服务注册客户端地址修改之后需要调用这个方法
	 * @param wsdl
	 */
	public static void cleanUp(String wsdl) {
		cache.remove(wsdl);
	}
//	public static void main(String[] args) throws ServiceException {
//		String wsdl  = "http://10.1.7.40:8888/adapter/ws/data_exch_recv_service?wsdl";
//		DataExchRecvService service=(DataExchRecvService) getDataExchRecvService(wsdl, DataExchRecvService.class);
//		System.out.println(service.receive(null, null, null));
//	}
}
