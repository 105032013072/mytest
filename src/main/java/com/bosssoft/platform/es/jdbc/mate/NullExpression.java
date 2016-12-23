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
 * TODO is null,is not null
 *
 * @author huangxuewen (mailto:huangxuewen@bosssoft.com.cn)
 */

public class NullExpression {
   
	private String filed;
	
	private String opration;

	public String getFiled() {
		return filed;
	}

	public void setFiled(String filed) {
		this.filed = filed;
	}

	public String getOpration() {
		return opration;
	}

	public void setOpration(String opration) {
		this.opration = opration;
	}

	@Override
	public String toString() {
		return "NullExpression [filed=" + filed + ", opration=" + opration
				+ "]";
	}
	
	
}

/*
 * 修改历史
 * $Log$ 
 */