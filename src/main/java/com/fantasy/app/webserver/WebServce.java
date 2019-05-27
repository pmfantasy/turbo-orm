package com.fantasy.app.webserver;

import java.io.File;
import java.io.IOException;

import org.apache.catalina.Host;
import org.apache.catalina.Server;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.core.AprLifecycleListener;
import org.apache.catalina.loader.WebappLoader;
import org.apache.catalina.startup.Tomcat;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

/**
 * tomcat 启动器
 * @author 公众号：18岁fantasy
 * @date 2019年5月5日 下午1:22:31
 */
public class WebServce {
	//设置主机或ip
	private String hostname="localhost";
	//设置端口,默认的端口，主要看配置属性
	private int port=8080;
	//
	private String webappDir="webapp";
	//设置 连接时的一些参数
	private int maxPostSize=0;
	private int maxThreads=200;
	private int acceptCount=100;

	//tomcat引用
	private Tomcat tomcat;

	public WebServce(){}


	//获取属性信息
	protected  void loadProperties()throws IOException{
		PropertiesConfiguration conf=new PropertiesConfiguration();
		try{
			//提供文件名
			conf.load("webserver.properties");
		}catch (ConfigurationException ce){

		}

		//根据配置文件修改初始值
		//第二个参数是默认值，当第一个为空时，使用默认值
		this.hostname=conf.getString("webserver.hostname","localhost");
		this.port=conf.getInt("webserver.port",port);

		this.webappDir=conf.getString("webserver.webappDir","src/main/webapp");
		this.maxPostSize=conf.getInt("webserver.maxPostSize",0);
		this.maxThreads=conf.getInt("webserver.maxThreads",200);
		this.acceptCount=conf.getInt("webserver.acceptCount",100);

	}

	//启动
	public void start(){
		try{
			//加载配置
			this.loadProperties();
			//tomcat实例
			this.tomcat=new Tomcat();
			//			this.tomcat.setPort(this.port);
			this.tomcat.setHostname(this.hostname);
			//tomcat存储自身信息，保存在项目目录下
			//			this.tomcat.setBaseDir(".");

			//			this.configServer(this.tomcat.getServer());
			//			this.tomcat.getEngine();
			//			this.configHost(this.tomcat.getHost());
			//			this.configConnector(this.tomcat.getConnector());
			//			//第一个参数上下文路径contextPath,第二个参数docBase
			//			this.tomcat.addWebapp("", System.getProperty("user.dir")+ File.separator+this.webappDir);

			//这种方式也行
			this.tomcat.getHost().setAppBase(System.getProperty("user.dir")+ File.separator+".");
			this.tomcat.getConnector().setPort( this.port );
			// https
			String useHttps = System.getProperty( "https.use" );
			if( "true".equals( useHttps )) {
				String keystoreFile = System.getProperty( "https.file" );
				keystoreFile = new File( keystoreFile ).getAbsolutePath();
				String passwd = System.getProperty( "https.passwd" );
				this.tomcat.getConnector().setProperty( "scheme", "https" );
				this.tomcat.getConnector().setProperty( "SSLEnabled", "true" );
				this.tomcat.getConnector().setSecure( true );
				this.tomcat.getConnector().setProperty( "clientAuth", "false" );
				this.tomcat.getConnector().setProperty( "sslProtocol", "TLS" );
				this.tomcat.getConnector().setProperty( "keystoreFile", keystoreFile );
				this.tomcat.getConnector().setProperty( "keystorePass", passwd );
			}

			final org.apache.catalina.Context context = this.tomcat.addWebapp("", this.webappDir);
			WebappLoader loader = new WebappLoader( this.getClass().getClassLoader() );
			context.setLoader( loader );
			loader.setDelegate( true );


			this.tomcat.start();

			this.tomcat.getServer().await();

		}catch(Exception e){

		}
	}

	private void configHost(Host host) {
		//user.dir  用户的当前工作目录
		host.setAppBase(System.getProperty("user.dir"));
	}

	private void configServer(Server server) {
		AprLifecycleListener listener = new AprLifecycleListener();
		server.addLifecycleListener(listener);
	}

	//设置连接属性
	private void configConnector(Connector connector) {
		connector.setURIEncoding("UTF-8");
		connector.setMaxPostSize(this.maxPostSize);
		connector.setAttribute("maxThreads", Integer.valueOf(this.maxThreads));
		connector.setAttribute("acceptCount", Integer.valueOf(this.acceptCount));
		connector.setAttribute("disableUploadTimeout", Boolean.valueOf(true));
		connector.setAttribute("enableLookups", Boolean.valueOf(false));
	}


	public static WebServce getWebserce(){
		return new WebServce();
	}

	public static void main(String[] args) {
		WebServce.getWebserce().start();
	  //System.out.println(System.getProperty("user.dir"));
	}



}