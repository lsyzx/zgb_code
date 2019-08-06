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
package com.zgb.test.sys.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zgb.test.utils.aspect.annotation.DictVal;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;

/**
 * @author: lsyzx (zhux@zhengebang.com)
 * 创建时间: 2019-08-07 00:18:27
 * 描述: 字典表
 * 历史修改:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@TableName("zgb_sys_log")
@ApiModel(value = "系统日志信息", description = "系统日志信息")
public class SysLog implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @ApiModelProperty(name = "id", value = "id")
    @TableId(type = IdType.UUID)
    private String id;

    /**
     * 耗时
     */
    @ApiModelProperty(name = "costTime", value = "耗时")
    private Long costTime;

    /**
     * 创建人
     */
    @ApiModelProperty(name = "createUser", value = "创建人")
    private String createUser;

    /**
     * IP
     */
    @ApiModelProperty(name = "ip", value = "ip")
    private String ip;

    /**
    * 状态 200：成功 500：异常
    */
    @ApiModelProperty(name = "状态", value = "status")
    private int status;

    /**
     * 异常信息
     */
    @ApiModelProperty(name = "异常信息", value = "errorMsg")
    private String errorMsg;

    /**
     * 请求参数
     */
    @ApiModelProperty(name = "requestParam", value = "请求参数")
    private String requestParam;

    /**
     * 请求类型
     */
    @ApiModelProperty(name = "requestType", value = "请求类型")
    private String requestType;

    /**
     * 请求路径
     */
    @ApiModelProperty(name = "requestUrl", value = "请求路径")
    private String requestUrl;
    /**
     * 请求方法
     */
    @ApiModelProperty(name = "method", value = "请求方法")
    private String method;

    /**
     * 操作详细日志
     */
    @ApiModelProperty(name = "logContent", value = "操作详细日志")
    private String logContent;

    /**
     * 日志类型（1登录日志，2操作日志，3定时任务）
     */
    @DictVal(dicCode = "log_type")
    private Integer logType;

    /**
     * 操作类型（1新增，2修改，3删除，4查询）
     */
    @DictVal(dicCode = "operate_type")
    private Integer operateType;
    
     /**
     * 创建时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(name = "createTime", value = "创建时间")
    private Date createTime;

}