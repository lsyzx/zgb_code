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
package com.zgb.test.sys.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zgb.test.sys.entity.SysRole;
import com.zgb.test.sys.entity.SysRoleMenu;
import com.zgb.test.sys.mapper.SysRoleMapper;
import com.zgb.test.sys.mapper.SysRoleMenuMapper;
import com.zgb.test.sys.service.SysRoleService;
import com.zgb.test.utils.em.CommonEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @author: lsyzx (zhux@zhengebang.com)
 * 创建时间: 2019-08-07 00:18:27
 * 描述: 系统角色
 * 历史修改:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements SysRoleService {


    @Autowired
    private SysRoleMenuMapper sysRoleMenuMapper;

    @Autowired
    private SysRoleMapper sysRoleMapper;

    /**
     * 保存角色信息
     *
     * @param sysRole
     */
    @Override
    public void saveRoleInfo(SysRole sysRole) {
        this.save(sysRole);
    }

    /**
     * 修改角色信息
     *
     * @param sysRole
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void modifyRoleInfo(SysRole sysRole) {
        //修改基本信息
        SysRole updRole = this.getById(sysRole.getRoleId());
        this.updateById(updRole.toBuilder().roleId(sysRole.getRoleId()).roleName(sysRole.getRoleName()).roleDesc(sysRole.getRoleDesc()).build());
        if (StrUtil.isNotEmpty(sysRole.getMenus())) {
            //删除角色菜单关联关系表
            sysRoleMenuMapper.delete(Wrappers.query(SysRoleMenu.builder().roleId(sysRole.getRoleId()).build()));
            //新建角色菜单关联关系
            addMenu(sysRole);
        }
    }

    /**
     * 删除角色信息
     *
     * @param roleIds
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delRoleInfo(String roleIds) {
        this.removeByIds(Arrays.asList(roleIds.split(",")));
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.in("role_id", roleIds.split(","));
        sysRoleMenuMapper.delete(wrapper);
    }

    /**
     * 查询角色分页信息
     *
     * @param page
     * @param sysRole
     * @return
     */
    @Override
    public IPage<SysRole> querySysRolePageInfo(Page page, SysRole sysRole) {
        IPage<SysRole> pageInfo = page.setRecords(sysRoleMapper.querySysRolePageInfo(page, sysRole));
        return pageInfo;
    }

    /**
     * 查询所有角色选中当前用户拥有的权限
     *
     * @param userId
     * @return
     */
    @Override
    public List<SysRole> queryRolesAndUserId(String userId) {
        return sysRoleMapper.queryRolesAndUserId(userId);
    }

    /**
     * 新增菜单角色关联关系
     *
     * @param sysRole
     */
    private void addMenu(SysRole sysRole) {
        List<SysRoleMenu> menus = new ArrayList<>();
        for (String id : sysRole.getMenus().split(",")) {
            menus.add(SysRoleMenu.builder().roleId(sysRole.getRoleId()).isDel(CommonEnum.ISDEL_NO.getCode()).menuId(id).createTime(new Date()).build());
        }
        sysRoleMenuMapper.batchInsert(menus);
    }

}