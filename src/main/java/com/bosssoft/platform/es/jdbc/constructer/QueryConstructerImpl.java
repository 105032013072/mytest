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
 * Created on 2016年12月22日
 *******************************************************************************/


package com.bosssoft.platform.es.jdbc.constructer;



import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AbstractAggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.filter.FilterAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.terms.TermsBuilder;
import org.elasticsearch.search.aggregations.metrics.MetricsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.avg.AvgBuilder;

import com.bosssoft.platform.es.jdbc.enumeration.AggType;
import com.bosssoft.platform.es.jdbc.mate.ColumnMate;
import com.bosssoft.platform.es.jdbc.mate.Inequality;
import com.bosssoft.platform.es.jdbc.model.ConditionExp;
import com.bosssoft.platform.es.jdbc.model.SelectSqlObj;


/**
 * TODO 此处填写 class 信息
 *
 * @author huangxuewen (mailto:huangxuewen@bosssoft.com.cn)
 */

public class QueryConstructerImpl implements QueryConstructer{

	/**
	 * distinct 处理
	 * @param obj
	 * @return
	 */
	public AggregationBuilder distinctConstruct(List<ColumnMate> selectItems){
		TermsBuilder result=null;
		for(int i=0;i<selectItems.size();i++){
		   if(i==0) result=getDistinctTerm(selectItems.get(i));
		   else result=result.subAggregation(getDistinctTerm(selectItems.get(i)));
		}
		return result;
 }
	
	private TermsBuilder getDistinctTerm(ColumnMate columnMate){
		return AggregationBuilders.terms(columnMate.getName()).field(columnMate.getName());
	}
	
	
	/**
	 * 聚合函数的处理
	 */
	public AggregationBuilder aggregateConstruct(List<ColumnMate> selectItems,QueryBuilder queryBuilder)throws SQLException{
		FilterAggregationBuilder result = AggregationBuilders.filter("aggregationfilter").filter(queryBuilder);
		
		for (ColumnMate columnMate : selectItems) {
			if(!AggType.NONE.equals(columnMate.getAggType())){
				result.subAggregation(getAggregateTerm(columnMate));
			}
		}
		
		return result;
	}
	
	private MetricsAggregationBuilder getAggregateTerm(ColumnMate columnMate)throws SQLException{
		MetricsAggregationBuilder builder=null;
		AggType aggType=columnMate.getAggType();
		
		//获取聚合名
		String aggName=null;
		if(columnMate.getAlias()==null) aggName=columnMate.getName();
		else aggName=columnMate.getAlias();
		
		if(AggType.AVG.equals(aggType))
			builder=AggregationBuilders.avg(aggName).field(columnMate.getName());
		else if(AggType.SUM.equals(aggType))
			builder=AggregationBuilders.sum(aggName).field(columnMate.getName());
		else if(AggType.MIN.equals(aggType))
			builder=AggregationBuilders.min(aggName).field(columnMate.getName());
		else if(AggType.MAX.equals(aggType))
			builder=AggregationBuilders.max(aggName).field(columnMate.getName());
		else if(AggType.COUNT.equals(aggType))
			builder=AggregationBuilders.count(aggName).field(columnMate.getName());
		else throw new SQLException("illegal Aggregate function");
		return builder;
	}
	
	/**
	 * where
	 * @throws SQLException 
	 * 
	 */
	public QueryBuilder whereConstruct(ConditionExp conditionExp) throws SQLException{
	    Object obj=conditionExp.getExpression();
		return getQueryBuilder(obj);
	}
	
	private QueryBuilder getQueryBuilder(Object obj) throws SQLException{
		QueryBuilder qb=null;
		if(obj instanceof ConditionExp){
			ConditionExp exp=(ConditionExp) obj;
			
			Object left= exp.getExpression();
			Object right= exp.getNext();
			if("and".equals(exp.getRelation())){
				qb=QueryBuilders.boolQuery().must(getQueryBuilder(left))
						                    .must(getQueryBuilder(right));
			}else{
				qb=QueryBuilders.boolQuery().should(getQueryBuilder(left))
	                    .should(getQueryBuilder(right));
			}	
		}else if(obj instanceof Inequality){//不等式
			qb=resolveInequality((Inequality) obj);
		}else if
		return null;
	}
	
	private QueryBuilder resolveInequality(Inequality inequality) throws SQLException{
		String op=inequality.getOperation();
		QueryBuilder qb=null;
		if("EQUAL".equals(op)){
		    String filed=inequality.getFiled();
		    Object value=inequality.getValue();
			if("".equals(value)) {//作为 is null 处理
				qb=QueryBuilders.boolQuery().mustNot(QueryBuilders.existsQuery(filed));
			}else{
				qb=QueryBuilders.matchQuery(filed,value);
			}
			
		}else if("NOT_EQUAL".equals(op)){//!=
			String filed=inequality.getFiled();
		    Object value=inequality.getValue();
		    
			if("".equals(value)){//作为not null 来处理
				qb=QueryBuilders.existsQuery(filed);
			}else{
				qb=QueryBuilders.boolQuery().mustNot(QueryBuilders.termQuery(filed, value));
			}
		}else if("LESS_THAN".equals(op)){//<
			qb = QueryBuilders.rangeQuery(inequality.getFiled()).lt(inequality.getValue());
			
		}else if("GREATER_THAN".equals(op)){//>
			qb = QueryBuilders.rangeQuery(inequality.getFiled()).gt(inequality.getValue());
		}else if("LESS_THAN_OR_EQUAL".equals(op)){//<=
			qb = QueryBuilders.rangeQuery(inequality.getFiled()).to(inequality.getValue());
		}else if("GREATER_THAN_OR_EQUAL".equals(op)){//>=
			qb = QueryBuilders.rangeQuery(inequality.getFiled()).from(inequality.getValue());
		}else throw new SQLException("illegal opreration");
		return qb;
	}
}

/*
 * 修改历史
 * $Log$ 
 */