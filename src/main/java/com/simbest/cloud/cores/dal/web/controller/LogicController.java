/*
 * 版权所有 © 北京晟壁科技有限公司 2008-2027。保留一切权利!
 */
package com.simbest.cloud.cores.dal.web.controller;

import com.simbest.cloud.cores.dal.model.LogicModel;
import com.simbest.cloud.cores.dal.service.ILogicService;
import com.simbest.cloud.orguser.pojo.JsonResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.Serializable;

/**
 * 用途：通用类对象控制器
 * 作者: lishuyi@simbest.com.cn
 * 时间: 2021/3/10  12:03
 */
public class LogicController<T extends LogicModel, PK extends Serializable> extends GenericController<T, PK>{

    private ILogicService<T, PK> service;

    public LogicController(ILogicService<T, PK> service) {
        super(service);
        this.service = service;
    }

    @PostMapping("/api/updateEnable")
    public JsonResponse updateEnable(@RequestParam PK id, @RequestParam boolean enabled) {
        return JsonResponse.success(service.updateEnable(id, enabled));
    }



}
