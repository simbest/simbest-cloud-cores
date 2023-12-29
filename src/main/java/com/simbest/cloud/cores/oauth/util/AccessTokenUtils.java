package com.simbest.cloud.cores.oauth.util;

import cn.hutool.core.codec.Base64Decoder;
import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTHeader;
import cn.hutool.jwt.JWTPayload;
import cn.hutool.jwt.JWTUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

/**
 * @description: 验签工具类
 * @author: yanqi
 * @date: 2023/11/28
 */
public class AccessTokenUtils {

    private final static Log logger = LogFactory.getLog(AccessTokenUtils.class);

    /**
     * 随机数分隔符
     */
    public static final String RDM_SEP = "#";


    /**
     * accessToken 签名
     *
     * @param claims
     * @param privateKey
     * @return
     */
    public static JwtClaimsSet.Builder signAccessToken(JwsHeader.Builder headers, JwtClaimsSet.Builder claims, String privateKey) {
        String uuIdStr = UUID.randomUUID().toString();
        String alg = headers.build().getHeader("alg").toString();
        Map<String, Object> dataClaims = claims.build().getClaims();
        String sub = dataClaims.get("sub").toString();
        long currentTimeMillis = System.currentTimeMillis();
        String rdmSource = uuIdStr + RDM_SEP + claims.build().getExpiresAt().getEpochSecond() + RDM_SEP + currentTimeMillis;
        String rdmTarget = RSAUtils.encryptByPriKey(rdmSource, privateKey);
        String signSource = uuIdStr + RDM_SEP + currentTimeMillis + alg + sub;
        String signature = "";
        try {
            signature = RSAUtils.sign(signSource.getBytes(), Base64Decoder.decode(privateKey));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        claims.claim("rdm", rdmTarget);
        claims.claim("sign", signature);


        return claims;
    }

    /**
     * AccessToken 验签
     *
     * @param authorizationToken
     * @param publicKey
     * @return
     */
    public static boolean verifyAccessToken(String authorizationToken, String publicKey) {

        String accessToken = authorizationToken.replace("Bearer ", "");
        JWT jwtToken = JWTUtil.parseToken(accessToken);
        JWTPayload jwtPayload = jwtToken.getPayload();
        JWTHeader jwtHeader = jwtToken.getHeader();

        String rdm = jwtPayload.getClaim("rdm") + "";
        String sign = jwtPayload.getClaim("sign") + "";
        //解密
        String rdmSource = "";
        try {
            rdmSource = RSAUtils.decryptByPubKey(rdm, publicKey);
        } catch (Exception e) {
            logger.info("rdm解密失败，rdm=" + rdm);
        }
        String[] rdmData = rdmSource.split(RDM_SEP);
        long exp = Long.parseLong(rdmData[1]);
        long now = Instant.now().getEpochSecond();
        if (exp < now) {
            logger.info("accessToken已过期，exp=" + exp);
            return false;
        }
        String signSource = rdmData[0] + RDM_SEP + rdmData[2] + jwtHeader.getClaim("alg") + jwtPayload.getClaim("sub");

        //验签
        boolean verifyResult = false;
        try {
            verifyResult = RSAUtils.verify(signSource.getBytes(), Base64Decoder.decode(sign), Base64Decoder.decode(publicKey));
        } catch (Exception e) {
            logger.info("accessToken验签异常，accessToken=" + accessToken, e);
        }
        if (!verifyResult) {
            logger.info("accessToken验签不通过，accessToken=" + accessToken);
            return false;
        }

        return true;
    }

    public static void main(String[] args) {
        String token = "eyJraWQiOiI0YmJmMjNkYS03Nzg4LTQ1ZjUtOTE5YS05YmJmYjNjNjA5ZWMiLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJ3YW5nYW8iLCJhdWQiOiJnYXRld2F5LWNsaWVudC1pZCIsIm5iZiI6MTcwMzA1MTgzMywic2NvcGUiOlsiUk9MRV9HV0JNX1JDU0MiLCJEUFJFVkVOVF9ST0xFX0lOU1BFQ1RPUiIsIlJPTEVfU1lfWUdaWiIsIlJPTEVfWkRHTF9BRE1JTiIsIkNESVNLX1JPTEVfR0VORVJBTF9VU0VSIiwiSEdHTF9KSkhHR0xZIiwiUk9MRV9ZV1pEIiwiUk9MRV9VU0VSIiwiU1BTQ19ST0xFX1BST1ZJTkNFIiwiUk9MRV9QUk9WSU5DRV9DT01QQU5ZX0FMTFVTRVJfTk8iLCJST0xFX1NIRU5HIiwidXNlcmluZm8iLCJST0xFX0dFTkVSQUxfU1RBRkYiLCJDTFlYVFpfU0dTIiwiUk9MRV9ZWlpYR1oiLCJESUNULTAzLTAwNSIsIlJPTEVfempyY3hiIiwiUk9MRV9KR0pQX09USEVSIiwiYWRvcC1BLWdlbmVyYWwtbWFuYWdlIiwiemh4Y18wMDgiLCJobGdqXzAzIiwiUk9MRV9ZR01ZX0NQIiwiUk9MRV9DUF9TWV9ZR1paIiwiUk9MRV9QUk9WSU5DRV9DT01QQU5ZX0FMTFVTRVIiLCJZR0xUX0FETUlOX0xBTiIsIkZEWkxfWk9ORV9BRE1JTiIsIlJPTEVfVVNFUl9OTyIsIlJPTEVfNUdfQ0ciXSwiaXNzIjoiIiwic2lnbiI6Ik81LzA1VlYrc2dqL1NHWkdiRVFucDZtVGRITm5EbjBYd201R3Aybm1JT0pJTFpvMTZsUlgva1JDTGladytyYXZ6RDdIN204UWNsemg3TFhhZ3p1MjJCR3p1UVNyUzk2R2dwWWhJMHM5SHMvRnIyM1cxb3ptRTVHaThCeng3UmFSYmh3bXF5aEJnWDBxQ0M4cHBCZENMM21qRm81cC9CQ1gwbngxNWZrcm8xUT0iLCJyZG0iOiJpSDV2UFBsbDU5cU1Udm40ZExocDJwcUFldmRESWRDTEVxNjBheU83eUYxRnZDaEJJaWFJbVQ5OHV4cjN4VlE5ZVBLZmM3Ynd0K0dBblR1SkNyZ2lJMTgyWVRkb0tualdMYmFsNzRQcmtZR0dSVUZzM0lkUHdnWmFhT1lLaDVvcnBMSEFucS84VGN0ZGFsWDZ3ei9NNWtZM2EzYUp3NUI0L3VoRlduQ0VJSm89IiwiZXhwIjoxNzAzMDU3ODMzLCJpYXQiOjE3MDMwNTE4MzN9.EDQ7VJZKrW2mYLZUjEsuSJ0umvrbUzjgiAGZrkTrc7NmOvpWChFLYo_ow-fcXFU4R5cwmDnK8bmcRj9mwJmAi2UatrFLVOjevxktWhvm1C_qNEfL4bdfmwuBoO2xPTlrV5nRaNYle1WB2rty0ladosxNTpWpMpBrYk-8QjhlufigNPB5b99IZ2Tm2Us72249vPCkEarEl4OWMpsYq2WwaC4cEW8NukkqiOHpy9cFMSQ3twfoFtRgw84iAF4RIG1HLeAqKsKP7IyAx3DaY2HVsLWhVqLnXjbtcWH_mFuSH1wI4pobgxwC8W6kWw5FrlqFVnZ9elW0NCWXhjahYvo9ag";
        String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDI90D8sRwU5rwagj9fgNQAFH/Ws03jR+qYUUtA3iI05IaqDLrVDdvMQU446/6c+nyJBtdO3P95+dLg7UVQn1bQSj1wLWa5nuJvTh5paBe1XWZj/HmISTpq+OhyGKmX5xNRU96fDld03JyrgEbmHb9T8jks7g5FhKmZmLJBeRTpoQIDAQAB";
        boolean b = verifyAccessToken(token, publicKey);
        System.out.println(b);
    }

}
