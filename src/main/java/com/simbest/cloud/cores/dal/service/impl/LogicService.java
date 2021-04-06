package com.simbest.cloud.cores.dal.service.impl;

import com.google.common.collect.Lists;
import com.simbest.cloud.cores.constants.ApplicationConstants;
import com.simbest.cloud.cores.dal.model.LogicModel;
import com.simbest.cloud.cores.dal.repository.LogicRepository;
import com.simbest.cloud.cores.dal.service.ILogicService;
import com.simbest.cloud.cores.exceptions.InsertExistObjectException;
import com.simbest.cloud.cores.exceptions.UpdateNotExistObjectException;
import com.simbest.cloud.cores.utils.CustomBeanUtil;
import com.simbest.cloud.cores.utils.DateUtil;
import com.simbest.cloud.cores.utils.ObjectUtil;
import com.simbest.cloud.cores.utils.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import javax.transaction.Transactional;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * 用途：业务逻辑实体通用服务层
 * 作者: lishuyi@simbest.com.cn
 * 时间: 2021/3/9  19:53
 */
@Slf4j
public class LogicService<T extends LogicModel,PK extends Serializable> extends SystemService<T,PK> implements ILogicService<T,PK> {

    private LogicRepository<T,PK> logicRepository;

    public LogicService ( LogicRepository<T, PK> logicRepository) {
        super(logicRepository);
        this.logicRepository = logicRepository;
    }

    @Override
    public long count ( ) {
        long count = logicRepository.countActive();
        log.debug("LogicService count 调用结果为【{}】", count);
        return count;
    }

    @Override
    public long count ( Specification<T> specification ) {
        long count = logicRepository.countActive(specification);
        log.debug("LogicService count 调用结果为【{}】", count);
        return count;
    }

    @Override
    public boolean exists ( PK id ) {
        boolean exist = logicRepository.existsActive( id );
        log.debug("LogicService exists 参数为【{}】，调用结果为【{}】", id, exist);
        return exist;
    }

    @Override
    public T findOne ( PK id ){
        T obj = logicRepository.findOneActive(id);
        log.debug("LogicService findOne 参数为【{}】，调用结果为【{}】", id, obj);
        return obj;
    }

    @Override
    public T findById ( PK id ){
        T obj = logicRepository.findOneActive(id);
        log.debug("LogicService findById 参数为【{}】，调用结果为【{}】", id, obj);
        return obj;
    }

    @Override
    public T findOne(Specification<T> conditions){
        T obj = logicRepository.findOneActive(conditions);
        log.debug("LogicService findOne 参数为【{}】，调用结果为【{}】", conditions, obj);
        return obj;
    }

    @Override
    public Page<T> findAll ( ) {
        Page<T> page = logicRepository.findAllActive();
        log.debug("LogicService findAll 调用结果返回记录数为【{}】", page.getTotalElements());
        return page;
    }

    @Override
    public Page<T> findAll (Pageable pageable ) {
        Page<T> page = logicRepository.findAllActive( pageable );
        log.debug("LogicService findAll 调用页码【{}】和页容量【{}】, 调用结果返回记录数为【{}】", pageable.getPageNumber(), pageable.getPageSize(), page.getTotalElements());
        return page;
    }

    @Override
    public Page<T> findAll (Sort sort ) {
        Page<T> page = logicRepository.findAllActive(PageRequest.of(ApplicationConstants.PAGE_NUMBER, ApplicationConstants.PAGE_SIZE, sort));
        log.debug("LogicService findAll 调用结果返回记录数为【{}】", page.getTotalElements());
        return page;
    }

    @Override
    public List<T> findAllNoPage(){
        List<T> list = logicRepository.findAllActiveNoPage();
        log.debug("LogicService findAllNoPage 调用结果返回记录数为【{}】", list.size());
        return list;
    }

    @Override
    public List<T> findAllNoPage(Sort sort){
        List<T> list = logicRepository.findAllActiveNoPage(sort);
        log.debug("LogicService findAllNoPage 调用结果返回记录数为【{}】", list.size());
        return list;
    }

    @Override
    public List<T> findAllByIDs(Iterable<PK> ids) {
        List<T> list = logicRepository.findAllActive(ids);
        log.debug("LogicService findAllNoPage 调用结果返回记录数为【{}】", list.size());
        return list;
    }

    @Override
    public Page<T> findAll (Specification<T> conditions, Pageable pageable ) {
        Page<T> page = logicRepository.findAllActive(conditions, pageable);
        log.debug("LogicService findAll 调用结果返回记录数为【{}】", page.getTotalElements());
        return page;
    }

    @Override
    public List<T> findAllNoPage(Specification<T> conditions){
        List<T> list = logicRepository.findAllActive(conditions);
        log.debug("LogicService findAllNoPage 调用结果返回记录数为【{}】", list.size());
        return list;
    }

