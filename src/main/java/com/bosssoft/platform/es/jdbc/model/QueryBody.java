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

import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilder;


/**
 * TODO 
 *
 * @author huangxuewen (mailto:huangxuewen@bosssoft.com.cn)
 */

public class QueryBody {

	private QueryBuilder queryBuilder;
	
	private AggregationBuilder aggregationBuilder;

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

	
	
	
	
}

/*
 * 修改历史
 * $Log$ 
 */