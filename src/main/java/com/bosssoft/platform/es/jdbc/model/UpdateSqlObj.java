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

import com.bosssoft.platform.es.jdbc.mate.ColumnValue;



/**
 * TODO 封装update语句的信息
 *
 * @author huangxuewen (mailto:huangxuewen@bosssoft.com.cn)
 */

public class UpdateSqlObj {
   
	private String index;
	
	private String type;
	
	private List<ColumnValue> updateList;//需要更新的列
	
	private List<String> ids;//满足条件的文档Id
	
	public UpdateSqlObj(){
		updateList=new ArrayList<>();
		ids=new ArrayList<>();
	}

	public String getIndex() {
		return index;
	}

	public void setIndex(String index) {
		this.index = index;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public List<ColumnValue> getUpdateList() {
		return updateList;
	}

	public void setUpdateList(List<ColumnValue> updateList) {
		this.updateList = updateList;
	}

	public List<String> getIds() {
		return ids;
	}

	public void setIds(List<String> ids) {
		this.ids = ids;
	}
	
	public void addUpdateList(String field,Object value){
		ColumnValue columnValue=new ColumnValue();
		columnValue.setField(field);
		columnValue.setValue(value);
		updateList.add(columnValue);
	}
	
	public void addIds(String id){
		ids.add(id);
	}
}

/*
 * 修改历史
 * $Log$ 
 */