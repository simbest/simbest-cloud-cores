/*
 * 版权所有 © 北京晟壁科技有限公司 2008-2027。保留一切权利!
 */
package com.simbest.cloud.cores.security.auth.config;

import com.simbest.cloud.cores.constants.AuthoritiesConstants;
import com.simbest.cloud.cores.security.auth.providers.CustomDaoAuthenticationProvider;
import com.simbest.cloud.orguser.service.IAuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


/**
 * 用途：配置多套 HttpSecurity
 * 作者: lishuyi@simbest.com.cn
 * 时间: 2021/3/9  19:07
 */
@Slf4j
@EnableWebSecurity
public class MultiHttpSecurityConfig {
    @Autowired
    private ApplicationContext appContext;

    @Autowired
    private IAuthService authService;

    @Bean
    public PasswordEncoder myBCryptPasswordEncoder() {
        // 默认密码加密长度12
        // 参考：http://zhjwpku.com/2017/11/30/bcrypt-in-spring-security.html
        return new BCryptPasswordEncoder(AuthoritiesConstants.PASSWORD_SALT_LENGTH);
    }

    @Bean
    public AuthenticationProvider jdbcAuthenticationProvider() {
        CustomDaoAuthenticationProvider impl = new CustomDaoAuthenticationProvider();
        impl.setUserDetailsService(authService);
        impl.setPasswordEncoder(myBCryptPasswordEncoder());
        impl.setHideUserNotFoundExceptions(true);
        return impl;
    }

    /**
     * 配置认证管理器
     *
     * @param auth 认证管理器构造器AuthenticationManagerBuilder
     * @throws Exception 异常
     */
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) {
        //基于用户名和密码验证
        auth.authenticationProvider(jdbcAuthenticationProvider());
    }

}
