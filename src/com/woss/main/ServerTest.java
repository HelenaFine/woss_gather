package com.woss.main;

import java.util.List;

import com.briup.util.BIDR;
import com.briup.util.Configuration;
import com.woss.common.ConfigImpl;
import com.woss.server.ServerImpl;

public class ServerTest {

	static {
		Configuration con = new ConfigImpl();
	}
	public static void main(String[] args) {
		
		try {
			Configuration con = new ConfigImpl();
			ServerImpl server = (ServerImpl) con.getServer();
			server.setConfiguration(con);
			List<BIDR> list = (List<BIDR>) server.revicer();
			//System.out.println(list.size());
			
			//server.shutdown();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
