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
import com.bosssoft.platform.es.jdbc.model.SelectSqlObj;
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
import com.facebook.presto.sql.tree.Query;
import com.facebook.presto.sql.tree.QueryBody;
import com.facebook.presto.sql.tree.QuerySpecification;
import com.facebook.presto.sql.tree.Relation;
import com.facebook.presto.sql.tree.Select;
import com.facebook.presto.sql.tree.SelectItem;
import com.facebook.presto.sql.tree.SimpleGroupBy;
import com.facebook.presto.sql.tree.SingleColumn;
import com.facebook.presto.sql.tree.SortItem;
import com.facebook.presto.sql.tree.Statement;
import com.facebook.presto.sql.tree.Table;


/**
 * TODO 此处填写 class 信息
 *
 * @author huangxuewen (mailto:huangxuewen@bosssoft.com.cn)
 */

public class SearchConverterImpl implements SearchConverter{

	/**
	 * 解析select ...
	 * @param qb
	 * @return
	 */
	public  List<ColumnMate> conventSlect(QueryBody qb) throws SQLException{
		Select select=((QuerySpecification)qb).getSelect();
		List<SelectItem> list=select.getSelectItems();
		
		if(list.size()==0) 
			throw new SQLException("illegal sql:can not find option of select items");
	
		List<ColumnMate> result=new ArrayList<>();
		for (SelectItem selectItem : list) {
			
			ColumnMate columnMate=new ColumnMate();
			
			if(selectItem instanceof AllColumns){//select *
				columnMate.setName(selectItem.toString());
				
			}else {//select coulumn
				SingleColumn sc=(SingleColumn) selectItem;
	            
	            //设置别名
	            if(sc.getAlias().isPresent())
	                 columnMate.setAlias(sc.getAlias().get().toString());
	            
	            
	            //根据是否是聚合函数来设置字段名和聚合函数类型
	            Expression exp =sc.getExpression();
	            if(exp instanceof FunctionCall){
	            	FunctionCall fc=(FunctionCall) exp;
	                columnMate.setAggType(AggType.valueOf(fc.getName().toString().toUpperCase()));
	                columnMate.setName(fc.getArguments().get(0).toString());;
	            }else {
	            	QualifiedNameReference qnf=(QualifiedNameReference) exp;
	            	columnMate.setName(qnf.getName().toString());
	            	
	            }
			}
			
            result.add(columnMate);
            
		}
		
		return result;
	}



	/**
	 * 解析order by
	 * @param qb
	 * @return
	 */
	public List<OrderbyMate> conventOrderBy(QueryBody qb) {
		List<OrderbyMate> result=new ArrayList<>();
		
		List<SortItem> list=((QuerySpecification)qb).getOrderBy();
		if(list.size()==0) return null;
		
		for (SortItem sortItem : list) {
			OrderbyMate mate=new OrderbyMate();
			mate.setField(parserField(sortItem.getSortKey()));
			mate.setOrderType(OrderType.valueOf(sortItem.getOrdering().toString()));
			result.add(mate);
		}
		return result;
	}



	/**
	 * 
	 * 解析limit 分页
	 * @param sql
	 * @return
	 */
	public PageMate conventLimit(String sql) {
		if(!sql.contains("limit")) return null;
		String limitStr=sql.substring(sql.indexOf("limit"),sql.length()).replaceAll(" ", "");
		String str=limitStr.substring(5, limitStr.length());
		String []reslut=str.split(",");
		PageMate mate=new PageMate();
		mate.setFrom(Integer.valueOf(reslut[0]));
		mate.setPageSize(Integer.valueOf(reslut[1]));
		return mate;
	}

