/*
 * 版权所有 © 北京晟壁科技有限公司 2008-2027。保留一切权利!
 */
package com.simbest.cloud.cores.security.auth.authentication;

import com.simbest.cloud.orguser.dto.IUser;
import com.simbest.cloud.cores.security.auth.authentication.principal.KeyTypePrincipal;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * 用途：基于UUMS主数据的登录认证，参考UsernamePasswordAuthenticationToken
 * 作者: lishuyi@simbest.com.cn
 * 时间: 2021/3/10  15:32
 */
public class GenericAuthentication extends AbstractAuthenticationToken {

    @Setter @Getter
    private Object principal;

    @Setter @Getter
    private UumsAuthenticationCredentials credentials;

    /**
     * 认证前
     * @param principal
     * @param credentials
     */
    public GenericAuthentication(KeyTypePrincipal principal, UumsAuthenticationCredentials credentials) {
        super(null);
        this.principal = principal;
        this.credentials = credentials;
        setAuthenticated(false);
    }

    /**
     * 认证后
     * @param principal
     * @param credentials
     * @param authorities
     */
    public GenericAuthentication(IUser principal, UumsAuthenticationCredentials credentials,
                                 Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.principal = principal;
        this.credentials = credentials;
        super.setAuthenticated(true); // must use super, as we override
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
