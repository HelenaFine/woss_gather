package com.woss.main;

import com.woss.server.ServerImpl;

public class CloseTest {

	public static void main(String[] args) {
		ServerImpl server = new ServerImpl();
		server.shutdown();
 	}
}
