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

import static com.simbest.cloud.cores.constants.AuthoritiesConstants.TOKEN_NOT_EXIST;

public class JsonClientAuthenticationFailedEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        if(authException instanceof InsufficientAuthenticationException){
            ResponseUtils.buildResponse(response, JsonResponse.fail(TOKEN_NOT_EXIST), HttpStatus.UNAUTHORIZED);
        }else{
            ResponseUtils.buildResponse(response, JsonResponse.fail(authException.getMessage()), HttpStatus.UNAUTHORIZED);
        }
    }

}
