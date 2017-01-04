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
 * TODO 封装insert语句信息
 *
 * @author huangxuewen (mailto:huangxuewen@bosssoft.com.cn)
 */

public class InsertSqlObj {
  
	private String type;
	
	private List<ColumnValue> valueList;
	
	public InsertSqlObj(){
		valueList=new ArrayList<>();
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public List<ColumnValue> getValueList() {
		return valueList;
	}

	public void setValueList(List<ColumnValue> valueList) {
		this.valueList = valueList;
	}
	
	public void addValue(String field,Object object){
		ColumnValue columnValue=new ColumnValue();
		columnValue.setField(field);
		columnValue.setValue(object);
		valueList.add(columnValue);
	}
}

/*
 * 修改历史
 * $Log$ 
 */