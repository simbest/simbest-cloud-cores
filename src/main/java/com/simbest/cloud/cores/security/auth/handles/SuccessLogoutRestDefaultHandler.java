/*
 * 版权所有 © 北京晟壁科技有限公司 2008-2027。保留一切权利!
 */
package com.simbest.cloud.cores.security.auth.handles;

import com.simbest.cloud.cores.utils.json.JacksonUtils;
import com.simbest.cloud.orguser.pojo.JsonResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 用途：登出处理器，Rest方式返回数据
 * 作者: lishuyi@simbest.com.cn
 * 时间: 2021/3/9  14:39
 */
@Slf4j
public class SuccessLogoutRestDefaultHandler implements SuccessLogoutRestHandler {

    private DefaultLogoutHandler defaultLogoutHandler;

    public SuccessLogoutRestDefaultHandler(DefaultLogoutHandler defaultLogoutHandler){
        this.defaultLogoutHandler = defaultLogoutHandler;
    }

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {
        log.debug("【{}】即将通过【{}】登出", authentication, request.getRequestURI());
        request.logout();
        defaultLogoutHandler.logout(request, response, authentication);
        response.setStatus(HttpServletResponse.SC_OK);
        response.setCharacterEncoding("utf-8");
        response.setContentType("text/javascript;charset=utf-8");
        PrintWriter writer = response.getWriter();
        writer.print(JacksonUtils.obj2json(JsonResponse.defaultSuccessResponse()));
        writer.flush();
        writer.close();
    }

}