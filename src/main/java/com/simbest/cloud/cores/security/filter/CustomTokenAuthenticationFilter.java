package com.simbest.cloud.cores.security.filter;

import cn.hutool.jwt.JWTUtil;
import com.simbest.boot.security.IUser;
import com.simbest.cloud.cores.config.AppConfig;
import com.simbest.cloud.cores.security.handler.MyAuthenticationFailureHandler;
import com.simbest.cloud.cores.util.SpringUtils;
import com.simbest.cloud.cores.constants.AuthoritiesConstants;
import com.simbest.cloud.cores.oauth.util.AccessTokenUtils;
import com.simbest.cloud.cores.util.LoadUserUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * @description: 服务认证连接器
 * @author: yanqi
 * @date: 2023/11/15
 */
public class CustomTokenAuthenticationFilter extends OncePerRequestFilter {
    private AuthenticationFailureHandler failureHandler = new MyAuthenticationFailureHandler();


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            // 认证逻辑
            String authHeader = request.getHeader(AuthoritiesConstants.AUTH_HEADER);
            if (authHeader != null && authHeader.startsWith(AuthoritiesConstants.AUTH_HEADER_PREFIX)) {
                String authorizationToken = authHeader.substring(AuthoritiesConstants.AUTH_HEADER_PREFIX_LENGTH);
                // 这里调用 Token 验证逻辑
                AppConfig appConfig = SpringUtils.getBean(AppConfig.class);
                boolean verifyResult = false;
                try {
                    verifyResult = AccessTokenUtils.verifyAccessToken(authorizationToken, appConfig.getCloudPublicKey());
                } catch (Exception e) {
                    failureHandler.onAuthenticationFailure(request, response, new AuthenticationException("请求登陆已过期，请重新登陆") {
                    });
                    return;
                }
                //如果验签成功，则返回一个OAuth2Authentication对象
                if (verifyResult) {
                    if (SecurityContextHolder.getContext().getAuthentication() == null) {
                        String username = JWTUtil.parseToken(authorizationToken).getPayload().getClaim(AuthoritiesConstants.OAUTH2_SUB).toString();
                        IUser user = LoadUserUtils.loadUser(username);
                        UsernamePasswordAuthenticationToken authentication =
                                new UsernamePasswordAuthenticationToken(user, username, user.getAuthorities());
                        authentication.setDetails(user);
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    }
                } else {
                    failureHandler.onAuthenticationFailure(request, response, new AuthenticationException("请求登陆已过期，请重新登陆") {
                    });
                    return;
                }
            }
        } catch (AuthenticationException ex) {
            // 认证失败处理
            failureHandler.onAuthenticationFailure(request, response, ex);
            return;
        }


        try {
            filterChain.doFilter(request, response);
        } finally {
            // 在请求完成后清理安全上下文，以防止内存泄露
            SecurityContextHolder.clearContext();
        }
    }
}