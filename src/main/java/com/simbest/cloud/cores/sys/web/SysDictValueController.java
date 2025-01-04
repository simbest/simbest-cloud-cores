/*
 * 版权所有 © 北京晟壁科技有限公司 2008-2027。保留一切权利!
 */
package com.simbest.cloud.cores.sys.web;

import com.simbest.cloud.cores.base.web.controller.LogicController;
import com.simbest.cloud.cores.exception.GlobalExceptionRegister;
import com.simbest.cloud.cores.response.JsonResponse;
import com.simbest.cloud.cores.sys.model.SysDictValue;
import com.simbest.cloud.cores.sys.service.ISysDictService;
import com.simbest.cloud.cores.sys.service.ISysDictValueService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

import static com.simbest.cloud.cores.constants.ApplicationConstants.MSG_SUCCESS;

/**
 * 用途：数据字典值控制器
 * 作者: zlxtk
 * 时间: 2018/2/23  10:14
 */
@Tag(name = "SysDictValueController", description = "系统管理-数据字典值管理")
@RestController
@RequestMapping("/sys/dictValue")
public class SysDictValueController extends LogicController<SysDictValue,String>{

    private ISysDictValueService sysDictValueService;

    @Autowired
    private ISysDictService dictService;

    @Autowired
    public SysDictValueController(ISysDictValueService sysDictValueService) {
        super(sysDictValueService);
        this.sysDictValueService=sysDictValueService;
    }

    /**
     * 新增一个字典值
     * @param sysDictValue
     * @return JsonResponse
     */
    //设置权限，后面再开启
    //@PreAuthorize ("hasAnyAuthority('ROLE_SUPER','ROLE_ADMIN')")
    @Operation(summary = "新增一个字典值", description = "新增一个字典值")
    public JsonResponse create(@RequestBody(required = false) SysDictValue sysDictValue) {
        try {
            sysDictValue = sysDictValueService.insert(sysDictValue);
            return JsonResponse.success(sysDictValue, MSG_SUCCESS);
        } catch (Exception e) {
            return GlobalExceptionRegister.returnErrorResponse(e);
        }
    }

    /**
     * 修改一个字典值
     * @param sysDictValue
     * @return JsonResponse
     */
    //设置权限，后面再开启
    //@PreAuthorize("hasAnyAuthority('ROLE_SUPER','ROLE_ADMIN')")
    @Operation(summary = "修改一个字典值", description = "修改一个字典值")
    public JsonResponse update( @RequestBody(required = false) SysDictValue sysDictValue) {
        try {
            sysDictValue = sysDictValueService.update(sysDictValue);
            return JsonResponse.success(sysDictValue,MSG_SUCCESS);
        } catch (Exception e) {
            return GlobalExceptionRegister.returnErrorResponse(e);
        }
    }

    /**
     * 根据id逻辑删除
     * @param id
     * @return JsonResponse
     */
    //@PreAuthorize("hasAnyAuthority('ROLE_SUPER','ROLE_ADMIN')")
    @Operation(summary = "根据id删除字典值", description = "根据id删除字典值")
    public JsonResponse deleteById(@RequestParam(required = false) String id) {
        try {
            sysDictValueService.deleteById(id);
            return JsonResponse.success(MSG_SUCCESS);
        } catch (Exception e) {
            return GlobalExceptionRegister.returnErrorResponse(e);
        }
    }

    /**
     * 先修改再逻辑删除字典值
     * @param sysDictValue
     * @return JsonResponse
     */
    @Operation(summary = "先修改再逻辑删除字典值", description = "先修改再逻辑删除字典值")
    public JsonResponse delete(SysDictValue sysDictValue) {
        try {
            sysDictValueService.delete(sysDictValue);
            return JsonResponse.success(MSG_SUCCESS);
        } catch (Exception e) {
            return GlobalExceptionRegister.returnErrorResponse(e);
        }
    }

    /**
     * 批量逻辑删除字典值
     * @param ids
     * @return JsonResponse
     */
    //@PreAuthorize("hasAuthority('ROLE_SUPER')")  // 指定角色权限才能操作方法
    @Operation(summary = "批量逻辑删除字典值", description = "批量逻辑删除字典值")
    public JsonResponse deleteAllByIds(@RequestBody(required = false) String[] ids) {
        try {
            sysDictValueService.deleteAllByIds(Arrays.asList(ids));
            return JsonResponse.success(MSG_SUCCESS);
        } catch (Exception e) {
            return GlobalExceptionRegister.returnErrorResponse(e);
        }
    }

    /**
     *修改可见
     * @param id
     * @param enabled
     * @return JsonResponse
     */
    @Operation(summary = "修改可见", description = "修改可见")
    @Override
    public JsonResponse updateEnable(@RequestParam(required = false) String id, @RequestParam(required = false) boolean enabled) {
        try {
            SysDictValue sysDictValue = sysDictValueService.updateEnable(id, enabled);
            return JsonResponse.success(sysDictValue, MSG_SUCCESS);
        } catch (Exception e) {
            return GlobalExceptionRegister.returnErrorResponse(e);
        }
    }

