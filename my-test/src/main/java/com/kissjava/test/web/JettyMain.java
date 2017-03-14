package com.kissjava.test.web;

import javax.servlet.ServletException;

import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.webapp.WebAppContext;
import org.springframework.mock.web.MockServletConfig;
import org.springframework.mock.web.MockServletContext;
import org.springframework.web.context.support.XmlWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

public class JettyMain {

	public static final int PORT = 9999;
	public static final String CONTEXT = "/";

	private static final String DEFAULT_WEBAPP_PATH = "src/main/webapp";
	
	//private DispatcherServlet dispatcherServlet;
	
	public DispatcherServlet initDispatcherServlet() throws ServletException {

		MockServletContext servletContext = new MockServletContext();
		MockServletConfig servletConfig = new MockServletConfig(servletContext);

		XmlWebApplicationContext wac = new XmlWebApplicationContext();
		wac.setServletContext(servletContext);
		wac.setServletConfig(servletConfig);
		wac.setConfigLocation("classpath:/spring-mvc.xml");

		DispatcherServlet dispatcherServlet = new DispatcherServlet(wac);
		dispatcherServlet.init(servletConfig);
		return dispatcherServlet;
	}
	/**
	 * 创建用于开发运行调试的Jetty Server, 以src/main/webapp为Web应用目录.
	 */
	public Server createServerInSource(int port, String contextPath) {
		Server server = new Server();
		// 设置在JVM退出时关闭Jetty的钩子。
		server.setStopAtShutdown(true);
		// 这是http的连接器
		ServerConnector connector = new ServerConnector(server);
		connector.setPort(port);
		// 解决Windows下重复启动Jetty居然不报告端口冲突的问题.
		connector.setReuseAddress(false);
		server.setConnectors(new Connector[] { connector });

		WebAppContext webContext = new WebAppContext(DEFAULT_WEBAPP_PATH, contextPath);
		webContext.setContextPath("/");
		//webContext.setDescriptor("src/main/webapp/WEB-INF/web.xml");
		// 设置webapp的位置
		webContext.setResourceBase(DEFAULT_WEBAPP_PATH);
		webContext.setClassLoader(Thread.currentThread().getContextClassLoader());
		server.setHandler(webContext);
		
		try {
			webContext.addServlet(new ServletHolder(initDispatcherServlet()), "*.do");
		} catch (ServletException e) {
			e.printStackTrace();
		}
		return server;
	}

	/**
	 * 启动jetty服务
	 * 
	 * @param port
	 * @param context
	 */
	public void startJetty(int port, String context) {
		final Server server = createServerInSource(PORT, CONTEXT);
		try {
			server.stop();
			server.start();
			server.join();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(-1);
		}
	}

	public static void main(String[] args) {
		new JettyMain().startJetty(8080, "");
	}
}