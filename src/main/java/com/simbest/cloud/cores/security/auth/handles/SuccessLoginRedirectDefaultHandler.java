/*
 * 版权所有 © 北京晟壁科技有限公司 2008-2027。保留一切权利!
 */
package com.simbest.cloud.cores.security.auth.handles;

import com.simbest.cloud.cores.constants.ApplicationConstants;
import com.simbest.cloud.cores.utils.redis.RedisRetryLoginCache;
import com.simbest.cloud.cores.utils.security.LoginUtils;
import com.simbest.cloud.orguser.dto.IUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 用途：登录处理器，Redirect方式重定向
 * 作者: lishuyi@simbest.com.cn
 * 时间: 2021/3/15  12:00
 */
@Slf4j
public class SuccessLoginRedirectDefaultHandler implements SuccessLoginRedirectHandler {

    private LoginUtils loginUtils;

    public SuccessLoginRedirectDefaultHandler(LoginUtils loginUtils){
        this.loginUtils = loginUtils;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        if(authentication.getPrincipal() instanceof IUser){
            IUser iUser = (IUser)authentication.getPrincipal();
            log.debug("用户【{}】登录成功，用户身份详细信息为【{}】", iUser.getUsername(), iUser);
            //登录成功后，立即清除失败缓存，不再等待错误缓存的到期时间
            RedisRetryLoginCache.cleanTryTimes(iUser.getUsername());

            //记录登录日志
            loginUtils.recordLoginLog(request, authentication);
            //记录当前登录账号
            loginUtils.recordLoginUsername(iUser.getUsername());

        }

        response.setStatus(HttpServletResponse.SC_OK);
        request.getRequestDispatcher(ApplicationConstants.HOME_PAGE).forward(request, response);
    }
}
