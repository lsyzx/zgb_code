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
package com.zgb.test.utils.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


/**
 * @author: lsyzx (zhux@zhengebang.com)
 * 创建时间: 2019-08-07 00:18:27
 * 描述: 登录请求对象
 * 历史修改:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "登录请求参数", description = "登录请求参数")
public class LoginRequest implements Serializable {

  /**
   * uuid
   */
  @ApiModelProperty(name = "uuid", value = "登录uuid", required = true)
  private String uuid;

  /**
   * 用户账号
   */
  @ApiModelProperty(name = "userAccount", value = "登录账号", required = true)
  private String userAccount;

  /**
   * 用户密码
   */
  @ApiModelProperty(name = "passWord", value = "登录密码", required = true)
  private String passWord;

  /**
   * 验证码
   */
  @ApiModelProperty(name = "verCode", value = "验证码", required = true)
  private String verCode;

}