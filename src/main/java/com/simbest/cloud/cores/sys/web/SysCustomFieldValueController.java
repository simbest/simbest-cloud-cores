/*
 * 版权所有 © 北京晟壁科技有限公司 2008-2027。保留一切权利!
 */
package com.simbest.cloud.cores.sys.web;


import com.simbest.cloud.cores.base.repository.Condition;
import com.simbest.cloud.cores.base.web.controller.LogicController;
import com.simbest.cloud.cores.base.web.response.JsonResponse;
import com.simbest.cloud.cores.sys.model.SysCustomFieldValue;
import com.simbest.cloud.cores.sys.model.SysCustomFieldValueDto;
import com.simbest.cloud.cores.sys.service.ISysCustomFieldService;
import com.simbest.cloud.cores.sys.service.ISysCustomFieldValueService;
import com.simbest.cloud.cores.sys.service.ISysDictService;
import com.simbest.cloud.cores.util.SecurityUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 用途：实体自定义字段值控制器
 * 作者: lishuyi
 * 时间: 2017/12/22  15:51
 */
@Tag(name = "SysCustomFieldValueController", description = "系统管理-自定义字段值管理")
@Slf4j
@RestController
@RequestMapping("/sys/sysfieldvalue")
public class SysCustomFieldValueController extends LogicController<SysCustomFieldValue, String> {

    @Autowired
    private ISysCustomFieldService fieldService;

    private ISysCustomFieldValueService fieldValueService;

    @Autowired
    private ISysDictService dictService;

    @Autowired
    public SysCustomFieldValueController(ISysCustomFieldValueService fieldValueService) {
        super(fieldValueService);
        this.fieldValueService=fieldValueService;
    }

    @PreAuthorize("hasAnyAuthority('ROLE_SUPER','ROLE_ADMIN')")
    @PostMapping(value = "getEntityValues")
    public JsonResponse getEntityValues(@RequestParam(required = true) String fieldClassify, //
                                        @RequestParam(required = true) Long fieldEntityId) {
        Condition c = new Condition();
        c.eq("fieldClassify", fieldClassify);
        c.eq("fieldEntityId", fieldEntityId);
        return JsonResponse.builder() //
                .errcode(JsonResponse.SUCCESS_CODE) //
                .message("查询成功！") //
                .data(fieldValueService.findAllNoPage(fieldValueService.getSpecification(c)))
                .build();
    }

    @PreAuthorize("hasAnyAuthority('ROLE_SUPER','ROLE_ADMIN')")
    @PostMapping(value = "/createDto")
    public JsonResponse createDto( @RequestBody SysCustomFieldValueDto fieldValues) {

        List<SysCustomFieldValue> fieldValuess = fieldValues.getSysfieldvalue();
        List<SysCustomFieldValue> fields =  new ArrayList<>(  );
        for(SysCustomFieldValue field  :fieldValuess){
            field.setCreator( SecurityUtils.getCurrentUserName());
            field.setModifier(SecurityUtils.getCurrentUserName());
            fields.add( field );
        }
          fieldValueService.saveAll(fields);
        return JsonResponse.defaultSuccessResponse();
    }

    @PreAuthorize("hasAnyAuthority('ROLE_SUPER','ROLE_ADMIN')")
    @PostMapping(value = "/updateDto")
    public JsonResponse updateDto(@RequestBody SysCustomFieldValueDto fieldValues) {
        List<SysCustomFieldValue> fieldValuess = fieldValues.getSysfieldvalue();
        List<SysCustomFieldValue> fields =  new ArrayList<>(  );
        for(SysCustomFieldValue field  :fieldValuess){
            field.setModifier(SecurityUtils.getCurrentUserName());
            if(null == field.getId()){
                field.setCreator( SecurityUtils.getCurrentUserName());
            }
            fields.add( field );
        }
        fieldValueService.saveAll(fields);
        return JsonResponse.defaultSuccessResponse();
    }

    @PreAuthorize("hasAnyAuthority('ROLE_SUPER','ROLE_ADMIN')")
    @Operation(summary = "根据id删除自定义字段值", description = "根据id删除自定义字段值")
    @Parameter(name = "id", description = "自定义字段值ID", in = ParameterIn.QUERY)
    public JsonResponse deleteById(@RequestParam(required = false) String id) {
        return super.deleteById( id );
    }

}
