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
		QueryBuilder qb=null;
		AggregationBuilder aggregationBuilder=null;
		//distinct
		if(obj.getDistinct()==true){
			aggregationBuilder=constructer.distinctConstruct(obj.getSelectItems());
		}
		
		//where
		if(obj.getWhere()==null){
			qb=QueryBuilders.matchAllQuery();
		}else{
			qb=constructer.whereConstruct(obj.getWhere());
		}
		
	    //聚合函数
		if(obj.hasAggregation()){
			if(obj.getGroupby()==null){//单纯只有聚合函数
				aggregationBuilder=constructer.aggregateConstruct(obj.getSelectItems(),qb);
			}else{//group by（在group by基础上处理聚合函数）
				aggregationBuilder=constructer.groupConstruct(obj.getGroupby(), obj.getSelectItems());
			}
			
		}
		queryBody.setAggregationBuilder(aggregationBuilder);
		queryBody.setOrderby(obj.getOrderby());
		queryBody.setPageMate(obj.getLimit());
		queryBody.setQueryBuilder(qb);
		return queryBody;
		
	}
}

/*
 * 修改历史
 * $Log$ 
 */