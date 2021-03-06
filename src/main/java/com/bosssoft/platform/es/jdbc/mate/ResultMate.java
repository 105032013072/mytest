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
 * Created on 2016年12月27日
 *******************************************************************************/


package com.bosssoft.platform.es.jdbc.mate;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO 此处填写 class 信息
 *
 * @author huangxuewen (mailto:huangxuewen@bosssoft.com.cn)
 */

public class ResultMate {

	private String field;
	
	private Object value;
	
	private List<Object> buckList=new ArrayList<>();

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	
	public List<Object> getBuckList() {
		return buckList;
	}

	public void setBuckList(List<Object> buckList) {
		this.buckList = buckList;
	}

	public void add(Object mate){
		buckList.add(mate);
	}
	
}

/*
 * 修改历史
 * $Log$ 
 */