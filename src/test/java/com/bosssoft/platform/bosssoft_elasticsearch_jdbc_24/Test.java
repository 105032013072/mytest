package com.bosssoft.platform.bosssoft_elasticsearch_jdbc_24;

import static org.junit.Assert.*;

import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.junit.Before;

public class Test {

   private TransportClient client=null;
	
	private String indexName="demo";

	@Before
	public void init(){
		try{
			try{
				Settings settings = Settings.settingsBuilder()
		    	        .put("cluster.name", "elasticsearch").build();
		    	client = TransportClient.builder().settings(settings).build().addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("192.168.10.51"), 9300));;
			}catch(Exception e){
				e.printStackTrace();
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	@org.junit.Test
	public void createIndex(){
		try{
			client.admin().indices().prepareCreate("demo").execute().actionGet();
			
			/*Map<String,String> map=new HashMap<>();
			map.put("USER_NAME", "string");
			buildMapping(map, "demo");*/
			
			
		}catch(Exception e){
			e.printStackTrace();
		}
	
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
	
}
