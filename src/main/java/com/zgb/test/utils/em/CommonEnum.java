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
package com.zgb.test.utils.em;

import com.zgb.test.utils.CommonConstants;

/**
 * @author: lsyzx (zhux@zhengebang.com)
 * 创建时间: 2019-08-07 00:18:27
 * 描述: 公共枚举类
 * 历史修改:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
public enum CommonEnum {

    /**
     * 用户名或密码错误
     */
    AUTH_LOGIN_FAIL(900001, "用户名或密码错误"),
    /**
     * 账号没有被授权
     */
    SYS_CODE_LOGIN_AUTH_FAIL(900002, "账号没有被授权"),

    /**
     * 验证码错误
     */
    SYS_CODE_LOGIN_CODE_FAIL(900003, "验证码输入错误"),

    /**
     * 系统异常
     */
    FAIL(CommonConstants.FAIL, "系统异常,请联系管理员"),
    /**
     * 操作成功
     */
    SUCCESS(CommonConstants.SUCCESS, "操作成功"),
    /**
     * Token校验成功
     */
    CHECK_JWT_SUCCESS(CommonConstants.CHECK_JWT_SUCCESS, "Token校验成功"),

    /**
     * Token已过期
     */
    CHECK_JWT_EXPIRE(CommonConstants.CHECK_JWT_EXPIRE, "Token已过期"),

    /**
     * Token校验未通过
     */
    CHECK_JWT_FAIL(CommonConstants.CHECK_JWT_FAIL, "Token校验未通过"),

    /**
     * Token不存在
     */
    TOKEN_ERROR_NULL(CommonConstants.TOKEN_ERROR_NULL, "Token不存在"),
    /**
     * 未启用
     */
    FLAT_FALSE(0, "未启用"),
    /**
     * 已启用
     */
    FLAT_TRUE(1, "已启用"),
    /**
     * 未删除
     */
    ISDEL_NO(0, "未删除"),
    /**
     * 已删除
     */
    ISDEL_YES(1, "已删除"),
    /*****************/

    /**************业务错误***************/
    ROLE_DELETE_ERROR(20001001, "该角色下存在用户，无法删除");
    /**************业务错误***************/
    /**
     * 编码
     */
    private int code;

    /**
     * 描述
     */
    private String msg;

    CommonEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}