/*
 * 版权所有 © 北京晟壁科技有限公司 2008-2027。保留一切权利!
 */
package com.simbest.cloud.cores.security.auth.handles;

import com.simbest.cloud.cores.constants.AuthoritiesConstants;
import com.simbest.cloud.cores.utils.json.JacksonUtils;
import com.simbest.cloud.cores.utils.redis.RedisRetryLoginCache;
import com.simbest.cloud.orguser.pojo.JsonResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 用途：登录失败处理器，，Rest方式返回数据
 * 作者: lishuyi@simbest.com.cn
 * 时间: 2021/3/9  10:16
 */
@Slf4j
public class FailedLoginRestDefaultHandler implements FailedLoginRestHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException {
        //登录发生错误计数，每错误一次，即向后再延时等待5分钟
        String username = request.getParameter(UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_USERNAME_KEY);
        if(StringUtils.isNotEmpty(username)){
            log.warn("用户【{}】尝试登录失败", username);
            RedisRetryLoginCache.addTryTimes(username);
        }

        response.setCharacterEncoding("utf-8");
        response.setContentType("text/javascript;charset=utf-8");
        PrintWriter writer = response.getWriter();
        JsonResponse jsonResponse = JsonResponse.unauthorized(request, exception);
        jsonResponse.setError(AuthoritiesConstants.BadCredentialsException);
        writer.print(JacksonUtils.obj2json(jsonResponse));
        writer.flush();
        writer.close();
    }

}
