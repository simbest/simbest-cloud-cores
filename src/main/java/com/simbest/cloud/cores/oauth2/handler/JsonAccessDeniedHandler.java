package com.simbest.cloud.cores.oauth2.handler;

import com.simbest.cloud.cores.json.ResponseUtils;
import com.simbest.cloud.cores.response.JsonResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;

import static com.simbest.cloud.cores.constants.AuthoritiesConstants.TOKEN_AUTH_UNKNOWN;

public class JsonAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        SecurityContextHolder.clearContext();
        OAuth2Error errorResponse = new OAuth2Error(accessDeniedException.getMessage());
        JsonResponse result = JsonResponse.fail(errorResponse, TOKEN_AUTH_UNKNOWN, HttpStatus.UNAUTHORIZED.value());
        ResponseUtils.buildResponse(response, result, HttpStatus.UNAUTHORIZED);
    }
}
