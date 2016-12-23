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
 * Created on 2016年12月23日
 *******************************************************************************/


package com.bosssoft.platform.es.jdbc.mate;

/**
 * 不等式 （>= = ><=,!=）
 *
 * @author huangxuewen (mailto:huangxuewen@bosssoft.com.cn)
 */

public class Inequality {

    private String filed;
	
	private Object value;
	
	private String operation;

	public String getFiled() {
		return filed;
	}

	public void setFiled(String filed) {
		this.filed = filed;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}

	@Override
	public String toString() {
		return "Inequality [filed=" + filed + ", value=" + value
				+ ", operation=" + operation + "]";
	}
	
	
}

/*
 * 修改历史
 * $Log$ 
 */