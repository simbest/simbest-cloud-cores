//package com.simbest.cloud.cores.redis;
//
//import com.fasterxml.jackson.core.type.TypeReference;
//import org.apache.http.client.methods.CloseableHttpResponse;
//import org.apache.http.client.methods.HttpUriRequest;
//import org.apache.http.client.methods.RequestBuilder;
//import org.apache.http.entity.ContentType;
//import org.apache.http.entity.StringEntity;
//import org.apache.http.impl.client.CloseableHttpClient;
//import org.apache.http.impl.client.HttpClients;
//import org.apache.http.util.EntityUtils;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.redis.connection.*;
//import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
//import org.springframework.data.redis.serializer.RedisSerializer;
//import org.springframework.data.redis.serializer.StringRedisSerializer;
//import org.springframework.util.Assert;
//import org.springframework.util.StringUtils;
//
//import java.util.Arrays;
//import java.util.HashSet;
//import java.util.List;
//import java.util.Set;
//
//@Configuration
//public class RedisTemplateConfig {
//
//    @Bean
//    @Qualifier("redisTemplate")
//    public <T> RedisTemplate<String, T> redisTemplate() {
//        /**
//         * 解决分离项目报空指针问题
//         * 参考：https://www.jianshu.com/p/32d38a7fd20a
//         */
//        ClassLoader classLoader = this.getClass().getClassLoader();
//        RedisTemplate<String, T> template = new RedisTemplate<>();
//        template.setConnectionFactory(redisConnectionFactory());
//        template.setKeySerializer(new StringRedisSerializer());
//        template.setValueSerializer(new JdkSerializationRedisSerializer(classLoader));
//        template.setHashKeySerializer(new JdkSerializationRedisSerializer(classLoader));
//        template.setHashValueSerializer(new JdkSerializationRedisSerializer(classLoader));
//        template.setDefaultSerializer(new JdkSerializationRedisSerializer(classLoader));
//        template.afterPropertiesSet();
//        return template;
//    }
//
//    @Bean
//    public RedisConnectionFactory redisConnectionFactory() {
//        LettuceConnectionFactory factory = new LettuceConnectionFactory();
//        factory.setHostName("localhost");
//        factory.setPort(6379);
//        factory.setPassword("123456");
//        factory.setDatabase(0);
//        factory.afterPropertiesSet();
//        return factory;
//    }
//}
