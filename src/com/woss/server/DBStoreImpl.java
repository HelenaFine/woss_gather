package com.woss.server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Collection;
import java.util.Properties;

import com.briup.util.BIDR;
import com.briup.util.Configuration;
import com.briup.woss.ConfigurationAWare;
import com.briup.woss.server.DBStore;
import com.woss.common.ConfigImpl;
import com.woss.common.LoggerImpl;

public class DBStoreImpl implements DBStore,ConfigurationAWare{
	private Configuration config = new ConfigImpl();
	private LoggerImpl log;
	String driver = "oracle.jdbc.driver.OracleDriver";
	String user;
	String password ;
	String url;
	

	public DBStoreImpl() {
		setConfiguration(config);
	}

	@Override
	public void init(Properties properties) {
		try {
			log = (LoggerImpl) config.getLogger();
			user = properties.getProperty("userName");
			password = properties.getProperty("userPwd");
			url = properties.getProperty("url");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void saveToDB(Collection<BIDR> colls) throws Exception {
		
		log.info("开始存入数据库");
		
		Connection conn = null;
		PreparedStatement ps = null;
		String sql=null;
		Class.forName(driver);
		conn = DriverManager.getConnection(url,user,password);
		
		//2009-04-08 18:46:10.0
		for(BIDR bidr:colls){
			String []dateStr = bidr.getLogin_date().toString().split(" ");
			String temp = dateStr[0];
			String i[]=temp.split("-");
			String tableNum=i[2];
			if(Integer.parseInt(tableNum)<10){
				String index = tableNum.substring(1);
				 sql = "insert into  t_detail_"+index+" values(?,?,?,?,?,?)";
			}
			else{		
				sql = "insert into  t_detail_"+tableNum+" values(?,?,?,?,?,?)";
			}
		ps = conn.prepareStatement(sql);
		ps.setString(1, bidr.getAAA_login_name());
		ps.setString(2, bidr.getLogin_ip());
		ps.setDate(3, new java.sql.Date(bidr.getLogin_date().getTime()));
		ps.setDate(4, new java.sql.Date(bidr.getLogout_date().getTime()));
		ps.setString(5, bidr.getNAS_ip());
		ps.setInt(6, bidr.getTime_deration());
		ps.executeQuery();
		if(ps!=null){
			ps.close();
		}
		}
		
		conn.commit();
		log.info("数据库存储完成");
		if(conn!=null){
			conn.close();
		}
		
		}

	@Override
	public void setConfiguration(Configuration arg0) {
		config = (ConfigImpl)arg0;
		init(ConfigImpl.properties);
	}
	
		public static void main(String args[]){
			DBStoreImpl i = new DBStoreImpl();
			i.setConfiguration(new ConfigImpl());
		}
		
	}

