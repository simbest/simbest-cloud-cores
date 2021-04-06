/*
 * 版权所有 © 北京晟壁科技有限公司 2008-2027。保留一切权利!
 */
package com.simbest.cloud.cores.security.auth.providers;

import com.simbest.cloud.cores.constants.ErrorCodeConstants;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * 用途：自定义默认的DaoAuthenticationProvider
 * 作者: lishuyi@simbest.com.cn
 * 时间: 2021/3/9  20:30
 */
//libeixiao
@Slf4j
public class CustomDaoAuthenticationProvider extends DaoAuthenticationProvider {

    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails,
                                                  UsernamePasswordAuthenticationToken authentication)
            throws AuthenticationException {
        if (authentication.getCredentials() == null) {
            log.error("Authentication failed: 密码不能为空");
            throw new BadCredentialsException(messages.getMessage(
                    "AbstractUserDetailsAuthenticationProvider.badCredentials",
                    "密码不能为空----Bad credentials"));
        }

        if(DigestUtils.md5Hex(authentication.getCredentials().toString()).equalsIgnoreCase(userDetails.getPassword())){
            return;
        }
        else{
            log.warn("CustomDaoAuthenticationProvider不支持此认证令牌【{}】，无法通过认证！", authentication);
            throw new BadCredentialsException(ErrorCodeConstants.LOGIN_ERROR_INVALIDATE_USERNAME_PASSWORD);
        }

    }

}
