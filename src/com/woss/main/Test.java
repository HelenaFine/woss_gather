package com.woss.main;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.woss.common.LoggerImpl;



public class Test {
	 private static  LoggerImpl logger = new LoggerImpl();
	 static {
		Logger.getRootLogger();
		 }
		
	public static void main(String[] args) {
		 
         logger.info("beautiful");
		String sql = "今天是个好日子";
		System.out.println(sql);
		
	}
}
