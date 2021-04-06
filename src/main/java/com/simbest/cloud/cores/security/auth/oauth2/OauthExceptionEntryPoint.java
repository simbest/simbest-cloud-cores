/*
 * 版权所有 © 北京晟壁科技有限公司 2008-2027。保留一切权利!
 */
package com.simbest.cloud.cores.security.auth.oauth2;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.simbest.cloud.orguser.pojo.JsonResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.provider.error.OAuth2AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 用途：自定义OAUTH2受保护的资源请求错误入口
 * 作者: lishuyi@simbest.com.cn
 * 时间: 2021/3/10  14:36
 */
@Slf4j
public class OauthExceptionEntryPoint extends OAuth2AuthenticationEntryPoint {

    public static final String SHOUD_LOGIN_1 = "Full authentication is required to access this resource";

    public static final String SHOUD_LOGIN_2 = "Invalid refresh token:";

    public static final String SHOUD_REFRESH = "Invalid access token:";

    /**
     * 与CustomOauthException配合
     *
     * @param request
     * @param response
     * @param authException
     * @throws ServletException
     */
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException)
            throws ServletException {
        response.setContentType("application/json");
        JsonResponse jsonResponse = JsonResponse.fail(authException.getMessage());
        if (SHOUD_LOGIN_1.equals(authException.getMessage()) ||
                authException.getMessage().startsWith(SHOUD_LOGIN_2)) {
            jsonResponse.setErrcode(HttpStatus.UNAUTHORIZED.value());
            jsonResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
        } else if (authException.getMessage().startsWith(SHOUD_REFRESH)) {
            jsonResponse.setErrcode(HttpStatus.FORBIDDEN.value());
            jsonResponse.setStatus(HttpStatus.FORBIDDEN.value());
            response.setStatus(HttpStatus.FORBIDDEN.value());
        }
        log.warn("OAUTH2方式无权限访问【{}】，状态码【{}】，错误信息【{}】", request.getRequestURI(), jsonResponse.getStatus(), authException.getMessage());
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.writeValue(response.getOutputStream(), jsonResponse);
        } catch (Exception e) {
            throw new ServletException();
        }
    }
}
