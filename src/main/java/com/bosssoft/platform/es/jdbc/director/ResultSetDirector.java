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


package com.bosssoft.platform.es.jdbc.director;

import java.sql.SQLException;

import org.elasticsearch.action.search.SearchResponse;

import com.bosssoft.platform.es.jdbc.constructer.ResultSetConstructer;
import com.bosssoft.platform.es.jdbc.model.ESResultSet;
import com.bosssoft.platform.es.jdbc.model.SelectSqlObj;

/**
 * TODO 指导构建resultSet
 *
 * @author huangxuewen (mailto:huangxuewen@bosssoft.com.cn)
 */

public class ResultSetDirector {

	private ResultSetConstructer constructer;
	
	public ResultSetDirector(ResultSetConstructer constructer){
		this.constructer=constructer;
	}
	
	public ESResultSet construct(SearchResponse response,SelectSqlObj obj) throws SQLException{
		ESResultSet esResultSet=null;
		//esResultSet=constructer.constructAllColumn(response.getHits());
		
		//esResultSet=constructer.constructSelectColumn(response.getHits(), obj.getSelectItems());
		
		//esResultSet=constructer.constructDistinct(response.getAggregations());
	   esResultSet=constructer.constructGroupby(response.getAggregations());
		//esResultSet=constructer.constructAggregation(response.getAggregations());
		
		
		//处理having
		constructer.resolveHaving(esResultSet, obj.getHaving());
		return esResultSet;
	}
}

/*
 * 修改历史
 * $Log$ 
 */