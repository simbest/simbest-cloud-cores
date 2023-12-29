//package com.simbest.core.redis;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.redis.connection.RedisConnectionFactory;
//import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.data.redis.core.StringRedisTemplate;
//import org.springframework.data.redis.serializer.RedisSerializer;
//import org.springframework.data.redis.serializer.StringRedisSerializer;
//
//@Configuration
//public class RedisConfig {
////
////    /**
////     * RedisTemplate模板
////     */
////    @Bean("redisTemplate")
////    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
////        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
////        redisTemplate.setConnectionFactory(factory);
////        redisTemplate.setKeySerializer(new StringRedisSerializer());
////        return redisTemplate;
////    }
////
////    /**
////     * StringRedisTemplate模板
////     */
////    @Bean
////    public StringRedisTemplate stringRedisTemplate(RedisConnectionFactory factory) {
////        StringRedisTemplate stringRedisTemplate = new StringRedisTemplate();
////        stringRedisTemplate.setConnectionFactory(factory);
////        stringRedisTemplate.setKeySerializer(new StringRedisSerializer());
////        return stringRedisTemplate;
////    }
//
//
//    @Bean
//    public RedisTemplate<String, Object> redisTemplate(LettuceConnectionFactory lettuceConnectionFactory) {
//        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
//        redisTemplate.setConnectionFactory(lettuceConnectionFactory);
//        // 设置key序列化方式string，RedisSerializer.string() 等价于 new StringRedisSerializer()
//        redisTemplate.setKeySerializer(RedisSerializer.string());
//        // 设置value的序列化方式json，使用GenericJackson2JsonRedisSerializer替换默认序列化，RedisSerializer.json() 等价于 new GenericJackson2JsonRedisSerializer()
//        redisTemplate.setValueSerializer(RedisSerializer.json());
//        // 设置hash的key的序列化方式
//        redisTemplate.setHashKeySerializer(RedisSerializer.string());
//        // 设置hash的value的序列化方式
//        redisTemplate.setHashValueSerializer(RedisSerializer.json());
//        // 使配置生效
//        redisTemplate.afterPropertiesSet();
//        return redisTemplate;
//    }
//}