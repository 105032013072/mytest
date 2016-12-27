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


package com.bosssoft.platform.es.jdbc.model;

import java.util.List;

import com.bosssoft.platform.es.jdbc.enumeration.AggType;
import com.bosssoft.platform.es.jdbc.mate.ColumnMate;
import com.bosssoft.platform.es.jdbc.mate.OrderbyMate;
import com.bosssoft.platform.es.jdbc.mate.PageMate;

/**
 * TODO sql语句封装对象
 *
 * @author huangxuewen (mailto:huangxuewen@bosssoft.com.cn)
 */

public class SelectSqlObj {

	private Boolean distinct;
	
	private List<ColumnMate> selectItems;
	
	private String from ;
	
	private ConditionExp where;
	
	private List<ColumnMate> groupby;
	
	private ConditionExp having;
	
	private List<OrderbyMate> orderby;
	
	private PageMate limit;

	public Boolean getDistinct() {
		return distinct;
	}

	public void setDistinct(Boolean distinct) {
		this.distinct = distinct;
	}

	public List<ColumnMate> getSelectItems() {
		return selectItems;
	}

	public void setSelectItems(List<ColumnMate> selectItems) {
		this.selectItems = selectItems;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	

	public List<ColumnMate> getGroupby() {
		return groupby;
	}

	public void setGroupby(List<ColumnMate> groupby) {
		this.groupby = groupby;
	}

	public List<OrderbyMate> getOrderby() {
		return orderby;
	}

	public void setOrderby(List<OrderbyMate> orderby) {
		this.orderby = orderby;
	}

	public PageMate getLimit() {
		return limit;
	}

	public void setLimit(PageMate limit) {
		this.limit = limit;
	}

	public ConditionExp getWhere() {
		return where;
	}

	public void setWhere(ConditionExp where) {
		this.where = where;
	}

	public ConditionExp getHaving() {
		return having;
	}

	public void setHaving(ConditionExp having) {
		this.having = having;
	}
	
	public  Boolean hasAggregation(){
		for (ColumnMate columnMate : selectItems) {
			if(!AggType.NONE.equals(columnMate.getAggType())) return true;
		}
		return false;
	}
	
}

