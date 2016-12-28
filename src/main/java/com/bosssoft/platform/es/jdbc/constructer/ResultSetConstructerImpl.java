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
import org.elasticsearch.search.aggregations.bucket.terms.Terms;

import com.bosssoft.platform.es.jdbc.mate.ColumnMate;
import com.bosssoft.platform.es.jdbc.mate.ReDistinctMate;
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
    public ESResultSet ConstructAllColumn(SearchHits hits){
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
    public ESResultSet ConstructSelectColumn(SearchHits hits,List<ColumnMate> selectItems){
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
    public void ConstructCount(SearchHits hits){
    	
    }
    
    /**
     * select distinct
     * @param aggregation
     */
    public void ConstructDistinct(Aggregations aggregations){
    	System.out.println(aggregations.toString());
    	List<Object> list=new ArrayList<>();
    	  
    		list=resolveAggsds(aggregations.asList());
    		
    	System.out.println("1231");
    }
    
    private List<Object> resolveAggsds(List<Aggregation> list){
    	List<Object> result=new ArrayList<>();
    	for (Aggregation agg : list) {
    		
    		Terms aggterms=(Terms) agg;
    		for(Terms.Bucket bucket : aggterms.getBuckets()){
    			
    			List<Aggregation> aggList = bucket.getAggregations().asList();
    			if(aggList.size()!=0) {
    				ReDistinctMate mate=new ReDistinctMate();
        			mate.setField(aggterms.getName());
    				mate.setValue(bucket.getKey());
        			mate.setBuckList(resolveAggsds(aggList));
        			result.add(mate);
    			}else {
    				/*ReDistinctMate dm=new ReDistinctMate();
    				dm.setField(aggterms.getName());
    				dm.setValue(bucket.getKey());*/
    				
    				result.add(bucket.getKey());
    			}
    			
    		}
    	}
    	return result;
    }
    	
    
    
    
    //解析聚合
   
   /* private ReDistinctMate  resolveAggsdsfsdfs(Aggregation agg){
    	ReDistinctMate distinctMate=new ReDistinctMate();
    	if(agg instanceof Terms){ 
    		
    		
    		Terms aggterms=(Terms) agg;
    		
    		distinctMate.setField(aggterms.getName());
    		for(Terms.Bucket bucket : aggterms.getBuckets()){
    			List<Aggregation> list = bucket.getAggregations().asList();
    			if(list.size()!=0) {
    				distinctMate.setValue(bucket.getKey());
    				for (Aggregation aggregation : list) {
    					distinctMate.add(resolveAgg(aggregation));
					}
    			}else {
    				ReDistinctMate dm=new ReDistinctMate();
    				dm.setField(aggterms.getName());
    				dm.setValue(bucket.getKey());
    				distinctMate.add(dm);
    			}
    		}
    	}
    	return distinctMate;
    }*/
    
    
    private ESResultSet parser(List<ReDistinctMate> list){
    	ESResultSet result=new ESResultSet();
    	for (ReDistinctMate mate : list) {
			
		}
    	return result;
    }
    
}

/*
 * 修改历史
 * $Log$ 
 */