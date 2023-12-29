package com.simbest.cloud.cores.util;

import cn.hutool.core.bean.BeanUtil;
import com.simbest.boot.security.IUser;
import com.simbest.boot.security.SimpleUser;
import com.simbest.cloud.cores.config.AppConfig;
import com.simbest.cloud.cores.security.service.IAuthUserCacheService;
import com.simbest.cloud.feign.uums.model.vo.KeyInfoVo;
import com.simbest.cloud.feign.uums.clients.AuthClient;
import com.simbest.cloud.feign.uums.util.JsonResponse;

public class LoadUserUtils {
    public static IUser loadUser(String username) {
        IAuthUserCacheService authUserCacheService = SpringUtils.getBean(IAuthUserCacheService.class);
        IUser user = authUserCacheService.loadCacheUser(username);
        if (user == null) {
            AuthClient authService = SpringUtils.getBean(AuthClient.class);
            AppConfig appConfig = SpringUtils.getBean(AppConfig.class);
            KeyInfoVo keyInfoVo = new KeyInfoVo(username, appConfig.getAppcode());
            JsonResponse jsonResponse = authService.findByUsername(keyInfoVo);
            user = BeanUtil.toBean(jsonResponse.getData(), SimpleUser.class);
            authUserCacheService.saveOrUpdateCacheUser(user);
        }
        return user;
    }
}
