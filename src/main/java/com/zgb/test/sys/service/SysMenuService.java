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

import com.baomidou.mybatisplus.extension.service.IService;
import com.zgb.test.sys.entity.SysMenu;
import java.util.List;
/**
 * @author: lsyzx (zhux@zhengebang.com)
 * 创建时间: 2019-08-07 00:18:27
 * 描述: 菜单信息
 * 历史修改:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
public interface SysMenuService extends IService<SysMenu> {

    /**
     * 根据用户id查询菜单信息
     * @param userId 用户id
     * @return
     */
    List<SysMenu> queryMenusByUserId(String userId);

    /**
     * 根据角色id查询菜单信息
     * @param roleId
     * @return
     */
    List<SysMenu> queryMenusByRole(String roleId);


    /**
     * 查询用户权限信息
     * @param userId 用户id
     * @return
     */
    List<SysMenu> queryPermissions(String userId);

    /**
     * 查询最大排序
     * @param parentId
     * @return
     */
    int queryMaxSort(String parentId);

    /**
     * 删除菜单信息
     * @param menuId
     */
    void deleteMenu(String menuId);

    /**
     * 排序
     * @param type
     * @param menuId
     * @param menuSort
     * @return
     */
    Boolean menuSort(int type, String menuId, int menuSort);

    /**
     * 保存菜单信息
     * @param sysMenu
     */
    void saveMenuInfo(SysMenu sysMenu);

    /**
     * 删除菜单信息
     * @param menuId
     */
    void deleteMenuInfo(String menuId);

    /**
     * 修改菜单信息
     * @param sysMenu
     */
    void modifyMenuInfo(SysMenu sysMenu);

}