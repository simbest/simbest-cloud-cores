package com.simbest.cloud.cores.dal.service;


import com.simbest.cloud.cores.dal.model.SystemModel;

import java.io.Serializable;

/**
 * 用途：系统实体通用服务层
 * 作者: lishuyi@simbest.com.cn
 * 时间: 2021/3/9  19:53
 */
public interface ISystemService <T extends SystemModel,PK extends Serializable> extends IGenericService<T,PK>{

}
