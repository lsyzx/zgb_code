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

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zgb.test.sys.entity.SysMenu;
import com.zgb.test.sys.mapper.SysMenuMapper;
import com.zgb.test.sys.service.SysMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
/**
 * @author: lsyzx【zhux@zhengebang.com】
 * @author: lsyzx (zhux@zhengebang.com)
 * 创建时间: 2019-08-07 00:18:27
 * 历史修改:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@Service
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements SysMenuService {

    @Autowired
    private SysMenuMapper sysMenuMapper;

    /**
     * 根据用户id查询菜单信息
     *
     * @param userId 用户id
     * @return
     */
    @Override
    public List<SysMenu> queryMenusByUserId(String userId) {
        List<SysMenu> sysMenus = sysMenuMapper.getMenusByUserId(userId);
        sysMenus = getMenuIfnfo(sysMenus);
        return sysMenus;
    }

    @Override
    public List<SysMenu> queryMenusByRole(String roleId) {

        return sysMenuMapper.queryMenusByRole(roleId);
    }

    /**
     * 查询用户权限信息
     *
     * @param userId 用户id
     * @return
     */
    @Override
    public List<SysMenu> queryPermissions(String userId) {
        return sysMenuMapper.queryPermissions(userId);
    }

    /**
     * 查询最大排序
     *
     * @param parentId
     * @return
     */
    @Override
    public int queryMaxSort(String parentId) {
        return sysMenuMapper.queryMaxSort(parentId);
    }

    /**
     * 删除菜单信息
     *
     * @param menuId 菜单id
     */
    @Override
    public void deleteMenu(String menuId) {
        sysMenuMapper.deleteById(menuId);
        sysMenuMapper.delete(Wrappers.query(SysMenu.builder().parentId(menuId).build()));
    }

    /**
     * 菜单排序
     *
     * @param type
     * @param menuId
     * @param menuSort
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean menuSort(int type, String menuId, int menuSort) {
        //查询当前菜单信息
        SysMenu sysMenu = sysMenuMapper.selectById(menuId);
        // TODO: 2019-05-08 判断是上移还是下移 0 上移 1 下移
        if (type == 0) {
            SysMenu lastMenu = sysMenuMapper.selectOne(Wrappers.query(new SysMenu(null, sysMenu.getParentId(), null, null, null, null, null, sysMenu.getMenuSort() - 1, null, null, null, null)));
            if (lastMenu == null || !lastMenu.getParentId().equals(sysMenu.getParentId())) {
                return false;
            }
            //更新当前菜单排序
            sysMenuMapper.updateById(SysMenu.builder().menuId(sysMenu.getMenuId()).menuSort(sysMenu.getMenuSort() - 1).build());
            //更新上级菜单
            sysMenu = sysMenuMapper.selectOne(Wrappers.query(new SysMenu(null, sysMenu.getParentId(), null, null, null, null, null, sysMenu.getMenuSort() - 1, null, null, null, null)).ne("menu_id", sysMenu.getMenuId()));
            sysMenuMapper.updateById(sysMenu.toBuilder().menuSort(sysMenu.getMenuSort() + 1).build());
        } else if (type == 1) {
            SysMenu fristMenu = sysMenuMapper.selectOne(Wrappers.query(new SysMenu(null, sysMenu.getParentId(), null, null, null, null, null, sysMenu.getMenuSort() + 1, null, null, null, null)));
            if (fristMenu == null || !fristMenu.getParentId().equals(sysMenu.getParentId())) {
                return false;
            }
            //更新当前菜单排序
            sysMenuMapper.updateById(SysMenu.builder().menuId(sysMenu.getMenuId()).menuSort(sysMenu.getMenuSort() + 1).build());
            //更新下级菜单
            sysMenu = sysMenuMapper.selectOne(Wrappers.query(new SysMenu(null, sysMenu.getParentId(), null, null, null, null, null, sysMenu.getMenuSort() + 1, null, null, null, null)).ne("menu_id", sysMenu.getMenuId()));
            sysMenuMapper.updateById(sysMenu.toBuilder().menuSort(sysMenu.getMenuSort() - 1).build());
        }
        return true;
    }

    /**
     * 保存菜单信息
     *
     * @param sysMenu
     */
    @Override
    public void saveMenuInfo(SysMenu sysMenu) {
        sysMenu.setMenuSort(sysMenuMapper.queryMaxSort(sysMenu.getParentId()));
        sysMenuMapper.insert(sysMenu);
    }

    /**
     * 删除菜单信息
     *
     * @param menuId
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteMenuInfo(String menuId) {
        sysMenuMapper.deleteById(menuId);
        sysMenuMapper.delete(Wrappers.query(SysMenu.builder().parentId(menuId).build()));
    }

    /**
     * 修改菜单信息
     *
     * @param sysMenu
     */
    @Override
    public void modifyMenuInfo(SysMenu sysMenu) {
        sysMenuMapper.updateById(sysMenu);
    }

    /**
     * 根据等级重新封装菜单权限信息
     *
     * @param menus
     * @return
     */
    private List<SysMenu> getMenuIfnfo(List<SysMenu> menus) {
        //定义一个空的菜单集合
        List<SysMenu> sysMenuList = new ArrayList<SysMenu>();
        //先找到所有的一级菜单
        for (int i = 0; i < menus.size(); i++) {
            // 一级菜单的parentid为1
            if (menus.get(i).getParentId().equals("60914034d768af34cedfa8d7f0defca2")) {
                //放入集合
                sysMenuList.add(menus.get(i));
            }
        }
        // 为一级菜单设置子菜单，getChild是递归调用的
        for (SysMenu permission : sysMenuList) {
            permission.setChildMenus(getChild(permission.getMenuId(), menus));
        }
        return sysMenuList;
    }

    /**
     * 递归查找子菜单
     *
     * @param id    当前菜单id
     * @param menus 要查找的列表
     * @return 重新封装的菜单集合
     */
    private List<SysMenu> getChild(String id, List<SysMenu> menus) {
        // 空子菜单集合
        List<SysMenu> childList = new ArrayList<SysMenu>();
        if (menus != null && menus.size() > 0) {
            for (SysMenu menuinfo : menus) {
                // 遍历所有节点，将父菜单id与传过来的id比较
                if (menuinfo.getParentId().equals(id)) {
                    childList.add(menuinfo);
                }
            }
        }
        // 把子菜单的子菜单再循环一遍
        for (SysMenu drpPermission : childList) {
            if (drpPermission != null) {
                // 递归
                drpPermission.setChildMenus(getChild(drpPermission.getMenuId(), menus));
            }
        }
        // 递归退出条件
        if (childList.size() == 0) {
            return null;
        }
        return childList;
    }

}