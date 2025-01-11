package com.simbest.cloud.cores.redis;

import com.simbest.cloud.cores.response.JsonResponse;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

import static com.simbest.cloud.cores.constants.ApplicationConstants.MSG_SUCCESS;

@RestController
@RequestMapping("/jwt")
public class RedisJwtValidatorController {

    /**
     * http://localhost:9003/authorder/jwt/anonymous/getJwtExpiryDate
     * 可访问
     * @return
     */
    @RequestMapping(value = "/anonymous/getJwtExpiryDate", method = {RequestMethod.GET, RequestMethod.POST})
    public JsonResponse getJwtExpiryDate(@RequestParam("subject") String subject, @RequestParam("jwtId") String jwtId) {
        Date expiryCache = RedisJwtValidator.getJwtExpiryDate(subject, jwtId);
        return JsonResponse.success(expiryCache, MSG_SUCCESS);
    }


}


