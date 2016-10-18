package com.woss.client;


import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.briup.util.BIDR;
import com.briup.util.BackUP;
import com.briup.util.Configuration;
import com.briup.util.Logger;
import com.briup.woss.ConfigurationAWare;
import com.briup.woss.client.Gather;
import com.woss.common.BackUpImpl;
import com.woss.common.ConfigImpl;
import com.woss.common.LoggerImpl;
//采集模块，拿出bidr对应的数据，封装成bidr对象
public class GatherImpl implements Gather,ConfigurationAWare{

	private Configuration config;
	private String aaa_filePath;
	private FileReader file =null;
	private BufferedReader br = null;
	private BIDR bidr;
	private Map<String,BIDR> map=new HashMap<String,BIDR>();
	private List<BIDR> list = new ArrayList<BIDR>();
	private BackUpImpl backUp;
	String filePath = "src/com/briup/file/backUp.txt";
	private LoggerImpl log ;

	private List<BIDR> backList = new ArrayList<BIDR>();
	

	public GatherImpl(){
		
	}
	@Override
	public void init(Properties properties) {
		try {
			aaa_filePath = properties.getProperty("AAA-filePath");
			file = new FileReader(aaa_filePath);
			br = new BufferedReader(file);
			

		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	

	@Override
	public Collection<BIDR> gather() throws Exception {
//		init(ConfigImpl.properties);
		// #| 037:wKgB1660A |8| 1239203860 | 44.211.221.247
		String str = null;
		log.info("开始读取radwtmp文件");
		while((str=br.readLine())!=null){
			String msg[]=str.split("[|]");
			if(msg.length==5){
			if(msg[2].equals("7")){
				bidr = new BIDR();
				bidr.setAAA_login_name(msg[0]);
				bidr.setLogin_ip(msg[4]);
				Timestamp login_date = changeTime(msg[3]);
				bidr.setLogin_date(login_date);
				bidr.setNAS_ip(msg[1]);
				map.put(msg[4], bidr);
				backList.add(bidr);
				
			}
			else {
				if(map.containsKey(msg[4])){
					bidr=map.get(msg[4]);
					backList.remove(bidr);
					Timestamp logout_date = changeTime(msg[3]);
					bidr.setLogout_date(logout_date);
					Timestamp login_date = bidr.getLogin_date();
					long logout_date1 =  logout_date.getTime();
					long login_date1 = login_date.getTime();//毫秒
					long deration=logout_date1-login_date1;
					if(deration%1000%60!=0){
						long time=deration/1000/60+1;
						bidr.setTime_deration((int)time);
					}else{
						long time=deration/1000/60;
						bidr.setTime_deration((int)time);
						}
					
					list.add(bidr);
					map.remove(msg[4]);
					}
				
				}
			}
			 
}
		 
		log.info("用户上线和下线对象封装完成");
		log.info("备份未下线的用户");
		backUp.store(filePath,map,BackUP.STORE_OVERRIDE);
		
//	for(BIDR b:backUpList){
//		System.out.println(b.getAAA_login_name()+b.getLogin_ip()+b.getLogout_date());
//		System.out.println("------------");
//	}
//	System.out.println(list.size());
		return list;
	}
	public Timestamp changeTime(String String){
		long l = Long.parseLong(String);
		Timestamp time  = new Timestamp(l*1000);
		return time;
	}


	@Override
	public void setConfiguration(Configuration arg0) {
		try {
			config = (ConfigImpl)arg0;
			log  =  (LoggerImpl) config.getLogger();
			backUp = (BackUpImpl) config.getBackup();
		
			backUp.setConfiguration(config);
			init(ConfigImpl.properties);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws Exception {
		GatherImpl g = new GatherImpl();
		g.setConfiguration(new ConfigImpl());
		g.gather();
	}


}
