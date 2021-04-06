package com.simbest.cloud.cores.dal.service.impl;

import com.simbest.cloud.cores.dal.model.SystemModel;
import com.simbest.cloud.cores.dal.repository.SystemRepository;
import com.simbest.cloud.cores.dal.service.ISystemService;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;

/**
 * 用途：系统实体通用服务层
 * 作者: lishuyi@simbest.com.cn
 * 时间: 2021/3/9  19:53
 */
@Slf4j
public class SystemService<T extends SystemModel,PK extends Serializable> extends GenericService<T,PK> implements ISystemService<T,PK> {

    private SystemRepository<T,PK> systemRepository;

    public SystemService(){}

    public SystemService (SystemRepository<T, PK> systemRepository ) {
        super(systemRepository);
        this.systemRepository = systemRepository;
    }

//    @Override
//    public List<T> saveAll(Iterable<T> entities) {
//        log.debug("@SystemService saveAll");
//        List<S> list = Lists.newArrayList();
//        for(S o : entities){
//            o = (S) insert(o);
//            list.add(o);
//        }
//        return list;
//    }

}
