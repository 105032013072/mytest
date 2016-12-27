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

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.bosssoft.platform.es.jdbc.enumeration.AggType;
import com.bosssoft.platform.es.jdbc.enumeration.OrderType;
import com.bosssoft.platform.es.jdbc.mate.BetweenExpression;
import com.bosssoft.platform.es.jdbc.mate.ColumnMate;
import com.bosssoft.platform.es.jdbc.mate.ConNotExpression;
import com.bosssoft.platform.es.jdbc.mate.InExpression;
import com.bosssoft.platform.es.jdbc.mate.Inequality;
import com.bosssoft.platform.es.jdbc.mate.NullExpression;
import com.bosssoft.platform.es.jdbc.mate.OrderbyMate;
import com.bosssoft.platform.es.jdbc.mate.PageMate;
import com.bosssoft.platform.es.jdbc.model.ConditionExp;
import com.facebook.presto.sql.tree.AllColumns;
import com.facebook.presto.sql.tree.BetweenPredicate;
import com.facebook.presto.sql.tree.ComparisonExpression;
import com.facebook.presto.sql.tree.Expression;
import com.facebook.presto.sql.tree.FunctionCall;
import com.facebook.presto.sql.tree.GroupingElement;
import com.facebook.presto.sql.tree.InListExpression;
import com.facebook.presto.sql.tree.InPredicate;
import com.facebook.presto.sql.tree.IsNotNullPredicate;
import com.facebook.presto.sql.tree.IsNullPredicate;
import com.facebook.presto.sql.tree.LogicalBinaryExpression;
import com.facebook.presto.sql.tree.NotExpression;
import com.facebook.presto.sql.tree.QualifiedNameReference;
import com.facebook.presto.sql.tree.QueryBody;
import com.facebook.presto.sql.tree.QuerySpecification;
import com.facebook.presto.sql.tree.Relation;
import com.facebook.presto.sql.tree.Select;
import com.facebook.presto.sql.tree.SelectItem;
import com.facebook.presto.sql.tree.SimpleGroupBy;
import com.facebook.presto.sql.tree.SingleColumn;
import com.facebook.presto.sql.tree.SortItem;
import com.facebook.presto.sql.tree.Table;

/**
 * TODO 此处填写 class 信息
 *
 * @author huangxuewen (mailto:huangxuewen@bosssoft.com.cn)
 */

public interface SearchConverter {
	public List<ColumnMate> conventSlect(QueryBody qb) throws SQLException;



	/**
	 * 解析order by
	 * @param qb
	 * @return
	 */
	public List<OrderbyMate> conventOrderBy(QueryBody qb) ;



	/**
	 * 
	 * 解析limit 分页
	 * @param sql
	 * @return
	 */
	public  PageMate conventLimit(String sql) ;

	/**
	 * 解析where 子句
	 * @param qb
	 * @return
	 */
	public  ConditionExp conventwhere(Expression expression) throws SQLException;

	/**
	 * Groupby
	 * @param qb
	 * @return
	 */
	public  List<ColumnMate> conventGroupby(QueryBody qb);

	/**
	 * @param qb
	 * @return
	 */
	public  String conventFrom(QueryBody qb)throws SQLException ;

	/**
	 * 解析获取Distinct
	 * @param qb
	 * @return
	 */
	public  Boolean conventDistinct(QueryBody qb) ;
}

/*
 * 修改历史
 * $Log$ 
 */