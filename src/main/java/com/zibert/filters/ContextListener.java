package com.zibert.filters;

import javax.servlet.*;
import javax.servlet.annotation.*;

import org.apache.logging.log4j.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

@WebListener
public class ContextListener implements ServletContextListener {

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		ServletContext ctx = sce.getServletContext();
		String path = ctx.getRealPath("/WEB-INF/log4j2.log");
		System.out.println("Path: " + path);
		System.setProperty("logFile", path);
		
//		final Logger log = LogManager.getLogger(ContextListener.class);
//		log.debug("path = " + path);

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
