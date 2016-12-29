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
 * Created on 2016年12月29日
 *******************************************************************************/


package com.bosssoft.platform.es.jdbc.mate;

import java.util.HashMap;
import java.util.Map;

/**
 * TODO 此处填写 class 信息
 *
 * @author huangxuewen (mailto:huangxuewen@bosssoft.com.cn)
 */

public class AggValue {
  
	private String field;
	
	private Object value;
	
	private Map<String, Object> map=new HashMap<>();

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

	public Map<String, Object> getMap() {
		return map;
	}

	public void setMap(Map<String, Object> map) {
		this.map = map;
	}
	
	public void add(String key,Object value){
		map.put(key, value);
	}
}

/*
 * 修改历史
 * $Log$ 
 */