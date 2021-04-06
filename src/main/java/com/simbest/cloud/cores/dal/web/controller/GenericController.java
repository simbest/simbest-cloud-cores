/*
 * 版权所有 © 北京晟壁科技有限公司 2008-2027。保留一切权利!
 */
package com.simbest.cloud.cores.dal.web.controller;

import com.simbest.cloud.cores.dal.model.GenericModel;
import com.simbest.cloud.cores.dal.service.IGenericService;
import com.simbest.cloud.cores.exceptions.GlobalExceptionRegister;
import com.simbest.cloud.cores.feign.GenericFeignClient;
import com.simbest.cloud.cores.utils.CustomBeanUtil;
import com.simbest.cloud.cores.utils.ObjectUtil;
import com.simbest.cloud.orguser.pojo.JsonResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.io.Serializable;
import java.util.Arrays;

/**
 * 用途：通用类对象控制器
 * 作者: lishuyi@simbest.com.cn
 * 时间: 2021/3/10  11:50
 */
@Slf4j
public class GenericController<T extends GenericModel, PK extends Serializable> implements GenericFeignClient<T, PK> {

    private IGenericService<T, PK> service;

    public GenericController(IGenericService<T, PK> service) {
        this.service = service;
    }

    @Override
    public JsonResponse findById(PK id) {
        return JsonResponse.success(service.findById(id));
    }

    @Override
    public JsonResponse findOne(T o) {
        T data = service.findOne(service.getSpecification(o));
        return JsonResponse.success(data);
    }

    @Override
    public JsonResponse findAll(int page, int size, String direction, String properties, T o) {
        Pageable pageable = service.getPageable(page, size, direction, properties);
        Page<T> pages = service.findAll(service.getSpecification(o), pageable);
        return JsonResponse.success(pages);
    }

    @Override
    public JsonResponse findAllNoPage(T o) {
        Iterable<T> datas = service.findAllNoPage(service.getSpecification(o));
        return JsonResponse.success(datas);
    }

    @Override
    public JsonResponse findAllSortNoPage(String direction,
                                          String properties,
                                          T o) {
        Sort sort = Sort.by(Sort.Direction.fromString(direction), properties);
        Iterable<T> datas = service.findAllNoPage(service.getSpecification(o), sort);
        return JsonResponse.success(datas);
    }

    @Override
    public JsonResponse create(T o) {
        try {
            o = service.insert(o);
            return JsonResponse.success(o);
        } catch (Exception e) {
            return GlobalExceptionRegister.returnErrorResponse(e);
        }
    }

    @Override
    public JsonResponse update(T o) {
        T oldObj = service.findById((PK) ObjectUtil.getEntityIdVaue(o));
        CustomBeanUtil.copyPropertiesIgnoreNull(o, oldObj);
        try {
            oldObj = service.update(oldObj);
            return JsonResponse.success(oldObj);
        } catch (Exception e) {
            return GlobalExceptionRegister.returnErrorResponse(e);
        }
    }

    @Override
    public JsonResponse deleteById(PK id) {
        try {
            service.deleteById(id);
            return JsonResponse.defaultSuccessResponse();
        } catch (Exception e) {
            return GlobalExceptionRegister.returnErrorResponse(e);
        }
    }

    @Override
    public JsonResponse delete(T o) {
        try {
            service.delete(o);
            return JsonResponse.defaultSuccessResponse();
        } catch (Exception e) {
            return GlobalExceptionRegister.returnErrorResponse(e);
        }
    }

    @Override
    public JsonResponse deleteAllByIds(PK[] ids) {
        try {
            service.deleteAllByIds(Arrays.asList(ids));
            return JsonResponse.defaultSuccessResponse();
        } catch (Exception e) {
            return GlobalExceptionRegister.returnErrorResponse(e);
        }
    }


}
