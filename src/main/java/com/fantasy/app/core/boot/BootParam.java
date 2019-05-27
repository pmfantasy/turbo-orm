package com.fantasy.app.core.boot;

import java.io.File;
import java.util.List;

import org.springframework.util.ResourceUtils;

import com.fantasy.app.core.exception.InitException;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.converters.collections.CollectionConverter;
import com.thoughtworks.xstream.mapper.ClassAliasingMapper;

/**
 * 可配置的项目启动参数
 * @author 公众号：18岁fantasy
 * 2017-5-3 下午4:58:31
 */
@XStreamAlias("BootParam")
public class BootParam {
	private static BootParam INSTANCE = null;
	@XStreamAlias("modulePackage")
	//@XStreamImplicit(itemFieldName="package")
	private List<String> modulePackage;
	@XStreamAlias("beforeSpringListener")
	private List<String> beforeSpringListener;
	@XStreamAlias("afterSpringListener")
	private List<String> afterSpringListener;
	@XStreamAlias("interceptors")
	private List<String> interceptors;

	public List<String> getModulePackage() {
		return modulePackage;
	}

	public List<String> getBeforeSpringListener() {
		return beforeSpringListener;
	}

	public List<String> getAfterSpringListener() {
		return afterSpringListener;
	}

	public List<String> getInterceptors() {
		return interceptors;
	}

	public void setModulePackage(List<String> modulePackage) {
		this.modulePackage = modulePackage;
	}

	public void setBeforeSpringListener(List<String> beforeSpringListener) {
		this.beforeSpringListener = beforeSpringListener;
	}

	public void setAfterSpringListener(List<String> afterSpringListener) {
		this.afterSpringListener = afterSpringListener;
	}

	public void setInterceptors(List<String> interceptors) {
		this.interceptors = interceptors;
	}

	public static BootParam getBootParam() throws Exception {
		if (INSTANCE == null) {
			XStream xstream = new XStream();
			xstream.processAnnotations(BootParam.class);

			ClassAliasingMapper mapper = new ClassAliasingMapper(xstream.getMapper());

			mapper.addClassAlias("package", String.class);
			xstream.registerLocalConverter(BootParam.class, "modulePackage", new CollectionConverter(mapper));

			mapper = new ClassAliasingMapper(xstream.getMapper());
			mapper.addClassAlias("listener", String.class);
			xstream.registerLocalConverter(BootParam.class, "beforeSpringListener", new CollectionConverter(mapper));

			mapper = new ClassAliasingMapper(xstream.getMapper());
			mapper.addClassAlias("listener", String.class);
			xstream.registerLocalConverter(BootParam.class, "afterSpringListener", new CollectionConverter(mapper));

			mapper = new ClassAliasingMapper(xstream.getMapper());
			mapper.addClassAlias("interceptor", String.class);
			xstream.registerLocalConverter(BootParam.class, "interceptors", new CollectionConverter(mapper));

			File bootXml = ResourceUtils.getFile("classpath:boot.xml");
			if (bootXml == null || !bootXml.exists()) {
				throw new InitException("classpath:boot.xml is NOT exist!Init App Fail...");
			}

			INSTANCE = (BootParam) xstream.fromXML(bootXml);
		}
		return INSTANCE;
	}

	@Override
	public String toString() {
		return "BootParam [modulePackage=" + modulePackage + ", beforeSpringListener=" + beforeSpringListener
				+ ", afterSpringListener=" + afterSpringListener + ", interceptors=" + interceptors + "]";
	}
}
