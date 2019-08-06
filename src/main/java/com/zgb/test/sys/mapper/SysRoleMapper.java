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
package com.zgb.test.sys.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zgb.test.sys.entity.SysRole;
import org.apache.ibatis.annotations.Param;

import java.util.List;
/**
 * @author: lsyzx (zhux@zhengebang.com)
 * 创建时间: 2019-08-07 00:18:27
 * 描述: 系统角色
 * 历史修改:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
public interface SysRoleMapper extends BaseMapper<SysRole> {

    /**
    * 查询角色分页信息
    *
    * @param page
    * @param sysRole
    * @return
    */
    List<SysRole> querySysRolePageInfo(Page page, @Param("sysRole") SysRole sysRole);

    /**
     * 查询所有角色选中当前用户拥有的权限
     * @param userId
     * @return
     */
    List<SysRole> queryRolesAndUserId(@Param("userId") String userId);
}