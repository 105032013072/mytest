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


package com.bosssoft.platform.es.jdbc.driver;

import java.net.InetAddress;
import java.util.List;
import java.util.Map;

import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bosssoft.platform.es.jdbc.mate.ColumnValue;
import com.bosssoft.platform.es.jdbc.mate.OrderbyMate;
import com.bosssoft.platform.es.jdbc.mate.PageMate;
import static org.elasticsearch.common.xcontent.XContentFactory.*;

/**
 * 封装对es的操作
 *
 * @author huangxuewen (mailto:huangxuewen@bosssoft.com.cn)
 */

public class ESClient {
	private static final Logger logger = LoggerFactory.getLogger(ESDriver.class.getName());
	
    private String host;
	
	private Integer port;
	
	private TransportClient client;
	
	private Settings settings;
	
	public ESClient(String host,Integer port){
		this.host=host;
		this.port=port;
		
		try{
			settings = Settings.builder()
	                .put("number_of_shards", 1)
	                .put("number_of_replicas", 0)
	                .build();
		 client = TransportClient.builder().settings(settings).build()
				.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(host),9300));
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	//查询
	public SearchResponse search(QueryBuilder queryBuilder,  AggregationBuilder aggregation,List<OrderbyMate> orderby,PageMate page,String indexName, String... indexType){
		SearchRequestBuilder srb=client.prepareSearch(indexName)
  				.setTypes(indexType);
		
		if(aggregation!=null){
			srb=srb.addAggregation(aggregation);
		}
		if(queryBuilder!=null){
			srb.setQuery(queryBuilder);
		}
		
		//System.out.println(srb.toString());
		
		//添加order by
		for (OrderbyMate orderbyMate : orderby) {
			srb.addSort(orderbyMate.getField(), SortOrder.valueOf(orderbyMate.getOrderType().toString()));
		}
		
		//添加分页信息
		srb.setFrom(page.getFrom()).setSize(page.getPageSize());

  		SearchResponse searchResponse =srb.execute().actionGet();
  		System.out.println(searchResponse.toString());
  		
  		return searchResponse;
  		
	}
	
	/**
	 * 创建索引
	 */
	public void createType(String indexName,String mapping,String typeName){
		client.admin().indices().preparePutMapping(indexName).setType(typeName) .setSource(mapping).execute().actionGet();
	}
	
	/**
	 * 更新文档
	 * @param index
	 * @param type
	 * @param newValue
	 * @param docid
	 */
    public void updateDoc(String index,String type,List<ColumnValue> newValue,String docid){
    	try{
    		UpdateRequest request=new UpdateRequest();
        	request.index(index).type(type).id(docid);
        	XContentBuilder builder = jsonBuilder().startObject();
        	for (ColumnValue columnValue : newValue) {
				builder.field(columnValue.getField(), columnValue.getValue());
			}
        	builder.endObject();
        	
        	request.doc(builder);
        	
        	client.update(request).get();
    	}catch(Exception e){
    		e.printStackTrace();
    	}
 
    }
    
    /**
     * 索引文档（insert）
     * @param index
     * @param type
     * @param columns
     */
    public void IndexDoc(String index,String type,List<ColumnValue> columns){
    	try {
    		IndexRequestBuilder indexRequestBuilder = client.prepareIndex(index, type);
    		XContentBuilder builder = jsonBuilder().startObject();
        	for (ColumnValue columnValue : columns) {
				builder.field(columnValue.getField(), columnValue.getValue());
			}
        	builder.endObject();
        	indexRequestBuilder.setSource(builder).get();
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
	
	
	public void close(){
		client.close();
	}
	

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public Integer getPort() {
		return port;
	}

	public void setPort(Integer port) {
		this.port = port;
	}

	public TransportClient getClient() {
		return client;
	}

	public void setClient(TransportClient client) {
		this.client = client;
	}
	
	
}

/*
 * 修改历史
 * $Log$ 
 */