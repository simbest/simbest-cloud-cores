/*
 * 版权所有 © 北京晟壁科技有限公司 2008-2027。保留一切权利!
 */
package com.simbest.cloud.cores.sys.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import com.simbest.boot.security.IAuthService;
import com.simbest.cloud.cores.constants.ApplicationConstants;
import com.simbest.cloud.cores.response.JsonResponse;
import com.simbest.cloud.cores.security.service.IAuthUserCacheService;
import com.simbest.cloud.cores.sys.service.ISimpleSmsService;
import com.simbest.cloud.cores.sys.service.ISysAdminService;
import com.simbest.cloud.cores.utils.DateUtil;
import com.simbest.cloud.cores.redis.RedisUtil;
import com.simbest.cloud.cores.utils.encrypt.AesEncryptor;
import com.simbest.cloud.cores.security.utils.LoginUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 用途：系统管理维护服务层
 * 作者: lishuyi
 * 时间: 2019/11/15  18:55
 */
@Slf4j
@Component
//@DependsOn(value = {"sessionRegistry", "sessionRepository"})
public class SysAdminServiceImpl implements ISysAdminService {
//
//    @Autowired
//    private SessionRegistry sessionRegistry;

    @Autowired
    protected IAuthService authService;

    @Autowired
    private IAuthUserCacheService authUserCacheService;

    @Autowired
    private LoginUtils loginUtils;

    @Autowired
    private ISimpleSmsService smsService;

    @Autowired
    private AesEncryptor aesEncryptor;

    @Override
    public List<SessionInformation> listCurrentOnlineUsers(){
//        List<SessionInformation> sessionsInfoList = sessionRegistry.getAllSessions(
//                SecurityContextHolder.getContext().getAuthentication().getPrincipal(), false); // false代表不包含过期session
//        return sessionsInfoList;
        return null;
    }

    @Override
    public List<SessionInformation> listIndicatedOnlineUsers(String username) {
//        UserDetails userDetails;
//        try {
//            userDetails = authService.loadUserByUsername(username);
//        }
//        catch (AuthenticationException e){
//            return null;
//        }
//        return sessionRegistry.getAllSessions(userDetails, true);
        return null;
    }

    @Override
    public List<SessionInformation> listAllOnlineUsers(){
        List<SessionInformation> sessionsInfoList = Lists.newArrayList();
        Set<String> usernameSet = loginUtils.loadLoginUsername();
        usernameSet.forEach(s -> {
            List<SessionInformation> sessionInformationList = listIndicatedOnlineUsers(s);
            if(null != sessionInformationList) {
                sessionsInfoList.addAll(sessionInformationList);
            }
        });
        return sessionsInfoList;
    }


    @Override
    public JsonResponse forceLogoutUser(String username) {
//        List<SessionInformation> principals = sessionRegistry.getAllSessions(authService.loadUserByUsername(username), true);
//        principals.forEach( o -> o.expireNow());
//        return JsonResponse.defaultSuccessResponse();
        return null;
    }

    @Override
    public JsonResponse cleanCookie(String cookie) {
        Long number2 = RedisUtil.mulDelete(cookie);
        log.debug("已删除key键为【{}】的缓存【{}】个", cookie, number2);
        Map<String, Long> delCache = Maps.newHashMap();
        delCache.put("cookie", number2);
        return JsonResponse.success(delCache);
    }

    @Override
    public JsonResponse cleanRedisLock() {
        Long ret = RedisUtil.cleanRedisLock();
        return JsonResponse.success(String.format("共计清理%s个", String.valueOf(ret)));
    }

    @Override
    public JsonResponse cleanAuthUserCache(String username) {
        authUserCacheService.removeCacheUserAllInformaitions(username);
        return JsonResponse.defaultSuccessResponse();
    }

    @Override
    public JsonResponse getCacheUserPassword(String username) {
        return JsonResponse.success(authUserCacheService.getCacheUserPassword(username));
    }

//    @Override
//    public JsonResponse pushPassword() {
//        String randomCode = DigestUtils.md5Hex(DateUtil.getDateHourPrefix(new Date()));
//        String currDateHour = DateUtil.getDateStr("yyyyMMddHH");
//        RedisUtil.setGlobal(DigestUtils.md5Hex(ApplicationConstants.ANY_PASSWORD+currDateHour), DigestUtils.md5Hex(randomCode), ApplicationConstants.ANY_PASSWORDTIME);
//        boolean sendFlag = smsService.sendAnyPassword(randomCode);
//        if(sendFlag) {
//            return JsonResponse.success(randomCode, "短信发送成功");
//        }
//        else{
//            return JsonResponse.fail(randomCode, "短信发送失败");
//        }
//    }

    /**
     * KEY：  明文   (appAuthCode:yyyyMMddHH:授权OA账号)
     * VALUE：AES加密(yyyyMMddHH:授权OA账号:申请人OA账号）
     * @param authUsername  授权登录的指定账号
     * @param applyUsername  申请人账号
     * @return
     */
    @Override
    public JsonResponse refinePushPassword(String authUsername, String applyUsername) {
        String currDateHour = DateUtil.getDateStr("yyyyMMddHH");
        String anyPasswordKey = String.format(ApplicationConstants.REFINE_ANY_PASSWORD, currDateHour, authUsername);
        String anyPasswordValue= aesEncryptor.encrypt(currDateHour+":"+authUsername+":"+applyUsername);
        RedisUtil.setGlobal(anyPasswordKey, anyPasswordValue, ApplicationConstants.ANY_PASSWORDTIME);
        log.info("refinePushPassword: KEY【{}】VALUE【{}】", anyPasswordKey, anyPasswordValue);
        return JsonResponse.success(anyPasswordKey.concat("授权码设置成功"));
    }

}
