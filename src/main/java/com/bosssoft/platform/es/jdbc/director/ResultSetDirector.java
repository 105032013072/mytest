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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.elasticsearch.action.search.SearchResponse;

import com.bosssoft.platform.es.jdbc.constructer.ResultSetConstructer;
import com.bosssoft.platform.es.jdbc.driver.ESConnection;
import com.bosssoft.platform.es.jdbc.mate.ColumnMate;
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
	
	public ESResultSet construct(SearchResponse response,SelectSqlObj obj,ESConnection connection) throws SQLException{
		ESResultSet esResultSet=null;
		
		if(response.getAggregations()==null){//没有聚合：
			if("*".equals(obj.getSelectItems().get(0).getName())){//select *
				esResultSet=constructer.constructAllColumn(response.getHits());
			}else {//select column
				esResultSet=constructer.constructSelectColumn(response.getHits(), obj.getSelectItems());
			}
			
		}else{
			if(obj.getDistinct()){//select distinct
				esResultSet=constructer.constructDistinct(response,obj.getSelectItems());
			}else{//group by +聚合函数
				if(obj.getGroupby()==null){//单纯只有聚合函数
					esResultSet=constructer.constructAggregation(response.getAggregations());
				}else{//group by（在group by基础上处理聚合函数）
					esResultSet=constructer.constructGroupby(response.getAggregations());
					//having
					if(obj.getHaving()!=null) constructer.resolveHaving(esResultSet, obj.getHaving());
				}
			}
		}
		
		
		
		if(response.getAggregations()!=null){
			if(obj.getOrderby().size()!=0) esResultSet.sort(obj.getOrderby());//排序
			if(obj.getLimit()!=null) esResultSet.page(obj.getLimit());//分页
		}
		
		esResultSet.setTotal(esResultSet.getResultList().size());
		
		//构建该type所有列
		constructer.buildAllColumn(esResultSet, obj, connection);
		constructer.buildMetaDta(esResultSet, obj, connection);
		
		return esResultSet;
		
		
	}

}

/*
 * 修改历史
 * $Log$ 
 */