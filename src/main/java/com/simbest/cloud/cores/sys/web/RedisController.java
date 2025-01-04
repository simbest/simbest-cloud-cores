/*
 * 版权所有 © 北京晟壁科技有限公司 2008-2027。保留一切权利!
 */
package com.simbest.cloud.cores.sys.web;

import com.google.common.collect.Maps;
import com.simbest.cloud.cores.component.distributed.lock.AppRuntimeMaster;
import com.simbest.cloud.cores.constants.ApplicationConstants;
import com.simbest.cloud.cores.response.JsonResponse;
import com.simbest.cloud.cores.redis.RedisUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Set;

import static com.simbest.cloud.cores.constants.ApplicationConstants.MSG_SUCCESS;

/**
 * 用途：定时任务日志记录控制器
 * 作者: lishuyi
 * 时间: 2018/2/22  10:14
 */
@Tag(name = "RedisController", description  = "系统管理-Redis缓存管理")
@RestController
@RequestMapping("/sys/redis")
public class RedisController {

    @Autowired
    private AppRuntimeMaster appRuntimeMaster;

    @Operation(summary = "模糊查找Key键")
    @PreAuthorize("hasAnyAuthority('ROLE_SUPER','ROLE_ADMIN')")
    @PostMapping("/findKeys")
    public JsonResponse findKeys(String pattern) {
        Set<String> keys = RedisUtil.globalKeys(ApplicationConstants.STAR+pattern+ ApplicationConstants.STAR);
        return JsonResponse.success(keys, MSG_SUCCESS);
    }

    @Operation(summary = "根据Key键查询Value并转换为String")
    @PreAuthorize("hasAnyAuthority('ROLE_SUPER','ROLE_ADMIN')")
    @PostMapping("/findValueByKeys")
    public JsonResponse findValueByKeys(String key) {
        String value = RedisUtil.getRedisTemplate().opsForValue().get(key);
        return JsonResponse.success(value, MSG_SUCCESS);
    }

    @Operation(summary = "精确通过Key键删除当前应用内的缓存", description = "注意RedisUtil会为应用增加编码前缀")
    @PreAuthorize("hasAnyAuthority('ROLE_SUPER','ROLE_ADMIN')")
    @PostMapping("/delCache")
    public JsonResponse delCache(String key) {
        Long number = RedisUtil.delete(key) == true ? 1L : 0L;
        Map<String, Long> delCache = Maps.newHashMap();
        delCache.put("caches", number);
        return JsonResponse.success(delCache, MSG_SUCCESS);
    }

    @Operation(summary = "精确通过Key键删除全局的缓存")
    @PreAuthorize("hasAnyAuthority('ROLE_SUPER','ROLE_ADMIN')")
    @PostMapping("/delCacheGlobal")
    public JsonResponse delCacheGlobal(String key) {
        Long number = RedisUtil.deleteGlobal(key) == true ? 1L : 0L;
        Map<String, Long> delCache = Maps.newHashMap();
        delCache.put("caches", number);
        return JsonResponse.success(delCache, MSG_SUCCESS);
    }

    @Operation(summary = "模糊通过Key键删除当前应用内的缓存", description = "注意是末尾加*的模糊删除")
    @PreAuthorize("hasAnyAuthority('ROLE_SUPER','ROLE_ADMIN')")
    @PostMapping("/mulDeleteCache")
    public JsonResponse mulDeleteCache(String pattern) {
        Long number = RedisUtil.mulDelete(pattern);
        Map<String, Long> delCache = Maps.newHashMap();
        delCache.put("caches", number);
        return JsonResponse.success(delCache, MSG_SUCCESS);
    }

    @Operation(summary = "模糊通过Key键删除全局的缓存", description = "注意是末尾加*的模糊删除")
    @PreAuthorize("hasAnyAuthority('ROLE_SUPER','ROLE_ADMIN')")
    @PostMapping("/mulDeleteCacheGlobal")
    public JsonResponse mulDeleteCacheGlobal(String pattern) {
        Long number = RedisUtil.mulDeleteGlobal(pattern);
        Map<String, Long> delCache = Maps.newHashMap();
        delCache.put("caches", number);
        return JsonResponse.success(delCache, MSG_SUCCESS);
    }

    @Operation(summary = "获取当前Redis集群中的主控节点", description = "注意是末尾加*的模糊删除")
    @PreAuthorize("hasAnyAuthority('ROLE_SUPER','ROLE_ADMIN')")
    @RequestMapping(value = {"/getCurrentMaster", "/getCurrentMaster/sso"}, method = {RequestMethod.POST, RequestMethod.GET})
    public JsonResponse getCurrentMaster() {
        return JsonResponse.success(appRuntimeMaster.getCurrentMaster(), MSG_SUCCESS);
    }


    @Operation(summary = "设置某主机作为MASTER", description = "设置某主机作为MASTER")
    @PreAuthorize("hasAnyAuthority('ROLE_SUPER','ROLE_ADMIN')")
    @RequestMapping(value = {"/makeMeAsMaster", "/makeMeAsMaster/sso"}, method = {RequestMethod.POST, RequestMethod.GET})
    public JsonResponse makeMeAsMaster(@RequestParam String host, @RequestParam Integer port) {
        appRuntimeMaster.makeMeAsMaster(host, port);
        return JsonResponse.defaultSuccessResponse();
    }

}
