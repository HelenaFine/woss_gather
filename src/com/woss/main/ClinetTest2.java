package com.woss.main;

import java.util.Collection;

import com.briup.util.BIDR;
import com.briup.util.Configuration;
import com.woss.client.ClientImpl;
import com.woss.client.GatherImpl;
import com.woss.common.BackUpImpl;
import com.woss.common.ConfigImpl;


public class ClinetTest2 {

	public static void main(String[] args) throws Exception {
		Configuration con = new ConfigImpl();
		ClientImpl client = (ClientImpl) con.getClient();
		GatherImpl gather = (GatherImpl) con.getGather();
		BackUpImpl back = (BackUpImpl) con.getBackup();
		Collection<BIDR> gather1 = gather.gather();
		client.setConfiguration(con);
		client.send(gather1);
		
	}
}
