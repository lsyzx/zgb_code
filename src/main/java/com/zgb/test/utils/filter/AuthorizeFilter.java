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
package com.zgb.test.utils.filter;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSON;
import com.zgb.test.utils.CommonConstants;
import com.zgb.test.utils.em.CommonEnum;
import com.zgb.test.utils.jwt.JwtUtils;
import com.zgb.test.utils.result.ApiResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author: lsyzx (zhux@zhengebang.com)
 * 创建时间: 2019-08-07 00:18:27
 * 描述: Token校验
 * 历史修改:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@Slf4j
public class AuthorizeFilter implements Filter {


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(this,
                filterConfig.getServletContext());
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) {
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        try {
            response.setHeader("Access-Control-Allow-Origin", "*");
            // 允许的访问方法
            response.setHeader("Access-Control-Allow-Methods", "POST, GET, PUT, OPTIONS, DELETE, PATCH");
            // Access-Control-Max-Age 用于 CORS 相关配置的缓存
            response.setHeader("Access-Control-Max-Age", "3600");
            response.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept, Cache-Control, Authorization");
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json; charset=utf-8");
            String method = ((HttpServletRequest) request).getMethod();
            if (method.equals("OPTIONS")) {
                response.setStatus(HttpServletResponse.SC_OK);
            } else {
                String authHeader = ObjectUtil.isNull(request.getHeader(CommonConstants.TOKEN_NAME)) ? request.getParameter(CommonConstants.TOKEN_NAME) : request.getHeader(CommonConstants.TOKEN_NAME);
                log.info("*********************token*********************");
                log.info("headerToken:" + request.getHeader("Authorization"));
                log.info("*********************token*********************");
                if (StringUtils.isEmpty(authHeader)) {
                    log.info(JSON.toJSONString(new ApiResult(CommonEnum.TOKEN_ERROR_NULL)));
                    print(response, new ApiResult(CommonEnum.TOKEN_ERROR_NULL));
                } else {
                    //验证JWT的签名，返回CheckResult对象
                    ApiResult result = JwtUtils.validateJWT(authHeader);
                    if (CommonEnum.CHECK_JWT_SUCCESS.getCode() == result.getCode()) {
                        chain.doFilter(request, response);
                    } else {
                        switch (result.getCode()) {
                            // 签名验证不通过
                            case CommonConstants.CHECK_JWT_FAIL:
                                log.info(JSON.toJSONString(new ApiResult(CommonEnum.CHECK_JWT_FAIL)));
                                print(response, new ApiResult(CommonEnum.CHECK_JWT_FAIL));
                                break;
                            // 签名过期，返回过期提示码
                            case CommonConstants.CHECK_JWT_EXPIRE:
                                log.info(JSON.toJSONString(new ApiResult(CommonEnum.CHECK_JWT_EXPIRE)));
                                print(response, new ApiResult(CommonEnum.CHECK_JWT_EXPIRE));
                                break;
                            default:
                                break;
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            print(response, new ApiResult(CommonEnum.FAIL));

        }
    }

    /**
     * 封装返回对象
     *
     * @param response
     * @param message
     */
    public void print(HttpServletResponse response, Object message) {
        try {
            response.setHeader("Access-Control-Allow-Origin", "*");
            response.setHeader("Access-Control-Allow-Methods", "POST, GET, PUT, OPTIONS, DELETE");
            response.setHeader("Access-Control-Max-Age", "3600");
            response.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept, Cache-Control, Authorization");
            response.setCharacterEncoding("UTF-8");
            response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            PrintWriter writer = response.getWriter();
            writer.write(JSON.toJSONString(message));
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}