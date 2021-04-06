/*
 * 版权所有 © 北京晟壁科技有限公司 2008-2027。保留一切权利!
 */
package com.simbest.cloud.cores.security.auth.authentication;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

/**
 * 用途：基于UUMS主数据的登录认证
 * 作者: lishuyi@simbest.com.cn
 * 时间: 2021/3/15  11:55
 */
public class UumsAuthentication extends UsernamePasswordAuthenticationToken {

    /**
     * 认证前
     * @param principal username
     * @param uumsAuthenticationCredentials password和appcode
     */
    public UumsAuthentication(Object principal, Object uumsAuthenticationCredentials) {
        super(principal, uumsAuthenticationCredentials);
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
