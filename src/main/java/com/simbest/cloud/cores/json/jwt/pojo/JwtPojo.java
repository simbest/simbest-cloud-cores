package com.simbest.cloud.cores.json.jwt.pojo;

import lombok.Data;

import java.util.Map;

@Data
public class JwtPojo {
    private String tokenValue;

    private String issuedAt;

    private String expiresAt;

    private Map<String, Object> headers;

    private Map<String, Object> claims;
}
