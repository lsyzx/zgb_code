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
package com.zgb.test.utils.jwt;

import com.alibaba.fastjson.JSON;
import com.zgb.test.utils.CommonConstants;
import com.zgb.test.utils.em.CommonEnum;
import com.zgb.test.utils.result.ApiResult;
import io.jsonwebtoken.*;
import org.bouncycastle.util.encoders.Base64;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Date;

/**
 * @author: lsyzx (zhux@zhengebang.com)
 * 创建时间: 2019-08-07 00:18:27
 * 描述: jwt加密和解密的工具类
 * 历史修改:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
public class JwtUtils {


    /**
     * 签发JWT
     *
     * @param id
     * @param subject   可以是JSON数据 尽可能少
     * @param ttlMillis
     * @return String
     */
    public static String createJWT(String id, String subject, long ttlMillis) {
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        SecretKey secretKey = generalKey();
        JwtBuilder builder = Jwts.builder()
                .setId(id)
                // 主题
                .setSubject(subject)
                // 签发者
                .setIssuer("南京真格邦软件有限公司")
                // 签发时间
                .setIssuedAt(now)
                // 签名算法以及密匙
                .signWith(signatureAlgorithm, secretKey);
        if (ttlMillis >= 0) {
            long expMillis = nowMillis + ttlMillis;
            Date expDate = new Date(expMillis);
            // 过期时间
            builder.setExpiration(expDate);
        }
        return builder.compact();
    }

    /**
     * 验证JWT
     *
     * @param jwtStr
     * @return
     */
    public static ApiResult validateJWT(String jwtStr) {
        Claims claims = null;
        try {
            claims = parseJWT(jwtStr);
            return new ApiResult(CommonEnum.CHECK_JWT_SUCCESS, claims);
        } catch (ExpiredJwtException e) {
            return new ApiResult(CommonEnum.CHECK_JWT_EXPIRE);
        } catch (SignatureException e) {
            return new ApiResult(CommonEnum.CHECK_JWT_FAIL);
        } catch (Exception e) {
            return new ApiResult(CommonEnum.CHECK_JWT_FAIL);
        }
    }

    /**
     * 生产key
     *
     * @return
     */
    public static SecretKey generalKey() {
        byte[] encodedKey = Base64.decode(CommonConstants.JWT_SECERT);
        SecretKey key = new SecretKeySpec(encodedKey, 0, encodedKey.length, "AES");
        return key;
    }

    /**
     * 解析JWT字符串
     *
     * @param jwt
     * @return
     * @throws Exception
     */
    public static Claims parseJWT(String jwt) {
        SecretKey secretKey = generalKey();
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(jwt)
                .getBody();
    }

    public static void main(String[] args)  {
        //小明失效 10s
        String sc = createJWT("1", "小明", 3000);
        System.out.println(sc);
        System.out.println(validateJWT(sc).getCode());
        Claims c1 = (Claims) validateJWT(sc).getData();
        System.out.println(c1.getId());
        System.out.println(JSON.toJSONString(validateJWT(sc)));
    }
}