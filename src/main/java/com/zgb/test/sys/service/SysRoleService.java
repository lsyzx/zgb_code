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
package com.zgb.test.sys.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zgb.test.sys.entity.SysRole;

import java.util.List;
import java.util.Map;

/**
 * @author: lsyzx (zhux@zhengebang.com)
 * 创建时间: 2019-08-07 00:18:27
 * 描述: 系统角色
 * 历史修改:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
public interface SysRoleService extends IService<SysRole> {

    /**
     * 保存角色信息
     * @param sysRole
     */
    void saveRoleInfo(SysRole sysRole);


    /**
     * 修改角色信息
     * @param sysRole
     */
    void modifyRoleInfo(SysRole sysRole);

    /**
     * 删除菜单信息
     * @param roleIds
     */
    void delRoleInfo(String roleIds);

    /**
     * 查询角色分页信息
     * @param page
     * @param sysRole
     * @return
     */
    IPage<SysRole> querySysRolePageInfo(Page page, SysRole sysRole);

    /**
     * 查询所有角色选中当前用户拥有的权限
     * @param userId
     * @return
     */
    List<SysRole> queryRolesAndUserId(String userId);
}