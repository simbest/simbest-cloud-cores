/*
 * 版权所有 © 北京晟壁科技有限公司 2008-2027。保留一切权利!
 */
package com.simbest.cloud.cores.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.simbest.cloud.cores.constants.ApplicationConstants;
import com.simbest.cloud.cores.constants.converter.StringToDateConverter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.HttpMethod;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.resource.ContentVersionStrategy;
import org.springframework.web.servlet.resource.VersionResourceResolver;

import java.util.List;

import static org.springframework.web.cors.CorsConfiguration.ALL;

/**
 * 用途：Web MVC 配置
 * 作者: lishuyi
 * 时间: 2018/10/11  17:32
 */
@Configuration
public class WebMvcConfigSupport extends WebMvcConfigurationSupport {

    @Autowired
    private AppConfig appConfig;

    /**
     * 统一使用JacksonConfiguration的ObjectMapper
     */
    @Autowired
    private ObjectMapper objectMapper;

    @Bean
    public MappingJackson2HttpMessageConverter customJackson2HttpMessageConverter() {
        MappingJackson2HttpMessageConverter jsonConverter = new MappingJackson2HttpMessageConverter();
        jsonConverter.setObjectMapper(objectMapper);
        return jsonConverter;
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(customJackson2HttpMessageConverter());
        super.addDefaultHttpMessageConverters(converters);
    }

    /**
     * 添加自定义的Converters和Formatters.
     */
    @Override
    protected void addFormatters(FormatterRegistry registry) {
        //表单参数转换日期类型
        registry.addConverter(new StringToDateConverter());
    }

    /**
     * SpringBoot 实现前后端分离的跨域访问（CORS）
     * http://www.spring4all.com/article/177
     * https://blog.csdn.net/zhangyuxuan2/article/details/90446670
     * @param registry
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
//                .allowedOrigins(appConfig.getAppHostPort())
//                .allowedOrigins(ALL)
               // .allowedOrigins(StringUtils.split(appConfig.getAllowedOrigins(), ApplicationConstants.COMMA))
                .allowedOriginPatterns(StringUtils.split(appConfig.getAllowedOrigins(), ApplicationConstants.COMMA))
                .allowedMethods(HttpMethod.OPTIONS.name(), HttpMethod.GET.name(), HttpMethod.POST.name())
                .allowedHeaders(ALL)
                .allowCredentials(true)
                .maxAge(3600);
    }

    /**
     * （1）以下配置解决在templates目录下后缀为html的thymeleaf的静态资源缓存问题，通过文件内容进行哈希动态重新命名文件路径，如：/js/jquery.min-6a4559f03dff81887a6af3fb89ae2db1.js
     * 由于设置了setCachePeriod(-1)，因此静态资源不会缓存。如果需要实现缓存，则通过Nginx配置动静分离实现，参考配置如下：
     * 		#拦截静态资源
     * 		location ~* \.(gif|jpg|jpeg|bmp|png|ico|js|css)$ {
     * 		   root static;
     * 		   expires      30d;
     *      }
     *
     *  (2) 对于在static\html的普通HTML文件，应用资源时由于不判断th语法，因此以下配置不会生效，实现缓存需要通过svn.revision占位符通过MAVEN的svn-revision-number-maven-plugin插件，将静态路径替换svn版本号实现不缓存变更内容，如：
     *  <link href="../../fonts/iconfont/iconfont.css?v=${svn.revision}"
     * @param registry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        VersionResourceResolver versionResourceResolver = new VersionResourceResolver()
                .addVersionStrategy(new ContentVersionStrategy(), "/**");

        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");

        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");

        registry.addResourceHandler("/css/**").addResourceLocations("classpath:/static/css/")
                .setCachePeriod(-1).resourceChain(true).addResolver(versionResourceResolver);

        registry.addResourceHandler("/js/**").addResourceLocations("classpath:/static/js/")
                .setCachePeriod(-1).resourceChain(true).addResolver(versionResourceResolver);

        registry.addResourceHandler("/html/**").addResourceLocations("classpath:/static/html/")
                .setCachePeriod(-1).resourceChain(true).addResolver(versionResourceResolver);

        registry.addResourceHandler("/fonts/**").addResourceLocations("classpath:/static/fonts/")
                .setCachePeriod(-1).resourceChain(true).addResolver(versionResourceResolver);

        registry.addResourceHandler("/img/**", "/images/**", "/favicon.ico")
                .addResourceLocations("classpath:/static/img/", "classpath:/static/images/")
                .setCachePeriod(-1).resourceChain(true).addResolver(versionResourceResolver);

        registry.addResourceHandler("/**").addResourceLocations("classpath:/static/", "classpath:/public/")
                .setCachePeriod(-1).resourceChain(true).addResolver(versionResourceResolver);

        super.addResourceHandlers(registry);
    }
}
