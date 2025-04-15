package com.simbest.cloud.cores.constants;

public class CloudAuthConstants {
    public static final String OAUTH21_SUB = "sub";
    public static final String OAUTH21_ALG = "alg";
    public static final String OAUTH21_AUTHORIZATION = "Authorization";
    public static final String OAUTH21_AUTHORIZATION_PREFIX = "Bearer ";
    public static final String OAUTH21_JWT = "oauth21:jwt:";
    public static final String OAUTH21_SUBJECT = "subject";
    public static final String OAUTH21_JWTID = "jwtId";


    // OAuth 2.1 API地址
    // 应用到auth统一认证中心中检查JWT过期时间
//    public static final String OAUTH21_CHECK_JWT_EXPIRE_DATETIME = "http://gateway/gateway/%s/jwt/getJwtExpiryDate";
    public static final String OAUTH21_CHECK_JWT_EXPIRE_DATETIME = "/%s/jwt/anonymous/getJwtExpiryDate";


}
