/*
 * 版权所有 © 北京晟壁科技有限公司 2008-2027。保留一切权利!
 */
package com.simbest.cloud.cores.sys.web;

import com.simbest.cloud.cores.base.repository.Condition;
import com.simbest.cloud.cores.base.web.controller.LogicController;
import com.simbest.cloud.cores.response.JsonResponse;
import com.simbest.cloud.cores.sys.model.SysCustomFieldValue;
import com.simbest.cloud.cores.sys.model.SysCustomFieldValueDto;
import com.simbest.cloud.cores.sys.service.ISysCustomFieldService;
import com.simbest.cloud.cores.sys.service.ISysCustomFieldValueService;
import com.simbest.cloud.cores.sys.service.ISysDictService;
import com.simbest.cloud.cores.security.utils.SecurityUtils;
import io.swagger.v3.oas.annotations.Operation;
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
    public JsonResponse deleteById(@RequestParam(required = false) String id) {
        return super.deleteById( id );
    }
//
//    @Operation(summary = "获取自定义字段列表", description = "通过此接口来获取某实体类型自定义字段")
//    @ApiImplicitParams({ //
//            @ApiImplicitParam(name = "page", value = "当前页码", dataType = "int", paramType = "query", //
//                    required = true, example = "1"), //
//            @ApiImplicitParam(name = "size", value = "每页数量", dataType = "int", paramType = "query", //
//                    required = true, example = "10"), //
//    })
//    @PostMapping(value = "getSysCustomFieldsByFieldClassify")
//    public JsonResponse getSysCustomFieldsByFieldClassify(@RequestParam(required = false, defaultValue = "1") int page, //
//                                       @RequestParam(required = false, defaultValue = "10") int size, //
//                                       @RequestParam(required = true) String fieldClassify, //
//                                       @RequestParam(required = false, defaultValue = "-1") long fieldEntityId //
//    ) {
//        // 获取分页规则
//        Pageable pageable = fieldRepository.getPageable(page, size, null, null);
//
//        // 获取查询条件
//        Condition condition = new Condition();
//        condition.eq("fieldClassify", fieldClassify);
//
//        Specification<SysCustomField> s = fieldRepository.getSpecification(condition);
//
//        // 获取查询结果
//        Page<SysCustomField> pages = fieldRepository.findAll(s, pageable);
//
//        // 构成返回信息
//        Map<String, Object> searchD = new HashMap<>();
//        searchD.put("fieldClassify", fieldClassify);
//        SysCustomField param = new SysCustomField();
//        param.setFieldClassify(fieldClassify);
//        searchD.put("fieldClassifyCn", param.getFieldClassifyCn());
//
//        Map<String, Object> map = new HashMap<>();
//        map.put("totalSize", pages.getTotalElements());
//        map.put("totalPage", pages.getTotalPages());
//        map.put("list", pages.getContent());
//        map.put("size", pages.getSize());
//        map.put("page", page);
//        map.put("searchD", searchD);
//
//        if (fieldEntityId != -1) {
//            Condition condition1 = new Condition();
//            condition1.eq("fieldClassify", fieldClassify);
//            condition1.eq("fieldEntityId", fieldEntityId);
//            Specification<SysCustomFieldValue> s1 = fieldValueService.getSpecification(condition1);
//            List<SysCustomFieldValue> values = fieldValueService.findAll(s1);
//            map.put("values", values);
//        }
//
//        JsonResponse res = JsonResponse.builder() //
//                .errcode(JsonResponse.SUCCESS_CODE) //
//                .errmsg("查询成功！") //
//                .data(map)
//                .build();
//        return res;
//    }
}
