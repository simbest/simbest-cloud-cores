/*
 * 版权所有 © 北京晟壁科技有限公司 2008-2027。保留一切权利!
 */
package com.simbest.cloud.cores.sys.web;


import com.simbest.cloud.cores.response.JsonResponse;
import com.simbest.cloud.cores.sys.service.ISimpleSmsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用途：简单短信接口控制器
 * 作者: lishuyi
 * 时间: 2019/8/10  9:16
 */
@Tag(name  = "SimpleSmsController", description = "系统管理-短信管理")
@Slf4j
@RestController
@RequestMapping("/sys/sms")
public class SimpleSmsController {

    @Autowired
    private ISimpleSmsService smsService;

    @Operation(summary = "发送随机验证码")
    @PostMapping(value = {"/sendRandomCode", "/sso/sendRandomCode", "/api/sendRandomCode"})
    public JsonResponse sendRandomCode(@RequestParam String phone,
                                       @RequestParam String randomCode,
                                       @RequestParam int minutes) {
        if (smsService.sendRandomCode(phone, randomCode, minutes)) {
            return JsonResponse.defaultSuccessResponse();
        } else {
            return JsonResponse.defaultErrorResponse();
        }
    }
}
