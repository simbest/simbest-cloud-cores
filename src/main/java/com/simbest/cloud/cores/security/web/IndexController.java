package com.simbest.cloud.cores.security.web;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.json.JSONUtil;
import com.simbest.boot.security.IUser;
import com.simbest.boot.security.SimpleUser;
import com.simbest.cloud.cores.base.web.response.JsonResponse;
import com.simbest.cloud.cores.config.AppConfig;
import com.simbest.cloud.feign.uums.clients.AuthClient;
import com.simbest.cloud.feign.uums.model.vo.KeyInfoVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description: 获取用户信息
 * @author: yanqi
 * @date: 2023/11/20
 */
@RestController
@RequiredArgsConstructor
@Slf4j
public class IndexController {

    private final AuthClient authService;
    private final AppConfig appConfig;

    @PostMapping("/getCurrentUser/api")
    public JsonResponse<SimpleUser> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            if (authentication.getPrincipal() instanceof IUser) {
                IUser principal = (IUser) authentication.getPrincipal();
                String username = principal.getUsername();
                KeyInfoVo keyInfoVo = new KeyInfoVo(username, appConfig.getAppcode());
                JsonResponse response = authService.findByUsername(keyInfoVo);
                SimpleUser user = BeanUtil.toBean(response.getData(), SimpleUser.class);
                log.debug("获取当前用户信息{}", JSONUtil.toJsonStr(user));
                //重新写入redis
                return JsonResponse.success(user);

            } else {
                if (authentication.getPrincipal() != null) {
                    log.warn("SecurityContextHolder包含Authentication信息，但返回认证主体Principal不是IUser，请检查Redis缓存，目前返回的Principal类型为【{}】,toString后为【{}】", authentication.getPrincipal().getClass(), authentication.getPrincipal().toString());
                } else {
                    log.warn("SecurityContextHolder包含Authentication信息，但返回认证主体Principal为空，请检查Redis缓存，目前返回的Principal为空，authentication是【{}】", authentication.toString());
                }
                return null;
            }
        }
        log.warn("SecurityContextHolder的Authentication为空，无法获取认证主体Principal");
        return JsonResponse.fail("获取失败");
    }
}
