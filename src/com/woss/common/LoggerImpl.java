package com.woss.common;


import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;



public class LoggerImpl  implements com.briup.util.Logger {

	private static Logger logger;

	static {
		
			  logger = Logger.getRootLogger();
		  
		 }

	@Override
	public void init(Properties arg0) {
		
	}

	@Override
	public void debug(String arg0) {
		logger.debug(arg0);
	}

	@Override
	public void error(String arg0) {
		logger.error(arg0);
	}

	@Override
	public void fatal(String arg0) {
		logger.fatal(arg0);
	}

	@Override
	public void info(String arg0) {
		logger.info(arg0);	
		}

	@Override
	public void warn(String arg0) {
		logger.warn(arg0);
	}



}
