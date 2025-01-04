/*
 * 版权所有 © 北京晟壁科技有限公司 2008-2027。保留一切权利!
 */
package com.simbest.cloud.cores.sys.service;


import com.simbest.cloud.cores.response.JsonResponse;
import org.springframework.security.core.session.SessionInformation;

import java.util.List;

/**
 * 用途：系统管理维护服务层
 * 作者: lishuyi
 * 时间: 2019/11/15  19:01
 */
public interface ISysAdminService {

    List<SessionInformation> listCurrentOnlineUsers();

    List<SessionInformation> listIndicatedOnlineUsers(String username);

    List<SessionInformation> listAllOnlineUsers();

    JsonResponse forceLogoutUser(String username);

    JsonResponse cleanCookie(String cookie);

    JsonResponse cleanRedisLock();

    JsonResponse cleanAuthUserCache(String username);

    JsonResponse getCacheUserPassword(String username);

//    JsonResponse pushPassword();

    /**
     * 授权登录指定账号，并向接收人员发送验证码短信
     * @param authUsername  授权登录的指定账号
     * @param applyUsername  申请人账号
     * @return
     */
    JsonResponse refinePushPassword(String authUsername, String applyUsername);
}
