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
import com.zgb.test.sys.entity.SysDict;
import com.zgb.test.sys.entity.SysDictItem;

import java.util.List;
import java.util.Map;
/**
 * @author: lsyzx (zhux@zhengebang.com)
 * 创建时间: 2019-08-07 00:18:27
 * 描述: 词典信息
 * 历史修改:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
public interface SysDictService  extends IService<SysDict> {

    /**
     * 通过查询指定code 获取字典值text
     * @param code
     * @param key
     * @return
     */
    String queryDictTextByKey(String code, String key);

    /**
     * 查询词典项分页信息
     * @param page
     * @param id
     * @return
     */
    IPage<SysDictItem> queryDictItemPageList(Page page,String id);

    /**
     * 添加词典项信息
     * @param sysDictItem
     */
    void saveDictItemInfo(SysDictItem sysDictItem);

    /**
     * 更新词典项信息
     * @param sysDictItem
     */
    void updDictItemInfo(SysDictItem sysDictItem);

    /**
     * 删除词典项信息
     * @param id
     */
    void delDictItemInfo(String id);

    /**
     * 根据词典编码查询词典项
     * @param dictCode
     * @return
     */
    List<Map<String,Integer>> queryDictItemList(String dictCode);

}