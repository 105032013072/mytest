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

import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bosssoft.platform.es.jdbc.mate.OrderbyMate;
import com.bosssoft.platform.es.jdbc.mate.PageMate;

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
	
	public ESClient(String host,Integer port){
		this.host=host;
		this.port=port;
		
		try{
			Settings.Builder settingsBuilder = Settings.settingsBuilder();
			Settings settings = settingsBuilder.build();
		 client = TransportClient.builder().settings(settings).build()
				.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(host),9300));
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	//查询
	public void search(QueryBuilder queryBuilder,  AggregationBuilder aggregation,List<OrderbyMate> orderby,PageMate page,String indexName, String... indexType){
		SearchRequestBuilder srb=client.prepareSearch(indexName)
  				.setTypes(indexType);
		
		if(aggregation!=null){
			srb=srb.addAggregation(aggregation);
		}
		if(queryBuilder!=null){
			srb.setQuery(queryBuilder);
		}
		
		System.out.println(srb.toString());
		
		//添加order by
		for (OrderbyMate orderbyMate : orderby) {
			srb.addSort(orderbyMate.getField(), SortOrder.valueOf(orderbyMate.getOrderType().toString()));
		}
		
		//添加分页信息
		srb.setFrom(page.getFrom()).setSize(page.getPageSize());

  		SearchResponse searchResponse =srb.execute().actionGet();
  		
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