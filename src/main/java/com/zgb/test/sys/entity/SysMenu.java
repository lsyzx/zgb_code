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
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.List;

/**
 * @author: lsyzx (zhux@zhengebang.com)
 * 创建时间: 2019-08-07 00:18:27
 * 描述: 系统菜单信息
 * 历史修改:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@TableName("zgb_sys_menu")
@ApiModel(value = "系统菜单信息请求参数", description = "系统菜单信息请求参数")
public class SysMenu extends Model<SysMenu> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 菜单主键ID
     */
    @TableId(type = IdType.UUID)
    @ApiModelProperty(name = "menuId", value = "菜单主键ID")
    private String menuId;

    /**
     * 父菜单ID
     */
    @ApiModelProperty(name = "parentId", value = "父菜单ID")
    private String parentId;
    /**
     * 菜单名称
     */
    @ApiModelProperty(name = "fullName", value = "菜单名称")
    private String fullName;
    /**
     * dic,menu, permission
     */
    @ApiModelProperty(name = "operType", value = "dic,menu, button")
    private String operType;

    /**
     * 权限表识
     */
    @ApiModelProperty(name = "menuAuthority", value = "权限表识")
    private String menuAuthority;

    /**
     * 菜单图标
     */
    @ApiModelProperty(name = "menuIcon", value = "菜单图标")
    private String menuIcon;
    /**
     * 菜单url
     */
    @ApiModelProperty(name = "menuUrl", value = "菜单url")
    private String menuUrl;
    /**
     * 排序字段
     */
    @ApiModelProperty(name = "menuSort", value = "排序字段")
    private Integer menuSort;
    /**
     * 0:有效 1：无效
     */
    @ApiModelProperty(name = "menuState", value = "0:有效 1：无效")
    private Integer menuState;
    /**
     * 0:未删除 1:已删除
     */
    @ApiModelProperty(name = "isDel", value = "0:未删除 1:已删除")
    @TableLogic
    private Integer isDel;
    /**
     * 创建时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(name = "createTime", value = "创建时间")
    private Date createTime;

    /**
     * 子菜单
     */
    @TableField(exist = false)
    @ApiModelProperty(name = "childMenus", value = "子菜单，作为查询条件无效")
    private List<SysMenu> childMenus;

}
