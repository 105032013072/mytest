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
 * Created on 2016年12月26日
 *******************************************************************************/


package com.bosssoft.platform.es.jdbc.model;

import java.util.List;

import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilder;

import com.bosssoft.platform.es.jdbc.mate.OrderbyMate;
import com.bosssoft.platform.es.jdbc.mate.PageMate;


/**
 * TODO 
 *
 * @author huangxuewen (mailto:huangxuewen@bosssoft.com.cn)
 */

public class QueryBody {

	private QueryBuilder queryBuilder;
	
	private AggregationBuilder aggregationBuilder;
	
	private PageMate pageMate;//分页信息
	
	private List<OrderbyMate> orderby;

	public QueryBuilder getQueryBuilder() {
		return queryBuilder;
	}

	public void setQueryBuilder(QueryBuilder queryBuilder) {
		this.queryBuilder = queryBuilder;
	}

	public AggregationBuilder getAggregationBuilder() {
		return aggregationBuilder;
	}

	public void setAggregationBuilder(AggregationBuilder aggregationBuilder) {
		this.aggregationBuilder = aggregationBuilder;
	}

	public PageMate getPageMate() {
		return pageMate;
	}

	public void setPageMate(PageMate pageMate) {
		this.pageMate = pageMate;
	}

	public List<OrderbyMate> getOrderby() {
		return orderby;
	}

	public void setOrderby(List<OrderbyMate> orderby) {
		this.orderby = orderby;
	}

	
	
	
	
}

/*
 * 修改历史
 * $Log$ 
 */