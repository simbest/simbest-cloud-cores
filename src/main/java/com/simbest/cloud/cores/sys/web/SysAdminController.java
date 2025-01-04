/*
 * 版权所有 © 北京晟壁科技有限公司 2008-2027。保留一切权利!
 */
package com.simbest.cloud.cores.sys.web;

import com.simbest.cloud.cores.constants.ApplicationConstants;
import com.simbest.cloud.cores.response.JsonResponse;
import com.simbest.cloud.cores.sys.service.ISysAdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用途：系统管理维护控制器
 * 作者: lishuyi
 * 时间: 2018/4/25  23:49
 */
@Tag(name  = "SysAdminController", description = "系统管理-通用维护管理")
@Slf4j
@RestController
@RequestMapping("/sys/admin")
public class SysAdminController {

    @Autowired
    private ISysAdminService sysAdminService;

    @Operation(summary = "查询当前应用-当前登录用户的在线实例", description = "注意是当前用户")
    @PreAuthorize("hasAnyAuthority('ROLE_SUPER','ROLE_ADMIN')")
    @PostMapping("/listCurrentOnlineUsers")
    public JsonResponse listCurrentOnlineUsers() {
        return JsonResponse.success(sysAdminService.listCurrentOnlineUsers());
    }

    @Operation(summary = "查询当前应用-指定登录用户的在线实例", description = "注意指定的用户必须在线")
    @PreAuthorize("hasAnyAuthority('ROLE_SUPER','ROLE_ADMIN')")
    @PostMapping("/listIndicatedOnlineUsers")
    public JsonResponse listIndicatedOnlineUsers(@RequestParam String username) {
        return JsonResponse.success(sysAdminService.listIndicatedOnlineUsers(username));
    }

    @Operation(summary = "查询当前应用-所有登录用户的在线实例", description = "注意是所有用户")
    @PreAuthorize("hasAnyAuthority('ROLE_SUPER','ROLE_ADMIN')")
    @PostMapping("/listAllOnlineUsers")
    public JsonResponse listAllOnlineUsers() {
        return JsonResponse.success(sysAdminService.listAllOnlineUsers());
    }

    @Operation(summary = "强制剔除某个用户", description = "强制剔除某个用户")
    @PreAuthorize("hasAnyAuthority('ROLE_SUPER','ROLE_ADMIN')")
    @PostMapping("/forceLogoutUser")
    public JsonResponse forceLogoutUser(@RequestParam String username) {
        return sysAdminService.forceLogoutUser(username);
    }

    @Operation(summary = "删除用户Cookie", description = "注意此接口将清理所有应用的cookie")
    @PreAuthorize("hasAnyAuthority('ROLE_SUPER','ROLE_ADMIN')")
    @PostMapping("/cleanCookie")
    public JsonResponse cleanCookie(@RequestParam String cookie) {
       return sysAdminService.cleanCookie(cookie);
    }

    @Operation(summary = "清理分布式事务锁", description = "注意此接口将清理所有应用的分布式锁")
    @PreAuthorize("hasAnyAuthority('ROLE_SUPER','ROLE_ADMIN')")
    @PostMapping("/cleanRedisLock")
    public JsonResponse cleanRedisLock() {
        return sysAdminService.cleanRedisLock();
    }

    @Operation(summary = "清理认证用户的所有身份缓存信息", description = "清理认证用户的所有身份缓存信息")
    @PreAuthorize("hasAnyAuthority('ROLE_SUPER','ROLE_ADMIN')")
    @PostMapping("/cleanAuthUserCache")
    public JsonResponse cleanAuthUserCache(@RequestParam String username) {
        String[] users = StringUtils.split(username, ApplicationConstants.COMMA);
        for(String user : users){
            sysAdminService.cleanAuthUserCache(user);
        }
        return JsonResponse.defaultSuccessResponse();
    }

    @Operation(summary = "获取用户密码", description = "获取用户密码")
    @PreAuthorize("hasAnyAuthority('ROLE_SUPER','ROLE_ADMIN')")
    @PostMapping("/getCacheUserPassword")
    public JsonResponse getCacheUserPassword(@RequestParam String username) {
        return sysAdminService.getCacheUserPassword(username);
    }

//    @Operation(summary = "下发通用密码", description = "下发通用密码")
//    @PreAuthorize("hasAnyAuthority('ROLE_SUPER','ROLE_ADMIN')")
//    @PostMapping("/pushPassword")
//    public JsonResponse pushPassword() {
//        return sysAdminService.pushPassword();
//    }

    /**
     * http://localhost:9080/ntododemo/sys/admin/refinePushPassword?authUsername=andong&applyUsername=zhaoxiang
     * @param authUsername  授权登录的指定账号
     * @param applyUsername 申请人账号
     * @return
     */
    @Operation(summary = "精细化下发通用密码", description = "精细化下发通用密码")
    @PreAuthorize("hasAnyAuthority('ROLE_SUPER','ROLE_ADMIN')")
    @PostMapping("/refinePushPassword")
    public JsonResponse refinePushPassword(String authUsername, String applyUsername) {
        return sysAdminService.refinePushPassword(authUsername, applyUsername);
    }


}
