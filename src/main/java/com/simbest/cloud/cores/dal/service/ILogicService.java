package com.simbest.cloud.cores.dal.service;

import com.simbest.cloud.cores.dal.model.LogicModel;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 用途：业务逻辑实体通用服务层
 * 作者: lishuyi@simbest.com.cn
 * 时间: 2021/3/9  19:53
 */
public interface ILogicService<T extends LogicModel,PK extends Serializable> extends ISystemService<T,PK>{

    /**
     * 根据主键ID更新是否可用状态
     * @param enabled
     * @param id
     * @return
     */
    T updateEnable(PK id, boolean enabled);

    /**
     * 根据设定时间，定时删除
     * @param id
     * @param localDateTime
     */
    void scheduleLogicDelete(PK id, LocalDateTime localDateTime);

    /**
     * 根据设定时间，定时删除
     * @param entity
     * @param localDateTime
     */
    void scheduleLogicDelete(T entity, LocalDateTime localDateTime);

    /**
     * 修改-允许实体主键字段无值
     * @param o
     * @return T
     */
    T updateWithNull(T o);

    //================以下将GenericService的Iterable转换为LogicService的List============//
    List<T> findAllNoPage();

    List<T> findAllNoPage(Sort sort);

    List<T> findAllByIDs(Iterable<PK> ids);

    List<T> findAllNoPage(Specification<T> conditions);

    List<T> findAllNoPage(Specification<T> conditions, Sort sort);
}
