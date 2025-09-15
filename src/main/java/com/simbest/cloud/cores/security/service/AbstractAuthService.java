package com.simbest.cloud.cores.security.service;

import cn.hutool.core.util.ObjectUtil;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.simbest.boot.security.*;
import com.simbest.cloud.cores.config.AppConfig;
import com.simbest.cloud.cores.constants.ApplicationConstants;
import com.simbest.cloud.cores.constants.AuthoritiesConstants;
import com.simbest.cloud.cores.redis.RedisUtil;
import com.simbest.cloud.cores.security.authtokens.GenericAuthentication;
import com.simbest.cloud.cores.security.authtokens.UumsAuthenticationCredentials;
import com.simbest.cloud.cores.utils.PhoneCheckUtil;
import com.simbest.cloud.cores.uums.api.user.UumsSysUserinfoApi;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.SpringSecurityMessageSource;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.util.Assert;

import java.util.Map;
import java.util.Set;

/**
 * 用途：抽象的认证服务
 * 作者: lishuyi
 * 时间: 2019/4/23  10:03
 */
@Slf4j
@Data
public abstract class AbstractAuthService implements IAuthService {

    protected MessageSourceAccessor messages = SpringSecurityMessageSource.getAccessor();

    protected IAuthUserCacheService authUserCacheService;

    protected AppConfig appConfig;

    protected UumsSysUserinfoApi userinfoApi;

    //Spring Security 框架默认的用户组
    private final static Set<IRole> DEFAULT_ROLE_USER = Sets.newHashSet();
    private final static Set<GrantedAuthority> DEFAULT_ROLE_USER_AUTHORITY = Sets.newHashSet();

    public static void init(){
        IRole ROLE_USER = new SimpleRole();
        ((SimpleRole) ROLE_USER).setRoleCode(AuthoritiesConstants.USER);
        DEFAULT_ROLE_USER.add(ROLE_USER);
        GrantedAuthority ROLE_USER_AUTHORITY = new MySimpleGrantedAuthority(AuthoritiesConstants.USER);
        DEFAULT_ROLE_USER_AUTHORITY.add(ROLE_USER_AUTHORITY);
    }

    public AbstractAuthService(IAuthUserCacheService authUserCacheService,
                               AppConfig appConfig, UumsSysUserinfoApi userinfoApi){
        this.authUserCacheService = authUserCacheService;
        this.appConfig = appConfig;
        this.userinfoApi = userinfoApi;
    }

    private IUser reloadUserFromUums(String keyword, KeyType keyType){
        IUser user = userinfoApi.findByKey(keyword, keyType, appConfig.getAppcode());
        if(null != user) {
            authUserCacheService.saveOrUpdateCacheUser(user);
        }
        return user;
    }

    @Override
    public IUser findByKey(String keyword, KeyType keyType) {
        IUser user = authUserCacheService.loadCacheUser(keyword);
        if(ObjectUtil.isEmpty(user)) {
            user = reloadUserFromUums(keyword, keyType);
        }
        else{
            //没有ROLE_USER默认角色，直接追加，不再远程调用
            if (null == user.getAuthRoles() || user.getAuthRoles().isEmpty()) {
                //直接设置默认值
                user.addAppRoles(DEFAULT_ROLE_USER);
                user.addAppAuthorities(DEFAULT_ROLE_USER_AUTHORITY);
            }

//            //没有角色，需要查库后，再设置缓存
//            if(null == user.getAuthRoles() || user.getAuthRoles().isEmpty()){
//                user = reloadUserFromUums(keyword, keyType);
//            }else{
//                AtomicBoolean HAVE_ROLE_USER = new AtomicBoolean(false);
//                user.getAuthRoles().forEach(role -> {
//                    if(USER.equals(role.getRoleCode())){
//                        HAVE_ROLE_USER.set(true);
//                    }
//                });
//                //没有ROLE_USER的角色，同样需要查库后，再设置缓存
//                if(!HAVE_ROLE_USER.get()){
//                    user = reloadUserFromUums(keyword, keyType);
//                }
//            }
//            //很多用户实际没有权限，因此增加无权限再次查询，很高概率会占满服务器资源
//            //没有权限，同样需要查库后，再设置缓存
//            if(null == user.getAuthPermissions() || user.getAuthPermissions().isEmpty()){
//                user = reloadUserFromUums(keyword, keyType);
//            }
        }
        log.debug("通过关键字【{}】和关键字类型【{}】应用代码【{}】获取用户信息为【{}】", keyword, keyType.name(), appConfig.getAppcode(), user);
        return user;
    }

    @Override
    public Set<? extends IPermission> findUserPermissionByAppcode(String username, String appcode) {
//        Set<IPermission> permissions = authUserCacheService.loadCacheUserPermission(username, appcode);
//        if(null == permissions) {
//            permissions = Sets.newHashSet();
//            Set<SimplePermission> simplePermissions = userinfoApi.findPermissionByAppUserNoSession(username, appcode);
//            if(null != simplePermissions && !simplePermissions.isEmpty()){
//                for(SimplePermission s : simplePermissions) {
//                    permissions.add(s);
//                }
//            }
//            authUserCacheService.saveOrUpdateCacheUserPermission(username, appcode, permissions);
//        }
//        log.debug("用户【{}】从应用【{}】获取到【{}】权限", username, appcode, permissions.size());
//        return permissions;

        //主数据使用规范问题--导致读取用户权限从缓存读取改为从主数据直接获取
        return userinfoApi.findPermissionByAppUserNoSession(username, appcode);
    }

