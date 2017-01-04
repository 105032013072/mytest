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
 * Created on 2016年12月25日
 *******************************************************************************/


package com.bosssoft.platform.es.jdbc.constructer;

import java.net.URISyntaxException;
import java.sql.SQLException;
import java.sql.Statement;

import com.bosssoft.platform.es.jdbc.model.InsertSqlObj;
import com.bosssoft.platform.es.jdbc.model.UpdateSqlObj;

/**
 * TODO 此处填写 class 信息
 *
 * @author huangxuewen (mailto:huangxuewen@bosssoft.com.cn)
 */

public interface UpdateConstructer {
	
	public  String buildCreate(String table,String index) throws URISyntaxException;
	
	public UpdateSqlObj buildUpdateObj(String sql,String index,Statement esStatement) throws SQLException;
	
	public InsertSqlObj buildInsertObj(com.facebook.presto.sql.tree.Statement statement)throws SQLException;
    
}

/*
 * 修改历史
 * $Log$ 
 */