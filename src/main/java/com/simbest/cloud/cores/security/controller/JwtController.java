package com.simbest.cloud.cores.security.controller;

import com.simbest.cloud.cores.json.jwt.pojo.JwtPojo;
import com.simbest.cloud.cores.redis.RedisJwtValidator;
import com.simbest.cloud.cores.response.JsonResponse;
import com.simbest.cloud.cores.utils.DateUtil;
import com.simbest.cloud.cores.utils.Rsa1024Util;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.DependsOn;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.simbest.cloud.cores.constants.ApplicationConstants.MSG_SUCCESS;

@RestController
@DependsOn({"rsa2048Util", "jwtDecoder"})
@RequestMapping("/jwt")
public class JwtController {

    public final static String JWT_PARAM = "jwt";

    @Resource
    private JwtDecoder jwtDecoder;

    /**
     * http://localhost:9003/authorder/jwt/getJwtExpiryDate
     * 基于Authorization   Bearer ${access_token}
     * @return
     */
    @RequestMapping(value = "/getJwtExpiryDate", method = {RequestMethod.GET, RequestMethod.POST})
    public JsonResponse getJwtExpiryDate(@RequestParam("subject") String subject, @RequestParam("jwtId") String jwtId) {
        Long expiryCache = RedisJwtValidator.getJwtExpiryDate(subject, jwtId);
        return JsonResponse.success(expiryCache, MSG_SUCCESS);
    }

    /**
     * http://localhost:9003/authorder/jwt/parseJwt
     * 基于Authorization   Bearer ${access_token}
     * @return
     */
    @PostMapping("/parseJwt")
    public JsonResponse parseJwt(@RequestBody Map<String,String> paramMap) throws InvocationTargetException, IllegalAccessException {
        Assert.notNull(paramMap.get(JWT_PARAM), "JWT令牌参数不能为空");
        //1、必须携带有效的token才能解析需要确认的token，这是RenewNimbusJwtDecoder中RedisJwtTimeServerValidator（或者RedisJwtTimeClientValidator）所要求的
        //2、该有效token的client信息必须在统一认证auth和所有微服务中双向注册
        Jwt jwt = jwtDecoder.decode(paramMap.get(JWT_PARAM));

        //获取可以拷贝的基本信息
        JwtPojo jwtPojo = new JwtPojo();
        BeanUtils.copyProperties(jwt, jwtPojo);
        Date issuedAt = Date.from(jwt.getIssuedAt());
        jwtPojo.setIssuedAt(DateUtil.getTimestamp(issuedAt));

        //获取实际到期时间
        String subject = jwt.getSubject();
        Assert.notNull(subject, "认证主题subject must not be null");
        String sub = Rsa1024Util.decryptCode(subject);
        Assert.notNull(subject, "认证主题subject must not be null");
        String jwtId = jwt.getId();
        Long expiryCache = RedisJwtValidator.getJwtExpiryDate(sub, jwtId);
        //缓存中如果没有过期时间，则以令牌携带的过期时间为准
        Date expiry = expiryCache == null ? Date.from(jwt.getExpiresAt()) : new Date(expiryCache);
        jwtPojo.setExpiresAt(DateUtil.getTimestamp(expiry));

        //覆盖令牌签发和到期中国区日期显示字符串
        Map<String, Object> mutableMap = new HashMap<>();
        mutableMap.putAll(jwtPojo.getClaims());
        mutableMap.put("nbf", jwtPojo.getIssuedAt());
        mutableMap.put("iat", jwtPojo.getIssuedAt());
        mutableMap.put("exp", jwtPojo.getExpiresAt());

        //追加额外信息
        mutableMap.put("jti", jwtId);
        mutableMap.put("sub", subject);

        jwtPojo.setClaims(mutableMap);
        return JsonResponse.success(jwtPojo, MSG_SUCCESS);
    }

}


