/*
 *    Copyright (c) 2018-2025, Nanjing Zhengebang Software Corp.,Ltd All rights reserved.
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
package com.zgb.test.utils.result;

import com.zgb.test.utils.em.CommonEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * @author: lsyzx (zhux@zhengebang.com)
 * 创建时间: 2019-08-07 00:18:27
 * 描述: 通用返回对象
 * 历史修改:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@ApiModel(value = "ApiResult", description = "ApiResult")
public class ApiResult implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 状态码.
     */
    @ApiModelProperty(name = "code", value = "状态码")
    private int code;

    /**
     * 信息.
     */
    @ApiModelProperty(name = "msg", value = "返回信息")
    private String msg;

    /**
     * 附加值.
     */
    @ApiModelProperty(name = "msg", value = "返回对象")
    private Object data;

    /**
     * Instantiates a new json result.
     */
    public ApiResult() {
        this.code = CommonEnum.SUCCESS.getCode();
        this.msg = CommonEnum.FAIL.getMsg();
    }

    /**
     * Instantiates a new json result.
     *
     * @param resultEnum the result enum
     */
    public ApiResult(CommonEnum resultEnum) {
        this.code = resultEnum.getCode();
        this.msg = resultEnum.getMsg();
    }

    /**
     * Instantiates a new json result.
     *
     * @param resultEnum the result enum
     * @param data       the data
     */

    public ApiResult(CommonEnum resultEnum, Object data) {
        this.code = resultEnum.getCode();
        this.msg = resultEnum.getMsg();
        this.data = data;
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

    /**
     * Gets the data.
     *
     * @return the data
     */
    public Object getData() {
        return data;
    }

    /**
     * Sets the data.
     *
     * @param data the new data
     */
    public void setData(Object data) {
        this.data = data;
    }
}
