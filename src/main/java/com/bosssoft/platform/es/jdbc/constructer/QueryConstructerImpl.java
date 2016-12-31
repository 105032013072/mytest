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

import org.elasticsearch.index.query.BoolQueryBuilder;
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
import com.bosssoft.platform.es.jdbc.mate.BetweenExpression;
import com.bosssoft.platform.es.jdbc.mate.ColumnMate;
import com.bosssoft.platform.es.jdbc.mate.ConNotExpression;
import com.bosssoft.platform.es.jdbc.mate.InExpression;
import com.bosssoft.platform.es.jdbc.mate.Inequality;
import com.bosssoft.platform.es.jdbc.mate.NullExpression;
import com.bosssoft.platform.es.jdbc.model.ConditionExp;
import com.bosssoft.platform.es.jdbc.model.SelectSqlObj;
import com.facebook.presto.sql.tree.DoubleLiteral;
import com.facebook.presto.sql.tree.Expression;
import com.facebook.presto.sql.tree.LongLiteral;
import com.facebook.presto.sql.tree.NotExpression;
import com.facebook.presto.sql.tree.StringLiteral;


/**
 * TODO 此处填写 class 信息
 *
 * @author huangxuewen (mailto:huangxuewen@bosssoft.com.cn)
 */

public class QueryConstructerImpl implements QueryConstructer{
	
	private static Judger judger=new Judger();

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
			if(!AggType.NONE.equals(columnMate.getAggType())&&!"*".equals(columnMate.getName())){//* 的聚合函数在结果集中处理
				result.subAggregation(getAggregateTerm(columnMate));
			}
		}
		
		return result;
	}
	
	private MetricsAggregationBuilder getAggregateTerm(ColumnMate columnMate)throws SQLException{
		MetricsAggregationBuilder builder=null;
		AggType aggType=columnMate.getAggType();
		
		//获取聚合名
		String aggName=columnMate.getAlias();
		
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
		return getQueryBuilder(conditionExp);
	}
	
	private QueryBuilder getQueryBuilder(ConditionExp conexp) throws SQLException{
		Object obj=conexp.getExpression();
		QueryBuilder qb=null;
		if(obj instanceof ConditionExp){
			qb=resolveConditionExp((ConditionExp) obj);	
		}else if(obj instanceof Inequality){//不等式
			qb=resolveInequality((Inequality) obj);
		}else if(obj instanceof NullExpression){//is null   is not null
			qb=resolveNullExpression((NullExpression) obj);
		}else if(obj instanceof InExpression){//where  in
			qb=resolveInExpression((InExpression) obj);
		}else if(obj instanceof ConNotExpression){
			ConNotExpression exp=(ConNotExpression) obj;
			qb=QueryBuilders.boolQuery().mustNot(whereConstruct(exp.getConditionExp()));
		}else if(obj instanceof BetweenExpression){
			qb=resolveBetween((BetweenExpression) obj);
		}
		return qb;
	}
	
	private QueryBuilder resolveConditionExp(ConditionExp exp) throws SQLException{
		QueryBuilder qb=null;
		ConditionExp left= (ConditionExp) exp.getExpression();
		ConditionExp right= (ConditionExp) exp.getNext();
		if("AND".equals(exp.getRelation())){
			qb=QueryBuilders.boolQuery().must(getQueryBuilder(left))
					                    .must(getQueryBuilder(right));
		}else{
			qb=QueryBuilders.boolQuery().should(getQueryBuilder(left))
                    .should(getQueryBuilder(right));
		}
		return qb;
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
	
	private QueryBuilder resolveNullExpression(NullExpression nullExpression) throws SQLException{
		String op=nullExpression.getOpration();
		QueryBuilder qb=null;
		if("is null".equals(op)){
			qb=QueryBuilders.existsQuery(nullExpression.getFiled());
		}else if("is not null".equals(op)){
			qb=qb=QueryBuilders.boolQuery().mustNot(QueryBuilders.existsQuery(nullExpression.getFiled()));
		}else throw new SQLException("illegal opreration");
		return qb;
	}
	
	 private QueryBuilder resolveInExpression(InExpression inExpression){
		 String field=inExpression.getField();
		 List<Expression> range=inExpression.getRangeList();
		 List<Object> target=new ArrayList<>();
		 Expression t= range.get(0);
		 
		 //转化范围的具体类型
		for (Expression expression : range) {
			target.add(judger.judgeNumType(expression));
		}
		 //正式处理
		 BoolQueryBuilder bqb=QueryBuilders.boolQuery();
		 for (Object object : target) {
			bqb.should(QueryBuilders.matchQuery(field, object));
		}
		 return bqb;
		 
	 }
	
	 private QueryBuilder resolveBetween(BetweenExpression betweenExpression){
		 
		 String field=betweenExpression.getFiled();
		 Object between=judger.judgeNumType(betweenExpression.getBewteen());
		 Object and=judger.judgeNumType(betweenExpression.getAnd());
		 return QueryBuilders.rangeQuery(field).from(between).to(and);
	 }
	 
	 /**
	  * group by
	 * @throws SQLException 
	  */
	 
	 public AggregationBuilder groupConstruct(List<ColumnMate> groupList,List<ColumnMate> selectItems) throws SQLException{
		 TermsBuilder result=null;
		 for (int i = 0; i < groupList.size(); i++) {
			if(i==0){
				result=AggregationBuilders.terms(groupList.get(i).getName()).field(groupList.get(i).getName());
				if(i==groupList.size()-1){//最后一个列的group by 添加聚合函数
					addAggregation(result,selectItems);
				}
			} 
			else {
				TermsBuilder helper=AggregationBuilders.terms(groupList.get(i).getName()).field(groupList.get(i).getName());
				if(i==groupList.size()-1){//最后一个列的group by 添加聚合函数
					addAggregation(helper,selectItems);
				}
				result=result.subAggregation(helper);
			}
		}
		 return result;
	 }
	 
	 private void addAggregation(TermsBuilder termsBuilder,List<ColumnMate> selectItems) throws SQLException{
		 for (ColumnMate columnMate : selectItems) {
			 if(!AggType.NONE.equals(columnMate.getAggType())){
				 termsBuilder.subAggregation(getAggregateTerm(columnMate));
			 }
		}
	 }
	 
}

/*
 * 修改历史
 * $Log$ 
 */