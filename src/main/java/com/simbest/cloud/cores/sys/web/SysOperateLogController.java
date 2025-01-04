/*
 * 版权所有 © 北京晟壁科技有限公司 2008-2027。保留一切权利!
 */
package com.simbest.cloud.cores.sys.web;

import com.simbest.cloud.cores.base.web.controller.GenericController;
import com.simbest.cloud.cores.sys.model.SysOperateLog;
import com.simbest.cloud.cores.sys.service.ISysOperateLogService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用途：操作日志控制器
 * 作者: lishuyi
 * 时间: 2018/2/22  10:14
 */
@Tag(name = "SysOperateLogController", description = "系统管理-操作日志管理")
@RestController
@RequestMapping("/sys/log/operate")
public class SysOperateLogController extends GenericController<SysOperateLog, String> {

    private ISysOperateLogService service;

    @Autowired
    public SysOperateLogController(ISysOperateLogService service) {
        super(service);
        this.service = service;
    }

}
