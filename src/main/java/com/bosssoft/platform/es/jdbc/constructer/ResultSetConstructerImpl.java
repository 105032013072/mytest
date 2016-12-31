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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.lucene.index.Term;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.filter.InternalFilter;
import org.elasticsearch.search.aggregations.bucket.filters.InternalFilters;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.metrics.InternalNumericMetricsAggregation;
import org.elasticsearch.search.aggregations.metrics.max.InternalMax;

import com.bosssoft.platform.es.jdbc.mate.AggValue;
import com.bosssoft.platform.es.jdbc.mate.ColumnMate;
import com.bosssoft.platform.es.jdbc.mate.ColumnValue;
import com.bosssoft.platform.es.jdbc.mate.ResultMate;
import com.bosssoft.platform.es.jdbc.model.ConditionExp;
import com.bosssoft.platform.es.jdbc.model.ESResultSet;

/**
 * TODO 此处填写 class 信息
 *
 * @author huangxuewen (mailto:huangxuewen@bosssoft.com.cn)
 */

public class ResultSetConstructerImpl implements ResultSetConstructer{

	/**
	 * select *
	 */
    public ESResultSet constructAllColumn(SearchHits hits){
    	ESResultSet resultSet=new ESResultSet();
    	SearchHit[] searchHists = hits.getHits();
    	if (searchHists.length > 0) {
			for (SearchHit hit : searchHists) {
				Map<String, Object> map=hit.getSource();
				resultSet.add(map);
			} 
		}
    	resultSet.setTotal(resultSet.getResultList().size());
    	return resultSet;
    }
    
    /**
     * select column
     * @param hits
     * @return
     */
    public ESResultSet constructSelectColumn(SearchHits hits,List<ColumnMate> selectItems){
    	ESResultSet resultSet=new ESResultSet();
    	SearchHit[] searchHists = hits.getHits();
    	if (searchHists.length > 0) {
			for (SearchHit hit : searchHists) {
				Map<String, Object> map=hit.getSource();
				Map<String,Object> help=new HashMap<>();
				//遍历结果集，判断是否在selectItems中
				for (String key : map.keySet()) {
					if(!isNeed(key, selectItems)) continue;
					help.put(key, map.get(key));
				}
				resultSet.add(help);
				
			} 
		}
    	resultSet.setTotal(resultSet.getResultList().size());
    	return resultSet;
    }
    
    private Boolean isNeed(String key,List<ColumnMate> selectItems){
    	for (ColumnMate columnMate : selectItems) {
			String filed=columnMate.getName();
			if(key.equals(filed)) return true;
		}
    	return false;
    }
    
    /**
     * count（*）
     */
    public void constructCount(SearchHits hits){
    	
    }
    
    /**
     * select distinct
     * @param aggregation
     */
    public ESResultSet constructDistinct(Aggregations aggregations){
    	   List<Object> list=new ArrayList<>();
    	  
    		list=resolveAggs(aggregations.asList());
    		return parserToResultSet(list);
    		
    }
    
    /**
     * 
     * group by
     * @param list
     * @return
     */
    public ESResultSet constructGroupby (Aggregations aggregations){
    	List<Object> list=new ArrayList<>();
  	  
		list=resolveAggs(aggregations.asList());
		return parserToResultSet(list);
		
    }
    
	private List<Object> resolveAggs(List<Aggregation> list) {
		List<Object> result=new ArrayList<>();
		Map<String,Object> columnMap=new HashMap<>();
		Boolean flag=false;
    	for (Aggregation agg : list) {
    		if(agg instanceof Terms){
    			Terms aggterms=(Terms) agg;
        		for(Terms.Bucket bucket : aggterms.getBuckets()){
        			
        			List<Aggregation> aggList = bucket.getAggregations().asList();
        			if(aggList.size()!=0) {
        				ResultMate mate=new ResultMate();
            			mate.setField(aggterms.getName());
        				mate.setValue(bucket.getKey());
            			mate.setBuckList(resolveAggs(aggList));
            			result.add(mate);
        			}else {
        				ColumnValue columnValue=new ColumnValue();
        				columnValue.setField(aggterms.getName());
        				columnValue.setValue(bucket.getKey());
        				
        				result.add(columnValue);
        			}
        		}
    		}else{//聚合函数
    			InternalNumericMetricsAggregation.SingleValue internalagg=(InternalNumericMetricsAggregation.SingleValue)agg;
    			columnMap.put(internalagg.getName(), internalagg.value());
               flag=true;
    		}
    		
    	}
    	if(flag) result.add(columnMap);
    	return result;
	}

	 /**
     * 纯聚合函数
     * @param list
     * @return
     */
    public ESResultSet constructAggregation(Aggregations aggregations){
    	ESResultSet result=new ESResultSet();
    	Aggregation agg=aggregations.asList().get(0);
    	InternalFilter aggfilter=(InternalFilter) agg;
    	List<Aggregation> aggList=aggfilter.getAggregations().asList();
    	for (Aggregation aggregation : aggList) {
    		InternalNumericMetricsAggregation.SingleValue internalagg=(InternalNumericMetricsAggregation.SingleValue)aggregation ;
		    result.add(internalagg.getName(), internalagg.value());
    	}
    	return result;
    }
	
    private ESResultSet parserToResultSet(List<Object> list){
    	List<Map<String,Object>> reult=parser(list);
    	ESResultSet set=new ESResultSet();
    	for (Map<String, Object> map : reult) {
			set.add(map);
		}
    	return set;
    }
    
	
	private List<Map<String,Object>> parser(List<Object> list){
		List<Map<String,Object>> result=new ArrayList<>();
		if(list.get(0) instanceof ResultMate){
			for (Object obj : list){
				ResultMate mate=(ResultMate) obj;
				List<Map<String,Object>> relastonList=parser(((ResultMate) obj).getBuckList());
				for (Map<String, Object> map : relastonList) {
					Map<String,Object> parentmap=new HashMap<>();
					parentmap.put(mate.getField(), mate.getValue());
					parentmap.putAll(map);
					result.add(parentmap);
				}
			}
		}else if(list.get(0) instanceof ColumnValue){
			
			for (Object obj : list){
				Map<String,Object> columnMap=new HashMap<>();
				ColumnValue columnValue=(ColumnValue) obj;
				columnMap.put(columnValue.getField(), columnValue.getValue());
				result.add(columnMap);
			}
			
		}else{//map
			result.add((Map<String, Object>) list.get(0));
		}
		return result;
	}
	
	
	/**
	 * 解析having
	 */

	public void resolveHaving(ESResultSet resultSet,ConditionExp having){
		
	}
}

/*
 * 修改历史
 * $Log$ 
 */