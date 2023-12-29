package com.simbest.cloud.cores.oauth.util;

import cn.hutool.core.codec.Base64Decoder;
import cn.hutool.core.codec.Base64Encoder;

import javax.crypto.Cipher;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.time.Instant;
import java.util.UUID;

/**
 * @description: 创建 RSA 工具类 RSAUtils 和 access_token 签名
 * @author: yanqi
 * @date: 2023/11/28
 */
public class RSAUtils {

    /**
     * 公钥
     */
    public static final String publicKeyBase64Default = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDI90D8sRwU5rwagj9fgNQAFH/Ws03jR+qYUUtA3iI05IaqDLrVDdvMQU446/6c+nyJBtdO3P95+dLg7UVQn1bQSj1wLWa5nuJvTh5paBe1XWZj/HmISTpq+OhyGKmX5xNRU96fDld03JyrgEbmHb9T8jks7g5FhKmZmLJBeRTpoQIDAQAB";

    /**
     * 私钥
     */
    public static final String privateKeyBase64Default = "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAMj3QPyxHBTmvBqCP1+A1AAUf9azTeNH6phRS0DeIjTkhqoMutUN28xBTjjr/pz6fIkG107c/3n50uDtRVCfVtBKPXAtZrme4m9OHmloF7VdZmP8eYhJOmr46HIYqZfnE1FT3p8OV3TcnKuARuYdv1PyOSzuDkWEqZmYskF5FOmhAgMBAAECgYASt7S91GEhMTxj2627X2xfdYlSnfCrMo+PEENKD2ZwNri0LetY3KmUJKD8fD6CsHHY8WIsXjkNS09w06iZEb4sDf1PLluo+HJTZsRWBb7Wa+PEdVwVwjO8FFnsmO3G9E/ObWQdlxMLbJeXN1l4uqbhSpkO/aTo7o/CzbQLbxtyAQJBAOESrdrO1o6OW/ztwFqHv+n0FmN/knHiHjJt5ILZE8QJAqi/pxUIbXmIOIDhbFVE9Foq5arN70rn0uROavVanAkCQQDklJDZBEmPWfQRc+YHp5sVEwBhl9u+HlPRTIwfnNnxGaBRGHgCAWti9BRtFPK7aMLYYGngHCke4u9onH4kJvbZAkBRINGICHxwQEJKJkzqlPoJU5FqZgachDwMQ25V9/dW90R9HTAVtcb4QrDTS+4nwkYt5j6I1dhGcM+kudt1+yY5AkA7qZONMZNJuX79NzUpdlQCUi1dS9ftbdkO3l4MulIgnkG8KRjZ3Sj8cR0Lw9X/mL6S38eC2ZbaGv3GXmKGaS0xAkBXUk5oNzRizhKyZDPfuaCaKMKySVhY2si3OQkuRd7vBbuZT3hgIwQxSX6oti9DCjnxHWj6+Fu+gy7VoNQnx32U";

    /**
     * 数字签名，密钥算法
     */
    private static final String RSA_KEY_ALGORITHM = "RSA";

    /**
     * 数字签名签名/验证算法
     */
    private static final String SIGNATURE_ALGORITHM = "MD5withRSA";

    /**
     * 公钥 key
     */
    private static final String PUBLIC_KEY = "RSAPublicKey";

    /**
     * 私钥 key
     */
    private static final String PRIVATE_KEY = "RSAPrivateKey";

    /**
     * RSA密钥长度，RSA算法的默认密钥长度是1024密钥长度必须是64的倍数，在512到65536位之间
     */
    private static final int KEY_SIZE = 1024;




    /**
     *
     * @param data   加密前的字符串
     * @param publicKey 公钥
     * @return 加密后的字符串
     * @description  公钥加密
     */
    public static String encryptByPubKey(String data, String publicKey) throws Exception {
        byte[] pubKey = Base64Decoder.decode(publicKey);
        byte[] enSign = encryptByPubKey(data.getBytes(), pubKey);
        return Base64Encoder.encode(enSign);
    }

    /**
     *
     * @param data 待加密数据
     * @param pubKey 公钥
     * @return
     * @throws Exception
     * @description  公钥加密
     */
    public static byte[] encryptByPubKey(byte[] data, byte[] pubKey) throws Exception {
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(pubKey);
        KeyFactory keyFactory = KeyFactory.getInstance(RSA_KEY_ALGORITHM);
        PublicKey publicKey = keyFactory.generatePublic(x509KeySpec);
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        return cipher.doFinal(data);
    }

