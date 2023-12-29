package com.simbest.cloud.cores.oauth.authorization;

import com.simbest.cloud.cores.config.AppConfig;
import com.simbest.cloud.cores.util.SpringUtils;
import com.simbest.cloud.cores.oauth.util.AccessTokenUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.util.StringUtils;

import java.util.function.Supplier;

/**
 * @description: 应用服务的鉴权管理器
 * @author: yanqi
 * @date: 2023/11/28
 */
public class WebMvcAuthorizationManager implements AuthorizationManager<RequestAuthorizationContext> {

    private final Log logger = LogFactory.getLog(getClass());

    @Override
    public AuthorizationDecision check(Supplier<Authentication> authentication, RequestAuthorizationContext object) {
        HttpServletRequest request = object.getRequest();
        String authorizationToken = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (!StringUtils.hasText(authorizationToken)) {
            return new AuthorizationDecision(false);
        }
        AppConfig appConfig = SpringUtils.getBean(AppConfig.class);

        boolean verifyResult = false;
        try {
            verifyResult = AccessTokenUtils.verifyAccessToken(authorizationToken, appConfig.getCloudPublicKey());
        } catch (Exception e) {
            return new AuthorizationDecision(false);
        }
        if (!verifyResult) {
            return new AuthorizationDecision(false);
        }

        return new AuthorizationDecision(true);
    }
}