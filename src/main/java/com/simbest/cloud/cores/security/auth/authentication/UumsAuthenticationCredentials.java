/*
 * 版权所有 © 北京晟壁科技有限公司 2008-2027。保留一切权利!
 */
package com.simbest.cloud.cores.security.auth.authentication;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;

/**
 * 用途：基于UUMS主数据的登录认证密码凭证
 * 作者: lishuyi@simbest.com.cn
 * 时间: 2021/3/10  15:33
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UumsAuthenticationCredentials implements Serializable {

    private String password;

    private String appcode;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}

