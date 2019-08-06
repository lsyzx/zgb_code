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
import com.zgb.test.sys.entity.SysUser;
/**
 * @author: lsyzx (zhux@zhengebang.com)
 * 创建时间: 2019-08-07 00:18:27
 * 描述: 用户信息
 * 历史修改:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
public interface SysUserService extends IService<SysUser> {

    /**
     * 根据账号密码查询用户信息
     * @param userAccount
     * @param userPass
     * @return
     */
    SysUser getUserInfo(String userAccount, String userPass);

    /**
     * 新增用户信息
     * @param sysUser
     */
    void saveUserInfo(SysUser sysUser);

    /**
     * 修改用户信息
     * @param sysUser
     */
    void modifyUserInfo(SysUser sysUser);

    /**
     * 删除用户信息
     * @param userIds
     */
    void delUserInfo(String userIds);

    /**
     * 查询用户分页信息
     * @param pageRequest
     * @param sysUser
     * @return
     */
    IPage<SysUser> queryUserPageInfo(Page pageRequest, SysUser sysUser);
}