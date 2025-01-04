package com.simbest.cloud.cores.constants;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class JwtOAuth21Constants {
    /**
     * redisToken前缀
     */
    public final static String CACHE_TOKEN_PREFIX = "jwt:";


    /**
     * authorization_id key
     */
    public static final String ID_TO_AUTHORIZATION = CACHE_TOKEN_PREFIX + "id_to_authorization:";

    /**
     * authorization_code key
     */
    public static final String CODE_TO_AUTHORIZATION = CACHE_TOKEN_PREFIX + "code_to_authorization:";

    /**
     * access_token key
     */
    public static final String ACCESS_TO_AUTHORIZATION = CACHE_TOKEN_PREFIX + "access_to_authorization:";

    /**
     * refresh_token key
     */
    public static final String REFRESH_TO_AUTHORIZATION = CACHE_TOKEN_PREFIX + "refresh_to_authorization:";

    /**
     * oidc_token key
     */
    public static final String ODIC_TO_AUTHORIZATION = CACHE_TOKEN_PREFIX + "oidc_to_authorization:";

    /**
     * oidc_token key
     */
    public static final String STATE_TO_AUTHORIZATION = CACHE_TOKEN_PREFIX + "state_to_authorization:";

    private static final MessageDigest DIGEST;

    static {
        try {
            DIGEST = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    protected static String generateKey(String rawKey) {
        byte[] bytes = DIGEST.digest(rawKey.getBytes(StandardCharsets.UTF_8));
        return String.format("%032x", new BigInteger(1, bytes));
    }

    public static String getIdToAuthorizationKey(String authorizationId) {
        return JwtOAuth21Constants.ID_TO_AUTHORIZATION + authorizationId;
    }

    public static String getCodeToAuthorization(String code) {
        return JwtOAuth21Constants.CODE_TO_AUTHORIZATION + generateKey(code);
    }

    public static String getAccessToAuthorization(String accessToken) {
        return JwtOAuth21Constants.ACCESS_TO_AUTHORIZATION + generateKey(accessToken);
    }

    public static String getRefreshToAuthorization(String refreshToken) {
        return JwtOAuth21Constants.REFRESH_TO_AUTHORIZATION + generateKey(refreshToken);
    }

    public static String getOidcToAuthorization(String oidcToken) {
        return JwtOAuth21Constants.ODIC_TO_AUTHORIZATION + generateKey(oidcToken);
    }

    public static String getStateToAuthorization(String stateToken) {
        return JwtOAuth21Constants.STATE_TO_AUTHORIZATION + generateKey(stateToken);
    }

}
