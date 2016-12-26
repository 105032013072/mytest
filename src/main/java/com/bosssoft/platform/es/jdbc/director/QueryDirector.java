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
 * Created on 2016年12月25日
 *******************************************************************************/


package com.bosssoft.platform.es.jdbc.director;

import org.elasticsearch.search.aggregations.AggregationBuilder;

import com.bosssoft.platform.es.jdbc.constructer.QueryConstructer;
import com.bosssoft.platform.es.jdbc.model.QueryBody;
import com.bosssoft.platform.es.jdbc.model.SelectSqlObj;

/**
 * TODO es的查询体的构建指导类
 *
 * @author huangxuewen (mailto:huangxuewen@bosssoft.com.cn)
 */

public class QueryDirector {
   
	private QueryConstructer constructer;
	
	public QueryDirector(QueryConstructer constructer){
		this.constructer=constructer;
		
	}
	
	public  QueryBody constructQuery(SelectSqlObj obj){
		QueryBody queryBody=new QueryBody();
		AggregationBuilder aggregationBuilder=constructer.distinctConstruct(obj);
		queryBody.setAggregationBuilder(aggregationBuilder);
		return queryBody;
		
	}
}

/*
 * 修改历史
 * $Log$ 
 */