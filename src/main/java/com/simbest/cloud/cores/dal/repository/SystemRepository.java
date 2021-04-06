/*
 * 版权所有 © 北京晟壁科技有限公司 2008-2027。保留一切权利!
 */
package com.simbest.cloud.cores.dal.repository;

import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;

/**
 * 用途：系统实体通用数据持久层
 * 作者: lishuyi@simbest.com.cn
 * 时间: 2021/3/9  19:53
 */
@Transactional
@NoRepositoryBean
public interface SystemRepository<T, PK extends Serializable> extends GenericRepository<T, PK>{


}