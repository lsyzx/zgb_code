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

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zgb.test.sys.entity.SysLog;
import com.zgb.test.sys.mapper.SysLogMapper;
import com.zgb.test.sys.service.SysLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;
/**
 * @author: lsyzx【zhux@zhengebang.com】
 * @author: lsyzx (zhux@zhengebang.com)
 * 创建时间: 2019-08-07 00:18:27
 * 历史修改:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@Service
public class SysLogServiceImpl extends ServiceImpl<SysLogMapper, SysLog> implements SysLogService {

    @Autowired
    private SysLogMapper sysLogMapper;

    /**
     * 清空所有日志记录
     */
    @Override
    public void removeAll() {
        sysLogMapper.removeAll();
    }

    /**
     * 获取系统总访问次数
     *
     * @return
     */
    @Override
    public Long findTotalVisitCount() {
        return sysLogMapper.findTotalVisitCount();
    }

    /**
     * 获取系统今日访问次数
     *
     * @param dayStart 开始使时间
     * @param dayEnd   结束时间
     * @return
     */
    @Override
    public Long findTodayVisitCount(Date dayStart, Date dayEnd) {
        return sysLogMapper.findTodayVisitCount(dayStart, dayEnd);
    }

    /**
     * 获取系统今日访问 IP数
     *
     * @param dayStart 开始时间
     * @param dayEnd   结束时间
     * @return
     */
    @Override
    public Long findTodayIp(Date dayStart, Date dayEnd) {
        return sysLogMapper.findTodayIp(dayStart, dayEnd);
    }

    /**
     * 获取日志分页信息
     * @param page
     * @param params
     * @return
     */
    @Override
    public IPage<SysLog> queryLogList(Page page, Map<String, Object> params) {
        IPage<SysLog> pageInfo = page.setRecords(sysLogMapper.queryLogList(page, params));
        return pageInfo;
    }
}