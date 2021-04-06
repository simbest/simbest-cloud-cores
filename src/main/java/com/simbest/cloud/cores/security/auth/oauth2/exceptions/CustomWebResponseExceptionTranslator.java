/*
 * 版权所有 © 北京晟壁科技有限公司 2008-2027。保留一切权利!
 */
package com.simbest.cloud.cores.security.auth.oauth2.exceptions;

import com.simbest.cloud.cores.constants.ApplicationConstants;
import com.simbest.cloud.cores.utils.json.JacksonUtils;
import com.simbest.cloud.orguser.pojo.JsonResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.provider.error.DefaultWebResponseExceptionTranslator;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;

import static com.simbest.cloud.cores.security.auth.oauth2.OauthExceptionEntryPoint.SHOUD_LOGIN_1;
import static com.simbest.cloud.cores.security.auth.oauth2.OauthExceptionEntryPoint.SHOUD_LOGIN_2;
import static com.simbest.cloud.cores.security.auth.oauth2.OauthExceptionEntryPoint.SHOUD_REFRESH;

/**
 * 用途：自定义OAUTH2受保护的资源请求异常
 * 作者: lishuyi@simbest.com.cn
 * 时间: 2021/3/10  14:33
 */
@Slf4j
@Component
public class CustomWebResponseExceptionTranslator extends DefaultWebResponseExceptionTranslator {

    private HttpHeaders jsonHttpHeaders = new HttpHeaders(){{
        setContentType(MediaType.APPLICATION_JSON_UTF8);
        setAccept(new ArrayList<MediaType>(){{
            add(MediaType.APPLICATION_JSON_UTF8);
        }});
    }};

    /**
     * 异常将由CustomOauthExceptionSerializer进行处理
     * @param e
     * @return ResponseEntity
     */
    @Override
    public ResponseEntity translate(Exception e) {
        log.warn("OAuth2 认证过程出了点问题，即将组装返回的错误信息【{}】", e.getMessage());
        if(e instanceof OAuth2Exception) {
            OAuth2Exception oAuth2Exception = (OAuth2Exception) e;
            if(StringUtils.isNotEmpty(oAuth2Exception.getMessage())){
                String[] result = StringUtils.split(oAuth2Exception.getMessage(), ApplicationConstants.VERTICAL);
                if(null != result && result.length == 2){
                    return ResponseEntity
                            .status(oAuth2Exception.getHttpErrorCode())
                            //解析WxmaCodeAuthenticationProvider和WxmaMiniAuthenticationProvider抛出在CustomOauthException定义的OAUTH2_LOGIN_ERROR和OAUTH2_MINI_ERROR错误
                            .body(new CustomOauthException(Integer.parseInt(result[0]), result[1]));
                }
            }
            if (SHOUD_LOGIN_1.equals(oAuth2Exception.getMessage()) ||
                    oAuth2Exception.getMessage().startsWith(SHOUD_LOGIN_2)) {
                return ResponseEntity
                        .status(HttpStatus.UNAUTHORIZED)
                        .body(new CustomOauthException(HttpStatus.UNAUTHORIZED.value(), oAuth2Exception.getMessage()));
            } else if (oAuth2Exception.getMessage().startsWith(SHOUD_REFRESH)) {
                return ResponseEntity
                        .status(HttpStatus.FORBIDDEN)
                        .body(new CustomOauthException(HttpStatus.FORBIDDEN.value(), oAuth2Exception.getMessage()));
            }
            else {
                return ResponseEntity
                        .status(oAuth2Exception.getHttpErrorCode())
                        .body(new CustomOauthException(oAuth2Exception.getMessage()));
            }
        }
        else {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            JsonResponse jsonResponse = JsonResponse.builder()
                    .errcode(HttpStatus.FORBIDDEN.value())
                    .status(HttpStatus.FORBIDDEN.value())
                    .error(HttpStatus.FORBIDDEN.name())
                    .message(e.getMessage())
                    .timestamp(new Date())
                    .path(request.getServletPath())
                    .build();

            return new ResponseEntity(JacksonUtils.obj2json(jsonResponse), jsonHttpHeaders, HttpStatus.FORBIDDEN);

//            return ResponseEntity
//                    .status(Integer.parseInt(OAUTH2_FORBIDDEN))
//                    .body(new OAuth2Exception(e.getMessage()));
        }

    }
}
