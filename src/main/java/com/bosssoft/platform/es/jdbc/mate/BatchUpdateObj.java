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
 * Created on 2017年1月5日
 *******************************************************************************/


package com.bosssoft.platform.es.jdbc.mate;

import java.util.ArrayList;
import java.util.List;

import com.bosssoft.platform.es.jdbc.model.DeleteSqlObj;
import com.bosssoft.platform.es.jdbc.model.InsertSqlObj;
import com.bosssoft.platform.es.jdbc.model.UpdateSqlObj;

/**
 * 批量更新封装类
 *
 * @author huangxuewen (mailto:huangxuewen@bosssoft.com.cn)
 */

public class BatchUpdateObj {

	
	private List<UpdateSqlObj> upobjList;
	
	private List<DeleteSqlObj> deobjList;
	
	private List<InsertSqlObj> inobjList;

	public BatchUpdateObj(){
		upobjList=new ArrayList<>();
		deobjList=new ArrayList<>();
		inobjList=new ArrayList<>();
	}
	
	
	public void addUpObj(UpdateSqlObj updateSqlObj){
		upobjList.add(updateSqlObj);
	}
	
	public void addDeObj(DeleteSqlObj deleteSqlObj){
		deobjList.add(deleteSqlObj);
	}
	
	public void addInObj(InsertSqlObj insertSqlObj){
		inobjList.add(insertSqlObj);
	}
	
	public List<UpdateSqlObj> getUpobjList() {
		return upobjList;
	}
	

	public void setUpobjList(List<UpdateSqlObj> upobjList) {
		this.upobjList = upobjList;
	}

	public List<DeleteSqlObj> getDeobjList() {
		return deobjList;
	}

	public void setDeobjList(List<DeleteSqlObj> deobjList) {
		this.deobjList = deobjList;
	}

	public List<InsertSqlObj> getInobjList() {
		return inobjList;
	}

	public void setInobjList(List<InsertSqlObj> inobjList) {
		this.inobjList = inobjList;
	}
	
	
	
}

/*
 * 修改历史
 * $Log$ 
 */