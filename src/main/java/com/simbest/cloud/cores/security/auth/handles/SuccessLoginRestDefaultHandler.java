/*
 * 版权所有 © 北京晟壁科技有限公司 2008-2027。保留一切权利!
 */
package com.simbest.cloud.cores.security.auth.handles;

import com.simbest.cloud.cores.utils.json.JacksonUtils;
import com.simbest.cloud.cores.utils.redis.RedisRetryLoginCache;
import com.simbest.cloud.cores.utils.security.LoginUtils;
import com.simbest.cloud.orguser.dto.IUser;
import com.simbest.cloud.orguser.pojo.JsonResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 用途：登出处理器，Rest方式返回数据
 * 作者: lishuyi@simbest.com.cn
 * 时间: 2021/3/9  10:14
 */

@Slf4j
public class SuccessLoginRestDefaultHandler implements SuccessLoginRestHandler {

    private LoginUtils loginUtils;

    public SuccessLoginRestDefaultHandler(LoginUtils loginUtils){
        this.loginUtils = loginUtils;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        Object returnObj = JsonResponse.authorized();
        if(authentication.getPrincipal() instanceof IUser){
            IUser iUser = (IUser)authentication.getPrincipal();
            returnObj = JsonResponse.success(iUser);
            //登录成功后，立即清除失败缓存，不再等待错误缓存的到期时间
            RedisRetryLoginCache.cleanTryTimes(iUser.getUsername());
            log.debug("用户【{}】登录成功，用户身份详细信息为【{}】", iUser.getUsername(), iUser);
            //记录登录日志
            loginUtils.recordLoginLog(request, authentication);
            //记录当前登录账号
            loginUtils.recordLoginUsername(iUser.getUsername());
        }
        PrintWriter writer = response.getWriter();
        response.setCharacterEncoding("utf-8");
        response.setContentType("text/javascript;charset=utf-8");
        writer.print(JacksonUtils.obj2json(returnObj));
        writer.flush();
        writer.close();
    }

}