    @Override
    public List<T> findAllNoPage(Specification<T> conditions, Sort sort){
        List<T> list = logicRepository.findAllActive(conditions, sort);
        log.debug("LogicService findAllNoPage 调用结果返回记录数为【{}】", list.size());
        return list;
    }

    @Override
    @Transactional
    public T updateEnable (PK id, boolean enabled) {
        T obj =  super.findById( id );
        if (obj == null) {
            log.warn("根据主键ID【{}】无法检索到对象，无法更新，请确认！", id);
            return null;
        }
        obj.setEnabled( enabled );
        obj = update(obj);
        log.debug("主键为【{}】的【{}】对象可用性已调整为【{}】", id, obj.getClass(), enabled);
        return obj;
    }

    @Override
    @Transactional
    public T insert ( T source) {
        if(null == ObjectUtil.getEntityIdVaue(source)) {
            wrapCreateInfo(source);
            T target = logicRepository.save(source);
            CustomBeanUtil.copyTransientProperties(source,target);
            log.debug("对象【{}】保存成功", target);
            return target;
        } else {
            throw new InsertExistObjectException();
        }
    }

    @Override
    @Transactional
    public T update ( T source) {
        PK pk = (PK)ObjectUtil.getEntityIdVaue(source);
        if(null != pk) {
            T target = findById(pk);
            CustomBeanUtil.copyPropertiesIgnoreNull(source, target);
            wrapUpdateInfo( target );
            T newTarget = logicRepository.save(target);
            CustomBeanUtil.copyTransientProperties(target,newTarget);
            log.debug("对象【{}】修改成功", newTarget);
            return newTarget;
        } else {
            throw new UpdateNotExistObjectException();
        }
    }

    @Override
    @Transactional
    public T updateWithNull ( T source) {
        PK pk = (PK)ObjectUtil.getEntityIdVaue(source);
        if(null != pk) {
            T target = findById(pk);
            String[] ignoreProperties = {"enabled","creator","modifier","createdTime","modifiedTime"};
            BeanUtils.copyProperties(source,target,ignoreProperties);
            wrapUpdateInfo( target );
            T newTarget = logicRepository.save(target);
            CustomBeanUtil.copyTransientProperties(target,newTarget);
            log.debug("对象【{}】修改成功", newTarget);
            return newTarget;
        } else {
            throw new UpdateNotExistObjectException();
        }
    }

    @Override
    @Transactional
    public List<T> saveAll(Iterable<T> entities) {
        List<T> list = Lists.newArrayList();
        for(T o : entities){
            o = insert(o);
            list.add(o);
        }
        log.debug("成功保存【{}】条记录", list.size());
        return list;
    }

    @Override
    @Transactional
    public void deleteById ( PK id ) {
        T o = findById(id);
        wrapUpdateInfo(o);
        log.debug("已成功删除主键为【{}】的记录", id);
        logicRepository.logicDelete( id );
    }

    @Override
    @Transactional
    public void delete ( T o ) {
        wrapUpdateInfo( o );
        logicRepository.logicDelete( o );
        log.debug("已成功删除对象【{}】", o);
    }

    @Override
    @Transactional
    public void deleteAll ( Iterable<? extends T> iterable ) {
        iterable.forEach( o -> {
            delete(o);
            log.debug("已成功删除对象【{}】", o);
        });
    }

    @Override
    @Transactional
    public void deleteAll ( ) {
        Iterable<? extends T> iterable = findAllNoPage();
        deleteAll(iterable);
        iterable.forEach( o -> log.debug("已成功删除对象【{}】", o));
    }

    @Override
    @Transactional
    public void deleteAllByIds ( Iterable<? extends PK> pks ) {
        pks.forEach( pk -> {
            deleteById(pk);
            log.debug("已成功删除对象主键为【{}】的记录", pk);
        });
    }

    @Override
    @Transactional
    public void scheduleLogicDelete(PK id, LocalDateTime localDateTime) {
        logicRepository.scheduleLogicDelete(id, localDateTime);
        log.debug("将在【{}】删除主键ID为【{}】的记录", localDateTime.now(), id);
    }

    @Override
    @Transactional
    public void scheduleLogicDelete(T entity, LocalDateTime localDateTime) {
        logicRepository.scheduleLogicDelete(entity, localDateTime);
        log.debug("将在【{}】删除对象【{}】", localDateTime.now(), entity);
    }

    protected void wrapCreateInfo(T o) {
        String userName = SecurityUtils.getCurrentUserName();
        o.setCreator(userName);
        o.setEnabled(true);
        o.setCreatedTime(DateUtil.date2LocalDateTime(new Date()));
        wrapUpdateInfo(o);
    }

    protected void wrapUpdateInfo(T o) {
        String userName = SecurityUtils.getCurrentUserName();
        o.setModifier(userName);
    }


}
