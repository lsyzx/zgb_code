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
package com.zgb.test.utils.config;
import cn.hutool.core.util.StrUtil;
import com.zgb.test.utils.filter.AuthorizeFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: lsyzx (zhux@zhengebang.com)
 * 创建时间: 2019-08-07 00:18:27
 * 描述: 配置过滤器 需要授权的API需要在下面添加
 * 历史修改:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@Configuration
@EnableConfigurationProperties(FilterProperties.class)
public class FilterConfig {

    private FilterProperties properties;

    @Autowired
    public FilterConfig(FilterProperties properties) {
        this.properties = properties;
    }

    /**
     * 添加本地需要授权的接口地址
     *
     * @return
     */
    @Bean
    public FilterRegistrationBean authFilterRegistrationBean() {
        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        AuthorizeFilter authorizeFilter = new AuthorizeFilter();
        registrationBean.setFilter(authorizeFilter);
        List<String> urlPatterns = new ArrayList<String>();
        if (StrUtil.isNotEmpty(properties.getSysUrls())) {
            String[] sysUrls = properties.getSysUrls().split(",");
            for (String sysUrl : sysUrls) {
                //添加此url需要授权/api/sys/*
                urlPatterns.add(sysUrl);
            }
        }
        registrationBean.setUrlPatterns(urlPatterns);
        return registrationBean;
    }
}