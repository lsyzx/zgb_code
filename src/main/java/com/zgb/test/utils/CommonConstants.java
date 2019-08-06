/*
 *   Copyright (c) 2018-2025, Nanjing Zhengebang Software Corp.,Ltd All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 * Neither the name of the pig4cloud.com developer nor the names of its
 * contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
 * Author: lsyzx (zhux@zhengebang.com)
 */
package com.zgb.test.utils;

import java.text.SimpleDateFormat;
/**
 * @author: lsyzx (zhux@zhengebang.com)
 * 创建时间: 2019-08-07 00:18:27
 * 描述: 公共通用常量类
 * 历史修改:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
public class CommonConstants {

    /**
     * 操作成功
     */
    public static final int SUCCESS = 0;

    /**
     * token名称
     */
    public static final String TOKEN_NAME="Authorization";

    /**
     * 系统异常,请联系管理员
     */
    public static final int FAIL = 1;

    /**
     * JWT
     */
    public static final String JWT_SECERT = "8677df7fc3a34e26a61c034d5ec8245d";

    /**
     * token有效时间【24小时】
     */
    public static final long JWT_TTL = 60 * 60 * 1000 * 24;

    /**
     * 开放接口token有效期【1小时】
     */
    public static final long OPEN_JWT_TTL = 60 * 60 * 1000;



    /**
     * Token校验成功
     */
    public static final int CHECK_JWT_SUCCESS = 1001;

    /**
     * Token已过期
     */
    public static final int CHECK_JWT_EXPIRE = 1000101;

    /**
     * Token不存在
     */
    public static final int TOKEN_ERROR_NULL = 1000102;

    /**
     * Token校验未通过
     */
    public static final int CHECK_JWT_FAIL = 1000103;

    /**
     * 系统日志类型： 登录
     */
    public static final int LOG_TYPE_1 = 1;

    /**
     * 系统日志类型： 操作
     */
    public static final  int LOG_TYPE_2 = 2;

    /**
     * 系统日志类型： 定时任务
     */
    public static final int LOG_TYPE_3 = 3;


    /**
     * 系统日志操作操作类型：新增
     */
    public static final int LOG_OPER_1 = 1;

    /**
     * 系统日志类型： 修改
     */
    public static final int LOG_OPER_2 = 2;

    /**
     * 系统日志类型： 删除
     */
    public static final  int LOG_OPER_3 = 3;

    /**
     * 系统日志类型： 查询
     */
    public static final  int LOG_OPER_4 = 4;

    /**
     * 登录
     */
    public static final  int LOG_OPER_5 = 5;

    /**
     * 格式化时间
     */
    public final static SimpleDateFormat YYYY_MM_DD_HH_MM_SS = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * redis存储分钟时间
     */
    public final static int REDIS_EXPIRE_MINITE = 1440 * 60;
}