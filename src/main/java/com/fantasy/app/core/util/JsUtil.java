package com.fantasy.app.core.util;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

/**
 * java 调用js进行计算
 * @author 公众号：18岁fantasy
 * @2014-6-4 @下午3:54:11
 */
public class JsUtil {

	public static Object simpleComputeExpress(String express) throws ScriptException{
		ScriptEngineManager mgr = new ScriptEngineManager(); 
		ScriptEngine engine = mgr.getEngineByName("JavaScript");
		return  engine.eval(express);   
	}
	@SuppressWarnings("unchecked")
	public static <T> T simpleComputeExpress(String express,Class<T> typeToReturn ) throws ScriptException{
		ScriptEngineManager mgr = new ScriptEngineManager(); 
		ScriptEngine engine = mgr.getEngineByName("JavaScript");
		return  (T)engine.eval(express);   
	}
//	public static void main(String[] args) {
//		try {
//			String forNode_v1 = "";
//			System.out.println(String.valueOf(JsUtil.simpleComputeExpress("1"+"0.0")));
//			boolean ok =  simpleComputeExpress("1<2&&3<4",Boolean.class);
//			System.out.println(ok);
//		} catch (ScriptException e) {
//			e.printStackTrace();
//		}
//	}
}
