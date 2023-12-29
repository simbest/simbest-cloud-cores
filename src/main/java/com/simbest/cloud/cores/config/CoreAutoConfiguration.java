package com.simbest.cloud.cores.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @description: 自动配置包扫描
 * @author: yanqi
 * @date: 2023/8/18
 */
@Configuration
@ComponentScan(basePackages = {"com.simbest.cloud.cores"})
public class CoreAutoConfiguration {
}
