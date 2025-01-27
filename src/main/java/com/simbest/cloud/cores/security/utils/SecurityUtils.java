package com.simbest.cloud.cores.security.utils;

import com.simbest.boot.security.IAuthService;
import com.simbest.boot.security.IUser;
import com.simbest.cloud.cores.config.AppConfig;
import com.simbest.cloud.cores.constants.ApplicationConstants;
import com.simbest.cloud.cores.utils.ApplicationContextProvider;
import com.simbest.cloud.cores.utils.DateUtil;
import com.simbest.cloud.cores.utils.encrypt.Des3Encryptor;
import com.simbest.cloud.cores.utils.encrypt.MochaEncryptor;
import com.simbest.cloud.cores.utils.encrypt.RsaEncryptor;
import com.simbest.cloud.cores.redis.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.util.Assert;

import java.util.Date;

@Slf4j
public class SecurityUtils {
    public static AppConfig appConfig = ApplicationContextProvider.getBean(AppConfig.class);
    public static Des3Encryptor des3Encryptor = ApplicationContextProvider.getBean(Des3Encryptor.class);
    public static MochaEncryptor mochaEncryptor = ApplicationContextProvider.getBean(MochaEncryptor.class);
    public static RsaEncryptor rsaEncryptor = ApplicationContextProvider.getBean(RsaEncryptor.class);
    public static IAuthService iAuthService = ApplicationContextProvider.getBean(IAuthService.class);

    public static IUser getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            if (authentication.getPrincipal() instanceof IUser) {
                return (IUser) authentication.getPrincipal();
            }
            else if (authentication.getPrincipal() instanceof Jwt) {
                Jwt jwt = (Jwt) authentication.getPrincipal();
                String subject = jwt.getSubject();
                if(StringUtils.isNotEmpty(subject)) {
                    String username = rsaEncryptor.decrypt(subject);
                    if(StringUtils.isNotEmpty(username)) {
                        IUser iUser = iAuthService.findByKey(username, IAuthService.KeyType.username);
                        log.debug("通过账号【{}】获取用户身份信息为【{}】", username, iUser);
                        return iUser;
                    }
                }
            }
            else {
                if (authentication.getPrincipal() != null) {
                    log.warn("SecurityContextHolder包含Authentication信息，但返回认证主体Principal不是IUser，请检查Redis缓存，目前返回的Principal类型为【{}】,toString后为【{}】", authentication.getPrincipal().getClass(), authentication.getPrincipal().toString());
                } else {
                    log.warn("SecurityContextHolder包含Authentication信息，但返回认证主体Principal为空，请检查Redis缓存，目前返回的Principal为空，authentication是【{}】", authentication.toString());
                }
                return null;
            }
        }
        log.warn("SecurityContextHolder的Authentication为空，无法获取认证主体Principal，请检查代码Session或API的access_token");
        return null;
    }

    /**
     * Get the login of the current user.
     *
     * @return the login of the current user
     */
    public static String getCurrentUserName() {
        String userName = null;
        IUser authUser = getCurrentUser();
        if (null != authUser) {
            userName = authUser.getUsername();
        }
        return userName;
    }
    /**
     * Check if a user is authenticated.
     *
     * @return true if the user is authenticated, false otherwise
     */
//    public static boolean isAuthenticated() {
//        SecurityContext securityContext = SecurityContextHolder.getContext();
//        Authentication authentication = securityContext.getAuthentication();
//        if (authentication != null) {
//            return authentication.getAuthorities()
//                    .stream()
//                    .noneMatch(grantedAuthority -> grantedAuthority.getAuthority()
//                            .equals(AuthoritiesConstants.ANONYMOUS));
//        }
//        return false;
//    }

    /**
     * If the current user has a specific authority (security role).
     * <p>
     * The name of this method comes from the isUserInRole() method in the Servlet API
     *
     * @param authority the authority to check
     * @return true if the current user has the authority, false otherwise
     */
    public static boolean hasPermission(String authority) {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        if (authentication != null) {
            return authentication.getAuthorities()
                    .stream()
                    .anyMatch(grantedAuthority -> grantedAuthority.getAuthority()
                            .equals(authority));
        }
        return false;
    }

    /**
     * 一个权限通过，即可判定成功
     * @param authorities
     * @return
     */
    public static boolean hasAnyPermission(String[] authorities) {
        for(String authority : authorities){
            if(hasPermission(authority)){
                return true;
            }
        }
        return false;
    }

    /**
     * 获取通用授权密码
     * @return
     */
//    public static String getAnyPassword(){
//        String currDateHour = DateUtil.getDateStr("yyyyMMddHH");
//        return RedisUtil.getGlobal(DigestUtils.md5Hex(ApplicationConstants.ANY_PASSWORD+currDateHour));
//    }
//    public static String getRefinePassword(String authUsername){
//        String currDateHour = DateUtil.getDateStr("yyyyMMddHH");
//        String anyPasswordKey = String.format(ApplicationConstants.REFINE_ANY_PASSWORD, currDateHour, authUsername);
//        return RedisUtil.getGlobal(anyPasswordKey);
//    }

