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

import java.io.IOException;
import java.net.InetAddress;
import java.util.List;
import java.util.Map;

import org.elasticsearch.action.admin.indices.settings.get.GetSettingsResponse;
import org.elasticsearch.action.bulk.BulkProcessor;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.Requests;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.cluster.metadata.MappingMetaData;
import org.elasticsearch.common.collect.ImmutableOpenMap;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.unit.ByteSizeValue;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bosssoft.platform.es.jdbc.mate.ColumnValue;
import com.bosssoft.platform.es.jdbc.mate.OrderbyMate;
import com.bosssoft.platform.es.jdbc.mate.PageMate;
import com.bosssoft.platform.es.jdbc.model.DeleteSqlObj;
import com.bosssoft.platform.es.jdbc.model.InsertSqlObj;
import com.bosssoft.platform.es.jdbc.model.UpdateSqlObj;

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
	
	//获取mapping
	public ImmutableOpenMap<String, MappingMetaData> getMapping(String indexName){
		return client.admin().cluster().prepareState().execute().actionGet().getState().getMetaData().getIndices().get(indexName).getMappings();
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
		
		
		
		//添加order by
		if(orderby!=null){
			for (OrderbyMate orderbyMate : orderby) {
				srb.addSort(orderbyMate.getField(), SortOrder.valueOf(orderbyMate.getOrderType().toString()));
			}
		}
			
		
		
		//添加分页信息
		if(page!=null){
			srb.setFrom(page.getFrom()).setSize(page.getPageSize());
		}else{
			srb.setFrom(0).setSize(200);
		}
		
		
  		SearchResponse searchResponse =srb.execute().actionGet();
  		
  		System.out.println("请求体：");
  		System.out.println(srb.toString());
  		System.out.println("结果体：");
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
        	request.doc(getXContentBuilder(newValue));
        	
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
        	indexRequestBuilder.setSource(getXContentBuilder(columns)).get();
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    
    /**
     * 删除文档
     * @param index
     * @param type
     * @param id
     */
    public void deleteDoc(String index,String type,String id){
    	client.prepareDelete(index, type, id).get();
    }
    
    /**
     * 批量索引文档
     * @param index
     * @param type
     * @param insertList
     */
    public void indexBatch(String index,List<InsertSqlObj> insertList){
    	try{
    		final BulkProcessor bulkProcessor = BulkProcessor.builder(client, new BulkProcessor.Listener(){

    			@Override
    			public void afterBulk(long executionId, BulkRequest request, BulkResponse response) { 
    				//System.out.println("Bulk execution completed [" + executionId + "].\n" + "Took (ms): " + response.getTookInMillis() + "\n" + "Failures: " + response.hasFailures()+ "\n" + "Count: " + response.getItems().length);
    			}
    			
    			@Override
    			public void afterBulk(long executionId, BulkRequest request, Throwable failure) {
    				logger.error("Bulk execution failed [" + executionId + "].\n" + failure.toString());
    			}
    			@Override
    			public void beforeBulk(long executionId, BulkRequest request) {
    				
    			}
    			
    		})
    		.setBulkActions(1000)
    		//.setBulkSize(new ByteSizeValue(1, ByteSizeUnit.GB))
    		.setBulkSize(new ByteSizeValue(-1))
    		.setConcurrentRequests(10)
    		.setFlushInterval(TimeValue.timeValueSeconds(5)) 
    		.build();
    		
    		for (InsertSqlObj obj : insertList) {
    			bulkProcessor.add(Requests.indexRequest(index).type(obj.getType()).source(getXContentBuilder(obj.getValueList())));
    		}
    		Settings settings1 = Settings.builder().put("refresh_interval", "1s").build();
            client.admin().indices().prepareUpdateSettings(index).setSettings(settings1).execute().actionGet();
            
            bulkProcessor.close();
    	}catch (Exception e){
    		e.printStackTrace();
    	}
    	
    }
	
    /**
     * 批量更新
     * @param index
     * @param type
     * @param updateList
     */
    public void updateBatch(String index,List<UpdateSqlObj> updateList){
    	try {
    		final BulkProcessor bulkProcessor = BulkProcessor.builder(client, new BulkProcessor.Listener(){

    			@Override
    			public void afterBulk(long executionId, BulkRequest request, BulkResponse response) { 
    				//System.out.println("Bulk execution completed [" + executionId + "].\n" + "Took (ms): " + response.getTookInMillis() + "\n" + "Failures: " + response.hasFailures()+ "\n" + "Count: " + response.getItems().length);
    			}
    			
    			@Override
    			public void afterBulk(long executionId, BulkRequest request, Throwable failure) {
    				logger.error("Bulk execution failed [" + executionId + "].\n" + failure.toString());
    			}
    			@Override
    			public void beforeBulk(long executionId, BulkRequest request) {
    				
    			}
    			
    		})
    		.setBulkActions(1000)
    		//.setBulkSize(new ByteSizeValue(1, ByteSizeUnit.GB))
    		.setBulkSize(new ByteSizeValue(-1))
    		.setConcurrentRequests(10)
    		.setFlushInterval(TimeValue.timeValueSeconds(5)) 
    		.build();
    		
    		for (UpdateSqlObj obj : updateList) {
    			for (String id : obj.getIds()) {
    				bulkProcessor.add(new UpdateRequest(index, obj.getType(), id).doc(getXContentBuilder(obj.getUpdateList())));	
    			}
    		}
    		Settings settings1 = Settings.builder().put("refresh_interval", "1s").build();
            client.admin().indices().prepareUpdateSettings(index).setSettings(settings1).execute().actionGet();
            bulkProcessor.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    
    /**
     * 批量删除
     * @param index
     * @param type
     * @param ids
     */
    public void deletBatch(String index,List<DeleteSqlObj> list){
    	try {
    		final BulkProcessor bulkProcessor = BulkProcessor.builder(client, new BulkProcessor.Listener(){

    			@Override
    			public void afterBulk(long executionId, BulkRequest request, BulkResponse response) { 
    				//System.out.println("Bulk execution completed [" + executionId + "].\n" + "Took (ms): " + response.getTookInMillis() + "\n" + "Failures: " + response.hasFailures()+ "\n" + "Count: " + response.getItems().length);
    			}
    			
    			@Override
    			public void afterBulk(long executionId, BulkRequest request, Throwable failure) {
    				logger.error("Bulk execution failed [" + executionId + "].\n" + failure.toString());
    			}
    			@Override
    			public void beforeBulk(long executionId, BulkRequest request) {
    				
    			}
    			
    		})
    		.setBulkActions(1000)
    		//.setBulkSize(new ByteSizeValue(1, ByteSizeUnit.GB))
    		.setBulkSize(new ByteSizeValue(-1))
    		.setConcurrentRequests(10)
    		.setFlushInterval(TimeValue.timeValueSeconds(5)) 
    		.build();
    		
    		for (DeleteSqlObj obj : list) {
    			for (String id : obj.getIds()) {
    				bulkProcessor.add(new DeleteRequest(index,obj.getType() , id));
				}
    			
    		}
    		Settings settings1 = Settings.builder().put("refresh_interval", "1s").build();
            client.admin().indices().prepareUpdateSettings(index).setSettings(settings1).execute().actionGet();
            bulkProcessor.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    
    /**
     * 为type设置Mapping
     * @param index
     * @param type
     * @param mapping
     */
    public void addMapping(String index,String type,String mapping){
    	client.admin().indices().preparePutMapping(index).setType(type).setSource(mapping).get();
    }
    
    public XContentBuilder getXContentBuilder(List<ColumnValue> list) throws IOException{
    	XContentBuilder builder = jsonBuilder().startObject();
    	for (ColumnValue columnValue : list) {
			builder.field(columnValue.getField(), columnValue.getValue());
		}
    	builder.endObject();
    	return builder;
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