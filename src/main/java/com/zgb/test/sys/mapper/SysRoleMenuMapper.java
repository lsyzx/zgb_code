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
import  com.zgb.test.sys.entity.SysRoleMenu;
import org.apache.ibatis.annotations.Param;

import java.util.List;
/**
 * @author: lsyzx (zhux@zhengebang.com)
 * 创建时间: 2019-08-07 00:18:27
 * 描述: 角色菜单关系
 * 历史修改:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
public interface SysRoleMenuMapper extends BaseMapper<SysRoleMenu> {

    /**
    * 批量插入
    * @param list
    */
    void batchInsert(@Param("list") List<SysRoleMenu> list);

    /**
     * 根据角色id查询菜单信息
     * @param roleId
     * @return
     */
    List<Long> querySelectMenus(@Param("roleId") String roleId);

}