/*
 * 版权所有 © 北京晟壁科技有限公司 2008-2027。保留一切权利!
 */
package com.simbest.cloud.cores.components.schedules;

import com.simbest.cloud.cores.components.distributed.AppRuntimeMaster;
import com.simbest.cloud.cores.config.AppConfig;
import com.simbest.cloud.cores.constants.ApplicationConstants;
import com.simbest.cloud.cores.sys.repository.SysTaskExecutedLogRepository;
import com.simbest.cloud.cores.sys.service.IHeartTestService;
import com.simbest.cloud.cores.utils.http.client.HttpClient;
import com.simbest.cloud.cores.utils.servers.HostUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 用途：心跳测试服务定时器
 * 作者: lishuyi@simbest.com.cn
 * 时间: 2021/9/19  21:31
 */
@Slf4j
@Component
public class HeartTestTask extends AbstractTaskSchedule {

    @Autowired
    private ApplicationContext appContext;

    @Autowired
    private AppConfig config;

    @Autowired
    private HostUtil hostUtil;

    @Autowired
    public HeartTestTask(AppRuntimeMaster master, SysTaskExecutedLogRepository repository) {
        super(master, repository);
    }

    @Scheduled(cron = "${app.task.cron.heart.test.job}")
    public void checkAndExecute() {
        super.checkAndExecute(false);
    }

    /**
     * @see com.simbest.boot.sys.service.ISysHealthService
     * @return
     */
    @Override
    public String execute() {
        if(config.isOpenHeartCheck()) {
            Map<String, IHeartTestService> heartTests = appContext.getBeansOfType(IHeartTestService.class);
            if (heartTests.size() > 0) {
                for (Map.Entry<String, IHeartTestService> entry : heartTests.entrySet()) {
                    entry.getValue().doTest();
                }
            } else {
                String testUrl = "http://localhost:" + hostUtil.getRunningPort() + config.getContextPath() + ApplicationConstants.LOGIN_PAGE;
                String response = HttpClient
                        // 请求方式和请求url
                        .get(testUrl)
                        .asString();
                if (StringUtils.contains(response, "username")) {
                    log.debug("Heart test login url check ok!");
                } else {
                    log.debug("Heart test login url check failed!");
                    return CHECK_FAILED;
                }
            }
        }
        return CHECK_SUCCESS;
    }
}
