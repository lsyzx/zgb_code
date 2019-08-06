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
package com.zgb.test.utils.aspect;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zgb.test.sys.entity.SysLog;
import com.zgb.test.sys.entity.SysUser;
import com.zgb.test.sys.service.SysLogService;
import com.zgb.test.utils.CommonConstants;
import com.zgb.test.utils.aspect.annotation.AutoLog;
import com.zgb.test.utils.em.CommonEnum;
import com.zgb.test.utils.jwt.JwtUtils;
import com.zgb.test.utils.result.ApiResult;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Method;
import java.util.Date;
/**
 * @author: lsyzx (zhux@zhengebang.com)
 * 创建时间: 2019-08-07 00:18:27
 * 描述: 字典aop类
 * 历史修改:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@Aspect
@Component
@Slf4j
public class AutoLogAspect {

    @Autowired
    private SysLogService sysLogService;

    @Pointcut("@annotation(com.zgb.test.utils.aspect.annotation.AutoLog)")
    public void logPointCut() {

    }

    @Around("logPointCut()")
    public Object around(ProceedingJoinPoint point) {
        long beginTime = 0L;
        long time = 0L;
        try {
            beginTime = System.currentTimeMillis();
            //执行方法
            Object result = point.proceed();
            //执行时长(毫秒)
            time = System.currentTimeMillis() - beginTime;
            //保存日志
            saveSysLog(point, time, 200, null);
            return result;
        } catch (Throwable throwable) {
            StringWriter sw = new StringWriter();
            try (PrintWriter pw = new PrintWriter(sw)) {
                throwable.printStackTrace(pw);
            }
            saveSysLog(point, time, 500, sw.toString());
            log.error("执行方法异常{}", sw.toString());
            return new ApiResult(CommonEnum.FAIL);
        }
    }

    private void saveSysLog(ProceedingJoinPoint joinPoint, long time, int status, String errorMsg) {
        //获取request
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        SysLog sysLog = new SysLog();
        AutoLog syslog = method.getAnnotation(AutoLog.class);
        if (syslog != null) {
            //注解上的描述,操作日志内容
            sysLog.setLogContent(syslog.value());
            sysLog.setLogType(syslog.logType());
            sysLog.setOperateType(syslog.operateType());
        }
        sysLog.setStatus(status);
        sysLog.setErrorMsg(StrUtil.isNotEmpty(errorMsg) ? String.valueOf(errorMsg) : null);
        sysLog.setRequestType(request.getMethod());
        sysLog.setRequestUrl(request.getRequestURI());
        //请求的方法名
        String className = joinPoint.getTarget().getClass().getName();
        String methodName = signature.getName();
        sysLog.setMethod(className + "." + methodName + "()");
        //请求的参数
        Object[] args = joinPoint.getArgs();
        try {
            String params = JSONObject.toJSONString(args);
            sysLog.setRequestParam(params);
        } catch (Exception e) {
            log.error("请求参数转换JSON异常", e);
            e.printStackTrace();
        }
        //设置IP地址
        sysLog.setIp(HttpUtil.getClientIP(request));
        String token = ObjectUtil.isNull(request.getHeader(CommonConstants.TOKEN_NAME)) ? request.getParameter(CommonConstants.TOKEN_NAME) : request.getHeader(CommonConstants.TOKEN_NAME);
        if (StrUtil.isNotEmpty(token)) {
            Claims claims = null;
            try {

                claims = JwtUtils.parseJWT(token);
            } catch (Exception e) {
                log.error("获取用户信息异常", e);
                e.printStackTrace();
            }
            if (ObjectUtil.isNotNull(claims)) {
                SysUser sysUser = JSON.parseObject(claims.getSubject(), SysUser.class);
                //获取登录用户信息
                if (sysUser != null) {
                    sysLog.setCreateUser(sysUser.getUserAccount());
                }
            }
        } else {
            sysLog.setCreateUser(request.getParameter("userAccount"));
        }
        //耗时
        sysLog.setCostTime(time);
        sysLog.setCreateTime(new Date());
        //保存系统日志
        sysLogService.save(sysLog);
    }
}