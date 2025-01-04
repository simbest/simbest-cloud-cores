package com.simbest.cloud.cores.utils.encrypt;

import com.simbest.cloud.cores.config.AppConfig;
import com.simbest.cloud.cores.exception.Exceptions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.ParseException;

@Slf4j
@Component
public class MochaEncryptor extends AbstractEncryptor {
    private final static Integer TIMEOUT = 1800;

    @Autowired
    private AppConfig config;

    /**
     *
     * @param source 普通文本
     * @return code
     */
    @Override
    protected String encryptSource(String source) {
        String result = null;
        try {
            result = MochaEncryptorUtil.encode(config.getMochaPortalToken(), source);
        } catch (Exception e) {
            Exceptions.printException(e);
        }
        return result;
    }

    /**
     *
     * @param code 加密文本
     * @return value
     */
    @Override
    protected String decryptCode(String code) {
        String result = null;
        try {
            result = MochaEncryptorUtil.decode(config.getMochaPortalToken(), code, TIMEOUT);// 解密
        } catch (Exception e) {
            log.warn("解密【{}】失败", code);
            throw new RuntimeException(e);
        }
        return result;
    }

    public static void main(String[] args) throws ParseException {
        String code = MochaEncryptorUtil.encode("SIMBEST_SSO", "hadmin3");
        System.out.println(code);
        System.out.println(MochaEncryptorUtil.decode("SIMBEST_SSO", code, TIMEOUT));
    }

}