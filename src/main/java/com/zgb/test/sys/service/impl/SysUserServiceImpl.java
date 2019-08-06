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

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zgb.test.sys.entity.SysRoleUser;
import com.zgb.test.sys.entity.SysUser;
import com.zgb.test.sys.mapper.SysRoleUserMapper;
import com.zgb.test.sys.mapper.SysUserMapper;
import com.zgb.test.sys.service.SysUserService;
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
 * 描述: 用户信息
 * 历史修改:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {

    @Autowired
    private SysRoleUserMapper sysRoleUserMapper;

    @Autowired
    private SysUserMapper sysUserMapper;

    /**
     * 根据账号密码查询用户信息
     *
     * @param userAccount
     * @param userPass
     * @return
     */
    @Override
    public SysUser getUserInfo(String userAccount, String userPass) {
        return this.getOne(Wrappers.query(SysUser.builder().userAccount(userAccount).userPass(userPass).userStatus(CommonEnum.FLAT_TRUE.getCode()).build()));
    }

    /**
     * 新增用户信息
     *
     * @param sysUser
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveUserInfo(SysUser sysUser) {
        this.save(sysUser);
        //添加用户角色信息
        addRoleUser(sysUser);
    }

    /**
     * 修改用户信息
     *
     * @param sysUser
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void modifyUserInfo(SysUser sysUser) {
        this.updateById(sysUser);
        //删除用户角色关联关系
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("user_id", sysUser.getUserId());
        sysRoleUserMapper.delete(wrapper);
        //添加用户角色信息
        addRoleUser(sysUser);
    }

    /**
     * 添加用户角色信息
     *
     * @param sysUser
     */
    private void addRoleUser(SysUser sysUser) {
        List<SysRoleUser> roleUserList = new ArrayList<>();
        for (String roleId : sysUser.getRoles().split(",")) {
            roleUserList.add(SysRoleUser.builder().userId(sysUser.getUserId()).roleId(roleId).isDel(CommonEnum.ISDEL_NO.getCode()).createTime(new Date()).build());
        }
        sysRoleUserMapper.batchInsert(roleUserList);
    }

    /**
     * 删除用户信息
     *
     * @param userIds
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delUserInfo(String userIds) {
        this.removeByIds(Arrays.asList(userIds.split(",")));
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.in("user_id", userIds.split(","));
        sysRoleUserMapper.delete(wrapper);
    }

    /**
     * 查询用户分页信息
     *
     * @param pageRequest
     * @param sysUser
     * @return
     */
    @Override
    public IPage<SysUser> queryUserPageInfo(Page pageRequest, SysUser sysUser) {
        IPage<SysUser> pageInfo = pageRequest.setRecords(sysUserMapper.queryUserPageInfo(pageRequest, sysUser));
        return pageInfo;
    }
}