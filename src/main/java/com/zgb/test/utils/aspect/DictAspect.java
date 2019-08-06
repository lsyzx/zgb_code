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
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zgb.test.sys.service.SysDictService;
import com.zgb.test.utils.aspect.annotation.DictVal;
import com.zgb.test.utils.result.ApiResult;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
/**
 * @author: lsyzx (zhux@zhengebang.com)
 * 创建时间: 2019-08-07 00:18:27
 * 描述: 字典切面
 * 历史修改:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@Aspect
@Component
@Slf4j
public class DictAspect {

    @Autowired
    private SysDictService dictService;

    @Pointcut("execution(public * com.zgb.test..*.*Api.*(..))")
    public void excudeService() {
    }

    @Around("excudeService()")
    public Object doAround(ProceedingJoinPoint pjp) throws Throwable {
        long time1 = System.currentTimeMillis();
        Object result = pjp.proceed();
        long time2 = System.currentTimeMillis();
        log.info("获取JSON数据 耗时：" + (time2 - time1) + "ms");
        long start = System.currentTimeMillis();
        parseDictText(result);
        long end = System.currentTimeMillis();
        log.info("解析注入JSON数据  耗时" + (end - start) + "ms");
        return result;
    }

    /**
     * 本方法针对返回对象为Result 的IPage的分页列表数据进行动态字典注入
     * 字典注入实现 通过对实体类添加注解@dict 来标识需要的字典内容,字典分为单字典code即可 ，table字典 code table text配合使用与原来jeecg的用法相同
     * 示例为SysUser   字段为sex 添加了注解@Dict(dicCode = "sex") 会在字典服务立马查出来对应的text 然后在请求list的时候将这个字典text，已字段名称加_dictText形式返回到前端
     * 例输入当前返回值的就会多出一个sex_dictText字段
     * {
     * sex:1,
     * sex_dictText:"男"
     * }
     * 前端直接取值sext_dictText在table里面无需再进行前端的字典转换了
     * customRender:function (text) {
     * if(text==1){
     * return "男";
     * }else if(text==2){
     * return "女";
     * }else{
     * return text;
     * }
     * }
     * 目前vue是这么进行字典渲染到table上的多了就很麻烦了 这个直接在服务端渲染完成前端可以直接用
     *
     * @param result
     */
    private void parseDictText(Object result) {
        if (result instanceof ApiResult) {
            if (((ApiResult) result).getData() instanceof IPage) {
                log.info("======================分页数据词典转换======================");
                List<JSONObject> items = new ArrayList<>();
                for (Object record : ((IPage) ((ApiResult) result).getData()).getRecords()) {
                    convertDic(items, record);
                }
                ((IPage) ((ApiResult) result).getData()).setRecords(items);
            } else {
                ApiResult apiResult = (ApiResult) result;
                Object object = apiResult.getData();
                if (object instanceof List) {
                    log.info("======================集合数据词典转换======================");
                    List<JSONObject> items = new ArrayList<>();
                    List<Object> list = (List<Object>) object;
                    for (Object obj : list) {
                        convertDic(items, obj);
                    }
                    apiResult.setData(items);
                } else {
                    String json = "{}";
                    ApiResult resultData = (ApiResult) result;
                    Object dataInfo = resultData.getData();
                    if(dataInfo instanceof String
                            || dataInfo instanceof Date
                            || dataInfo instanceof Byte
                            || dataInfo instanceof Long
                            || dataInfo instanceof Short
                            || dataInfo instanceof Integer
                            || dataInfo instanceof Float
                            || dataInfo instanceof Double
                            || dataInfo instanceof Boolean
                            || dataInfo instanceof Character)
                    {
                        return;
                    }
                    try {
                        ObjectMapper mapper = new ObjectMapper();
                        //解决@JsonFormat注解解析不了的问题详见SysAnnouncement类的@JsonFormat
                        json = mapper.writeValueAsString(dataInfo);
                    } catch (JsonProcessingException e) {
                        log.error("json解析失败" + e.getMessage(), e);
                    }
                    log.info("======================单个对象数据词典转换======================");
                    JSONObject item = JSONObject.parseObject(json);
                    if(ObjectUtil.isNotNull(dataInfo)) {
                        for (Field field : dataInfo.getClass().getDeclaredFields()) {
                            if (field.getAnnotation(DictVal.class) != null) {
                                String key = String.valueOf(item.get(field.getName()));
                                String code = field.getAnnotation(DictVal.class).dicCode();
                                String textValue = dictService.queryDictTextByKey(code, key);
                                item.put(field.getName() + "_dictText", textValue);
                            }
                            //date类型默认转换string格式化日期
                            if (field.getAnnotation(JsonFormat.class) == null && field.getType().getName().equals("java.util.Date") && item.get(field.getName()) != null) {
                                SimpleDateFormat aDate = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss");
                                item.put(field.getName(), aDate.format(new Date((Long) item.get(field.getName()))));
                            }
                        }
                        resultData.setData(item);
                    }
                }
            }
        }
    }

    /**
     * 转换词典数据
     *
     * @param items
     * @param record
     */
    private void convertDic(List<JSONObject> items, Object record) {
        ObjectMapper mapper = new ObjectMapper();
        String json = "{}";
        try {
            //解决@JsonFormat注解解析不了的问题详见SysAnnouncement类的@JsonFormat
            json = mapper.writeValueAsString(record);
        } catch (JsonProcessingException e) {
            log.error("json解析失败" + e.getMessage(), e);
        }
        JSONObject item = JSONObject.parseObject(json);
        for (Field field : record.getClass().getDeclaredFields()) {
            if (field.getAnnotation(DictVal.class) != null) {
                String code = field.getAnnotation(DictVal.class).dicCode();
                String key = String.valueOf(item.get(field.getName()));
                String textValue = dictService.queryDictTextByKey(code, key);
                item.put(field.getName() + "_dictText", textValue);
            }
            //date类型默认转换string格式化日期
            if (field.getType().getName().equals("java.util.Date") && field.getAnnotation(JsonFormat.class) == null && item.get(field.getName()) != null) {
                SimpleDateFormat aDate = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss");
                item.put(field.getName(), aDate.format(new Date((Long) item.get(field.getName()))));
            }
        }
        items.add(item);
    }
}