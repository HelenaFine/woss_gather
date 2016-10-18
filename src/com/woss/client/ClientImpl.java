package com.woss.client;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

import com.briup.util.BIDR;
import com.briup.util.Configuration;
import com.briup.woss.ConfigurationAWare;
import com.briup.woss.client.Client;
import com.woss.common.ConfigImpl;
import com.woss.common.LoggerImpl;

public class ClientImpl implements Client,ConfigurationAWare{

	private Socket socket=null;
	 String ip;
	 int port;
	private ObjectOutputStream oos = null;
	private LoggerImpl log;
	private  Configuration config;

	
	@Override
	public void init(Properties properties) {
		try {
			ip = properties.getProperty("ip");
			port = Integer.parseInt(properties.getProperty("port"));
			log = (LoggerImpl) config.getLogger();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void send(Collection<BIDR> colls) throws Exception {
		init(ConfigImpl.properties);
		socket = new Socket(ip,port);
		log.info("客户端连接服务器成功");
//		GatherImpl gather = new GatherImpl();
//		List<BIDR> list=(List<BIDR>) gather.gather();
		oos = new ObjectOutputStream(socket.getOutputStream());
		oos.writeObject(colls);
		oos.flush();
		if(oos!=null){
			oos.close();
		}
		if(socket!=null){
			socket.close();
		}
	}

	@Override
	public void setConfiguration(Configuration arg0) {
		config  =(ConfigImpl)arg0;
		init(ConfigImpl.properties);
	}

	public static void main(String[] args) {
		ClientImpl i = new ClientImpl();
		i.setConfiguration(new ConfigImpl());
	}
}
