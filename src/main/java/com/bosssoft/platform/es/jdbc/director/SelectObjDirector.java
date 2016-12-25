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


package com.bosssoft.platform.es.jdbc.director;

import java.sql.SQLException;
import java.util.Optional;

import com.bosssoft.platform.es.jdbc.builder.SearchConverter;
import com.bosssoft.platform.es.jdbc.model.SelectSqlObj;
import com.facebook.presto.sql.tree.Expression;
import com.facebook.presto.sql.tree.Query;
import com.facebook.presto.sql.tree.QueryBody;
import com.facebook.presto.sql.tree.QuerySpecification;
import com.facebook.presto.sql.tree.Statement;

/**
 * TODO 构建SelectSqlObj的指导类
 *
 * @author huangxuewen (mailto:huangxuewen@bosssoft.com.cn)
 */

public class SelectObjDirector {
	
	private SearchConverter converter;
	
	public SelectObjDirector(SearchConverter converter){
		this.converter=converter;
		
	}
	
	public SelectSqlObj convent(Statement statement,String sql)throws SQLException{
		SelectSqlObj obj=new SelectSqlObj();
		QueryBody qb=((Query)statement).getQueryBody();
		
		obj.setDistinct(converter.conventDistinct(qb));
		
		obj.setFrom(converter.conventFrom(qb));
		
		obj.setGroupby(converter.conventGroupby(qb));
		
		obj.setLimit(converter.conventLimit(sql));
		
		obj.setOrderby(converter.conventOrderBy(qb));
		
		obj.setSelectItems(converter.conventSlect(qb));
		
		
		Optional<Expression> opwhere=((QuerySpecification)qb).getWhere();
		if(opwhere.isPresent()){
			Expression where=opwhere.get();
			obj.setWhere(converter.conventwhere(where));
		}
		
		Optional<Expression> ophaving=((QuerySpecification)qb).getHaving();
	    if(ophaving.isPresent()){
	    	Expression having=ophaving.get();
			obj.setHaving(converter.conventwhere(having));
	    }
	    
	    return obj;
	}

}

/*
 * 修改历史
 * $Log$ 
 */