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
 * 描述: 角色菜单关系
 * 历史修改:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@Data
@Builder(toBuilder = true)
@TableName("zgb_sys_role_menu")
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "角色菜单关系请求参数", description = "角色菜单关系请求参数")
public class SysRoleMenu extends Model<SysRoleMenu>  implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * 菜单角色关联关系表主键ID
     */
    @TableId(type = IdType.UUID)
    @ApiModelProperty(name = "roleMenuId", value = "菜单角色关联关系表主键ID")
    private String roleMenuId;
    /**
     * 系统角色主键ID
     */
    @ApiModelProperty(name = "roleId", value = "系统角色主键ID")
    private String roleId;
    /**
     * 菜单主键ID
     */
    @ApiModelProperty(name = "menuId", value = "菜单主键ID")
    private String menuId;

    /**
     * 是否删除 0：未删除 1：已删除
     */
    @ApiModelProperty(name = "isDel", value = "是否删除 0：未删除 1：已删除")
    @TableLogic
    private int isDel;

    /**
     * 创建时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(name = "createTime", value = "创建时间")
    private Date createTime;

}