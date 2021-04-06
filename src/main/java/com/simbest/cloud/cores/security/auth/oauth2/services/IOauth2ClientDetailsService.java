/*
 * 版权所有 © 北京晟壁科技有限公司 2008-2027。保留一切权利!
 */
package com.simbest.cloud.cores.security.auth.oauth2.services;

import com.simbest.cloud.cores.dal.service.IGenericService;
import com.simbest.cloud.cores.security.auth.oauth2.model.Oauth2ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;

/**
 * 用途：Oauth2 客户端信息逻辑层
 * 作者: lishuyi@simbest.com.cn
 * 时间: 2021/3/12  20:50
 */
public interface IOauth2ClientDetailsService extends IGenericService<Oauth2ClientDetails, String>, ClientDetailsService {


}
