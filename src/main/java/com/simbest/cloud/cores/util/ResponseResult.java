package com.simbest.cloud.cores.util;

import cn.hutool.json.JSONUtil;
import com.simbest.cloud.feign.uums.util.JsonResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.codec.Charsets;
import org.springframework.http.MediaType;

import java.io.IOException;

/**
 * @author yanqi
 */
public class ResponseResult {

    public static void exceptionResponse(HttpServletRequest request, HttpServletResponse response, Exception exception) throws IOException {

        JsonResponse jsonResponse = JsonResponse.unauthorized(request, exception);
        String jsonStr = JSONUtil.toJsonStr(jsonResponse);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(Charsets.UTF_8.name());
        response.getWriter().print(jsonStr);
    }

    public static void exceptionResponse(HttpServletResponse response, String exceptionMessage) throws IOException {

        JsonResponse jsonResponse = JsonResponse.unauthorized(exceptionMessage);
        String jsonStr = JSONUtil.toJsonStr(jsonResponse);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(Charsets.UTF_8.name());
        response.getWriter().print(jsonStr);
    }

}