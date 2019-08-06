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
import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
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
@TableName("zgb_sys_dict_item")
@ApiModel(value = "字典项表", description = "字典项表")
public class SysDictItem implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @ApiModelProperty(name = "id", value = "主键ID")
    @TableId(type = IdType.UUID)
    private String id;

    /**
     * 字典id
     */
    @ApiModelProperty(name = "dictId", value = "字典id")
    private String dictId;


    /**
     * 字典项文本
     */
    @ApiModelProperty(name = "itemText", value = "字典项文本")
    private String itemText;

    /**
     * 字典项值
     */
    @ApiModelProperty(name = "itemValue", value = "字典项值")
    private String itemValue;

    /**
     * 描述
     */
    @ApiModelProperty(name = "description", value = "描述")
    private String description;

    /**
     * 排序
     */
    @ApiModelProperty(name = "sortOrder", value = "排序")
    private Integer sortOrder;

    /**
     * 状态（1启用 0不启用）
     */

    @DictVal(dicCode = "common_status")
    @ApiModelProperty(name = "status", value = "状态（1启用 0不启用）")
    private Integer status;


    /**
     * 是否删除 0:未删除 1:已删除
     */
    @ApiModelProperty(name = "isDel", value = "是否删除 0:未删除 1:已删除")
    private Integer isDel;


    /**
     * 状态（1启用 0不启用）
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(name = "createTime", value = "创建时间")
    private Date createTime;

}