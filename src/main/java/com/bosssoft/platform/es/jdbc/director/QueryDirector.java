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

import java.sql.SQLException;

import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
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
	
	public  QueryBody constructQuery(SelectSqlObj obj) throws SQLException{
		QueryBody queryBody=new QueryBody();
		
		//distinct
		if(obj.getDistinct()==true){
			AggregationBuilder aggregationBuilder=constructer.distinctConstruct(obj.getSelectItems());
			queryBody.setAggregationBuilder(aggregationBuilder);
		}
		
		//聚合函数(一定要有一个QueryBuilder)
		QueryBuilder queryBuilder=QueryBuilders.matchAllQuery();
		AggregationBuilder aggregationBuilder=constructer.aggregateConstruct(obj.getSelectItems(),queryBuilder);
	    queryBody.setAggregationBuilder(aggregationBuilder);
		return queryBody;
		
	}
}

/*
 * 修改历史
 * $Log$ 
 */