	/**
	 * 解析where 子句
	 * @param qb
	 * @return
	 */
  public ConditionExp conventwhere(Expression expression) throws SQLException{
		ConditionExp exp=new ConditionExp();
		
		if(expression instanceof ComparisonExpression){//不等式
			ComparisonExpression ces=(ComparisonExpression) expression;	
			Inequality inequality=new Inequality();
			
			inequality.setFiled(parserField(ces.getLeft()));
			inequality.setValue(ces.getRight());
			inequality.setOperation(ces.getType().toString());
			
			exp.setExpression(inequality);
			return exp;
		}else if(expression instanceof IsNullPredicate){//为空
			IsNullPredicate nullPredicate=(IsNullPredicate) expression;
			NullExpression  nullExpression=new NullExpression();
			
			nullExpression.setFiled(parserField(nullPredicate.getValue()));
			nullExpression.setOpration("is null");
			
			exp.setExpression(nullExpression);
			return exp;
			
			
			
		}else if(expression instanceof IsNotNullPredicate){//不为空
			IsNotNullPredicate isnp=(IsNotNullPredicate) expression;
			NullExpression  nullExpression=new NullExpression();
			nullExpression.setFiled(parserField(isnp.getValue()));
			nullExpression.setOpration("is not null");
			exp.setExpression(nullExpression);
			return exp;
			
		}else if(expression instanceof InPredicate){//in
			InPredicate inPredicate=(InPredicate) expression;
			InExpression inExpression=new InExpression();
			
			inExpression.setField(parserField(inPredicate.getValue()));
			
			//设置范围
			InListExpression inexp=(InListExpression)inPredicate.getValueList();
			inExpression.setRangeList(inexp.getValues());
		
			exp.setExpression(inExpression);
			return exp;
			
		}else if (expression instanceof NotExpression){//not [condition]
			NotExpression notExpression=(NotExpression) expression;
			ConNotExpression cne=new ConNotExpression();
			cne.setConditionExp(conventwhere(notExpression.getValue()));
			exp.setExpression(cne);
			return exp;
			
			
		}else if (expression instanceof BetweenPredicate){
			BetweenPredicate betweenPredicate=(BetweenPredicate) expression;
			BetweenExpression betweenExpression=new BetweenExpression();
			//获取字段
			betweenExpression.setFiled(parserField(betweenPredicate.getValue()));
			
			//区间(设置类型)
			betweenExpression.setBewteen(betweenPredicate.getMin());
			betweenExpression.setAnd(betweenPredicate.getMax());
			
			exp.setExpression(betweenExpression);
			return exp;
			
		}else if(expression instanceof LogicalBinaryExpression){//逻辑表达式
			LogicalBinaryExpression logicalexp=(LogicalBinaryExpression) expression;
			Expression left=logicalexp.getLeft();
			Expression rigth=logicalexp.getRight();
			
			ConditionExp leftCon=conventwhere(left);
			ConditionExp rightCon=conventwhere(rigth);
			exp.setExpression(leftCon);
			exp.setNext(rightCon);
			exp.setRelation(logicalexp.getType().toString());
			
			return exp;
			
		}else {
			throw new SQLException("expression is illegal");
		}
		
	}

	/**
	 * Groupby
	 * @param qb
	 * @return
	 */
	public List<String> conventGroupby(QueryBody qb) {
		List<GroupingElement> list=((QuerySpecification)qb).getGroupBy();
		if(list.size()==0) return null;
		List<String> result=new ArrayList<>();
		
		for (GroupingElement element : list) {
			SimpleGroupBy sgb=(SimpleGroupBy) element;
			result.add(parserField(sgb.getColumnExpressions().get(0)));
		}
		
		
		return result;
	}

	/**
	 * @param qb
	 * @return
	 */
	public  String conventFrom(QueryBody qb)throws SQLException {
	  Optional<Relation> op=((QuerySpecification)qb).getFrom();
		if(op.isPresent()){
			Table table=(Table) op.get();
			return table.getName().toString();
		}else {
			throw new SQLException("illegal sql:can not find option of table");
		}
	}

	/**
	 * 解析获取Distinct
	 * @param qb
	 * @return
	 */
	public  Boolean conventDistinct(QueryBody qb) {
		Select select=((QuerySpecification)qb).getSelect();
		return select.isDistinct();
	}

	
	private String parserField(Expression e){
		return ((QualifiedNameReference)e).getName().toString();
	}
}

/*
 * 修改历史
 * $Log$ 
 */