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


package com.bosssoft.platform.es.jdbc.builder;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
	    return rs;
	}
}
