package com.simbest.cloud.cores.sys.service.impl;

import com.simbest.cloud.cores.base.service.impl.LogicService;
import com.simbest.cloud.cores.exceptions.BusinessForbiddenException;
import com.simbest.cloud.cores.sys.model.SysDict;
import com.simbest.cloud.cores.sys.model.SysDictValue;
import com.simbest.cloud.cores.sys.repository.SysDictRepository;
import com.simbest.cloud.cores.sys.repository.SysDictValueRepository;
import com.simbest.cloud.cores.sys.service.ISysDictService;
import com.simbest.cloud.cores.sys.service.ISysDictValueService;
import com.simbest.cloud.cores.security.utils.SecurityUtils;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.time.LocalDateTime;
import java.util.List;

import static com.simbest.cloud.cores.constants.AuthoritiesConstants.*;


@Service
public class SysDictService extends LogicService<SysDict, String> implements ISysDictService {

    private SysDictRepository dictRepository;

    @Autowired
    private SysDictValueRepository dictValueRepository;;

    @Autowired
    public SysDictService(SysDictRepository dictRepository ) {
        super(dictRepository);
        this.dictRepository = dictRepository;
    }

    @Override
    public SysDict findByDictType(String dictType){
        return dictRepository.findByDictType(dictType);
    }

    @Override
    public List<SysDict> findByParentId(String parentId) {
        return dictRepository.findByParentId(parentId);
    }

    @Override
    @Cacheable
    public List<SysDict> findByEnabled(Boolean enabled) {
        return dictRepository.findByEnabled(enabled);
    }


    @Override
    @Transactional
    public SysDict updateEnable(String id, boolean enabled) {
        if(SecurityUtils.hasPermission(SUPER_ADMIN))
            return super.updateEnable(id, enabled);
        else
            throw new AccessDeniedException(ACCESS_FORBIDDEN);
    }

    @Override
    @Transactional
    public SysDict insert(SysDict source) {
        if(SecurityUtils.hasPermission(SUPER_ADMIN)) {
            SysDict sysDict = dictRepository.findByDictType(source.getDictType());
            Assert.isNull(sysDict, "字典代码已存在或已归档，不能重复添加");
            return super.insert(source);
        }
        else
            throw new AccessDeniedException(ACCESS_FORBIDDEN);
    }

    @Override
    @Transactional
    public SysDict update(SysDict source) {
        if(SecurityUtils.hasPermission(SUPER_ADMIN))
            return super.update(source);
        else
            throw new AccessDeniedException(ACCESS_FORBIDDEN);
    }

    @Override
    @Transactional
    public List<SysDict> saveAll(Iterable<SysDict> entities) {
        if(SecurityUtils.hasPermission(SUPER_ADMIN))
            return super.saveAll(entities);
        else
            throw new AccessDeniedException(ACCESS_FORBIDDEN);
    }

    @Override
    @Transactional
    public void deleteById(String id ) {
        if(SecurityUtils.hasAnyPermission(new String[]{SUPER_ADMIN, ROLE_ADMIN})) {
            SysDict sysDict = findById(id);
            if (sysDict != null){
                SysDictValue sysDictValue = SysDictValue.builder().dictType(sysDict.getDictType()).build();
                dictValueRepository.delete(sysDictValue);
            }
            super.deleteById(id);
        }else {
            throw new AccessDeniedException(ACCESS_FORBIDDEN);
        }
    }

    @Override
    @Transactional
    public void delete(SysDict o ) {
        throw new BusinessForbiddenException(BUSINESS_FORBIDDEN);
    }

    @Override
    @Transactional
    public void deleteAll(Iterable<? extends SysDict> iterable ) {
        throw new BusinessForbiddenException(BUSINESS_FORBIDDEN);
    }

    @Override
    @Transactional
    public void deleteAll() {
        throw new BusinessForbiddenException(BUSINESS_FORBIDDEN);
    }

    @Override
    @Transactional
    public void deleteAllByIds(Iterable<? extends String> pks ) {
        if(SecurityUtils.hasAnyPermission(new String[]{SUPER_ADMIN, ROLE_ADMIN})) {
            //删除数据字典类型对应的枚举值
            pks.forEach( pk -> {
                SysDict sysDict = findById(pk);
                if (sysDict != null){
                    SysDictValue sysDictValue = SysDictValue.builder().dictType(sysDict.getDictType()).build();
                    dictValueRepository.delete(sysDictValue);
                }
            });
            super.deleteAllByIds(pks);
        }else {
            throw new BusinessForbiddenException(BUSINESS_FORBIDDEN);
        }
    }

    @Override
    @Transactional
    public void scheduleLogicDelete(String id, LocalDateTime localDateTime) {
        throw new BusinessForbiddenException(BUSINESS_FORBIDDEN);
    }

    @Override
    @Transactional
    public void scheduleLogicDelete(SysDict entity, LocalDateTime localDateTime) {
        throw new BusinessForbiddenException(BUSINESS_FORBIDDEN);
    }

}
