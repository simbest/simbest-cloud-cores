/*
 * 版权所有 © 北京晟壁科技有限公司 2008-2027。保留一切权利!
 */
package com.simbest.cloud.cores.sys.web;


import com.simbest.cloud.cores.base.web.response.JsonResponse;
import com.simbest.cloud.cores.sys.service.ISimpleSmsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
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
@Tag(name = "SimpleSmsController", description = "系统管理-短信管理")
@Slf4j
@RestController
@RequestMapping("/sys/sms")
public class SimpleSmsController {

    @Autowired
    private ISimpleSmsService smsService;

    @Operation(summary = "发送随机验证码")
    @Parameters({
            @Parameter(name = "phone", description = "接收手机号", in = ParameterIn.QUERY, required = true),
            @Parameter(name = "randomCode", description = "随机数据码", in = ParameterIn.QUERY, required = true),
            @Parameter(name = "minutes", description = "有效分钟数", in = ParameterIn.QUERY, required = true
)
    })
    @PostMapping(value = {"/sendRandomCode", "/sso/sendRandomCode", "/api/sendRandomCode"})
    public JsonResponse sendRandomCode(@RequestParam String phone,
                                       @RequestParam @Schema(name ="随机数据码" ) String randomCode,
                                       @RequestParam @Schema(name ="有效分钟数" ) int minutes) {
        if (smsService.sendRandomCode(phone, randomCode, minutes)) {
            return JsonResponse.defaultSuccessResponse();
        } else {
            return JsonResponse.defaultErrorResponse();
        }
    }
}
