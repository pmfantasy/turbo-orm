package com.fantasy.app.core.listener.inner;

import java.io.File;

import javax.servlet.ServletContextEvent;

import org.apache.log4j.Logger;

import com.fantasy.app.core.base.InterceptorBean.SIGNAL;
import com.fantasy.app.core.base.log.LogType;
import com.fantasy.app.core.boot.BootParam;
import com.fantasy.app.core.component.log.Log;
import com.fantasy.app.core.exception.InitException;
import com.fantasy.app.core.listener.AppInitListener;
import com.fantasy.app.core.para.CorePara.CoreInitCtx;
import com.fantasy.app.core.util.StrUtil;


/**
 * 核心参数监听器
 * APP启动后默认加载必要的系统配置参数
 * @author 公众号：18岁fantasy
 * 2017-5-25 下午3:20:03
 */
public class CoreParamInitListener implements AppInitListener {

  static Logger logger = Log.getLogger(LogType.SYSINNER);
	@Override
	public void init(ServletContextEvent contextEvent, BootParam bootParam) throws InitException {
		//设置项目绝对路径
		CoreInitCtx.APP_ROOT = contextEvent.getServletContext().getRealPath("/");
		
		
		if(contextEvent!=null) {
		 
		}
		/**
		 * 设置环境变量
		 */
		if(StrUtil.isNotBank(CoreInitCtx.ZLWORKDIR)) {
		  File f = new File(CoreInitCtx.ZLWORKDIR);
		  if(!f.exists()) {
		    f.mkdir();
		  }
		  if(contextEvent!=null) {
		     contextEvent.getServletContext().log("使用工作目录："+CoreInitCtx.ZLWORKDIR);
	      }
		}else {
		  if(contextEvent!=null) {
		    contextEvent.getServletContext().log("未设置工作目录参数["+CoreInitCtx.ZLWORKDIR_KEY+"]...");
		  }
		}
	}

	@Override
	public void destroyed(ServletContextEvent contextEvent, BootParam bootParam) throws InitException {
	}

	@Override
	public SIGNAL onError(ServletContextEvent contextEvent, BootParam bootParam)
			throws InitException {
		return SIGNAL.STOP;
	}
}
