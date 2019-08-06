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
package com.zgb.test.auth.api;

import cn.hutool.crypto.digest.DigestUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.DigestUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zgb.test.sys.entity.SysLog;
import com.zgb.test.sys.entity.SysUser;
import com.zgb.test.sys.service.SysLogService;
import com.zgb.test.sys.service.SysUserService;
import com.zgb.test.utils.CommonConstants;
import com.zgb.test.utils.RedisUtil;
import com.zgb.test.utils.VerificationCode;
import com.zgb.test.utils.aspect.annotation.AutoLog;
import com.zgb.test.utils.em.CommonEnum;
import com.zgb.test.utils.jwt.JwtUtils;
import com.zgb.test.utils.request.LoginRequest;
import com.zgb.test.utils.result.ApiResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author: lsyzx (zhux@zhengebang.com)
 * 创建时间: 2019-08-07 00:18:27
 * 描述: 授权API
 * 历史修改:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/auth")
@Api(tags = "授权API",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class AuthApi {

    private final SysUserService sysUserService;

    @Lazy
    private RedisUtil redisUtil;


    /**s
     * 获取验证码
     */
    @ApiOperation(value = "获取验证码", notes = "获取验证码")
    @GetMapping("/getCode")
    public ApiResult get() {
        try {
            Map<String, Object> resultMap = new HashMap<>();
            BASE64Encoder encoder = new BASE64Encoder();
            VerificationCode verificationCode = new VerificationCode();
            //获取验证码图片
            BufferedImage image = verificationCode.getImage();
            //获取验证码内容
            String code = verificationCode.getText();
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ImageIO.write(image, "jpeg", outputStream);
            String base64Img = encoder.encode(outputStream.toByteArray());
            String uuid = UUID.randomUUID().toString();
            redisUtil.set(uuid, code, CommonConstants.REDIS_EXPIRE_MINITE);
            resultMap.put("uuid", uuid);
            resultMap.put("image", base64Img);
            return new ApiResult(CommonEnum.SUCCESS, resultMap);
        } catch (Exception e) {
            e.printStackTrace();
            return new ApiResult(CommonEnum.FAIL);
        }

    }

    /**
     * 获取token
     *
     * @return
     */
    @AutoLog(value = "用户登录", logType = CommonConstants.LOG_TYPE_1, operateType = CommonConstants.LOG_OPER_5)
    @ApiOperation(value = "获取token", notes = "获取token")
    @GetMapping("/getToken")
    public ApiResult getToken(LoginRequest loginRequest) {
            Object signCode = redisUtil.get(loginRequest.getUuid());
            if (!loginRequest.getVerCode().toLowerCase().equals(String.valueOf(signCode).toLowerCase())) {
                return new ApiResult(CommonEnum.SYS_CODE_LOGIN_CODE_FAIL);
            }
            //验证通过删除验证码
            redisUtil.del(loginRequest.getUuid());
            SysUser sysUser = sysUserService.getUserInfo(loginRequest.getUserAccount(), DigestUtil.md5Hex(loginRequest.getPassWord()));
            if (ObjectUtil.isNull(sysUser)) {
                return new ApiResult(CommonEnum.AUTH_LOGIN_FAIL);
            }
            if (CommonEnum.FLAT_FALSE.getCode() == sysUser.getUserStatus()) {
                return new ApiResult(CommonEnum.SYS_CODE_LOGIN_AUTH_FAIL);
            }
            String jwt = JwtUtils.createJWT(sysUser.getUserId().toString(), JSON.toJSONString(sysUser), CommonConstants.JWT_TTL);
            return new ApiResult(CommonEnum.SUCCESS, jwt);
    }
}