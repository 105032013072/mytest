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

import java.util.List;

import com.facebook.presto.sql.tree.Expression;

/**
 * TODO IN
 *
 * @author huangxuewen (mailto:huangxuewen@bosssoft.com.cn)
 */

public class InExpression {
   private String field;
   
   private List<Expression> rangeList;
   
   private String type;//数据类型

public String getField() {
	return field;
}

public void setField(String field) {
	this.field = field;
}



public List<Expression> getRangeList() {
	return rangeList;
}

public void setRangeList(List<Expression> rangeList) {
	this.rangeList = rangeList;
}

public String getType() {
	return type;
}

public void setType(String type) {
	this.type = type;
}

@Override
public String toString() {
	return "InExpression [field=" + field + ", rangeList=" + rangeList
			+ ", type=" + type + "]";
}
   
   
}

/*
 * 修改历史
 * $Log$ 
 */