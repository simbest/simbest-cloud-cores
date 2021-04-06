/*
 * 版权所有 © 北京晟壁科技有限公司 2008-2027。保留一切权利!
 */
package com.simbest.cloud.cores.dal.repository;

import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;

/**
 * 用途：基础实体通用数据库持久层
 * 作者: lishuyi@simbest.com.cn
 * 时间: 2021/3/9  19:52
 */
@Transactional
@NoRepositoryBean
public interface GenericRepository<T, PK extends Serializable> extends BaseRepository<T, PK>{


}

