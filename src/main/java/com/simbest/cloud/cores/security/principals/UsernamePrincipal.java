package com.simbest.cloud.cores.security.principals;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.security.Principal;

/**
 * 用途：关键字KeyType的SSO认证实体
 * 作者: lishuyi
 * 时间: 2018/8/18  23:59
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UsernamePrincipal implements Principal, Serializable {

    private String username;

    @Override
    public String getName() {
        return username;
    }
}
