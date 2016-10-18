package com.woss.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import com.briup.util.BIDR;
import com.briup.util.BackUP;
import com.briup.util.Configuration;
import com.briup.util.Logger;
import com.briup.woss.ConfigurationAWare;

public class BackUpImpl implements BackUP,ConfigurationAWare {

	private Configuration config;
	private Logger log;

	public BackUpImpl() {
		
	}

	@Override
	public void init(Properties properties) {

		try {
			log = config.getLogger();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	// 读备份文件
	@SuppressWarnings("unchecked")
	@Override
	public Object load(String filePath, boolean flag) throws Exception {
//		init(ConfigImpl.properties);
		log.info("开始加载未下线的用户");
		Map<String, BIDR> map = new HashMap<String, BIDR>();
		File file = new File(filePath);
		if (!file.exists()) {
			return null;
		}
		FileInputStream fis = new FileInputStream(file);
		ObjectInputStream oos = new ObjectInputStream(fis);
		map = (Map<String, BIDR>) oos.readObject();
		if (flag == true) {
			file.delete();
			log.info("已移除所有未下线用户");
		} else {
			log.info("加载未下线用户成功");
		}
		return map;
	}

	// 写备份文件
	@Override
	public void store(String filePath, Object map, boolean flag) throws Exception {
		// flag:是否追加文件
		log.info("开始备份未下线的用户");
		FileOutputStream fs = new FileOutputStream(filePath, flag);
		ObjectOutputStream oos = new ObjectOutputStream(fs);
		oos.writeObject(map);
		oos.flush();
		log.info("备份结束，成功写入文件backUp.txt");
		if (fs != null) {
			fs.close();
		}
		if (oos != null) {
			oos.close();
		}
	}

	@Override
	public void setConfiguration(Configuration arg0) {
		config = (ConfigImpl)arg0;
		init(ConfigImpl.properties);
		
	}

	public static void main(String[] args) {
		BackUpImpl b = new BackUpImpl();
		b.setConfiguration(new ConfigImpl());
	}

}
