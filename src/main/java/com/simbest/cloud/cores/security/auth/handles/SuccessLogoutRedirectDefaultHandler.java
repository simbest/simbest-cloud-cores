/*
 * 版权所有 © 北京晟壁科技有限公司 2008-2027。保留一切权利!
 */
package com.simbest.cloud.cores.security.auth.handles;

import com.simbest.cloud.cores.constants.ApplicationConstants;
import com.simbest.cloud.cores.utils.ApplicationContextProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.security.core.Authentication;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 用途：登出处理器，Redirect方式重定向
 * 作者: lishuyi
 * 时间: 2018/2/25  18:49
 */
@Slf4j
public class SuccessLogoutRedirectDefaultHandler implements SuccessLogoutRedirectHandler {

    private DefaultLogoutHandler defaultLogoutHandler;

    public SuccessLogoutRedirectDefaultHandler(DefaultLogoutHandler defaultLogoutHandler){
        this.defaultLogoutHandler = defaultLogoutHandler;
    }

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {
        log.debug("【{}】即将通过【{}】登出", authentication, request.getRequestURI());
        request.logout();
        defaultLogoutHandler.logout(request, response, authentication);
        response.setStatus(HttpServletResponse.SC_OK);

        Object casSecurityConfigurer = null;
        try{
            casSecurityConfigurer = ApplicationContextProvider.getBean("casSecurityConfigurer");
        }catch(NoSuchBeanDefinitionException e){
        }
        if(null != casSecurityConfigurer){
            log.warn("---------------------------------CAS LOGOUT 重定向---------------------------------------------");
            response.sendRedirect(request.getContextPath() + ApplicationConstants.CAS_LOGOUT_PAGE);
            log.warn("---------------------------------CAS LOGOUT 重定向---------------------------------------------");
        }else {
            response.setStatus(HttpServletResponse.SC_OK);
            request.getRequestDispatcher(ApplicationConstants.LOGIN_PAGE).forward(request, response);
        }
    }
}
