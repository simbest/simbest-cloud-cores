/*
 * 版权所有 © 北京晟壁科技有限公司 2008-2027。保留一切权利!
 */
package com.simbest.cloud.cores.security.auth.oauth2.repository;

import com.simbest.cloud.cores.dal.repository.GenericRepository;
import com.simbest.cloud.cores.security.auth.oauth2.model.Oauth2ClientDetails;
import org.springframework.stereotype.Repository;

/**
 * 用途：Oauth2 客户端信息持久层
 * 作者: lishuyi@simbest.com.cn
 * 时间: 2021/3/12  20:54
 */
@Repository
public interface Oauth2ClientDetailsRepository extends GenericRepository<Oauth2ClientDetails, String> {

    Oauth2ClientDetails findByClientId(String clientId);

}
