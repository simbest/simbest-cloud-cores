/*
 * 版权所有 © 北京晟壁科技有限公司 2008-2027。保留一切权利!
 */
package com.simbest.cloud.cores.security.auth.handles;

import com.simbest.cloud.cores.utils.security.LoginUtils;
import com.simbest.cloud.orguser.dto.IUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.CompositeLogoutHandler;
import org.springframework.security.web.authentication.logout.CookieClearingLogoutHandler;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.authentication.rememberme.AbstractRememberMeServices;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

/**
 * 用途：默认登录处理器
 * 参考：
 * https://niocoder.com/2018/01/18/Spring-Security%E6%BA%90%E7%A0%81%E5%88%86%E6%9E%90%E5%85%AB-Spring-Security-%E9%80%80%E5%87%BA/
 * https://blog.csdn.net/py_xin/article/details/52634880
 * 作者: lishuyi@simbest.com.cn
 * 时间: 2021/3/9  14:38
 */
@Slf4j
public class DefaultLogoutHandler implements LogoutHandler {

    private LoginUtils loginUtils;

    private LogoutHandler handler;

    public DefaultLogoutHandler(LoginUtils loginUtils){
        SecurityContextLogoutHandler securityContextLogoutHandler = new SecurityContextLogoutHandler();
        CookieClearingLogoutHandler cookieClearingLogoutHandler = new CookieClearingLogoutHandler(AbstractRememberMeServices.SPRING_SECURITY_REMEMBER_ME_COOKIE_KEY);
        this.handler = new CompositeLogoutHandler(Arrays.asList(securityContextLogoutHandler, cookieClearingLogoutHandler));
        this.loginUtils = loginUtils;
    }

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        if(null != authentication) {
            if (authentication.getPrincipal() instanceof IUser) {
                IUser iUser = (IUser) authentication.getPrincipal();
                loginUtils.recordLogoutUsername(iUser.getUsername());
            }
        }
        handler.logout(request, response, authentication);
    }
}
