package com.simbest.cloud.cores.redis;

import com.simbest.cloud.cores.json.JacksonUtils;
import com.simbest.cloud.cores.utils.DateUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.concurrent.TimeUnit;

@Slf4j
public class RedisJwtValidator {

    public static final String OAUTH21_JWT = "oauth21:jwt:";

    public static Date getJwtExpiryDate(String subject, String jwtId){
        String jwtKey = OAUTH21_JWT.format("%s:%s", subject, jwtId);
        Date expiryCache = RedisUtil.getBean(jwtKey, Date.class);
        if(null == expiryCache){
            log.trace("令牌信息为【{}】到期时间为空", jwtKey);
        }
        else {
            log.trace("令牌信息为【{}】到期时间为【{}】", jwtKey, DateUtil.getTimestamp(expiryCache));
        }
        return expiryCache;
    }

    public static void setJwtExpiryDate(String subject, String jwtId, Date now, int seconds){
        RedisUtil.setEx(OAUTH21_JWT.format("%s:%s", subject, jwtId), JacksonUtils.obj2json(now), seconds, TimeUnit.SECONDS);
    }

}
