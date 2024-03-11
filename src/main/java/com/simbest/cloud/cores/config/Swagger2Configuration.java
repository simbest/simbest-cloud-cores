/*
 * 版权所有 © 北京晟壁科技有限公司 2008-2027。保留一切权利!
 */
package com.simbest.cloud.cores.config;


import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;


/**
 * 用途：RESTFUL API 在线文档
 * 访问路径： http://localhost:8001/uums/swagger-ui.html
 * 作者: lishuyi
 * 时间: 2018/3/7  14:39
 */
@Configuration
@Profile(value = {"dev", "test", "uat"})
public class Swagger2Configuration {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info().title("API在线文档")
                        .description("SpringBoot3 Swagger3")
                        .version("v1.0.0"))
                .externalDocs(new ExternalDocumentation());
    }




}
