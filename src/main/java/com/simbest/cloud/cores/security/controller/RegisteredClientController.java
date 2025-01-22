package com.simbest.cloud.cores.security.controller;

import com.simbest.cloud.cores.exception.Exceptions;
import com.simbest.cloud.cores.response.JsonResponse;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.web.bind.annotation.*;

import static com.simbest.cloud.cores.constants.ApplicationConstants.MSG_ERROR;
import static com.simbest.cloud.cores.constants.ApplicationConstants.MSG_SUCCESS;

/**
 * @author lishuyi
 * @version 1.0
 * @since 2023/4/26
 */
@RestController
@RequestMapping("/client")
@RequiredArgsConstructor
public class RegisteredClientController {

    @Resource
    private final RegisteredClientRepository registeredClientRepository;

    /**
     * 获取Client令牌有效时间，单位：秒
     * http://localhost:9001/auth/client/anonymous/findClientTokenExpireSecond/robot
     * @param clientId
     * @return
     */
    @GetMapping("/anonymous/findClientTokenExpireSecond/{clientId}")
    public JsonResponse findClientTokenExpireSecond(@PathVariable("clientId") String clientId) {
        RegisteredClient client = registeredClientRepository.findByClientId(clientId);
        Long seconds = client == null ? null : client.getTokenSettings().getAccessTokenTimeToLive().getSeconds();
        return JsonResponse.success(seconds, MSG_SUCCESS);
    }

    @PostMapping("/findClient/{clientId}")
    public JsonResponse findClient(@PathVariable("clientId") String clientId) {
        RegisteredClient client = registeredClientRepository.findByClientId(clientId);
        return JsonResponse.success(client, MSG_SUCCESS);
    }

    @PostMapping("/createClient")
    public JsonResponse createClient(@RequestBody RegisteredClient client) {
        try {
            registeredClientRepository.save(client);
            return JsonResponse.defaultSuccessResponse();
        }
        catch (Exception e) {
            Exceptions.printException(e);
            return JsonResponse.fail(e.getMessage(), MSG_ERROR);
        }
    }

}
