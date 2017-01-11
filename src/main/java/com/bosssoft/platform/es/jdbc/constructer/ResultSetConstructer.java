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
 * Created on 2016年12月27日
 *******************************************************************************/


package com.bosssoft.platform.es.jdbc.constructer;

import java.sql.SQLException;
import java.util.List;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.Aggregations;

import com.bosssoft.platform.es.jdbc.driver.ESConnection;
import com.bosssoft.platform.es.jdbc.mate.ColumnMate;
import com.bosssoft.platform.es.jdbc.model.ConditionExp;
import com.bosssoft.platform.es.jdbc.model.ESResultSet;
import com.bosssoft.platform.es.jdbc.model.SelectSqlObj;

/**
 * TODO 此处填写 class 信息
 *
 * @author huangxuewen (mailto:huangxuewen@bosssoft.com.cn)
 */

public interface ResultSetConstructer {
	
	public ESResultSet constructAllColumn(SearchHits hits);
	
	public ESResultSet constructSelectColumn(SearchHits hits,List<ColumnMate> selectItems);
	
	public ESResultSet constructDistinct(SearchResponse response,List<ColumnMate> selectItems);
	 
	 public ESResultSet constructGroupby (Aggregations aggregations);
	 
	 public ESResultSet constructAggregation(Aggregations aggregations);
	 
	 public void resolveHaving(ESResultSet resultSet,ConditionExp having) throws SQLException;
	 
	 public void buildAllColumn(ESResultSet resultSet,SelectSqlObj obj,ESConnection connection);
	 
	 public void buildMetaDta(ESResultSet resultSet, SelectSqlObj obj,ESConnection connection);

}

/*
 * 修改历史
 * $Log$ 
 */