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
 * Created on 2017年1月4日
 *******************************************************************************/


package com.bosssoft.platform.es.jdbc.model;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO 封装delete信息
 *
 * @author huangxuewen (mailto:huangxuewen@bosssoft.com.cn)
 */

public class DeleteSqlObj {

	private String type;
	
	private List<String> ids;
	
	public DeleteSqlObj(){
		ids=new ArrayList<>();
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public List<String> getIds() {
		return ids;
	}

	public void setIds(List<String> ids) {
		this.ids = ids;
	}
	
	public void addid(String id){
		ids.add(id);
	}
	
	
	
}

/*
 * 修改历史
 * $Log$ 
 */