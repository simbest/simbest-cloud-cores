/*
 * 版权所有 © 北京晟壁科技有限公司 2008-2027。保留一切权利!
 */
package com.simbest.cloud.cores.sys.repository;

import com.simbest.cloud.cores.dal.repository.SystemRepository;
import com.simbest.cloud.cores.sys.model.SysLogWeb;
import org.springframework.stereotype.Repository;

/**
 * 用途：系统Web请求日志
 * 作者: lishuyi
 * 时间: 2018/6/10  13:42
 */
@Repository
public interface SysLogWebRepository extends SystemRepository<SysLogWeb, String> {

}

