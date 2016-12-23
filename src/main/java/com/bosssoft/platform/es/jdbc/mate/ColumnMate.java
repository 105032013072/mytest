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
 * Created on 2016年12月21日
 *******************************************************************************/


package com.bosssoft.platform.es.jdbc.mate;

import com.bosssoft.platform.es.jdbc.enumeration.AggType;

/**
 * TODO 此处填写 class 信息
 *
 * @author huangxuewen (mailto:huangxuewen@bosssoft.com.cn)
 */

public class ColumnMate {

	private AggType aggType;
	
	private String name;
	
	private String alias;

	public AggType getAggType() {
		return aggType;
	}

	public void setAggType(AggType aggType) {
		this.aggType = aggType;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}
	
	
	
}
