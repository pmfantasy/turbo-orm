package com.fantasy.app.core.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

@SuppressWarnings("deprecation")
public class HttpClient4xUtil {
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
	 * 通过Get发送请求
	 * @param url
	 * @return
	 */
	public static Map<String, Object> get(String url) {
		return get(url, CHARSET_UTF_8);
	}

	/**
	 * 通过Get发送请求
	 * @param url
	 * @param defaultCharset 默认字符集
	 * @return
	 */
	@SuppressWarnings("resource")
	public static Map<String, Object> get(String url, String charset) {
		HttpClient httpclient = new DefaultHttpClient();
		Map<String, Object> dataMap = new HashMap<String, Object>();
		Integer status = 0;
		Integer httpStatusCode = 500;
		String response = "";
		String errorMsg = "";
		try {
			HttpGet httpGet = new HttpGet(url);
			HttpResponse httpResponse = httpclient.execute(httpGet);
			HttpEntity responseEntity = httpResponse.getEntity();
			httpStatusCode = httpResponse.getStatusLine().getStatusCode();
			if (responseEntity != null && httpStatusCode == 200) {
				status = 1;
				response = EntityUtils.toString(responseEntity, StringUtils.defaultIfEmpty(charset, CHARSET_UTF_8));
			}
		} catch (UnsupportedEncodingException e) {
			errorMsg = "UnsupportedEncodingException:" + e.fillInStackTrace();
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			errorMsg = "ClientProtocolException:" + e.fillInStackTrace();
			e.printStackTrace();
		} catch (IOException e) {
			errorMsg = "UnsupportedEncodingException:" + e.fillInStackTrace();
			e.printStackTrace();
		} catch (Exception e) {
			errorMsg = "Exception:" + e.fillInStackTrace();
			e.printStackTrace();
		} finally {
			httpclient.getConnectionManager().shutdown();
		}
		dataMap.put(STATUS_KEY, status);
		dataMap.put(HTTP_STATUS_CODE_KEY, httpStatusCode);
		dataMap.put(RESPONSE_KEY, response);
		dataMap.put(ERROR_KEY, errorMsg);
		return dataMap;
	}

	/**
	 * 通过Post发送请求
	 * @param url
	 * @return
	 */
	public static Map<String, Object> post(String url) {
		return post(url, CHARSET_UTF_8, null);
	}

	/**
	 * 通过Post发送请求
	 * @param url
	 * @param paramMap --Form表单参数
	 * @return
	 */
	public static Map<String, Object> post(String url, Map<String, String> paramMap) {
		return post(url, CHARSET_UTF_8, paramMap);
	}

	/**
	 * 通过Post发送请求
	 * @param url
	 * @param defaultCharset
	 * @param paramMap  --Form表单参数
	 * @return
	 */
	@SuppressWarnings("resource")
	public static Map<String, Object> post(String url, String charset, Map<String, String> paramMap) {
		HttpClient httpclient = new DefaultHttpClient();
		Map<String, Object> dataMap = new HashMap<String, Object>();
		Integer status = 0;
		Integer httpStatusCode = 500;
		String response = "";
		String errorMsg = "";
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
					UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(formparams, StringUtils.defaultIfEmpty(charset,CHARSET_UTF_8));
					httpPost.setEntity(formEntity);
				} catch (ConcurrentModificationException e) {
					//this is should not be happend
					errorMsg = "ConcurrentModificationException:" + e.getMessage();
					e.printStackTrace();
				}
			}
			HttpResponse httpResponse = httpclient.execute(httpPost);
			HttpEntity responseEntity = httpResponse.getEntity();
			httpStatusCode = httpResponse.getStatusLine().getStatusCode();
			if (responseEntity != null && httpStatusCode == 200) {
				status = 1;
				response = EntityUtils.toString(responseEntity, StringUtils.defaultIfEmpty(charset, CHARSET_UTF_8));
			}
		} catch (UnsupportedEncodingException e) {
			errorMsg = "UnsupportedEncodingException:" + e.fillInStackTrace();
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			errorMsg = "ClientProtocolException:" + e.fillInStackTrace();
			e.printStackTrace();
		} catch (IOException e) {
			errorMsg = "UnsupportedEncodingException:" + e.fillInStackTrace();
			e.printStackTrace();
		} catch (Exception e) {
			errorMsg = "Exception:" + e.fillInStackTrace();
			e.printStackTrace();
		} finally {
			httpclient.getConnectionManager().shutdown();
		}
		dataMap.put(STATUS_KEY, status);
		dataMap.put(HTTP_STATUS_CODE_KEY, httpStatusCode);
		dataMap.put(RESPONSE_KEY, response);
		dataMap.put(ERROR_KEY, errorMsg);
		return dataMap;
	}
}
