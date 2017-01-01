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

import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.Aggregations;

import com.bosssoft.platform.es.jdbc.mate.ColumnMate;
import com.bosssoft.platform.es.jdbc.model.ConditionExp;
import com.bosssoft.platform.es.jdbc.model.ESResultSet;

/**
 * TODO 此处填写 class 信息
 *
 * @author huangxuewen (mailto:huangxuewen@bosssoft.com.cn)
 */

public interface ResultSetConstructer {
	
	public ESResultSet constructAllColumn(SearchHits hits);
	
	public ESResultSet constructSelectColumn(SearchHits hits,List<ColumnMate> selectItems);
	
	 public ESResultSet constructDistinct(Aggregations aggregations);
	 
	 public ESResultSet constructGroupby (Aggregations aggregations);
	 
	 public ESResultSet constructAggregation(Aggregations aggregations);
	 
	 public void resolveHaving(ESResultSet resultSet,ConditionExp having) throws SQLException;

}

/*
 * 修改历史
 * $Log$ 
 */