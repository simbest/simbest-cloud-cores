package com.simbest.cloud.cores.oauth.authorization;

import com.simbest.cloud.cores.config.AppConfig;
import com.simbest.cloud.cores.util.SpringUtils;
import com.simbest.cloud.cores.oauth.util.AccessTokenUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.ReactiveAuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.authorization.AuthorizationContext;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @description: 创建网关服务鉴权管理器
 * @author: yanqi
 * @date: 2023/11/28
 */
public class WebFluxAuthorizationManager implements ReactiveAuthorizationManager<AuthorizationContext> {

    private final Log logger = LogFactory.getLog(getClass());

    @Override
    public Mono<AuthorizationDecision> check(Mono<Authentication> mono, AuthorizationContext authorizationContext) {

        ServerWebExchange exchange = authorizationContext.getExchange();

        // 从Header里取出token的值
        String authorizationToken = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (!StringUtils.hasText(authorizationToken)) {
            logger.warn("当前请求头Authorization中的值不存在");
            return Mono.just(new AuthorizationDecision(false));
        }
        AppConfig appConfig = SpringUtils.getBean(AppConfig.class);
        boolean verifyResult = AccessTokenUtils.verifyAccessToken(authorizationToken, appConfig.getCloudPublicKey());
        if (!verifyResult) {
            return Mono.just(new AuthorizationDecision(false));
        }

        return Mono.just(new AuthorizationDecision(true));
    }
}