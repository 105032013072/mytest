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

import com.bosssoft.platform.es.jdbc.model.ConditionExp;

/**
 * TODO NOT [condition]
 *
 * @author huangxuewen (mailto:huangxuewen@bosssoft.com.cn)
 */

public class ConNotExpression {
   
	private ConditionExp conditionExp;

	public ConditionExp getConditionExp() {
		return conditionExp;
	}

	public void setConditionExp(ConditionExp conditionExp) {
		this.conditionExp = conditionExp;
	}

	@Override
	public String toString() {
		return "ConNotExpression [conditionExp=" + conditionExp + "]";
	}
	
	
	
}

/*
 * 修改历史
 * $Log$ 
 */