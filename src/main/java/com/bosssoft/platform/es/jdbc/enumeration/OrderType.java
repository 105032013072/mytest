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
 * Created on 2016年12月21日
 *******************************************************************************/


package com.bosssoft.platform.es.jdbc.enumeration;

/**
 * TODO 此处填写 class 信息
 *
 * @author huangxuewen (mailto:huangxuewen@bosssoft.com.cn)
 */

public enum OrderType {
	ASCENDING {
        @Override
        public String toString() {
            return "asc";
        }
    },
    /**
     * Descending order.
     */
	DESCENDING{
        @Override
        public String toString() {
            return "desc";
        }
    };
}

/*
 * 修改历史
 * $Log$ 
 */