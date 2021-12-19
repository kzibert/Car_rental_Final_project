package com.zibert.listeners;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

@WebListener
public class ContextListener implements ServletContextListener {

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		ServletContext ctx = sce.getServletContext();
		String path = ctx.getRealPath("/WEB-INF/log4j2.log");
		System.setProperty("logFile", path);

		String localesFileName = ctx.getInitParameter("locales");
		String localesFileRealPath = ctx.getRealPath(localesFileName);
		Properties locales = new Properties();
		try {
			locales.load(new FileInputStream(localesFileRealPath));
		} catch (IOException e) {
			e.printStackTrace();
		}

		ctx.setAttribute("locales", locales);
	}

}
