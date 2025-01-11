package com.simbest.cloud.cores.redis;

import com.simbest.cloud.cores.utils.DateUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.concurrent.TimeUnit;

@Slf4j
public class RedisJwtValidator {

    public static final String OAUTH21_JWT = "oauth21:jwt:";

    public static Long getJwtExpiryDate(String subject, String jwtId){
        String jwtKey = OAUTH21_JWT.format("%s:%s", subject, jwtId);
        Date expiryCache = RedisUtil.getBean(jwtKey, Date.class);
        if(null == expiryCache){
            log.trace("令牌信息为【{}】到期时间为空", jwtKey);
        }
        else {
            log.trace("令牌信息为【{}】到期时间为【{}】", jwtKey, DateUtil.getTimestamp(expiryCache));
        }
        return null == expiryCache ? null : expiryCache.getTime();
    }

    public static void setJwtExpiryDate(String subject, String jwtId, Long now, int seconds){
        RedisUtil.setEx(OAUTH21_JWT.format("%s:%s", subject, jwtId), now.toString(), seconds, TimeUnit.SECONDS);
    }

    public static void main(String[] args) {
        Date now = new Date();
        System.out.println(now);
        Long time = now.getTime();
        System.out.println(time.toString());
        Date now1 = new Date(time);
        System.out.println(now1);
        Date now2 = new Date(time);
        System.out.println(now2);
    }

}
