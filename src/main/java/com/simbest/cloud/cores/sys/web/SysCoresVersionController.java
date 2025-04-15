/*
 * 版权所有 © 北京晟壁科技有限公司 2008-2027。保留一切权利!
 */
package com.simbest.cloud.cores.sys.web;

import cn.hutool.http.HttpUtil;
import com.simbest.cloud.cores.config.AppConfig;
import com.simbest.cloud.cores.json.JacksonUtils;
import com.simbest.cloud.cores.response.JsonResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static com.simbest.cloud.cores.constants.ApplicationConstants.MSG_SUCCESS;


/**
 * 用途：组件版本控制器
 * 作者: lishuyi
 * 时间: 2019/12/6  10:01
 */
@Tag(name = "SysCoresVersionController",   description = "系统管理-组件版本控制器")
@Slf4j
@RestController
@RequestMapping("/version")
public class SysCoresVersionController {

    @Autowired
    private AppConfig appConfig;

    /**
     * GET请求获取各组件当前最新更新情况，目前包括cores、cmcc、bps、flowable，请求格式如下：
     * http://nginxIp:nginxPort/appcode/version/anonymous?url=http://nginxIp:nginxPort
     * 如：
     * http://localhost:6979/robot/version/anonymous?url=http://localhost:6979
     *
     * @param url
     * @return
     * @throws ExecutionException
     * @throws InterruptedException
     */
    @Operation(summary = "查看所有组件当前版本", description = "查看所有组件当前版本")
    @GetMapping(value = "/anonymous")
    public JsonResponse all(String url) throws ExecutionException, InterruptedException {
        String coresVersionUrl = url + "/"+appConfig.getContextPath()+"/version/anonymous/cores";
        String cmccVersionUrl = url + "/"+appConfig.getContextPath() + "/version/anonymous/cmcc";
        String flowableVersionUrl = url + "/"+appConfig.getContextPath() + "/version/anonymous/flowableDriver";
        String bpsVersionUrl = url + "/"+appConfig.getContextPath() + "/version/anonymous/bpsDriver";
        String mqVersionUrl = url + "/"+appConfig.getContextPath() + "/version/anonymous/mqDriver";
        String mqProcessVersionUrl = url + "/"+appConfig.getContextPath() + "/version/anonymous/processMqDriver";
        String historyVersionUrl = url + "/"+appConfig.getContextPath() + "/version/anonymous/historyDriver";

        String coresResult = getAsyncVersion(coresVersionUrl).get();
        String cmccResult = getAsyncVersion(cmccVersionUrl).get();
        String flowableResult = getAsyncVersion(flowableVersionUrl).get();
        String bpsResult = getAsyncVersion(bpsVersionUrl).get();
        String mqResult = getAsyncVersion(mqVersionUrl).get();
        String mqProcessResult = getAsyncVersion(mqProcessVersionUrl).get();
        String historyResult = getAsyncVersion(historyVersionUrl).get();

        Map<String, String> resultMap = new HashMap<String, String>(){
        {
            put("cores", coresResult);
            put("cmcc", cmccResult);
            put("flowable", flowableResult);
            put("bps", bpsResult);
            put("mq", mqResult);
            put("mqProcess", mqProcessResult);
            put("history", historyResult);
        }};
        return JsonResponse.success(resultMap, MSG_SUCCESS);
    }



    /**
     * GET请求获取Cores最新更新情况
     * http://url/appcode/version/anonymous/cores
     *
     * @return
     */
    @Operation(summary = "查看CORES组件当前版本", description = "查看CORES组件当前版本")
    @GetMapping(value = "/anonymous/cores")
    public JsonResponse version() {
        return JsonResponse.success(String.format("【%s】-【%s】", "2025-04-14", "本次升级内容：增加网关地址，并给一个gateway网关的默认值！"), MSG_SUCCESS);
    }



    public CompletableFuture<String> getAsyncVersion(String url) {
        return CompletableFuture.supplyAsync(() -> {
            String result = HttpUtil.createGet(url).execute().body();
            JsonResponse jsonResponse = JacksonUtils.json2obj(result, JsonResponse.class);
            if(null != jsonResponse){
                return jsonResponse.getData().toString();
            }
            else{
                return "";
            }
        });
    }

}
