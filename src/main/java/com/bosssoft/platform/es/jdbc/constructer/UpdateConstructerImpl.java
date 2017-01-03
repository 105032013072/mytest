/*******************************************************************************
 * $Header$
 * $Revision$
 * $Date$
 *
 *==============================================================================
 *
 * Copyright (c) 2001-2016 Bosssoft Co, Ltd.
 * All rights reserved.
 * 
 * Created on 2016年12月22日
 *******************************************************************************/


package com.bosssoft.platform.es.jdbc.constructer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * TODO 此处填写 class 信息
 *
 * @author huangxuewen (mailto:huangxuewen@bosssoft.com.cn)
 */

public class UpdateConstructerImpl implements UpdateConstructer{
   
	public  String buildCreate(String table,String index) {
		try{
			String mapping=null;
			URL url=Thread.currentThread().getContextClassLoader().getResource("mapping");
			File configDir=new File(url.toURI());
			File[] fileList=configDir.listFiles();
			for (File file : fileList) {
				if(file.getName().equals(table+".json")){
				   mapping=readFile(file);
					break;
				}
			}
			return mapping;
		}catch(Exception e){
			e.printStackTrace();
			return  null;
		}
		
	}
	
	
	private String readFile(File file) throws Exception{
		StringBuffer buffer=new StringBuffer();
		InputStream is = new FileInputStream(file);
		String line; // 用来保存每行读取的内容
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        line = reader.readLine(); // 读取第一行
        while (line != null) { // 如果 line 为空说明读完了
            buffer.append(line); // 将读到的内容添加到 buffer 中
            buffer.append("\n"); // 添加换行符
            line = reader.readLine(); // 读取下一行
        }
        reader.close();
        is.close();
        return buffer.toString();
	}
	
}

/*
 * 修改历史
 * $Log$ 
 */