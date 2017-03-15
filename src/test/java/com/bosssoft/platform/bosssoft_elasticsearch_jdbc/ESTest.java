package com.bosssoft.platform.bosssoft_elasticsearch_jdbc;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.Before;
import org.junit.Test;






public class ESTest {
	
 
	private TransportClient client=null;
	
	private String indexName="demo";
	
	
	@Before
	public void init(){
		try{
			Settings settings = Settings.settingsBuilder().build();
		 client = TransportClient.builder().settings(settings).build()
				//.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("localhost"),9300));
		 .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("localhost"),9300));
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	@Test
	public void createIndex(){
		try{
			deleteIndex();
			client.admin().indices().prepareCreate("demo").execute().actionGet();
			
			//Map<String,String> map=new HashMap<>();
			//map.put("USER_NAME", "string");
			//buildMapping(map, "demo");
			
			
		}catch(Exception e){
			e.printStackTrace();
		}
	
	}
	
	@Test
	public void deleteIndex(){
		if (client.admin().indices().prepareExists("demo").execute().actionGet().isExists()){
			client.admin().indices().prepareDelete("demo").execute().actionGet();
		}
	}
	
	@Test
	public void testUpdate(){
		try{
			Map<String,Object> map=new HashMap<>();
			map.put("staff_name", "bb");
			update(indexName, "jdbc", "AVFRG",map);
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	@Test
	public void search(){
		QueryBuilder queryBuilder=QueryBuilders.termQuery("fid", 6);
		List<Map<String, Object>> list=search(null, 0, 10, indexName, "jdbc");
		System.out.println(list.size());
		for (Map<String, Object> map : list) {
			System.out.println(map);
		}
	}
	
	
	 private List<Map<String, Object>> search(QueryBuilder queryBuilder, int from,int pageSize,String indexName, String... indexType) {
			
			List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
			SearchRequestBuilder srb=client.prepareSearch(indexName)
					.setTypes(indexType)
					.setQuery(queryBuilder)
					.setFrom(from)
					.setSize(pageSize);
		
			SearchResponse searchResponse =srb.execute().actionGet();
			
			SearchHits hits = searchResponse.getHits();
			
			System.out.println("查询到记录数:" + hits.getTotalHits()+",耗时:"+searchResponse.getTookInMillis()+"ms");

			SearchHit[] searchHists = hits.getHits();//真正的搜索结果数组（默认是前10个文档）
			if (searchHists.length > 0) {
				for (SearchHit hit : searchHists) {
					Map<String,Object> map=hit.getSource();
					map.put("docId", hit.getId());
					list.add(map);
				}
			}

			return list;
		}
	 
	 private  void update(String indexName,String type,String id,Map<String,Object> map){
		 client.prepareUpdate(indexName, type, id);
	
	 }
	 
	 private  void buildMapping(Map<String,String> fieldMap,String typeName){
		 try{
				XContentBuilder builder=XContentFactory.jsonBuilder()
						.startObject()
						  .startObject("_all")
						    /*.field("analyzer", "ik_max_word")
					        .field("search_analyzer", "ik_max_word")*/
					        .field("term_vector", "no")
					        .field("store", "no")
					      .endObject();
				
				if(fieldMap!=null&&fieldMap.size()!=0){
					builder.startObject("properties");
					for(Map.Entry<String, String> entry:fieldMap.entrySet()){
						builder.startObject(entry.getKey())
						.field("type", entry.getValue())
						.field("store", "yes")
						.field("analyzer", "ik_max_word")
						.field("search_analyzer", "ik_max_word")
						.field("include_in_all", "true")
						.field("boost", 8)
						.endObject();
					}
					builder.endObject();
				}
				
				builder.endObject();
				String mapping=builder.string();
				System.out.println(mapping);
				client.admin().indices().preparePutMapping(indexName).setType(typeName)
	            .setSource(mapping).execute().actionGet();
			}catch(Exception e){
				e.printStackTrace();
				//logger.info("putMappings error");
				System.out.println("putMappings error");
			}
			
	 }
	 
	 public XContentBuilder getXContentBuilder(Map<String,Object> map){
	    	
    	 try {
    		 XContentBuilder bulider=XContentFactory.jsonBuilder().startObject();
    	    	for(Map.Entry<String, Object> entry:map.entrySet()){
    	    		if(!"docId".equals(entry.getKey())){
    	    			bulider.field(entry.getKey(), entry.getValue());
    	    		}
    	    	}
    	    	bulider.endObject();
    	    	return bulider;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	 }
}
