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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.facebook.presto.sql.tree.BooleanLiteral;
import com.facebook.presto.sql.tree.DoubleLiteral;
import com.facebook.presto.sql.tree.Expression;
import com.facebook.presto.sql.tree.LongLiteral;
import com.facebook.presto.sql.tree.StringLiteral;

/**
 * 判断输入的字符串是否合法
 *
 * @author huangxuewen (mailto:huangxuewen@bosssoft.com.cn)
 */

public class Judger {
   
	public static boolean judgeUrl(String url){
		String regEx="^jdbc:es://[a-zA-Z_]{1,}[0-9]{0,}:[0-9]{1,}/[a-zA-Z_]{1,}";
		Pattern pattern=Pattern.compile(regEx);
		Matcher matcher = pattern.matcher(url);
	    // 字符串是否与正则表达式相匹配
	    boolean rs = matcher.matches();
	    //return rs;
	    return true;
	}
	
	public static Object judgeNumType(Expression e){
		Object result=null;
		if(e instanceof StringLiteral){
			
			 StringLiteral literal=(StringLiteral) e;
			 result=literal.getValue();

		 }else if(e instanceof DoubleLiteral){
			
			DoubleLiteral literal=(DoubleLiteral) e;
			result=literal.getValue();
			
		 }else if(e instanceof LongLiteral){
			
			 LongLiteral literal=(LongLiteral) e;
			result=literal.getValue();
		 }else{
			 BooleanLiteral literal=(BooleanLiteral) e;
			 result=literal.getValue();
		 }
		return result;
	}
}
