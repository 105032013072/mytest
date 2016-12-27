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

import org.elasticsearch.search.SearchHits;

import com.bosssoft.platform.es.jdbc.model.ESResultSet;

/**
 * TODO 此处填写 class 信息
 *
 * @author huangxuewen (mailto:huangxuewen@bosssoft.com.cn)
 */

public interface ResultSetConstructer {
	
	public ESResultSet ConstructAllColumn(SearchHits hits);

}

/*
 * 修改历史
 * $Log$ 
 */