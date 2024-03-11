package com.simbest.cloud.cores.security.config;

import com.simbest.cloud.cores.oauth.authorization.WebMvcAuthorizationManager;
import com.simbest.cloud.cores.security.filter.CustomTokenAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

/**
 * @description: 应用服务的鉴权管理器配置类
 * @author: yanqi
 * @date: 2023/11/28
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(jsr250Enabled = true, securedEnabled = true)
public class AuthorizationClientConfig {

    @Bean
    public WebMvcAuthorizationManager webMvcAuthorizationManager() {
        return new WebMvcAuthorizationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)
            throws Exception {
        //uri放行
        String[] ignoreUrls = new String[]{"/*.html", "/favicon.ico", "/webjars/**", "/*/v3/api-docs**", "/v3/api-docs/**","/oauth2/**","/swagger-resources/**",
                "/swagger-ui/**"};
        http
                .addFilterBefore(new CustomTokenAuthenticationFilter(), BasicAuthenticationFilter.class)
                .authorizeHttpRequests(authorize ->
                        authorize.requestMatchers(ignoreUrls)
                                .permitAll()
                                // 鉴权管理器配置
                                .anyRequest().access(webMvcAuthorizationManager())
                );
        http.csrf(AbstractHttpConfigurer::disable);
        return http.build();
    }

}