    @Override
    public boolean checkUserAccessApp(String username, String appcode) {
        Boolean isPermit = authUserCacheService.loadCacheUserAccess(username, appcode);
        if(null == isPermit) {
            isPermit = userinfoApi.checkUserAccessAppNoSession(username, appcode);
            if(null != isPermit) {
                authUserCacheService.saveOrUpdateCacheUserAccess(username, appcode, isPermit);
            }
        }
        return null == isPermit ? false : isPermit;
    }

    /**
     * 默认抽象类，不做定制实现
     * @param iUser
     * @param appcode
     * @return IUser
     */
    @Override
    public IUser customUserForApp(IUser iUser, String appcode){
        return iUser;
    }

    @Override
    public void changeUserSessionByCorp(IUser newUser) {
        Assert.notNull(newUser, "更新用户不能为空！");
        Authentication existingAuth = SecurityContextHolder.getContext().getAuthentication();
        Assert.notNull(existingAuth, "当前认证信息不能为空！");

        //清空当前会话--Start
        SecurityContextHolder.getContext().setAuthentication(null);
        Map<String, Long> delPrincipal = Maps.newHashMap();
        Set<String> keys = RedisUtil.globalKeys(ApplicationConstants.STAR + ":org.springframework.session.FindByIndexNameSessionRepository.PRINCIPAL_NAME_INDEX_NAME:" + newUser.getUsername());
        for (String key : keys) {
            //删除 spring:session:uums:index:org.springframework.session.FindByIndexNameSessionRepository.PRINCIPAL_NAME_INDEX_NAME:litingmin
            Set<String> redisKeys1 = RedisUtil.getRedisTemplate().keys(key + ApplicationConstants.STAR);
            Long number1 = RedisUtil.getRedisTemplate().delete(redisKeys1);
            log.debug("清理键值【{}】结果为【{}】", key, number1);
        }
        //清空当前会话--End

        //构建已认证通过的上下文--Start
        Authentication newAuth = null;
        if(existingAuth instanceof GenericAuthentication){
            newAuth = new GenericAuthentication(newUser, (UumsAuthenticationCredentials)existingAuth.getCredentials(), existingAuth.getAuthorities());
        }

        SecurityContextHolder.getContext().setAuthentication(newAuth);
        log.debug("更新后的认证信息为【{}】", newAuth);
        //构建已认证通过的上下文--End
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDetails userDetails = null;
        try {
            if (PhoneCheckUtil.isPhoneLegal(username)) {
                userDetails = findByKey(username, KeyType.preferredMobile);
            }
            if (null == userDetails) {
                userDetails = findByKey(username, KeyType.username);
            }
        } catch (Exception e){
            log.debug("通过SSO单点调用findByKey获取用户认证主体信息发生异常【{}】", e.getMessage());
        }
        log.debug("通过用户名【{}】和应用代码【{}】提取到的用户信息为【{}】", username, appConfig.getAppcode(), userDetails);
        if(null == userDetails){
            throw new UsernameNotFoundException(AuthoritiesConstants.LOGIN_NOT_EXIST_USER);
        }
        return userDetails;
    }

    @Override
    public int updateUserOpenidAndUnionid(String preferredMobile, String openid, String unionid, String appcode){
        org.springframework.util.Assert.notNull(preferredMobile, "preferredMobile不可为空");
        org.springframework.util.Assert.notNull(openid, "openid不可为空");
        org.springframework.util.Assert.notNull(appcode, "appcode不可为空");
        IUser iUser = userinfoApi.findByKey(preferredMobile, KeyType.preferredMobile, appcode);
        if(null == iUser){
            throw new UsernameNotFoundException(String.format("在应用%s中用户不存在，手机号码%s无效", appcode, preferredMobile));
        }
        else {
            //openid不等于当前用户的openid时，进行绑定更新
            if(!openid.equalsIgnoreCase(iUser.getOpenid())){
                SimpleUser simpleUser = new SimpleUser();
                BeanUtils.copyProperties(iUser, simpleUser);
                simpleUser.setOpenid(openid);
                if(StringUtils.isNotEmpty(unionid)) {
                    simpleUser.setUnionid(unionid);
                }
                simpleUser = userinfoApi.update(preferredMobile, KeyType.preferredMobile, appcode, simpleUser);
                if(StringUtils.isNotEmpty(simpleUser.getId())){
                    return ApplicationConstants.ONE;
                }
            }
        }
        return ApplicationConstants.ZERO;
    }

    @Override
    public IUser createUser(String keyword, IAuthService.KeyType keytype ,String appcode, SimpleUser user){
        return userinfoApi.create(keyword, keytype, appcode, user);
    }

    @Override
    public
    IUser updateUser(String keyword, IAuthService.KeyType keytype, String appcode, SimpleUser user){
        return userinfoApi.update(keyword, keytype, appcode, user);
    }

}
