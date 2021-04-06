/*
 * 版权所有 © 北京晟壁科技有限公司 2008-2027。保留一切权利!
 */
package com.simbest.cloud.cores.security.auth.service;

import com.simbest.cloud.orguser.service.IAuthService;
import com.simbest.cloud.orguser.dto.IPermission;
import com.simbest.cloud.orguser.dto.IUser;
import com.simbest.cloud.orguser.dto.SimpleUser;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Set;

/**
 * libeixiao
 * 用途：抽象的认证服务
 * 作者: lishuyi@simbest.com.cn
 * 时间: 2021/3/11  15:45
 */
@Slf4j
@Data
public abstract class AbstractAuthService implements IAuthService {

    @Override
    public SimpleUser findByKey(String keyword, KeyType keyType) {
        return null;
    }

    @Override
    public Set<? extends IPermission> findUserPermissionByAppcode(String username, String appcode) {
        return null;
    }

    @Override
    public boolean checkUserAccessApp(String username, String appcode) {
        return false;
    }

    @Override
    public IUser customUserForApp(IUser iUser, String appcode) {
        return null;
    }

    @Override
    public void changeUserSessionByCorp(IUser newuser) {

    }

    @Override
    public int updateUserOpenidAndUnionid(String preferredMobile, String openid, String unionid, String appcode) {
        return 0;
    }

    @Override
    public IUser createUser(String keyword, KeyType keytype, String appcode, SimpleUser user) {
        return null;
    }

    @Override
    public IUser updateUser(String keyword, KeyType keytype, String appcode, SimpleUser user) {
        return null;
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        return null;
    }

}
