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

/**
 * TODO 存储列以及对应的值
 *
 * @author huangxuewen (mailto:huangxuewen@bosssoft.com.cn)
 */

public class ColumnValue {

	private String field;
	
	private Object value;

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

	@Override
	public String toString() {
		return "ColumnValue [field=" + field + ", value=" + value + "]";
	}
	
	
}

/*
 * 修改历史
 * $Log$ 
 */