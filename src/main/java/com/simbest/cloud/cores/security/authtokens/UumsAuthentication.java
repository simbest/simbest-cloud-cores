package com.simbest.cloud.cores.security.authtokens;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

/**
 * 用途：基于UUMS主数据的登录认证
 * 作者: lishuyi
 * 时间: 2018/1/20  15:25
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
