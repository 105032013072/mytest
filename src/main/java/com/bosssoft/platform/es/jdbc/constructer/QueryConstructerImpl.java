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



import java.util.List;

import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.TermsBuilder;

import com.bosssoft.platform.es.jdbc.mate.ColumnMate;
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
	public AggregationBuilder distinctConstruct(SelectSqlObj obj){
		if(obj.getDistinct()==true){
		List<ColumnMate> selectItems=obj.getSelectItems();
		TermsBuilder result=null;
		for(int i=0;i<selectItems.size();i++){
		   if(i==0) result=getTerm(selectItems.get(i));
		   else result=result.subAggregation(getTerm(selectItems.get(i)));
		}
		return result;
	}else return null;
 }
	
	private TermsBuilder getTerm(ColumnMate columnMate){
		return AggregationBuilders.terms(columnMate.getName()).field(columnMate.getName());
	}
}

/*
 * 修改历史
 * $Log$ 
 */