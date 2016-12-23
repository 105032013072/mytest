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
 * bewteen.. and ..
 *
 * @author huangxuewen (mailto:huangxuewen@bosssoft.com.cn)
 */

public class BetweenExpression {
   private String filed;
   
   private Object bewteen;
   
   private Object and;

public String getFiled() {
	return filed;
}

public void setFiled(String filed) {
	this.filed = filed;
}

public Object getBewteen() {
	return bewteen;
}

public void setBewteen(Object bewteen) {
	this.bewteen = bewteen;
}

public Object getAnd() {
	return and;
}

public void setAnd(Object and) {
	this.and = and;
}

@Override
public String toString() {
	return "BetweenExpression [filed=" + filed + ", bewteen=" + bewteen
			+ ", and=" + and + "]";
}
   
   
}

/*
 * 修改历史
 * $Log$ 
 */