//    public static String getRefinePasswordMd5(String authUsername){
//        String asePassword = getRefinePassword(authUsername);
//        return null == asePassword ? null : DigestUtils.md5Hex(asePassword);
//    }
//
//    public static String getRefinePasswordApplyUsername(String authUsername){
//        String asePassword = getRefinePassword(authUsername);
//        String password = AesEncryptor.staticDecrypt(asePassword);
//        String applyUsername = StringUtils.substringAfterLast(password,":");
//        return null == applyUsername ? null : applyUsername;
//    }


    /**
     * 前后1分钟内，未作单点认证，返回true，可进行单点；否则返回false，不可认证。
     * 基于时间戳的新单点
     * 在线时间工具： https://www.bejson.com/convert/unix/index.html   选择：13位时间戳(豪秒级)
     * 在线MD5工具   https://www.bejson.com/enc/md5/#google_vignette  输入OA账号+tSsoSalt+上面的13位时间戳进行32位MD5计数，如：wenyiXianzai@20991718109929851
     *
     * @param username
     * @param ts    13位时间戳
     * @param tk    username+appConfig.getSsoSalt()+ts进行32位MD5计数
     * @param appcode
     * @return
     *
     * ts示例：1718109929851
     * tk示例：wenyiXianzai@20991718109929851进行32位MD5计数
     * url示例：http://10.92.82.161:8088/nccpn/getCurrentUser/sso?loginuser=Dst8zkGn+hw4cqrGR8YIXYdORwd7/iuBsBclMlCyqAI6RapyXQWg8cxjFUJprAY7GxpSFl6YwAnvHYGin0+ae0fulLZAB0opAxop8dEnDKHjYWh/bmUOE3MZ/RxydwgDkqs7rIl7eNFmz6qON1+cl1+lt32djjYf3II5CkwrLYo=&appcode=nccpn&ts=1718109929851&tk=A9EFA3AD69FAAA3AB818DC2742C235D8
     *
     */
    public static boolean nssoAccessCheck(String username, String ts, String tk, String appcode){
        boolean checkResult = false;
        Assert.notNull(username, "新单点认证username不可为空");
        Assert.notNull(ts, "新单点认证ts不可为空");  //在线时间工具    https://www.bejson.com/convert/unix/index.html
        Assert.notNull(tk, "新单点认证tk不可为空");  //在线MD5工具   https://www.bejson.com/enc/md5/#google_vignette
        Assert.notNull(appcode, "新单点认证appcode不可为空");
        Date requestTime = new Date(Long.valueOf(ts));
        Date currentTime = new Date();
        Date lastTime = DateUtil.subMinutes(currentTime, appConfig.getSsoTime());
        Date nextTime = DateUtil.addMinutes(currentTime, appConfig.getSsoTime());
        if(tk.equalsIgnoreCase(nssoTkGet(username,ts))) {
            //请求发生在前后1分钟内，允许认证
            if (DateUtil.compareTimestamp(requestTime, lastTime) > 0 && DateUtil.compareTimestamp(nextTime, requestTime) > 0) {
                String nssoUserAccessKey = String.format(ApplicationConstants.NSSO_USER_ACCESS, username, DateUtil.getDate(requestTime, "yyyyMMddHHmmss"), tk);
                //两分钟内认证过，不可再次使用相同单点加密串进行认证
                if (StringUtils.isEmpty(RedisUtil.getGlobal(nssoUserAccessKey))) {
                    checkResult = true;
                    Integer expireMinutes = appConfig.getSsoTime() * 2; // 默认2分钟内时效
                    RedisUtil.setGlobal(nssoUserAccessKey, nssoUserAccessKey, expireMinutes * 60);
                }
                else{
                    log.error("NEWSSO-attemptAuthentication-ERROR-REUSED，账号【{}】时间【{}】令牌【{}】应用【{}】，重复调用！", username, ts, tk, appcode);
                }
            }
            else{
                log.error("NEWSSO-attemptAuthentication-ERROR-TIME，账号【{}】时间【{}】令牌【{}】应用【{}】，时间非法！", username, ts, tk, appcode);
            }
        }
        else {
            log.error("NEWSSO-attemptAuthentication-ERROR-TOKEN，账号【{}】时间【{}】令牌【{}】应用【{}】，令牌非法！", username, ts, tk, appcode);
        }
        return checkResult;
    }

    /**
     * 根据账号明文和13位时间戳获取tk
     * @param username
     * @param ts
     * @return
     */
    public static String nssoTkGet(String username, String ts){
        return DigestUtils.md5Hex(username+appConfig.getSsoSalt()+ts);
    }


    public static String encryptorUserName(String username){
        String encryptorUsername = null;
        encryptorUsername = rsaEncryptor.encrypt(username);
        if(StringUtils.isEmpty(username)){
            encryptorUsername = mochaEncryptor.encrypt(username);
        }
        else if(StringUtils.isEmpty(username)){
            encryptorUsername = des3Encryptor.encrypt(username);
        }
        return encryptorUsername;
    }

    public static String decryptorUserName(String encryptorUsername){
        String decryptorUserName = null;
        decryptorUserName = rsaEncryptor.decrypt(encryptorUsername);
        if(StringUtils.isEmpty(decryptorUserName)){
            decryptorUserName = mochaEncryptor.decrypt(encryptorUsername);
        }
        else if(StringUtils.isEmpty(decryptorUserName)){
            decryptorUserName = des3Encryptor.decrypt(encryptorUsername);
        }
        return decryptorUserName;
    }
}
