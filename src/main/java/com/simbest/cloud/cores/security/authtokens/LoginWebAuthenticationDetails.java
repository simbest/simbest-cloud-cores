package com.simbest.cloud.cores.security.authtokens;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * 认证登录后Authentication的detail，PC是GenericAuthenticationChecker处理，OAuth2是UumsTokenGranter处理
 */
@Data
@Builder
public class LoginWebAuthenticationDetails implements Serializable {

    public static final String APPLY_USERNAME = "applyUsername";

    // PC 登录是WebAuthenticationDetails， 由GenericAuthenticationChecker封装传参
    // OAuth2登录是java.util.LinkedHashMap，由自定义UumsTokenGranter的getOAuth2Authentication方法封装传参
    private Object details;

    //当前只有一个属性，用于记录登录的授权申请账号，后续可随时扩展
    private String applyUsername;



}
