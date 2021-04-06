/*
 * 版权所有 © 北京晟壁科技有限公司 2008-2027。保留一切权利!
 */
package com.simbest.cloud.cores.dal.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 用途：业务逻辑实体通用数据库持久层
 * 作者: lishuyi@simbest.com.cn
 * 时间: 2021/3/9  19:53
 */
@Transactional
@NoRepositoryBean
public interface LogicRepository<T, PK extends Serializable> extends SystemRepository<T, PK> {

    long countActive();

    long countActive(Specification<T> conditions);

    boolean existsActive(PK id);

    Page<T> findAllActive();

    Page<T> findAllActive(Sort sort);

    Page<T> findAllActive(Pageable pageable);

    List<T> findAllActiveNoPage();

    List<T> findAllActiveNoPage(Sort sort);

    List<T> findAllActive(Iterable<PK> ids);

    List<T> findAllActive(Specification<T> conditions);

    Page<T> findAllActive(Specification<T> conditions, Pageable pageable);

    List<T> findAllActive(Specification<T> conditions, Sort sort);

    T findByIdActive(PK id);

    T findOneActive(PK id);

    T findOneActive(Specification<T> conditions);

    @Modifying
    void logicDelete(PK id);

    @Modifying
    void logicDelete(T entity);

    @Modifying
    void logicDelete(Iterable<? extends T> entities);

    @Modifying
    void logicDeleteAll();

    @Modifying
    void deleteAllByIds(Iterable<? extends PK> ids);

    @Modifying
    void scheduleLogicDelete(PK id, LocalDateTime localDateTime);

    @Modifying
    void scheduleLogicDelete(T entity, LocalDateTime localDateTime);

}
