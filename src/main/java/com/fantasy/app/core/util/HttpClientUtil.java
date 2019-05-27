package com.fantasy.app.core.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import com.fantasy.app.core.base.ResultVo;


public class HttpClientUtil {
	public static final String STATUS_KEY = "status"; //方法调用状态  1.成功  0.失败
	public static final String HTTP_STATUS_CODE_KEY = "httpStatusCode"; //HTTP请求返回的代码，诸如500、404、403等
	public static final String RESPONSE_KEY = "response"; //请求返回信息（字符串形式）
	public static final String ERROR_KEY = "error"; //异常信息

	public static final String DEFAULT_SCHEME = "http";
	public static final String SCHEME_HTTPS = "https";

	public static final String CHARSET_ISO_8859_1 = "ISO_8859_1";
	public static final String CHARSET_UTF_8 = "UTF-8";
	public static final String CHARSET_GB2312 = "gb2312";
	public static final String CHARSET_GBK = "gbk";

	
	/**
	 * http原生的方法
	 * isNative 是否是java原生的方法
	 * @return
	 */
	public static ResultVo post(String url,String charset, Map<String, String> paramMap,boolean isNative){
		if (!isNative) {//如果调用的不是原生的方法
			return post(url, charset, paramMap);
		}
		ResultVo status = new ResultVo();
		StringBuffer sb=new StringBuffer(); 
		//1.create connection 
		URL httpUrl;
		HttpURLConnection connection=null;
		BufferedWriter writer = null;
		try {
			httpUrl = new URL(url);
			connection=(HttpURLConnection) httpUrl.openConnection();
			//2.set http params
			connection.setDoOutput(true); //post请求 参数是放置在 http正文内，因此需要设为true, 默认情况下是false;
			connection.setDoInput(true);
			connection.setRequestMethod("POST");//设置请求方法为get请求，默认就是get
			connection.setConnectTimeout(1000000000);//设置设置连接服务器超时时间
			connection.setReadTimeout(300000);//设置从服务器读取数据超时（单位：毫秒）
			connection.setUseCaches(false);//设置请求不使用缓存 
			connection.setRequestProperty("Accept-Charset", charset);
			connection.setRequestProperty("Accept", "*/*");
			connection.setRequestProperty("connection", "Keep-Alive");
//			connection.setRequestProperty("Content-Type", "*/*");
			//3.发送消息体
			writer=new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()));//获取输出流
//			System.err.println("==============================");
			writer.write(serializeParam(paramMap,charset));//将参数传递过去
			writer.flush();
			//4. 无论是post还是get，http请求实际上直到HttpURLConnection的getInputStream()这个函数里面才正式发送出去
			int responseCode=connection.getResponseCode();
			if (responseCode==200) {//表示请求成功
				BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(),charset));//设置编码,否则中文乱码 
				String line;  
				 while ((line = reader.readLine()) != null){  
		            //lines = new String(lines.getBytes(), "utf-8");  //如果reader没有使用utf-8编码的话，在这里也可以转换
		            sb.append(line);
			     }
				 if (reader!=null) {
					reader.close();  
				}
				status.setSuccess(true);
				status.setMsg("请求成功");
				status.setData(sb.toString());
			}else{
				status.setSuccess(false);
				status.setMsg("请求失败:状态码【"+responseCode+"】");
			}
		} catch (MalformedURLException e) {
			status.setSuccess(false);
			status.setMsg("请求失败:"+e);
		} catch (IOException e) {
			status.setSuccess(false);
			status.setMsg("请求失败:"+e);
		}finally{
			if (null != writer){
				try {
					writer.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (connection!=null) {
				//4.断开连接  
				connection.disconnect();
			}
		}
		return status;
	}
	
	
	public static String serializeParam(Map<String,String> params,String charset) throws UnsupportedEncodingException{
		StringBuilder sb=new StringBuilder();
//		System.out.println("Before serialize data:" + params.get("exch"));
		if (params!=null && !params.isEmpty()) {
			Set<String> set=params.keySet();
			for (String key : set) {
				sb.append(key+"="+URLEncoder.encode(params.get(key), charset)+"&");
			}
		}
		if (sb.length() > 0)
        {
			sb = sb.deleteCharAt(sb.length() - 1);
        }
//		System.out.println("After serialize parameters:" + sb.toString());
		return sb.toString();
	}
	
	
	/**
	 * 通过Get发送请求
	 * @param url
	 * @return
	 */
	public static ResultVo get(String url) {
		return get(url, CHARSET_UTF_8);
	}

	/**
	 * 通过Get发送请求
	 * @param url
	 * @param defaultCharset 默认字符集
	 * @return
	 */
	public static ResultVo get(String url, String charset) {
		ResultVo status = new ResultVo();
		ConnectionConfig connectionConfig = ConnectionConfig.custom()
                .setBufferSize(4128)
                .build();
	    CloseableHttpClient httpclient =  HttpClients.custom()
            .setDefaultConnectionConfig(connectionConfig)
            .build();
//		CloseableHttpClient httpclient = HttpClients.createDefault();
		CloseableHttpResponse httpResponse = null;
		String errorMsg = "";
		String response = "";
		try {
			HttpGet httpGet = new HttpGet(url);
			httpResponse = httpclient.execute(httpGet);
			HttpEntity responseEntity = httpResponse.getEntity();
			int httpStatusCode = httpResponse.getStatusLine().getStatusCode();
			if (responseEntity != null && httpStatusCode == 200) {
				response = EntityUtils.toString(responseEntity, StringUtils.defaultIfEmpty(charset, CHARSET_UTF_8));
				status.setSuccess(true);
				status.setMsg("OK");
				status.setData(response);
			} else {
				status.setSuccess(false);
				status.setMsg("httpStatusCode error:" + httpStatusCode);
			}
			return status;
		} catch (UnsupportedEncodingException e) {
			errorMsg = "UnsupportedEncodingException:" + e.fillInStackTrace();
			status.setSuccess(false);
			status.setMsg(errorMsg);
			return status;
		} catch (ClientProtocolException e) {
			errorMsg = "ClientProtocolException:" + e.fillInStackTrace();
			status.setSuccess(false);
			status.setMsg(errorMsg);
			return status;
		} catch (IOException e) {
			errorMsg = "UnsupportedEncodingException:" + e.fillInStackTrace();
			status.setSuccess(false);
			status.setMsg(errorMsg);
			return status;
		} catch (Exception e) {
			errorMsg = "Exception:" + e.fillInStackTrace();
			status.setSuccess(false);
			status.setMsg(errorMsg);
			return status;
		} finally {
			try {
				httpclient.close();
				if (httpResponse!=null) {
					httpResponse.close();
				}
			} catch (IOException e) {
				//ignored...
			}
		}
	}

	/**
	 * 通过Post发送请求
	 * @param url
	 * @return
	 */
	public static ResultVo post(String url) {
		return post(url, CHARSET_UTF_8, null);
	}

	/**
	 * 通过Post发送请求
	 * @param url
	 * @param paramMap --Form表单参数
	 * @return
	 */
	public static ResultVo post(String url, Map<String, String> paramMap) {
		return post(url, CHARSET_UTF_8, paramMap,true);
	}

	/**
	 * 通过Post发送请求
	 * @param url
	 * @param defaultCharset
	 * @param paramMap  --Form表单参数
	 * @return
	 */
	public static ResultVo post(String url, String charset, Map<String, String> paramMap) {
		ResultVo status = new  ResultVo();
		ConnectionConfig connectionConfig = ConnectionConfig.custom()
	                .setBufferSize(4128)
	                .build();
		CloseableHttpClient httpclient =  HttpClients.custom()
                .setDefaultConnectionConfig(connectionConfig)
                .build();
		CloseableHttpResponse httpResponse = null;
		String errorMsg = "";
		String response = "";
		try {
			HttpPost httpPost = new HttpPost(url);
			List<NameValuePair> formparams = new ArrayList<NameValuePair>();
			if (paramMap != null && paramMap.size() > 0) {
				Iterator<Map.Entry<String, String>> iter = paramMap.entrySet().iterator();
				try {
					while (iter.hasNext()) {
						Map.Entry<String, String> entry = iter.next();
						String key = entry.getKey().toString();
						String value = entry.getValue().toString();
						formparams.add(new BasicNameValuePair(key, value));
					}
					UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(formparams, StringUtils.defaultIfEmpty(
							charset, CHARSET_UTF_8));
					httpPost.setEntity(formEntity);
				} catch (ConcurrentModificationException e) {
					//this is should not be happend
					errorMsg = "ConcurrentModificationException:" + e.getMessage();
				}
			}
			httpResponse = httpclient.execute(httpPost);
			HttpEntity responseEntity = httpResponse.getEntity();
			int httpStatusCode = httpResponse.getStatusLine().getStatusCode();
			if (responseEntity != null && httpStatusCode == 200) {
				response = EntityUtils.toString(responseEntity, StringUtils.defaultIfEmpty(charset, CHARSET_UTF_8));
				status.setSuccess(true);
				status.setMsg("OK");
				status.setData(response);
			} else {
				status.setSuccess(false);
				status.setMsg("httpStatusCode error:" + httpStatusCode);
			}
			return status;
		} catch (UnsupportedEncodingException e) {
			errorMsg = "UnsupportedEncodingException:" + e.fillInStackTrace();
			status.setSuccess(false);
			status.setMsg(errorMsg);
			return status;
		} catch (ClientProtocolException e) {
			errorMsg = "ClientProtocolException:" + e.fillInStackTrace();
			status.setSuccess(false);
			status.setMsg(errorMsg);
			return status;
		} catch (IOException e) {
			errorMsg = "UnsupportedEncodingException:" + e.fillInStackTrace();
			status.setSuccess(false);
			status.setMsg(errorMsg);
			return status;
		} catch (Exception e) {
			errorMsg = "Exception:" + e.fillInStackTrace();
			status.setSuccess(false);
			status.setMsg(errorMsg);
			return status;
		} finally {
			try {
				httpclient.close();
				if (httpResponse!=null) {
					httpResponse.close();
				}
			} catch (IOException e) {
				//ignored...
			}
		}
	}
	
	public static void main(String[] args) throws UnsupportedEncodingException {
		Map<String,String> map=new HashMap<String, String>();
		map.put("exch", "<exch>124</exch>");
		map.put("css", "<exch>125</exch>");
		System.out.println(serializeParam(map,"utf-8"));
	}
}
