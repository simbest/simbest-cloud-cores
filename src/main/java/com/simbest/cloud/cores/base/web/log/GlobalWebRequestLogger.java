/**
 * 版权所有 © 北京晟壁科技有限公司 2017-2027。保留一切权利!
 */
package com.simbest.cloud.cores.base.web.log;

import com.google.common.collect.ImmutableSet;
import com.simbest.cloud.cores.constants.ApplicationConstants;
import com.simbest.cloud.cores.utils.server.HostUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.NamedThreadLocal;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Arrays;
import java.util.Set;



/**
 * 用途：全局Web请求日志拦截器
 * 作者: lishuyi 
 * 时间: 2017/11/5  23:44 
 */
@Slf4j
@Aspect
@Order(10)
@Component
public class GlobalWebRequestLogger {

    private final static String LOGTAG = "GWL=======>>";



    private ThreadLocal<Long> startTime = new NamedThreadLocal<>("global-web-logger");

    private Set<String> notRecordController = ImmutableSet.of("com.simbest.boot.security.auth.controller.CaptchaController",
            "com.simbest.boot.security.auth.controller.LoginController", "com.simbest.boot.security.auth.controller.IndexController",
            "com.simbest.boot.sys.web.SysHealthController","com.simbest.exclude.web.GlobalErrorController");

    @Pointcut("execution(* *..web..*Controller.*(..))")
    public void webLog() { }



    /**
     * 方案二：使用Around处理
     * @param pjp
     * @return
     * @throws Throwable
     */
    @Around(value = "webLog()")
    public Object aroundAdvice(ProceedingJoinPoint pjp) throws Throwable {
        startTime.set(System.currentTimeMillis());
        RequestAttributes ra = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes sra = (ServletRequestAttributes) ra;
        //防止不是http请求的方法，例如：scheduled
        if (ra == null || sra == null) {
            return pjp.proceed();
        }

        HttpServletRequest request = sra.getRequest();
        String url = request.getRequestURL().toString();
        String ip = HostUtil.getClientIpAddress(request);
        String controller = pjp.getSignature().getDeclaringTypeName();
        String methodname = pjp.getSignature().getName();
        String args = Arrays.toString(pjp.getArgs());
        log.debug(LOGTAG + "请求路径【{}】", url);
        log.debug(LOGTAG + "IP地址【{}】", ip);
        log.debug(LOGTAG + "请求方法【{}】", controller.concat(ApplicationConstants.DOT).concat(methodname));
        log.debug(LOGTAG + "请求参数【{}】", args);
        try {
            Object response = pjp.proceed();
            return response;
        } catch (Throwable e) {
            log.debug(LOGTAG + "异常信息: " + e.getMessage());
            throw e;
        } finally {
            long duration = System.currentTimeMillis() - startTime.get();
            log.debug(LOGTAG + "花费耗时【{}】毫秒", duration);

        }
    }


}
