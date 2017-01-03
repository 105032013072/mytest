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
 * Created on 2016年12月25日
 *******************************************************************************/


package com.bosssoft.platform.es.jdbc.director;

import java.net.URISyntaxException;

import com.bosssoft.platform.es.jdbc.constructer.UpdateConstructer;

/**
 * TODO es的更新体的构建指导类
 *
 * @author huangxuewen (mailto:huangxuewen@bosssoft.com.cn)
 */

public class UpdateDirector {

	private UpdateConstructer builder;
	
	public UpdateDirector(UpdateConstructer builder){
		this.builder=builder;
	}
	
	public String buildCreate(String table,String index) {
		try {
			return builder.buildCreate(table, index);
		} catch (URISyntaxException e) {
			
			e.printStackTrace();
			return null;
		}
	}
}

/*
 * 修改历史
 * $Log$ 
 */