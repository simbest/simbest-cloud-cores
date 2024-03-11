package com.simbest.cloud.cores.util;

import jakarta.annotation.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.Locale;
import java.util.Map;

@Component
public class SpringContextUtil {

    @Autowired
    private ApplicationContext context;

    // 国际化使用
    public String getMessage(String key) {
        return context.getMessage(key, null, Locale.getDefault());
    }

    /// 获取当前环境
    public String getActiveProfile() {
        return context.getEnvironment().getActiveProfiles()[0];
    }

    // 获取Bean
    public <T> T getBean(String beanName, @Nullable Class<T> requiredType) {
        return  context.getBean(beanName, requiredType);
    }

    public <T> T getBean(Class<T> requiredType) {
        return context.getBean(requiredType);
    }

    public <T> Map<String, T> getBeansOfType(@Nullable Class<T> type) {
        return context.getBeansOfType(type);
    }
}