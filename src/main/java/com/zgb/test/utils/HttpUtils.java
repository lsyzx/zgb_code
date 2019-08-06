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
package com.zgb.test.utils;


import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpStatus;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * @author: lsyzx (zhux@zhengebang.com)
 * 创建时间: 2019-08-07 00:18:27
 * 描述: Http请求工具
 * 历史修改:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@Slf4j
public class HttpUtils {

    /**
     * HttpGet 请求
     *
     * @param url     请求URL
     * @param body    请求消息体
     * @param headers 头部信息
     * @param timeout 超时
     * @param cls     class
     * @param <T>
     * @return
     */
    public static <T> T get(String url, String body, Map<String, String> headers, int timeout, Class<T> cls) {
        try {
            log.info("GET REQUEST -> URL -> {} BODY -> {} HEADERS -> {} TIMEOUT -> {} CLASS -> {}", url, body, headers, timeout, cls);
            HttpRequest httpRequest = cn.hutool.http.HttpUtil.createGet(url);
            httpRequest.contentType("application/json; charset=UTF-8");
            // TODO: 2019/1/7 设置请求消息体
            httpRequest.body(body);
            // TODO: 2019/1/7 设置头部信息
            httpRequest.addHeaders(headers);
            // TODO: 2019/1/7 设置超时时间
            httpRequest.timeout(timeout);
            HttpResponse httpResponse = httpRequest.execute();
            if (httpResponse.getStatus() == HttpStatus.HTTP_OK) {
                log.info("GET RESPONSE -> {}", httpResponse.body());
                return (T) JSON.parseObject(httpResponse.body(), cls);
            }else{
                log.info("GET RESPONSE -> {}", httpResponse.body());
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("GET -> URL -> {} BODY -> {} HEADERS -> {} TIMEOUT -> {} CLASS -> {}", url, body, headers, timeout, cls);
        }
        return null;
    }

    /**
     * HttpPost 请求
     *
     * @param url     请求URL
     * @param body    请求消息体
     * @param headers 头部信息
     * @param timeout 超时
     * @param cls     class
     * @param <T>
     * @return
     */
    public static <T> T post(String url, String body, Map<String, String> headers, int timeout, Class<T> cls) {
        try {
            log.info("POST REQUEST -> URL -> {}, BODY -> {},HEADERS -> {},TIMEOUT -> {},CLASS -> {}", url, body, headers, timeout, cls);
            HttpRequest httpRequest = cn.hutool.http.HttpUtil.createPost(url);
            httpRequest.contentType("application/json; charset=UTF-8");
            // TODO: 2019/1/7 设置请求消息体
            httpRequest.body(body);
            // TODO: 2019/1/7 设置头部信息
            httpRequest.addHeaders(headers);
            // TODO: 2019/1/7 设置超时时间
            httpRequest.timeout(timeout);
            HttpResponse httpResponse = httpRequest.execute();
            if (httpResponse.getStatus() == HttpStatus.HTTP_OK) {
                log.info("POST RESPONSE -> {}", httpResponse.body());
                return (T) JSON.parseObject(httpResponse.body(), cls);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("POST -> URL -> {} BODY -> {} HEADERS -> {} TIMEOUT -> {} CLASS -> {}", url, body, headers, timeout, cls);
        }
        return null;
    }
}