    /**
     *
     * @param text   加密前的字符串
     * @param privateKey 私钥
     * @return 加密后的字符串
     * @description  私钥加密
     */
    public static String encryptByPriKey(String text, String privateKey) {
        try {
            byte[] priKey = Base64Decoder.decode(privateKey);
            byte[] enSign = encryptByPriKey(text.getBytes(), priKey);
            return Base64Encoder.encode(enSign);
        } catch (Exception e) {
            throw new RuntimeException("加密字符串[" + text + "]时遇到异常", e);
        }
    }

    /**
     *
     * @param data 待加密的数据
     * @param priKey 私钥
     * @return 加密后的数据
     * @throws Exception
     * @description  私钥加密
     */
    public static byte[] encryptByPriKey(byte[] data, byte[] priKey) throws Exception {
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(priKey);
        KeyFactory keyFactory = KeyFactory.getInstance(RSA_KEY_ALGORITHM);
        PrivateKey privateKey = keyFactory.generatePrivate(pkcs8KeySpec);
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, privateKey);
        return cipher.doFinal(data);
    }

    /**
     *
     * @param data 待解密的数据
     * @param pubKey 公钥
     * @return 解密后的数据
     * @throws Exception
     * @description  公钥解密
     */
    public static byte[] decryptByPubKey(byte[] data, byte[] pubKey) throws Exception {
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(pubKey);
        KeyFactory keyFactory = KeyFactory.getInstance(RSA_KEY_ALGORITHM);
        PublicKey publicKey = keyFactory.generatePublic(x509KeySpec);
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, publicKey);
        return cipher.doFinal(data);
    }

    /**
     *
     * @param data    解密前的字符串
     * @param publicKey 公钥
     * @return 解密后的字符串
     * @throws Exception
     * @description  公钥解密
     */
    public static String decryptByPubKey(String data, String publicKey) throws Exception {
        byte[] pubKey = Base64Decoder.decode(publicKey);;
        byte[] design = decryptByPubKey(Base64Decoder.decode(data), pubKey);
        return new String(design);
    }

    /**
     *
     * @param data 待解密的数据
     * @param priKey 私钥
     * @return
     * @throws Exception
     * @description  私钥解密
     */
    public static byte[] decryptByPriKey(byte[] data, byte[] priKey) throws Exception {
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(priKey);
        KeyFactory keyFactory = KeyFactory.getInstance(RSA_KEY_ALGORITHM);
        PrivateKey privateKey = keyFactory.generatePrivate(pkcs8KeySpec);
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        return cipher.doFinal(data);
    }

    /**
     *
     * @param secretText 解密前的字符串
     * @param privateKey 私钥
     * @return 解密后的字符串
     * @description  私钥解密
     */
    public static String decryptByPriKey(String secretText, String privateKey) {
        try {
            byte[] priKey = Base64Decoder.decode(privateKey);;
            byte[] design = decryptByPriKey(Base64Decoder.decode(secretText), priKey);
            return new String(design);
        } catch (Exception e) {
            throw new RuntimeException("解密字符串[" + secretText + "]时遇到异常", e);
        }
    }

    /**
     *
     * @param data 待签名数据
     * @param priKey 私钥
     * @return 签名
     * @throws Exception
     * @description  RSA签名
     */
    public static String sign(byte[] data, byte[] priKey) throws Exception {
        // 取得私钥
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(priKey);
        KeyFactory keyFactory = KeyFactory.getInstance(RSA_KEY_ALGORITHM);
        // 生成私钥
        PrivateKey privateKey = keyFactory.generatePrivate(pkcs8KeySpec);
        // 实例化Signature
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        // 初始化Signature
        signature.initSign(privateKey);
        // 更新
        signature.update(data);
        return Base64Encoder.encode(signature.sign());
    }


    /**
     *
     * @param data 待校验数据
     * @param sign 数字签名
     * @param pubKey 公钥
     * @return boolean 校验成功返回true，失败返回false
     * @throws Exception
     * @author  Rommel
     * @date    2023/10/5-23:44
     * @version 1.0
     * @description  RSA校验数字签名
     */
    public static boolean verify(byte[] data, byte[] sign, byte[] pubKey) throws Exception {
        // 实例化密钥工厂
        KeyFactory keyFactory = KeyFactory.getInstance(RSA_KEY_ALGORITHM);
        // 初始化公钥
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(pubKey);
        // 产生公钥
        PublicKey publicKey = keyFactory.generatePublic(x509KeySpec);
        // 实例化Signature
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        // 初始化Signature
        signature.initVerify(publicKey);
        // 更新
        signature.update(data);
        // 验证
        return signature.verify(sign);
    }



    /**
     *
     * @return
     * @description  生成 RSA 密钥对
     */
    public static KeyPair generateRsaKey(int keySize) {
        if(keySize==0){
            keySize = KEY_SIZE;
        }
        KeyPair keyPair;
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(keySize);
            keyPair = keyPairGenerator.generateKeyPair();
        }
        catch (Exception ex) {
            throw new IllegalStateException(ex);
        }
        return keyPair;
    }

    /**
     *
     * @param key
     * @return
     * @description  公钥转base64字符串
     */
    public static String publicKeyToBase64(RSAPublicKey key){
        return Base64Encoder.encode(key.getEncoded());
    }

    /**
     *
     * @param key
     * @return
     * @description  私钥转base64字符串
     */
    public static String privateKeyToBase64(RSAPrivateKey key){
        return Base64Encoder.encode(key.getEncoded());
    }

    /**
     *
     * @param base64
     * @return
     * @description  base64字符串转公钥
     */
    public static RSAPublicKey base64ToPublicKey(String base64)  {
        byte[] keyBytes = Base64Decoder.decode(base64.getBytes());
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        RSAPublicKey rsaPublicKey = null;
        try {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            rsaPublicKey = (RSAPublicKey)keyFactory.generatePublic(keySpec);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }

        return rsaPublicKey;
    }

    public static RSAPrivateKey base64ToPrivateKey(String base64)  {
        byte[] keyBytes = Base64Decoder.decode(base64.getBytes());
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        RSAPrivateKey rsaPrivateKey = null;
        try {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            rsaPrivateKey = (RSAPrivateKey)keyFactory.generatePrivate(keySpec);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }

        return rsaPrivateKey;
    }


    public static void main(String[] args) throws Exception {
//        KeyPair keyPair = RSAUtils.generateRsaKey(1024);
//        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
//        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
//        System.out.println("publicKey="+publicKey);
//        System.out.println("privateKey="+privateKey);
//
//        String base64PublicKey = RSAUtils.publicKeyToBase64(publicKey);
//        String base64PrivateKey = privateKeyToBase64(privateKey);
//        System.out.println("publicKey-base64="+base64PublicKey);
//        System.out.println("privateKey-base64="+base64PrivateKey);
//
//        RSAPublicKey publicKeyBase64 = RSAUtils.base64ToPublicKey(base64PublicKey);
//        RSAPrivateKey privateKeyBase64 = RSAUtils.base64ToPrivateKey(base64PrivateKey);
//        System.out.println("base64-publicKey="+publicKeyBase64);
//        System.out.println("base64-privateKey="+privateKeyBase64);
//
//
//        // 待加密数据
//        String data = "hello ras";
//        // 公钥加密
//        String encrypt = RSAUtils.encryptByPubKey(data, base64PublicKey);
//        // 私钥解密
//        String decrypt = RSAUtils.decryptByPriKey(encrypt, base64PrivateKey);
//
//        System.out.println("加密前:" + data);
//        System.out.println("明文length：" + data.length());
//        System.out.println("加密后:" + encrypt);
//        System.out.println("解密后:" + decrypt);

        //构造签名数据
        StringBuilder signBuilder = new StringBuilder();
        signBuilder.append(UUID.randomUUID());
        signBuilder.append("-").append(Instant.now());
        System.out.println("签名前数据:" + signBuilder);
        String signature = RSAUtils.sign(signBuilder.toString().getBytes(),Base64Decoder.decode(privateKeyBase64Default));
        System.out.println("签名后数据:" + signature);
        boolean verifyResult = RSAUtils.verify(signBuilder.toString().getBytes(),Base64Decoder.decode(signature),Base64Decoder.decode(publicKeyBase64Default));
        System.out.println("验签结果:" + verifyResult);


    }

}