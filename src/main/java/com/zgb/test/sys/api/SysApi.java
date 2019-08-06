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
package com.zgb.test.sys.api;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.DigestUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zgb.test.sys.entity.*;
import com.zgb.test.sys.service.*;
import com.zgb.test.utils.CommonConstants;
import com.zgb.test.utils.aspect.annotation.AutoLog;
import com.zgb.test.utils.em.CommonEnum;
import com.zgb.test.utils.jwt.JwtUtils;
import com.zgb.test.utils.request.PageRequest;
import com.zgb.test.utils.result.ApiResult;
import com.zgb.test.utils.result.UserDataResult;
import io.jsonwebtoken.Claims;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
/**
 * @author: lsyzx (zhux@zhengebang.com)
 * 创建时间: 2019-08-07 00:18:27
 * 描述: 系统相关API
 * 历史修改:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping(value = "/api/sys", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Api(tags = "系统相关API")
public class SysApi {

    private final SysMenuService sysMenuService;

    private final SysRoleService sysRoleService;

    private final SysUserService sysUserService;

    private final SysRoleUserService sysRoleUserService;

    private final SysLogService sysLogService;

    private final SysDictService sysDictService;


    /**
     * 加载首页信息
     *
     * @return
     */
    @AutoLog(value = "加载首页信息", logType = CommonConstants.LOG_TYPE_2, operateType = CommonConstants.LOG_OPER_4)
    @ApiOperation(value = "加载首页信息", notes = "加载首页信息")
    @GetMapping("/loadIndex")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "jwt", value = "jwt", dataType = "string", paramType = "query"),
    })
    public ApiResult loadIndex(String jwt) {
        UserDataResult response = new UserDataResult();
        //设置用户信息
        Claims claims = JwtUtils.parseJWT(jwt);
        SysUser sysUser = JSON.parseObject(claims.getSubject(), SysUser.class);
        response.setSysUser(sysUser);
        //设置菜单信息
        List<SysMenu> menus = sysMenuService.queryMenusByUserId(sysUser.getUserId());
        menus = menus.stream().filter(info->!info.getFullName().equals("首页")).collect(Collectors.toList());
        response.setMenus(menus);
        //设置权限信息
        List<SysMenu> permissins = sysMenuService.queryPermissions(sysUser.getUserId());
        response.setPermissions(permissins);
        //个性化配置
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("user_id", sysUser.getUserId());
        //设置权限信息
        return new ApiResult(CommonEnum.SUCCESS, response);
    }

    /**
     * 查询所有菜单信息
     *
     * @return
     */
    @AutoLog(value = "系统管理 》菜单管理 》菜单信息查询", logType = CommonConstants.LOG_TYPE_2, operateType = CommonConstants.LOG_OPER_4)
    @ApiOperation(value = "查询所有菜单信息", notes = "查询所有菜单信息")
    @GetMapping("/queryMenuAll")
    public ApiResult queryMenuAll() {
        SysMenu sysMenu = new SysMenu();
        sysMenu.setIsDel(0);
        List<SysMenu> sysMenus = sysMenuService.list(Wrappers.query(sysMenu).orderByAsc("menu_sort").notIn("oper_type", "button"));
        return new ApiResult(CommonEnum.SUCCESS, sysMenus);
    }

    /**
     * 查询所有菜单和权限信息
     *
     * @return
     */
    @AutoLog(value = "系统管理 》角色管理 》分配菜单 》查询所有权限", logType = CommonConstants.LOG_TYPE_2, operateType = CommonConstants.LOG_OPER_4)
    @ApiOperation(value = "查询所有菜单信息", notes = "查询所有菜单信息")
    @GetMapping("/queryMenus")
    public ApiResult queryMenus() {
        List<SysMenu> sysMenus = sysMenuService.list(Wrappers.query(SysMenu.builder().isDel(1).build()).orderByAsc("menu_sort"));
        return new ApiResult(CommonEnum.SUCCESS, sysMenus);
    }

    /**
     * 根据菜单ID查询操作权限信息
     *
     * @param menuId
     * @return
     */
    @AutoLog(value = "系统管理 》角色管理 》分配菜单 》查询拥有权限", logType = CommonConstants.LOG_TYPE_2, operateType = CommonConstants.LOG_OPER_4)
    @ApiOperation(value = "根据菜单ID查询操作权限信息", notes = "根据菜单ID查询操作权限信息")
    @GetMapping("/queryPermisByMenuId")
    public ApiResult queryPermisByMenuId(String menuId) {
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("parent_id", menuId);
        wrapper.eq("oper_type", "button");
        wrapper.eq("is_del", CommonEnum.ISDEL_NO.getCode());
        List<SysMenu> permissions = sysMenuService.list(wrapper);
        return new ApiResult(CommonEnum.SUCCESS, permissions);
    }

    /**
     * 校验菜单是否唯一
     *
     * @param menuName
     * @param parentId
     * @return
     */
    @AutoLog(value = "校验菜单是否唯一", logType = CommonConstants.LOG_TYPE_2, operateType = CommonConstants.LOG_OPER_4)
    @ApiOperation(value = "校验菜单是否唯一", notes = "校验菜单是否唯一")
    @PostMapping("/checkMenuNameOnelyone")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "menuName", value = "菜单名称", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "parentId", value = "父菜单ID", dataType = "string", paramType = "query"),
    })
    public ApiResult checkMenuNameOnelyone(String menuName, String parentId) {
        int count = sysMenuService.count(Wrappers.query(SysMenu.builder().isDel(CommonEnum.ISDEL_NO.getCode()).fullName(menuName).parentId(parentId).build()));
        return new ApiResult(CommonEnum.SUCCESS, count == 0 ? true : false);
    }

    /**
     * 校验权限是否唯一
     *
     * @param menuAuthority
     * @param parentId
     * @return
     */
    @AutoLog(value = "校验权限是否唯一", logType = CommonConstants.LOG_TYPE_2, operateType = CommonConstants.LOG_OPER_4)
    @ApiOperation(value = "校验权限是否唯一", notes = "校验权限是否唯一")
    @PostMapping("/checkAuthOnelyone")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "menuAuthority", value = "权限名称", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "parentId", value = "父菜单ID", dataType = "string", paramType = "query"),
    })
    public ApiResult checkAuthOnelyone(String menuAuthority, String parentId) {
        int count = sysMenuService.count(Wrappers.query(SysMenu.builder().isDel(CommonEnum.ISDEL_NO.getCode()).menuAuthority(menuAuthority).parentId(parentId).build()));
        return new ApiResult(CommonEnum.SUCCESS, count == 0 ? true : false);
    }

    /**
     * 添加菜单
     *
     * @param sysMenu
     * @return
     */
    @AutoLog(value = "系统管理 》菜单管理 》添加菜单信息", logType = CommonConstants.LOG_TYPE_2, operateType = CommonConstants.LOG_OPER_1)
    @ApiOperation(value = "添加菜单", notes = "添加菜单")
    @PostMapping("/saveMenuInfo")
    public ApiResult saveMenuInfo(SysMenu sysMenu) {
        sysMenuService.saveMenuInfo(sysMenu.toBuilder().createTime(new Date()).build());
        return new ApiResult(CommonEnum.SUCCESS);
    }

    /**
     * 删除菜单
     *
     * @param menuId
     * @return
     */
    @AutoLog(value = "系统管理 》菜单管理 》删除菜单", logType = CommonConstants.LOG_TYPE_2, operateType = CommonConstants.LOG_OPER_3)
    @ApiOperation(value = "删除菜单", notes = "删除菜单")
    @PostMapping("/deleteMenuInfo")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "menuId", value = "菜单ID", dataType = "string", paramType = "query"),
    })
    public ApiResult deleteMenuInfo(String menuId) {
        sysMenuService.deleteMenuInfo(menuId);
        return new ApiResult(CommonEnum.SUCCESS);
    }

    /**
     * 菜单排序
     *
     * @param type
     * @param menuId
     * @param menuSort
     * @return
     */
    @AutoLog(value = "系统管理 》菜单管理 》菜单排序", logType = CommonConstants.LOG_TYPE_2, operateType = CommonConstants.LOG_OPER_2)
    @ApiOperation(value = "菜单排序", notes = "菜单排序")
    @PostMapping("/menuSort")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "type", value = "类型【0：上移 1：下移】", dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "menuId", value = "菜单ID", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "menuSort", value = "序号", dataType = "int", paramType = "query")
    })
    public ApiResult menuSort(int type, String menuId, int menuSort) {
        Boolean isFlag = sysMenuService.menuSort(type, menuId, menuSort);
        return new ApiResult(CommonEnum.SUCCESS, isFlag);
    }

    /**
     * 修改菜单
     *
     * @param sysMenu
     * @return
     */
    @AutoLog(value = "系统管理 》菜单管理 》修改菜单", logType = CommonConstants.LOG_TYPE_2, operateType = CommonConstants.LOG_OPER_2)
    @ApiOperation(value = "修改菜单", notes = "修改菜单")
    @PostMapping("/modifyMenuInfo")
    public ApiResult modifyMenuInfo(SysMenu sysMenu) {
        sysMenuService.modifyMenuInfo(sysMenu);
        return new ApiResult(CommonEnum.SUCCESS);
    }

    /**
     * 查询角色信息
     *
     * @return
     */
    @AutoLog(value = "系统管理 》角色管理 》查询角色信息", logType = CommonConstants.LOG_TYPE_2, operateType = CommonConstants.LOG_OPER_4)
    @ApiOperation(value = "查询角色信息", notes = "查询角色信息")
    @GetMapping("/queryRoleList")
    public ApiResult queryRoleList(PageRequest page, SysRole sysRole) {
        IPage<SysRole> pageInfo = sysRoleService.querySysRolePageInfo(new Page(page.getCurrent(), page.getSize(), page.getTotal()), sysRole);
        return new ApiResult(CommonEnum.SUCCESS, pageInfo);
    }


    /**
     * 保存角色信息
     *
     * @param sysRole
     * @return
     */
    @AutoLog(value = "系统管理 》角色管理 》保存角色", logType = CommonConstants.LOG_TYPE_2, operateType = CommonConstants.LOG_OPER_2)
    @ApiOperation(value = "保存角色信息", notes = "菜单排序")
    @PostMapping("/saveRoleInfo")
    public ApiResult saveRoleInfo(SysRole sysRole) {
        sysRoleService.saveRoleInfo(sysRole.toBuilder().isDel(CommonEnum.ISDEL_NO.getCode()).createTime(new Date()).build());
        return new ApiResult(CommonEnum.SUCCESS);
    }

    /**
     * 删除角色
     *
     * @param roleIds
     * @return
     */
    @AutoLog(value = "系统管理 》角色管理 》删除角色", logType = CommonConstants.LOG_TYPE_2, operateType = CommonConstants.LOG_OPER_3)
    @ApiOperation(value = "删除角色", notes = "删除角色")
    @PostMapping(value = "/delRoleInfo")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "roleIds", value = "角色id", dataType = "sring", paramType = "query")
    })
    public ApiResult delRoleInfo(String roleIds) {
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.in("role_id", roleIds.split(","));
        wrapper.eq("is_del", CommonEnum.ISDEL_NO);
        int count = sysRoleUserService.count(wrapper);
        if (count > 0) {
            return new ApiResult(CommonEnum.ROLE_DELETE_ERROR, "存在用户数:" + count);
        }
        sysRoleService.delRoleInfo(roleIds);
        return new ApiResult(CommonEnum.SUCCESS);
    }

    /**
     * 查询角色信息
     *
     * @param roleId
     * @return
     */
    @AutoLog(value = "系统管理 》角色管理 》查询角色权限信息", logType = CommonConstants.LOG_TYPE_2, operateType = CommonConstants.LOG_OPER_4)
    @ApiOperation(value = "查询角色信息", notes = "查询角色信息")
    @GetMapping("/queryMenuByRoleId")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "roleId", value = "角色id", dataType = "string", paramType = "query")
    })
    public ApiResult queryMenuByRoleId(String roleId) {
        List<SysMenu> sysMenus = sysMenuService.queryMenusByRole(roleId);
        return new ApiResult(CommonEnum.SUCCESS, sysMenus);
    }


    /**
     * 修改角色信息
     *
     * @param sysRole
     * @return
     */
    @AutoLog(value = "系统管理 》角色管理 》修改角色", logType = CommonConstants.LOG_TYPE_2, operateType = CommonConstants.LOG_OPER_2)
    @ApiOperation(value = "修改角色信息", notes = "修改角色信息")
    @PostMapping("/modifyRoleInfo")
    public ApiResult modifyRoleInfo(SysRole sysRole) {
        sysRoleService.modifyRoleInfo(sysRole);
        return new ApiResult(CommonEnum.SUCCESS);
    }


    /**
     * 用户信息分页查询
     *
     * @param page 分页对象
     * @param sysUser     用户信息
     * @return ApiResult
     */
    @AutoLog(value = "系统管理 》用户管理 》查询用户信息", logType = CommonConstants.LOG_TYPE_2, operateType = CommonConstants.LOG_OPER_4)
    @ApiOperation(value = "用户信息分页查询", notes = "分页查询")
    @GetMapping("/getSysUserPage")
    public ApiResult getSysUserPage(PageRequest page, SysUser sysUser) {
        IPage<SysUser> pageInfo = sysUserService.queryUserPageInfo(new Page(page.getCurrent(),page.getSize(),page.getTotal()), sysUser);
        return new ApiResult(CommonEnum.SUCCESS, pageInfo);
    }

    /**
     * 校验账号是否存在
     *
     * @param userAccount
     * @return
     */
    @AutoLog(value = "系统管理 》用户管理 》校验用户是否存在", logType = CommonConstants.LOG_TYPE_2, operateType = CommonConstants.LOG_OPER_4)
    @ApiOperation(value = "校验账号是否存在", notes = "校验账号是否存在")
    @PostMapping("/checkUserAccountOnely")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userAccount", value = "登录账号", dataType = "string", required = true, paramType = "query"),
    })
    public ApiResult checkUserAccountOnely(String userAccount) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("user_account", userAccount);
        int count = sysUserService.count(queryWrapper);
        return new ApiResult(CommonEnum.SUCCESS, count > 0 ? false : true);
    }

    /**
     * 新增用户信息
     *
     * @param sysUser 用户信息
     * @return ApiResult
     */
    @AutoLog(value = "系统管理 》用户管理 》新增用户", logType = CommonConstants.LOG_TYPE_2, operateType = CommonConstants.LOG_OPER_1)
    @ApiOperation(value = "新增/用户信息", notes = "新增/用户信息")
    @PostMapping("/saveUser")
    public ApiResult saveUser(SysUser sysUser) {
        sysUserService.saveUserInfo(sysUser.toBuilder().userPass(DigestUtil.md5Hex(sysUser.getUserPass())).isDel(CommonEnum.ISDEL_NO.getCode()).createTime(new Date()).build());
        return new ApiResult(CommonEnum.SUCCESS);
    }

    /**
     * 删除用户信息
     *
     * @param userIds
     * @return
     */
    @AutoLog(value = "系统管理 》用户管理 》删除用户", logType = CommonConstants.LOG_TYPE_2, operateType = CommonConstants.LOG_OPER_3)
    @ApiOperation(value = "删除用户信息", notes = "删除用户信息")
    @PostMapping(value = "/delUserInfo")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userIds", value = "用户id", dataType = "string", required = true, paramType = "query"),
    })
    public ApiResult delUserInfo(String userIds) {
        sysUserService.delUserInfo(userIds);
        return new ApiResult(CommonEnum.SUCCESS);
    }


    /**
     * 修改用户信息
     *
     * @param sysUser
     * @return
     */
    @AutoLog(value = "系统管理 》用户管理 》修改用户", logType = CommonConstants.LOG_TYPE_2, operateType = CommonConstants.LOG_OPER_2)
    @ApiOperation(value = "修改/用户信息", notes = "修改/用户信息")
    @PostMapping("/modifyUser")
    public ApiResult modifyUser(SysUser sysUser) {
        String oldPass = sysUserService.getById(sysUser.getUserId()).getUserPass();
        if (!oldPass.equals(sysUser.getUserPass())) {
            sysUser.setUserPass(DigestUtil.md5Hex(sysUser.getUserPass()));
        }
        sysUserService.modifyUserInfo(sysUser);
        return new ApiResult(CommonEnum.SUCCESS);
    }


    /**
     * 校验登录账号是否存在
     *
     * @param userAccount
     * @return
     */
    @AutoLog(value = "系统管理 》用户管理 》校验登录账号是否存在", logType = CommonConstants.LOG_TYPE_2, operateType = CommonConstants.LOG_OPER_4)
    @ApiOperation(value = "校验登录账号是否存在", notes = "校验登录账号是否存在")
    @PostMapping("/checkUserAccountOnelyOne")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userAccount", value = "登录账号", dataType = "string", paramType = "query"),
    })
    public ApiResult checkUserAccountOnelyOne(String userAccount) {
        int count = sysUserService.count(Wrappers.query(SysUser.builder().isDel(CommonEnum.ISDEL_NO.getCode()).userAccount(userAccount).build()));
        return new ApiResult(CommonEnum.SUCCESS, count == 0 ? true : false);
    }

    /**
     * 获取角色信息
     *
     * @return
     */
    @AutoLog(value = "系统管理 》用户管理 》获取角色", logType = CommonConstants.LOG_TYPE_2, operateType = CommonConstants.LOG_OPER_4)
    @ApiOperation(value = "获取角色信息", notes = "获取角色信息")
    @GetMapping("/getRoles")
    public ApiResult getRoles() {
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("is_del", CommonEnum.ISDEL_NO.getCode());
        List<SysRole> roles = sysRoleService.list(wrapper);
        return new ApiResult(CommonEnum.SUCCESS, roles);
    }

    /**
     * 查询日志分页信息
     *
     * @return
     */
    @ApiOperation(value = "日志管理 》登录日志 》日志查询", notes = "查询日志分页信息")
    @GetMapping("/queryLogList")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userAccount", value = "用户账号", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "logType", value = "日志类型【1：登录日志 2：操作日志】", dataType = "string", paramType = "query"),
    })
    public ApiResult queryLogList(PageRequest page, String logType, String userAccount) {
        Map<String, Object> params = new HashMap<>();
        params.put("userAccount", userAccount);
        params.put("logType", logType);
        IPage<SysLog> logIPage = sysLogService.queryLogList(new Page(page.getCurrent(),page.getSize(),page.getTotal()), params);
        return new ApiResult(CommonEnum.SUCCESS, logIPage);
    }

    /**
     * 查询词典分页信息
     *
     * @param page
     * @param dictName
     * @return
     */
    @AutoLog(value = "系统管理 》词典管理 》词典查询", logType = CommonConstants.LOG_TYPE_2, operateType = CommonConstants.LOG_OPER_4)
    @ApiOperation(value = "查询词典分页信息", notes = "查询词典分页信息")
    @GetMapping("/queryDictList")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "dictName", value = "词典名称", dataType = "string", paramType = "query"),
    })
    public ApiResult queryDictList(PageRequest page, String dictName) {
        QueryWrapper wrapper = new QueryWrapper();
        if (StrUtil.isNotEmpty(dictName)) {
            wrapper.eq("dict_name", dictName);
        }
        wrapper.orderByDesc("create_time");
        IPage<SysDict> dictIpage = sysDictService.page(new Page(page.getCurrent(),page.getSize(),page.getTotal()), wrapper);
        return new ApiResult(CommonEnum.SUCCESS, dictIpage);
    }

    /**
     * 添加词典信息
     *
     * @param sysDict
     * @return
     */
    @AutoLog(value = "系统管理 》词典管理 》添加词典", logType = CommonConstants.LOG_TYPE_2, operateType = CommonConstants.LOG_OPER_1)
    @ApiOperation(value = "添加词典信息", notes = "添加词典信息")
    @PostMapping("/saveDictInfo")
    public ApiResult saveDictInfo(SysDict sysDict) {
        sysDictService.save(sysDict.toBuilder().createTime(new Date()).build());
        return new ApiResult(CommonEnum.SUCCESS);
    }

    /**
     * 修改词典信息
     *
     * @param sysDict
     * @return
     */
    @AutoLog(value = "系统管理 》词典管理 》修改词典", logType = CommonConstants.LOG_TYPE_2, operateType = CommonConstants.LOG_OPER_2)
    @ApiOperation(value = "修改词典信息", notes = "修改词典信息")
    @PostMapping("/updDictInfo")
    public ApiResult updDictInfo(SysDict sysDict) {
        sysDictService.updateById(sysDict);
        return new ApiResult(CommonEnum.SUCCESS);
    }

    /**
     * 删除词典信息
     *
     * @param id
     * @return
     */
    @AutoLog(value = "系统管理 》词典管理 》删除词典", logType = CommonConstants.LOG_TYPE_2, operateType = CommonConstants.LOG_OPER_3)
    @ApiOperation(value = "删除词典信息", notes = "删除词典信息")
    @PostMapping("/deleteDictInfo")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "主键id", dataType = "string", paramType = "query"),
    })
    public ApiResult deleteDictInfo(String id) {
        sysDictService.removeById(id);
        return new ApiResult(CommonEnum.SUCCESS);
    }

    /**
     * 查询词典分页信息
     *
     * @param page
     * @param id
     * @return
     */
    @AutoLog(value = "系统管理 》词典管理 》分配词典 》词典项查询", logType = CommonConstants.LOG_TYPE_2, operateType = CommonConstants.LOG_OPER_4)
    @ApiOperation(value = "查询词典项分页信息", notes = "查询词典项分页信息")
    @GetMapping("/queryDictItemList")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "词典主键id", dataType = "string", paramType = "query"),
    })
    public ApiResult queryDictItemList(PageRequest page, String id) {
        IPage<SysDictItem> pageInfo = sysDictService.queryDictItemPageList(new Page(page.getCurrent(),page.getSize(),page.getTotal()), id);
        return new ApiResult(CommonEnum.SUCCESS, pageInfo);
    }

    /**
     * 保存词典项信息
     *
     * @param sysDictItem
     * @return
     */
    @AutoLog(value = "系统管理 》词典管理 》分配词典 》添加词典项", logType = CommonConstants.LOG_TYPE_2, operateType = CommonConstants.LOG_OPER_1)
    @ApiOperation(value = "保存词典项信息", notes = "保存词典项信息")
    @PostMapping("/saveDictItemInfo")
    public ApiResult saveDictItemInfo(SysDictItem sysDictItem) {
        sysDictService.saveDictItemInfo(sysDictItem.toBuilder().isDel(CommonEnum.ISDEL_NO.getCode()).createTime(new Date()).build());
        return new ApiResult(CommonEnum.SUCCESS);
    }

    /**
     * 修改词典项信息
     *
     * @param sysDictItem
     * @return
     */
    @AutoLog(value = "系统管理 》词典管理 》分配词典 》修改词典项", logType = CommonConstants.LOG_TYPE_2, operateType = CommonConstants.LOG_OPER_2)
    @ApiOperation(value = "修改词典项信息", notes = "保存词典项信息")
    @PostMapping("/updDictItemInfo")
    public ApiResult updDictItemInfo(SysDictItem sysDictItem) {
        sysDictService.updDictItemInfo(sysDictItem);
        return new ApiResult(CommonEnum.SUCCESS);
    }

    /**
     * 删除词典项信息
     *
     * @param id
     * @return
     */
    @AutoLog(value = "系统管理 》词典管理 》分配词典 》删除词典项", logType = CommonConstants.LOG_TYPE_2, operateType = CommonConstants.LOG_OPER_4)
    @ApiOperation(value = "删除词典项信息", notes = "删除词典项信息")
    @PostMapping("/delDictItemInfo")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "词典主键id", dataType = "string", paramType = "query"),
    })
    public ApiResult delDictItemInfo(String id) {
        sysDictService.delDictItemInfo(id);
        return new ApiResult(CommonEnum.SUCCESS);
    }

    /**
     * 修改密码
     *
     * @param userId
     * @param userPass
     * @return
     */
    @AutoLog(value = "修改密码", logType = CommonConstants.LOG_TYPE_2, operateType = CommonConstants.LOG_OPER_2)
    @ApiOperation(value = "修改密码", notes = "修改密码")
    @PostMapping("/updPassword")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户id", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "userPass", value = "用户密码", dataType = "string", paramType = "query"),

    })
    public ApiResult updPassword(String userId, String userPass) {
        sysUserService.updateById(SysUser.builder().userId(userId).userPass(DigestUtil.md5Hex(userPass)).build());
        return new ApiResult(CommonEnum.SUCCESS);
    }

    /**
     * 查询词典项集合信息
     *
     * @param dictCode
     * @return
     */
    @AutoLog(value = "词典信息", logType = CommonConstants.LOG_TYPE_2, operateType = CommonConstants.LOG_OPER_4)
    @ApiOperation(value = "查询词典项集合信息", notes = "查询词典项集合信息")
    @GetMapping("/queryDicItemList")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "dictCode", value = "词典编码", dataType = "string", paramType = "query"),

    })
    public ApiResult queryDicItemList(String dictCode) {
        List<Map<String, Integer>> distItems = sysDictService.queryDictItemList(dictCode);
        return new ApiResult(CommonEnum.SUCCESS, distItems);
    }
}