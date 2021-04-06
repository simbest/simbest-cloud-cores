/*
 * 版权所有 © 北京晟壁科技有限公司 2008-2027。保留一切权利!
 */
package com.simbest.cloud.cores.security.auth.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

/**
 * 用途：认证中心服务器控制器
 * 作者: lishuyi@simbest.com.cn
 * 时间: 2021/3/8  9:29
 */
@RestController
public class AuthServerController {

    /**
     * OAuth2 单点登录获取用户身份信息
     * @param principal
     * @return
     */
    @GetMapping("/api/security/getUserInfo")
    public Principal getUserInfo(Principal principal) {
        return principal;
    }


}
