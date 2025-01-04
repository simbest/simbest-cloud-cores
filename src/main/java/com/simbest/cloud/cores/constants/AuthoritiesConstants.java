/*
 * 版权所有 © 北京晟壁科技有限公司 2008-2027。保留一切权利!
 */
package com.simbest.cloud.cores.constants;

/**
 * 用途：定义权限相关常量
 * 作者: lishuyi
 * 时间: 2018/2/6  17:11
 */
public class AuthoritiesConstants {
    public static final String SUPER_ADMIN = "ROLE_SUPER";
    public static final String ROLE_ADMIN = "ROLE_ADMIN";
    public static final String USER = "ROLE_USER";
    public static final String ANONYMOUS = "ROLE_ANONYMOUS";
    public static final String ROLE_PREFIX = "ROLE_";
    public static final String ROLE = "role";
    public static final String PERMISSION = "permission";
    public static final String PERMISSION_UPDATE = "PERMISSION_UPDATE";
    public static final String PERMISSION_DELETE = "PERMISSION_DELETE";
    public static final String PERMISSION_CREATOR = "creator";
    public static final String PERMISSION_MODIFIER = "modifier";
    public static final String SSO_UUMS_USERNAME = "username";
    public static final String SSO_UUMS_PASSWORD = "password";
    public static final String SSO_API_KEYWORD = "keyword";
    public static final String SSO_API_KEYTYPE = "keytype";
    public static final String SSO_API_USERNAME = "loginuser";
    public static final String SSO_API_APP_CODE = "appcode";
    public static final String SSO_API_UID = "uid";
    public static final String SSO_AUTH_HADMIN = "hadmin";


    public static final int PASSWORD_SALT_LENGTH = 12;
    public static final int ATTEMPT_LOGIN_INIT_TIMES = 1;
    public static final int ATTEMPT_LOGIN_MAX_TIMES = 5;
    public static final int ATTEMPT_LOGIN_FAILED_WAIT_SECONDS = 60 * 5;
    public static final String LOGIN_FAILED_KEY = "LOGIN_FAILED:";

    public static final String ACCESS_FORBIDDEN = "权限不足，禁止访问!";
    public static final String BUSINESS_FORBIDDEN = "业务禁止访问!";

    public static final String OAUTH2_CODE_ERROR = "0-认证授权错误";  //实则账
    public static final String LOGIN_NOT_EXIST_USER = "1-账号或密码异常";  //实则账户不存在
    public static final String LOGIN_ERROR_CAPTCHA = "2-账号密码及验证码认证错误";  //实则验证码错误
    public static final String LOGIN_ERROR_PASSWORD = "3-账号密码认证错误"; //实则密码错误
    public static final String TOKEN_NOT_EXIST = "4-请求未携带访问令牌或系统内部错误";
    public static final String TOKEN_IS_EXPIRED = "5-访问令牌已过期";
    public static final String AccountExpiredException = "账户已到期";
    public static final String DisabledException = "账号已禁用";
    public static final String LockedException = "账号已锁定";
    public static final String CredentialsExpiredException = "账户密码已到期";
    public static final String LOGIN_ERROR_EXCEED_MAX_TIMES = "尝试登录错误超过最大次数";
    public static final String LOGIN_APP_UNREGISTER_GROUP = "用户未注册此应用访问群组";
    public static final String AttempMaxLoginFaildException = "错误登录超过" + ATTEMPT_LOGIN_MAX_TIMES + "次，锁定" + ATTEMPT_LOGIN_FAILED_WAIT_SECONDS / 60 + "分钟";
    public static final String InternalAuthenticationServiceException = "权限认证错误";

    public static final String OAUTH2_UNKNOW_CLIENT = "错误的客户端：";

    public static final String OAUTH2_SUB = "sub";
    public static final String AUTH_HEADER = "Authorization";
    public static final String AUTH_HEADER_PREFIX = "Bearer ";

    public static final Integer AUTH_HEADER_PREFIX_LENGTH = 7;
}
