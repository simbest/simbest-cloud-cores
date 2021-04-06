/*
 * 版权所有 © 北京晟壁科技有限公司 2008-2027。保留一切权利!
 */
package com.simbest.cloud.cores.security.auth.filters;

import com.simbest.cloud.cores.config.AppConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.util.matcher.RequestMatcher;

/**
 * 用途：验证码过滤器
 * 作者: lishuyi@simbest.com.cn
 * 时间: 2021/3/15  11:50
 */
public class RestCaptchaAuthenticationFilter extends CaptchaAuthenticationFilter {

    @Autowired
    private AppConfig config;

    public RestCaptchaAuthenticationFilter(RequestMatcher requiresAuthenticationRequestMatcher) {
        super(requiresAuthenticationRequestMatcher);
    }

}
