package com.fantasy.app.core.base;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.fantasy.app.core.boot.BootParam;
import com.fantasy.app.core.listener.inner.DaoInitListener;
import com.fantasy.app.core.para.CorePara.CoreInitCtx;


/**
 * 测试用基类
 * @日期：2012-12-14下午11:06:42
 * @作者：公众号：18岁fantasy
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration({ "classpath:spring/applicationContext.xml", "classpath:spring/spring-mvc-servlet.xml" })
public class BaseTest {

	@Before
	public void init() throws Exception {
		BootParam bootParam = BootParam.getBootParam();
		DaoInitListener daoInitListener = new DaoInitListener();
		daoInitListener.init(null, bootParam);
	}

	
	
	public static String WSD_TABLE(String tableName) {
		return CoreInitCtx.DEFAULT_SCHEMA_NAME + "." + tableName;
	}
}
