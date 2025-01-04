/*
 * 版权所有 © 北京晟壁科技有限公司 2008-2027。保留一切权利!
 */
package com.simbest.cloud.cores.sys.web;

import com.simbest.cloud.cores.base.service.ISystemService;
import com.simbest.cloud.cores.base.web.controller.GenericController;
import com.simbest.cloud.cores.sys.model.SysTaskExecutedLog;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用途：定时任务日志记录控制器
 * 作者: lishuyi
 * 时间: 2018/2/22  10:14
 */
@Tag(name  = "SysTaskExecutedLogController", description = "系统管理-定时任务管理")
@RestController
@RequestMapping("/sys/task/log")
public class SysTaskExecutedLogController extends GenericController<SysTaskExecutedLog, String> {

    @Autowired
    public SysTaskExecutedLogController(@Qualifier("sysTaskExecutedLogService") ISystemService service) {
        super(service);
    }

}