    //批量修改可见

    /**
     *根据id查询字典值
     * @param id
     * @return JsonResponse
     */
    @Operation(summary = "根据id查询字典值", description = "根据id查询字典值")
    @PostMapping(value = {"/findById","/findById/sso","/findById/api"})
    public JsonResponse findById(@RequestParam(required = false) String id) {
        return super.findById( id );
    }

    /**
     *获取字典值信息列表并分页
     * @param page
     * @param size
     * @param direction
     * @param properties
     * @param sysDictValue
     * @return JsonResponse
     */
    @Operation(summary = "获取字典值信息列表并分页", description = "获取字典值信息列表并分页")
    @PostMapping(value = {"/findAll","/findAll/sso","/findAll/api"})
    public JsonResponse findAll( @RequestParam(required = false, defaultValue = "1") int page, //
                                 @RequestParam(required = false, defaultValue = "10") int size, //
                                 @RequestParam(required = false) String direction, //
                                 @RequestParam(required = false) String properties, //
                                 @RequestBody(required = false) SysDictValue sysDictValue //
    ) {
        return super.findAll( page,size,direction, properties,sysDictValue);
    }

    /**
     * 新增子字典值
     * @param dictValue
     * @return JsonResponse
     */
    //@PreAuthorize("hasAuthority('ROLE_ADMIN')")  // 指定角色权限才能操作方法
    @PostMapping(value = "/createChild")
    @ResponseBody
    public JsonResponse createChild(@RequestBody(required = false) SysDictValue dictValue) {
        if (dictValue == null) {
            return JsonResponse.defaultErrorResponse();
        }
        dictValue.setParentId(dictValue.getId());
        dictValue.setId(null);
        SysDictValue newSysDictValue = sysDictValueService.insert(dictValue);
        return JsonResponse.defaultSuccessResponse();
    }

    /**
     *
     * @param sysDictValue
     * @return JsonResponse
     */
    @Operation(summary = "根据字典值对象查询满足条件的数据字典值，若提供上级数据字典值id，则直接返回所有字典值")
    @PostMapping(value = {"/findDictValue", "/findDictValue/sso", "/findDictValue/api", "/findDictValue/anonymous"})
    public JsonResponse findDictValue(@RequestBody(required = false) SysDictValue sysDictValue){
        return JsonResponse.success(sysDictValueService.findDictValue(sysDictValue));
    }

    /**
     *
     * @param dictType
     * @param name
     * @return JsonResponse
     */
    @Operation(summary = "根据字典类型和字典值名称，获取字典值")
    @PostMapping(value = {"/findByDictTypeAndName", "/findByDictTypeAndName/sso", "/findByDictTypeAndName/api"})
    public JsonResponse findByDictTypeAndName(@RequestParam String dictType, @RequestParam String name){
        return JsonResponse.success(sysDictValueService.findByDictTypeAndName(dictType, name));
    }

    /**
     *
     * @param dictType
     * @param name
     * @param blocid
     * @param corpid
     * @return JsonResponse
     */
    @Operation(summary = "根据字典类型和字典值名称，以及集团Id、企业Id，获取字典值")
    @PostMapping(value = {"/findByDictTypeAndNameAndBlocidAndCorpid", "/findByDictTypeAndNameAndBlocidAndCorpid/sso", "/findByDictTypeAndNameAndBlocidAndCorpid/api"})
    public JsonResponse findByDictTypeAndNameAndBlocidAndCorpid(@RequestParam String dictType, @RequestParam String name,
                                                                @RequestParam String blocid, @RequestParam String corpid){
        return JsonResponse.success(sysDictValueService.findByDictTypeAndNameAndBlocidAndCorpid(dictType, name, blocid, corpid));
    }

    /**
     *
     * @return JsonResponse
     */
    @Operation(summary = "查看数据字典的所有值", description = "查看数据字典的所有值")
    @PostMapping(value = {"/findAllDictValue", "/findAllDictValue/sso", "/findAllDictValue/api"})
    public JsonResponse findAllDictValue(){
        return JsonResponse.success(sysDictValueService.findAllDictValue());
    }

    /**
     *
     * @return JsonResponse
     */
    @Operation(summary = "查看数据字典的所有值, Map结构，key为dictType，value为字典值list")
    @PostMapping(value = {"/findAllDictValueMapList", "/findAllDictValueMapList/sso", "/findAllDictValueMapList/api"})
    public JsonResponse findAllDictValueMapList(){
        return JsonResponse.success(sysDictValueService.findAllDictValueMapList());
    }

    /**
     *
     * @return JsonResponse
     */
    @Operation(summary = "查看指定数据字典类型的字典值, Map结构，key为dictType，value为字典值list")
    @PostMapping(value = {"/findDictValueMapList", "/findDictValueMapList/sso", "/findDictValueMapList/api"})
    public JsonResponse findDictValueMapList(@RequestBody String[] typeList){
        return JsonResponse.success(sysDictValueService.findDictValueMapList(typeList));
    }


}
