package com.simbest.cloud.cores.base.service.impl;


import com.simbest.cloud.cores.base.model.SystemModel;
import com.simbest.cloud.cores.base.repository.SystemRepository;
import com.simbest.cloud.cores.base.service.ISystemService;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;

/**
 * <strong>Title : 系统实体通用服务层</strong><br>
 * <strong>
 *     Description : 需要记录数据操作的创建时间和更新时间
 * </strong><br>
 * <strong>Create on : 2018/5/17</strong><br>
 * <strong>Modify on : 2018/5/17</strong><br>
 * <strong>Copyright (C) Ltd.</strong><br>
 *
 * @author LJW lijianwu@simbest.com.cn
 * @version <strong>V1.0.0</strong><br>
 * <strong>修改历史:</strong><br>
 * 修改人 修改日期 修改描述<br>
 * -------------------------------------------<br>
 */
@Slf4j
public class SystemService<T extends SystemModel,PK extends Serializable> extends GenericService<T,PK> implements ISystemService<T,PK> {

    private SystemRepository<T,PK> systemRepository;

    public SystemService(){}

    public SystemService (SystemRepository<T, PK> systemRepository ) {
        super(systemRepository);
        this.systemRepository = systemRepository;
    }



}
