package com.woss.common;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.briup.util.BackUP;
import com.briup.util.Configuration;
import com.briup.util.Logger;
import com.briup.woss.client.Client;
import com.briup.woss.client.Gather;
import com.briup.woss.server.DBStore;
import com.briup.woss.server.Server;
import com.woss.client.ClientImpl;
import com.woss.client.GatherImpl;
import com.woss.server.DBStoreImpl;
import com.woss.server.ServerImpl;

public class ConfigImpl implements Configuration {

	public static Properties properties = new Properties();
	private BackUP backUp;
	private Client client;
	private DBStore dbStore;
	private Server server;
	private Gather gather;
	private Logger log;
	private String filePath = "src/com/briup/file/conf.xml";
	

	public ConfigImpl() {
	
		try {
			SAXReader reader = new SAXReader();
			File file = new File(filePath);
			Document document;
			document = reader.read(file);
//			log.info("xml文件解析开始");
			// 得到根元素
			Element root = document.getRootElement();
			// 得到根元素下面的子元素
			List<Element> elements = new ArrayList<Element>();
			elements = root.elements();
			// 遍历子元素
			for (Element e1 : elements) {
				String tagName = e1.getName();
				if (tagName.equals("gather")) {
					List<Attribute> attributes = e1.attributes();
					for (Attribute att : attributes) {
						String gatherClass = att.getValue();
						// 将解析出来的gather属性封装到properties中
						properties.setProperty("gather", gatherClass);
					}
					List<Element> elements2 = new ArrayList<Element>();
					elements2 = e1.elements();
					for (Element e2 : elements2) {
						String childTagName = e2.getName();
						if ("AAA-filePath".equals(childTagName)) {
							// 得到AAA-filePath，封装到properties中
							properties.setProperty("AAA-filePath", e2.getText());
						}
						if ("nasIp".equals(childTagName)) {
							properties.setProperty("nasIp", e2.getText());
						}
					}
				}
				if (tagName.equals("logger")) {
					List<Attribute> attributes = new ArrayList<Attribute>();
					attributes = e1.attributes();
					for (Attribute att : attributes) {
						String loggerClass = att.getValue();
						properties.setProperty("logger", loggerClass);
					}
					List<Element> elements2 = new ArrayList<Element>();
					elements2 = e1.elements();
					for (Element e2 : elements2) {
						String childTagName = e2.getName();
						if ("file".equals(childTagName)) {
							// 获得日志配置文件的地址；
							properties.setProperty("file", e2.getText());
						}
					}
				}
				if (tagName.equals("Back")) {
					List<Attribute> attributes = new ArrayList<Attribute>();
					attributes = e1.attributes();
					for (Attribute att : attributes) {
						String backClass = att.getValue();
						// 获得back属性中的类名
						properties.setProperty("Back", backClass);
					}
					List<Element> elements2 = new ArrayList<Element>();
					elements2 = e1.elements();
					for (Element e2 : elements2) {
						String childTagName = e2.getName();
						if ("file".equals(childTagName)) {
							String filePath = e2.getText();
							// 获得file的text值
							properties.setProperty("file", filePath);
						}
					}
				}
				if (tagName.equals("client")) {
					List<Attribute> attributes = new ArrayList<Attribute>();
					attributes = e1.attributes();
					for (Attribute att : attributes) {
						String clientClass = att.getValue();
						properties.setProperty("client", clientClass);
					}
					List<Element> elements2 = new ArrayList<Element>();
					elements2 = e1.elements();
					for (Element e2 : elements2) {
						String childTagName = e2.getName();
						if ("port".equals(childTagName)) {
							properties.setProperty("port", e2.getText());
						}
						if ("ip".equals(childTagName)) {
							properties.setProperty("ip", e2.getText());
						}

					}
				}
				if (tagName.equals("server")) {
					List<Attribute> attributes = new ArrayList<Attribute>();
					attributes = e1.attributes();
					for (Attribute att : attributes) {
						properties.setProperty("server", att.getValue());

					}
					List<Element> elements2 = new ArrayList<Element>();
					elements2 = e1.elements();
					for(Element e2 : elements2){
						String childTagName = e2.getName();
						if("serverport".equals(childTagName)){
							properties.setProperty("serverport", e2.getText());
						}
					}
					
				}
				if (tagName.equals("dbStore")) {
					List<Attribute> attributes = new ArrayList<Attribute>();
					attributes = e1.attributes();
					for (Attribute att : attributes) {
						properties.setProperty("dbStore", att.getValue());
					}
					List<Element> elements2 = new ArrayList<Element>();
					elements2 = e1.elements();
					for(Element e2 : elements2){
						String childTagName = e2.getName();
						if("url".equals(childTagName)){
							properties.setProperty("url", e2.getText());
						}
						if("userName".equals(childTagName)){
							properties.setProperty("userName", e2.getText());
						}
						if("userPwd".equals(childTagName)){
							properties.setProperty("userPwd", e2.getText());
						}
					}
					
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			log.debug("文件解析错误");
		}
	}

	


	@Override
	public BackUP getBackup() throws Exception {
		String cName = properties.getProperty("Back");
		Class c = Class.forName(cName);
		backUp = (BackUpImpl) c.newInstance();
		return this.backUp;
	}

	@Override
	public Client getClient() throws Exception {
		String cName = properties.getProperty("client");
		Class c = Class.forName(cName);
		client = (ClientImpl) c.newInstance();
		return this.client;
	}

	@Override
	public DBStore getDBStore() throws Exception {
		String cName = properties.getProperty("dbStore");
		Class c = Class.forName(cName);
		dbStore = (DBStoreImpl) c.newInstance();
		return this.dbStore;
	}

	@Override
	public Gather getGather() throws Exception {
		String cName = properties.getProperty("gather");
		Class c = Class.forName(cName);
		gather = (GatherImpl) c.newInstance();
		((GatherImpl) gather).setConfiguration(this);
		return this.gather;
	}

	@Override
	public Logger getLogger() throws Exception {
		String cName = properties.getProperty("logger");
		Class c = Class.forName(cName);
		log = (LoggerImpl) c.newInstance();
		return this.log;
	}

	@Override
	public Server getServer() throws Exception {
		String cName = properties.getProperty("server");
		Class c = Class.forName(cName);
		server = (ServerImpl) c.newInstance();
		return this.server;
	}

}
