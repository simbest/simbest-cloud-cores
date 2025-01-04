/*
 * 版权所有 © 北京晟壁科技有限公司 2008-2027。保留一切权利!
 */
package com.simbest.cloud.cores.sys.repository;

import com.simbest.cloud.cores.base.repository.LogicRepository;
import com.simbest.cloud.cores.sys.model.SysCustomFieldValue;
import org.springframework.stereotype.Repository;

/**
 * 用途：实体自定义字段值持久层
 * 作者: lishuyi
 * 时间: 2017/12/22  15:51
 */
@Repository
public interface SysCustomFieldValueRepository extends LogicRepository<SysCustomFieldValue, String> {

}
