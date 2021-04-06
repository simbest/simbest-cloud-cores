/*
 * 版权所有 © 北京晟壁科技有限公司 2008-2027。保留一切权利!
 */
package com.simbest.cloud.cores.feign;

import com.simbest.cloud.cores.dal.model.LogicModel;
import com.simbest.cloud.orguser.pojo.JsonResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.Serializable;

/**
 * 用途：逻辑类对象Feign接口
 * 作者: lishuyi@simbest.com.cn
 * 时间: 2021/3/15  18:27
 */
public interface LogicFeignClient<T extends LogicModel, PK extends Serializable> extends GenericFeignClient<T, PK> {

    @PostMapping("/api/updateEnable")
    JsonResponse updateEnable(@RequestParam(name="id") PK id, @RequestParam(name="enabled") boolean enabled);

}
