package com.simbest.cloud.cores.oauth2.handler;

import com.simbest.cloud.cores.json.ResponseUtils;
import com.simbest.cloud.cores.response.JsonResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;

import static com.simbest.cloud.cores.constants.AuthoritiesConstants.TOKEN_AUTH_UNKNOWN;
import static com.simbest.cloud.cores.constants.AuthoritiesConstants.TOKEN_NOT_EXIST;

/**
 * 未携带令牌访问失败处理类 JSON形式返回未授权
 */
public class JsonClientAuthenticationFailedEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        if(authException instanceof InsufficientAuthenticationException){
            ResponseUtils.buildResponse(response, JsonResponse.fail(TOKEN_NOT_EXIST, authException.getMessage(), HttpStatus.UNAUTHORIZED.value()), HttpStatus.UNAUTHORIZED);
        }else{
            ResponseUtils.buildResponse(response, JsonResponse.fail(TOKEN_AUTH_UNKNOWN, authException.getMessage(), HttpStatus.UNAUTHORIZED.value()), HttpStatus.UNAUTHORIZED);
        }
    }

}
