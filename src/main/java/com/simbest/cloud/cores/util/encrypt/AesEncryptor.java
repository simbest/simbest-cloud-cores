///**
// * 版权所有 © 北京晟壁科技有限公司 2008-2027。保留一切权利!
// */
//package com.simbest.core.util.encrypt;
//
//
//import com.simbest.core.constants.ApplicationConstants;
//import com.simbest.core.exception.Exceptions;
//import jakarta.annotation.PostConstruct;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.commons.lang3.StringUtils;
//import org.springframework.stereotype.Component;
//import javax.crypto.Cipher;
//import javax.crypto.spec.IvParameterSpec;
//import javax.crypto.spec.SecretKeySpec;
//import java.net.URLEncoder;
//import java.nio.charset.StandardCharsets;
//
//
///**
// * 用途：AES加密工具类
// * 作者: lishuyi
// * 时间: 2017/12/28  22:11
// */
//@Slf4j
//public class AesEncryptor extends AbstractEncryptor {
//
//    public static final String key = "dW5uR,Yml#y%PeLG";
//    public static final String iv = "Be*Kn0xJ&XHc(Jl0";
//
//    private static AesEncryptor aesEncryptor;
//
//    @PostConstruct
//    public void init() {
//        aesEncryptor = this;
//    }
//
//    @Override
//    protected String encryptSource(String source) {
//        try {
//            Cipher cipher = getCipher(Cipher.ENCRYPT_MODE, key, iv);
//            byte[] encrypted = cipher.doFinal(source.getBytes(StandardCharsets.UTF_8));
//            return org.apache.commons.codec.binary.Base64.encodeBase64String(encrypted);
//        }catch (Exception e){
//            Exceptions.printException(e);
//            return null;
//        }
//    }
//
//    @Override
//    protected String decryptCode(String code) {
//        try {
//            Cipher cipher = getCipher(Cipher.DECRYPT_MODE, key, iv);
//            byte[] encrypted1 = org.apache.commons.codec.binary.Base64.decodeBase64(code.getBytes(UTF_8));
//            byte[] original = cipher.doFinal(encrypted1);
//            return new String(original);
//        }catch (Exception e){
//            Exceptions.printException(e);
//            return null;
//        }
//    }
//
//    public static String staticEncrypt(String source) {
//        return aesEncryptor.encryptSource(source);
//    }
//
//    public static String staticDecrypt(String code) {
//        return aesEncryptor.decryptCode(code);
//    }
//
//    public String decrypt(String code) {
//        decryptBefore(code);
//        String value = decryptCode(code);
//        decryptAfter(value);
//        return value;
//    }
//
//
//    private static Cipher getCipher(int model, String key,String iv) throws Exception{
//        if(StringUtils.isEmpty(key) || key.length() != 16){
//            System.out.print("Key不合法");
//            return null;
//        }
//        if(StringUtils.isEmpty(iv) || iv.length() != 16){
//            System.out.print("iv不合法");
//            return null;
//        }
//        byte[] raw = key.getBytes("ASCII");
//        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
//        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
//        IvParameterSpec ivps = new IvParameterSpec(iv.getBytes());
//        cipher.init(model, skeySpec, ivps);
//        return cipher;
//    }
//
//
//    public static void main(String[] args) throws Exception {
//        AesEncryptor aesEncryptor = new AesEncryptor();
//        String src = "13673357559";
//        System.out.println("加密前：" + src);
//        // 加密
//        long start = System.currentTimeMillis();
//        String enString = aesEncryptor.encryptSource(src);
//        System.out.println("密文是：" + enString);
//
//        long useTime = System.currentTimeMillis() - start;
//        System.out.println("加密耗时：" + useTime + "毫秒");
//        System.out.println("密文URLEncode后是：" + URLEncoder.encode(enString, "UTF-8"));
//        // 解密
//        start = System.currentTimeMillis();
//        String DeString = aesEncryptor.decryptCode(enString);
//        System.out.println("解密后的明文是：" + DeString);
//        useTime = System.currentTimeMillis() - start;
//        System.out.println("解密耗时：" + useTime + "毫秒");
//
//    }
//}
