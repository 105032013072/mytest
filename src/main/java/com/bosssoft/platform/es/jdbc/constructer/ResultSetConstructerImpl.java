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

import java.util.Map;

import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;

import com.bosssoft.platform.es.jdbc.model.ESResultSet;

/**
 * TODO 此处填写 class 信息
 *
 * @author huangxuewen (mailto:huangxuewen@bosssoft.com.cn)
 */

public class ResultSetConstructerImpl implements ResultSetConstructer{

    public ESResultSet ConstructAllColumn(SearchHits hits){//select *
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
    
   
}

/*
 * 修改历史
 * $Log$ 
 */