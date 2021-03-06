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

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Builder;
import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;

/**
 * @author: lsyzx (zhux@zhengebang.com)
 * 创建时间: 2019-08-07 00:18:27
 * 描述: 用户角色关系请求参数
 * 历史修改:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@Data
@Builder(toBuilder = true)
@TableName("zgb_sys_role_user")
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "用户角色关系请求参数", description = "用户角色关系请求参数")
public class SysRoleUser extends Model<SysRoleUser> implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 系统用户角色主键ID
     */
    @TableId(type = IdType.UUID)
    @ApiModelProperty(name = "roleUserId", value = "系统用户角色主键ID")
    private String roleUserId;
    /**
     * 系统角色主键ID
     */
    @ApiModelProperty(name = "roleId", value = "系统角色主键ID")
    private String roleId;
    /**
     * 系统用户主键ID
     */
    @ApiModelProperty(name = "userId", value = "系统用户主键ID")
    private String userId;

    /**
     * 是否删除 0：未删除 1：已删除
     */
    @ApiModelProperty(name = "isDel", value = "角色描述")
    @TableLogic
    private int isDel;

    /**
     * 创建时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(name = "createTime", value = "创建时间")
    private Date createTime;

}