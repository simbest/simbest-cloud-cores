/*
 * 版权所有 © 北京晟壁科技有限公司 2008-2027。保留一切权利!
 */
package com.simbest.cloud.cores.security.auth.handles;

import com.simbest.cloud.cores.utils.security.LoginUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 用途：登录、登出处理器配置
 * 作者: lishuyi@simbest.com.cn
 * 时间: 2021/3/16  15:29
 */
@Configuration
public class AuthHandleConfiguration {

    @Autowired
    private LoginUtils loginUtils;

    @Bean
    public DefaultLogoutHandler defaultLogoutHandler(){
        return new DefaultLogoutHandler(loginUtils);
    }

    /**
     * 配置缺省的登录成功处理器--重定向方式
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(value = SuccessLoginRedirectHandler.class)
    public SuccessLoginRedirectHandler defaultSuccessLoginRedirectHandler() {
        return new SuccessLoginRedirectDefaultHandler(loginUtils);
    }

    /**
     * 配置缺省的登录成功处理器--Rest JSON数据返回方式
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(value = SuccessLoginRestHandler.class)
    public SuccessLoginRestHandler defaultSuccessLoginRestHandler() {
        return new SuccessLoginRestDefaultHandler(loginUtils);
    }

    /**
     * 配置缺省的登录失败处理器--重定向方式
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(value = FailedLoginRedirectHandler.class)
    public FailedLoginRedirectHandler defaultFailedLoginRedirectDefaultHandler() {
        return new FailedLoginRedirectDefaultHandler();
    }

    /**
     * 配置缺省的登录失败处理器--Rest JSON数据返回方式
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(value = FailedLoginRestHandler.class)
    public FailedLoginRestHandler defaultFailedLoginRestDefaultHandler() {
        return new FailedLoginRestDefaultHandler();
    }

    /**
     * 配置缺省的登出处理器--重定向方式
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(value = SuccessLogoutRedirectHandler.class)
    public SuccessLogoutRedirectHandler defaultSuccessLogoutRedirectHandler() {
        return new SuccessLogoutRedirectDefaultHandler(defaultLogoutHandler());
    }

    /**
     * 配置缺省的登出处理器--Rest JSON数据返回方式
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(value = SuccessLogoutRestHandler.class)
    public SuccessLogoutRestHandler defaultSuccessLogoutRestHandler() {
        return new SuccessLogoutRestDefaultHandler(defaultLogoutHandler());
    }


}
