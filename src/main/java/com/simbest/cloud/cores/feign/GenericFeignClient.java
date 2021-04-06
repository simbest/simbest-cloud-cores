/*
 * 版权所有 © 北京晟壁科技有限公司 2008-2027。保留一切权利!
 */
package com.simbest.cloud.cores.feign;

import com.simbest.cloud.cores.dal.model.GenericModel;
import com.simbest.cloud.orguser.pojo.JsonResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.Serializable;

/**
 * 用途：通用类对象Feign接口
 * 作者: lishuyi@simbest.com.cn
 * 时间: 2021/3/15  18:27
 */
public interface GenericFeignClient<T extends GenericModel, PK extends Serializable> {

    @PostMapping("/api/findById")
    JsonResponse findById(@RequestParam(name = "id") PK id);

    @PostMapping("/api/findOne")
    JsonResponse findOne(@RequestBody T o);

    @PostMapping("/api/findAll")
    JsonResponse findAll(@RequestParam(name = "page", required = false, defaultValue = "1") int page, // 页码
                         @RequestParam(name = "size", required = false, defaultValue = "10") int size, // 页容量
                         @RequestParam(name = "direction", required = false) String direction, // 排序方向
                         @RequestParam(name = "properties", required = false) String properties, // 排序属性
                         @RequestBody T o);  //查询参数

    @PostMapping("/api/findAllNoPage")
    JsonResponse findAllNoPage(@RequestBody T o);

    @PostMapping("/api/findAllSortNoPage")
    JsonResponse findAllSortNoPage(@RequestParam(name = "direction", required = false) String direction,
                                   @RequestParam(name = "properties", required = false) String properties,
                                   @RequestBody T o);

    @PostMapping("/api/create")
    JsonResponse create(@RequestBody T o);

    @PostMapping("/api/update")
    JsonResponse update(@RequestBody T o);

    @PostMapping("/api/deleteById")
    JsonResponse deleteById(@RequestParam(name = "id") PK id);

    @PostMapping("/api/delete")
    JsonResponse delete(@RequestBody T o);

    @PostMapping("/api/deleteAllByIds")
    JsonResponse deleteAllByIds(@RequestBody PK[] ids);
}
