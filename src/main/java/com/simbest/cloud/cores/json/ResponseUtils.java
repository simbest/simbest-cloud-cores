package com.simbest.cloud.cores.json;

import cn.hutool.json.JSONUtil;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.io.IOException;

/**
 * @author lishuyi
 * @version 1.0
 * @since 2023/4/18
 */
public class ResponseUtils {

    public static void buildResponse(HttpServletResponse response, Object result, HttpStatus httpStatus) throws IOException {
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE); // 返回JSON
        response.setStatus(httpStatus.value());  // 状态码
        response.getWriter().write(JSONUtil.toJsonStr(result));
    }
}
