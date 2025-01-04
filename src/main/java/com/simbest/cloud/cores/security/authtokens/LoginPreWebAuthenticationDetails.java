package com.simbest.cloud.cores.security.authtokens;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * 认证登录前Authentication的detail，用于记录登录认证时客户端IP地址，判断授权码是否可用。 由UumsAuthenticationFilter和 RsaAuthenticationFilter进行处理
 */
@Data
@Builder
public class LoginPreWebAuthenticationDetails implements Serializable {

    private String remoteAddress;

    private String sessionId;

}
