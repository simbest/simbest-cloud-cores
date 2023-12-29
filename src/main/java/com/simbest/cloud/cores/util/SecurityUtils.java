package com.simbest.cloud.cores.util;

import com.simbest.boot.security.IUser;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

@Slf4j
public class SecurityUtils {
    public static IUser getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            if (authentication.getPrincipal() instanceof IUser) {
                return (IUser) authentication.getPrincipal();
            } else {
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

}
