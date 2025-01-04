/*
 * 版权所有 © 北京晟壁科技有限公司 2008-2027。保留一切权利!
 */
package com.simbest.cloud.cores.sys.web;

import com.simbest.cloud.cores.base.web.controller.GenericController;
import com.simbest.cloud.cores.response.JsonResponse;
import com.simbest.cloud.cores.security.utils.LoginUtils;
import com.simbest.cloud.cores.security.utils.SecurityUtils;
import com.simbest.cloud.cores.sys.model.SysLogLogin;
import com.simbest.cloud.cores.sys.service.ISysLogLoginService;
import com.simbest.cloud.cores.utils.MapUtil;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.criteria.Predicate;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用途：登录日志控制器
 * 作者: lishuyi
 * 时间: 2018/2/22  10:14
 */
@Tag(name = "SysLogLoginController", description = "系统管理-登录日志管理")
@RestController
@RequestMapping("/sys/log/login")
public class SysLogLoginController extends GenericController<SysLogLogin, String> {

    private ISysLogLoginService service;

    @Autowired
    private LoginUtils loginUtils;

    @Autowired
    public SysLogLoginController(ISysLogLoginService service) {
        super(service);
        this.service = service;
    }

    @PostMapping(value = "/countLogin")
    public JsonResponse countLogin(@RequestBody SysLogLogin o) {
        return JsonResponse.success(service.countLogin(MapUtil.objectToMap(o)));
    }

    @PostMapping(value = {"/syncLoginLog", "/syncLoginLog/sso"})
    public JsonResponse sysLoginLog(@RequestBody SysLogLogin o) {
        Specification<SysLogLogin> specification = (root, query, cb) -> {
            Predicate loginTimePredicate = cb.between(root.get("loginTime"), o.getSsDate(), o.getEeDate());
            return cb.and(loginTimePredicate);
        };
        Iterable<SysLogLogin> datas = service.findAllNoPage(specification);
        return JsonResponse.success(datas);
    }


    /**
     * 接入4A统一认证时，提供前端判断当前请求是否记录登录日志（也可以用于判断当前请求是否是北环或省公司开发运维人员）
     * @param request
     * @return
     *
     * http://localhost:8080/appcode/sys/log/login/checkRecordLoginLog
     */
    @PostMapping("/checkRecordLoginLog")
    public boolean checkRecordLoginLog(HttpServletRequest request){
        String currentUserName = SecurityUtils.getCurrentUserName();
        return loginUtils.checkRecordLoginLog(request, currentUserName, null);
    }

}
