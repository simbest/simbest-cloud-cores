/*
 * 版权所有 © 北京晟壁科技有限公司 2008-2027。保留一切权利!
 */
package com.simbest.cloud.exclude.web;


import com.simbest.cloud.cores.common.web.response.JsonResponse;
import com.simbest.cloud.cores.util.json.GetJsonRequestUtil;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.autoconfigure.web.servlet.error.AbstractErrorController;
import org.springframework.boot.autoconfigure.web.servlet.error.BasicErrorController;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorViewResolver;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

import static org.springframework.http.HttpStatus.NOT_FOUND;

/**
 * 用途：统一异常处理 参考：https://blog.csdn.net/king_is_everyone/article/details/53080851
 * 作者: lishuyi
 * 时间: 2018/5/15  22:03
 */
@Tag(name = "GlobalErrorController", description = "系统管理-全局异常日志管理")
@Slf4j
@RequestMapping("${server.error.path:${error.path:/error}}")
public class GlobalErrorController extends AbstractErrorController {

    private final ErrorProperties errorProperties;

    /**
     * Create a new {@link BasicErrorController} instance.
     *
     * @param errorAttributes    the error attributes
     * @param errorProperties    configuration properties
     * @param errorViewResolvers error view resolvers
     */
    public GlobalErrorController(ErrorAttributes errorAttributes,
                                 ServerProperties errorProperties, List<ErrorViewResolver> errorViewResolvers) {
        super(errorAttributes, errorViewResolvers);
        Assert.notNull(errorProperties, "ErrorProperties must not be null");
        this.errorProperties = errorProperties.getError();
    }


    /**
     * 处理错误页面
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(produces = "text/html")
    public ModelAndView errorHtml(HttpServletRequest request,
                                  HttpServletResponse response) {
        logErrorInformation(request);
        HttpStatus status = getStatus(request);
        Map<String, Object> model = getErrorAttributes(
                request, getErrorAttributeOptions(request, MediaType.TEXT_HTML));
        model.put("errcode", JsonResponse.ERROR_CODE);
        response.setStatus(status.value());
        ModelAndView modelAndView = resolveErrorView(request, response, status, model);
        return modelAndView == null ? new ModelAndView("error", model) : modelAndView;
    }

    /**
     * 处理Restful请求Json数据
     * @param request
     * @return
     */
    @RequestMapping
    @ResponseBody
    public ResponseEntity<Map<String, Object>> error(HttpServletRequest request) {
        logErrorInformation(request);
        Map<String, Object> body = getErrorAttributes(request,
                getErrorAttributeOptions(request, MediaType.ALL));
        body.put("errcode", JsonResponse.ERROR_CODE);
        HttpStatus status = getStatus(request);
        return new ResponseEntity(body, status);
    }

    /**
     * Determine if the stacktrace attribute should be included.
     *
     * @param request  the source request
     * @param produces the media type produced (or {@code MediaType.ALL})
     * @return if the stacktrace attribute should be included
     */
    protected boolean isIncludeStackTrace(HttpServletRequest request,
                                          MediaType produces) {
        switch (getErrorProperties().getIncludeStacktrace()) {
            case ALWAYS:
                return true;
            case ON_PARAM:
            case NEVER:
                return getTraceParameter(request);
            default:
                return false;
        }
    }

    protected ErrorAttributeOptions getErrorAttributeOptions(HttpServletRequest request, MediaType mediaType) {
        ErrorAttributeOptions options = ErrorAttributeOptions.defaults();
        //server.error.includeException，是否输出异常类名
        if (this.errorProperties.isIncludeException()) {
            options = options.including(ErrorAttributeOptions.Include.EXCEPTION);
        }
        //是否打印堆栈，server.error.includeStacktrace= IncludeStacktrace.NEVER
        if (isIncludeStackTrace(request, mediaType)) {
            options = options.including(ErrorAttributeOptions.Include.STACK_TRACE);
        }
        //是否输出message
        if (isIncludeMessage(request, mediaType)) {
            options = options.including(ErrorAttributeOptions.Include.MESSAGE);
        }
        //和上面是使用的同一个配置项
/*        if (isIncludeBindingErrors(request, mediaType)) {
            options = options.including(ErrorAttributeOptions.Include.BINDING_ERRORS);
        }*/
        return options;
    }
    //添加了一个新的配置项，2.2.0是直接输出的
//server.error.includeMessage=IncludeAttribute.NEVER;
    protected boolean isIncludeMessage(HttpServletRequest request, MediaType produces) {
        switch (getErrorProperties().getIncludeMessage()) {
            case ALWAYS:
                return true;
            case ON_PARAM:
                return getMessageParameter(request);
            default:
                return false;
        }
    }


    /**
     * Provide access to the error properties.
     *
     * @return the error properties
     */
    protected ErrorProperties getErrorProperties() {
        return this.errorProperties;
    }

    public void logErrorInformation(HttpServletRequest request){
        HttpStatus status = getStatus(request);
        log.error("Access Error Attention, httpstatus name 【{}】 code 【{}】 url 【{}】, AEA请注意,请求响应发生异常!!!",
                status.name(), status.value(), null == request.getRequestURL() ? request.getRequestURI() : request.getRequestURL().toString());
        Map<String, Object> body = getErrorAttributes(request, getErrorAttributeOptions(request, MediaType.ALL));
        if(NOT_FOUND.equals(status)){
            log.warn("请注意所访问URL【{}】发生404错误，资源地址不存在！", body.get("path"));
        }
        log.error("HTTP请求错误发生时的请求信息如下：$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
        printErrorRequest(request);
        log.error("\r\n");
        log.error("HTTP请求错误发生后的响应信息如下：@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
        for (Map.Entry<String, Object> entry : body.entrySet()) {
            log.error("Error body key 【{}】 value 【{}】", entry.getKey(), entry.getValue());
        }

    }

    public static void printErrorRequest(HttpServletRequest request){
        if (null != request.getCookies() && request.getCookies().length > 0) {
            for (int i = 0; i < request.getCookies().length; i++) {
                log.error("Cookie 名称【{}】 值【{}】", i, request.getCookies()[i]);
            }
        }
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String header = headerNames.nextElement();
            log.error("Header 参数【{}】 值【{}】", header, request.getHeader(header));
        }
        log.error("\r\n");
        Enumeration<String> parameterNames = request.getParameterNames();
        while (parameterNames.hasMoreElements()) {
            String parameter = parameterNames.nextElement();
            log.error("URL 参数【{}】 值 【{}】", parameter, request.getParameter(parameter));
        }
        log.error("\r\n");
        try {
            final String requestJsonString = GetJsonRequestUtil.getRequestJsonString(request);
            log.error("JSON 参数【{}】", requestJsonString);
        } catch (IOException e) {
        }

    }
}
