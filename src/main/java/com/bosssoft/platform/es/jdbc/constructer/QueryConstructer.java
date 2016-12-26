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


package com.bosssoft.platform.es.jdbc.constructer;

import java.sql.SQLException;
import java.util.List;

import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilder;

import com.bosssoft.platform.es.jdbc.mate.ColumnMate;
import com.bosssoft.platform.es.jdbc.model.SelectSqlObj;

/**
 * TODO 此处填写 class 信息
 *
 * @author huangxuewen (mailto:huangxuewen@bosssoft.com.cn)
 */

public interface QueryConstructer {
   
	public AggregationBuilder distinctConstruct(List<ColumnMate> selectItems);
	
	/**
	 * 聚合函数的处理
	 */
	public AggregationBuilder aggregateConstruct(List<ColumnMate> selectItems,QueryBuilder queryBuilder)throws SQLException;
}

/*
 * 修改历史
 * $Log$ 
 */