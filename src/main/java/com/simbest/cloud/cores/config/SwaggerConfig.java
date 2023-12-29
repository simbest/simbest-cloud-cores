package com.simbest.cloud.cores.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.OAuthFlow;
import io.swagger.v3.oas.models.security.OAuthFlows;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;

/**
 * Swagger 配置类
 * <p>
 * 基于 OpenAPI 3.0 规范 + SpringDoc 实现 + knife4j 增强
 *
 * @author yanqi
 */
@Configuration
public class SwaggerConfig {

    /**
     * OAuth2 认证 endpoint
     */
//    @Value("${spring.security.oauth2.authorizationserver.token-uri}")
    private String tokenUrl = "http://localhost:9001/oauth2/token";

    /**
     * OpenAPI 配置（元信息、安全协议）
     */
    @Bean
    public OpenAPI apiInfo() {
        return new OpenAPI()
                .components(new Components()
                        .addSecuritySchemes(HttpHeaders.AUTHORIZATION,
                                new SecurityScheme()
                                        // OAuth2 授权模式
                                        .type(SecurityScheme.Type.OAUTH2)
                                        .name(HttpHeaders.AUTHORIZATION)
                                        .flows(new OAuthFlows()
                                                .password(
                                                        new OAuthFlow()
                                                                .tokenUrl(tokenUrl)
                                                                .refreshUrl(tokenUrl)
                                                )
                                        )
                                        // 安全模式使用Bearer令牌（即JWT）
                                        .in(SecurityScheme.In.HEADER)
                                        .scheme("Bearer")
                                        .bearerFormat("JWT")
                        )
                )
                // 接口全局添加 Authorization 参数
                .addSecurityItem(new SecurityRequirement().addList(HttpHeaders.AUTHORIZATION))
                // 接口信息定义
                .info(new Info()
                        .title("系统服务")
                        .version("3.0.0")
                        .description("用户、角色、菜单、部门、字典等接口")
                        .license(new License().name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0"))
                );
    }


}
