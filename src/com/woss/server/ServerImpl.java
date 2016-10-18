package com.woss.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

import com.briup.util.BIDR;
import com.briup.util.Configuration;
import com.briup.util.Logger;
import com.briup.woss.ConfigurationAWare;
import com.briup.woss.server.Server;
import com.woss.common.ConfigImpl;
import com.woss.common.LoggerImpl;

public class ServerImpl implements Server,ConfigurationAWare {

	private ServerSocket server = null;
	private Socket client = null;
	int port ;
	private Logger log;
	private Configuration config ;

	@Override
	public void init(Properties properties) {
			port =Integer.parseInt(properties.getProperty("serverport"));	
			try {
				log =(LoggerImpl) config.getLogger();
			} catch (Exception e) {
				e.printStackTrace();
			}
	}
	

	@Override
	public Collection<BIDR> revicer() throws Exception {
		
		try {
			server = new ServerSocket(port);
			log.info("服务器启动，等待客户端连接...");
			while (true) {
				client = server.accept();
				log.info("服务器接收到的客户端" + client);
				new MyThread(client).start();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// 关闭服务器
	@Override
	public void shutdown() {
		if (server != null && !server.isClosed()) {
			try {
				server.close();
				log.info("服务器手动关闭");
			} catch (IOException e) {
				e.printStackTrace();
				System.out.println("cuowu");
			}
		}
		
	}
	

@Override
public void setConfiguration(Configuration arg0) {
	config = (ConfigImpl)arg0;
	init(ConfigImpl.properties);
}

public static void main(String[] args) {
	ServerImpl s = new ServerImpl();
	s.setConfiguration(new ConfigImpl());
}
}

class MyThread extends Thread{
	private Socket client ;
	private ObjectInputStream ois = null;
	private List<BIDR> colls= new ArrayList<BIDR>();
	public MyThread(Socket client){
		this.client=client;
	}
	public void run() {
		try {
			ois = new ObjectInputStream(client.getInputStream());
			colls = (List<BIDR>) ois.readObject();
			DBStoreImpl db = new DBStoreImpl();
			db.saveToDB(colls);
		} catch (Exception e) {
		} finally {
			if (ois != null) {
				try {
					ois.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}
	

}









