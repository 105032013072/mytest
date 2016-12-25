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

import com.bosssoft.platform.es.jdbc.builder.UpdateBuilder;

/**
 * TODO es的更新体的构建指导类
 *
 * @author huangxuewen (mailto:huangxuewen@bosssoft.com.cn)
 */

public class UpdateDirector {

	private UpdateBuilder builder;
	
	public UpdateDirector(UpdateBuilder builder){
		this.builder=builder;
	}
}

/*
 * 修改历史
 * $Log$ 
 */