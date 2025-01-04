package com.simbest.cloud.cores.sys.web;

import com.simbest.cloud.cores.base.web.controller.LogicController;
import com.simbest.cloud.cores.exception.GlobalExceptionRegister;
import com.simbest.cloud.cores.response.JsonResponse;
import com.simbest.cloud.cores.sys.model.SysDict;
import com.simbest.cloud.cores.sys.service.ISysDictService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

import static com.simbest.cloud.cores.constants.ApplicationConstants.MSG_SUCCESS;
import static com.simbest.cloud.cores.response.JsonResponse.SUCCESS_CODE;


/**
 * 用途：数据字典控制器
 * 作者: zlxtk
 * 时间: 2018/2/22  10:14
 */
@Tag(name = "SysDictController", description = "系统管理-数据字典管理")
@RestController
@RequestMapping("/sys/dict")
public class SysDictController extends LogicController<SysDict, String> {
    private ISysDictService sysDictService;
    @Autowired
    public SysDictController(ISysDictService sysDictService) {
        super(sysDictService);
        this.sysDictService=sysDictService;
    }

    /**
     * 新增一个字典类型
     * @param sysDict
     * @return JsonResponse
     */
    //设置权限，后面再开启
    //@PreAuthorize ("hasAnyAuthority('ROLE_SUPER','ROLE_ADMIN')")
    @Operation(summary = "新增一个字典类型", description = "新增一个字典类型")
    public JsonResponse create(@RequestBody(required = false) SysDict sysDict) {
        try {
            sysDict = sysDictService.insert(sysDict);
            return JsonResponse.success(sysDict,MSG_SUCCESS);
        } catch (Exception e) {
            return GlobalExceptionRegister.returnErrorResponse(e);
        }
    }

    /**
     * 修改一个字典类型
     * @param sysDict
     * @return JsonResponse
     */
    //设置权限，后面再开启
    //@PreAuthorize("hasAnyAuthority('ROLE_SUPER','ROLE_ADMIN')")
    @Operation(summary = "修改一个字典类型", description = "修改一个字典类型")
    public JsonResponse update( @RequestBody(required = false) SysDict sysDict) {
        try {
            sysDict = sysDictService.update(sysDict);
            return JsonResponse.success(sysDict,MSG_SUCCESS);
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
    @Operation(summary = "根据id删除字典类型", description = "根据id删除字典类型")
    public JsonResponse deleteById(@RequestParam(required = false) String id) {
        try {
            sysDictService.deleteById(id);
            return JsonResponse.success(MSG_SUCCESS);
        } catch (Exception e) {
            return GlobalExceptionRegister.returnErrorResponse(e);
        }
    }

    /**
     * 先修改再逻辑删除字典类型
     * @param sysDict
     * @return JsonResponse
     */
    @Operation(summary = "先修改再逻辑删除字典类型", description = "先修改再逻辑删除字典类型")
    public JsonResponse delete(@RequestBody(required = false) SysDict sysDict) {
        try {
            sysDictService.delete(sysDict);
            return JsonResponse.success(MSG_SUCCESS);
        } catch (Exception e) {
            return GlobalExceptionRegister.returnErrorResponse(e);
        }
    }

    /**
     * 批量逻辑删除字典类型
     * @param ids
     * @return JsonResponse
     */
    //@PreAuthorize("hasAuthority('ROLE_SUPER')")  // 指定角色权限才能操作方法
    @Operation(summary = "批量逻辑删除字典类型", description = "批量逻辑删除字典类型")
    public JsonResponse deleteAllByIds(@RequestBody(required = false) String[] ids) {
        try {
            sysDictService.deleteAllByIds(Arrays.asList(ids));
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
            SysDict sysDict = sysDictService.updateEnable(id, enabled);
            return JsonResponse.success(sysDict, MSG_SUCCESS);
        } catch (Exception e) {
            return GlobalExceptionRegister.returnErrorResponse(e);
        }
    }

    //批量修改可见

//    /**
//     * 注释原因：点击编辑字典时，不应该马上就提示操作成功
//     *
//     * 根据id查询字典类型信息
//     * @param id
//     * @return JsonResponse
//     */
//    @Operation(summary = "根据id查询字典类型信息", description = "根据id查询字典类型信息")
//    @ApiImplicitParam(name = "id", value = "字典类型ID", dataType = "String", paramType = "query")
//    @PostMapping(value = {"/findById","/findById/sso"})
//    public JsonResponse findById(@RequestParam(required = false) String id) {
//        JsonResponse response = super.findById( id );
//        if(response.getErrcode().equals(SUCCESS_CODE)) {
//            response.setMessage(MSG_SUCCESS);
//        }
//        return response;
//    }

    /**
     *获取字典类型信息列表并分页
     * @param page
     * @param size
     * @param direction
     * @param properties
     * @param sysDict
     * @return JsonResponse
     */
    @Operation(summary = "获取字典类型信息列表并分页", description = "获取字典类型信息列表并分页")
    @PostMapping(value = {"/findAll","/findAll/sso"})
    public JsonResponse findAll( @RequestParam(required = false, defaultValue = "1") int page, //
                                 @RequestParam(required = false, defaultValue = "10") int size, //
                                 @RequestParam(required = false) String direction, //
                                 @RequestParam(required = false) String properties, //
                                 @RequestBody(required = false) SysDict sysDict //
    ) {
        return super.findAll( page,size,direction, properties,sysDict);
    }


    /**
     * 获取字典树不分页
     *
     * @return JsonResponse
     */
    @PostMapping(value = "/findDictTree")
    public JsonResponse findDictTree() {
        Iterable<SysDict> roots = sysDictService.findAllNoPage();
        return JsonResponse.success( roots);
    }


    /**
     * 新增下级 "sys/dict/createChild"
     *
     * @param dict
     * @return JsonResponse
     */
    //@PreAuthorize("hasAuthority('ROLE_ADMIN')")  // 指定角色权限才能操作方法
    @PostMapping(value = "/createChild")
    public JsonResponse createChild(@RequestBody(required = false) SysDict dict) {
        if (dict == null) {
            return JsonResponse.defaultErrorResponse();
        }
        dict.setParentId(dict.getId());
        dict.setId(null);
        SysDict newDict = sysDictService.insert(dict);
        return JsonResponse.success(null, MSG_SUCCESS);
    }


    /**
     * 获取json格式数据字典
     * @return JsonResponse
     */
    //@PreAuthorize("hasAuthority('ROLE_USER')")  // 指定角色权限才能操作方法
    @PostMapping(value = "/listJson")
    public JsonResponse listJson() {
        List<SysDict> list = sysDictService.findByEnabled(true);
        return JsonResponse.builder().errcode(SUCCESS_CODE).message(MSG_SUCCESS).data(list).build();
    }


   /* @Operation(summary = "查询字段树", description = "通过此接口来查询字段树信息")
    @ApiImplicitParam(name = "id", value = "字典ID", required = true, dataType = "Long", paramType = "path")
    @RequestMapping(value = "/select/tree", method = RequestMethod.GET)
    public ModelAndView getSelectTree() {
        // 获取查询结果
        List<SysDict> pages = sysDictService.findByEnabled(true);

        Map<String, Object> map = new HashMap<>();
        map.put("list", pages);
        map.put("size", pages.size());

        Map<String, Object> maps = new HashMap<>();
        maps.put("tree", map);
        return new ModelAndView("sys/sysdict/chooseDictValue", "dictModel", maps);
    }*/
}
