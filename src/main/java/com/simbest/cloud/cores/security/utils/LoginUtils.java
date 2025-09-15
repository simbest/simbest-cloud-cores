/*
 * 版权所有 © 北京晟壁科技有限公司 2008-2027。保留一切权利!
 */
package com.simbest.cloud.cores.security.utils;

import cn.hutool.http.useragent.UserAgent;
import cn.hutool.http.useragent.UserAgentUtil;
import com.simbest.boot.security.IAuthService;
import com.simbest.boot.security.IAuthService.KeyType;
import com.simbest.boot.security.IUser;
import com.simbest.cloud.cores.base.model.CheckIpIsDevOpsResult;
import com.simbest.cloud.cores.base.service.IGenericService;
import com.simbest.cloud.cores.component.distributed.lock.AppRuntimeMaster;
import com.simbest.cloud.cores.config.AppConfig;
import com.simbest.cloud.cores.constants.ApplicationConstants;
import com.simbest.cloud.cores.redis.RedisUtil;
import com.simbest.cloud.cores.security.authtokens.GenericAuthentication;
import com.simbest.cloud.cores.security.authtokens.LoginWebAuthenticationDetails;
import com.simbest.cloud.cores.security.authtokens.UumsAuthentication;
import com.simbest.cloud.cores.security.authtokens.UumsAuthenticationCredentials;
import com.simbest.cloud.cores.sys.model.SysLogLogin;
import com.simbest.cloud.cores.sys.model.SysLogLoginAdmin;
import com.simbest.cloud.cores.sys.service.ISysLogLoginService;
import com.simbest.cloud.cores.utils.ApplicationContextProvider;
import com.simbest.cloud.cores.utils.DateUtil;
import com.simbest.cloud.cores.utils.server.HostUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.DependsOn;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Set;

import static com.simbest.cloud.cores.constants.ApplicationConstants.COMMA;
import static com.simbest.cloud.cores.constants.ApplicationConstants.STAR;
import static com.simbest.cloud.cores.security.authtokens.LoginWebAuthenticationDetails.APPLY_USERNAME;


/**
 * 用途：登录工具类
 * 作者: lishuyi
 * 时间: 2018/9/21  10:43
 */
@Slf4j
@Component
@DependsOn(value = {"redisUtil"})
public class LoginUtils {

    @Autowired
    private AppConfig appConfig;

/*    @Autowired
    @Qualifier(BeanIds.AUTHENTICATION_MANAGER)
    private AuthenticationManager authenticationManager;*/
//    @Autowired
//    protected IAuthService authService;

    @Autowired
    private ISysLogLoginService loginService;

    @Autowired
    private AppRuntimeMaster appRuntime;

    @Autowired
    @Qualifier("sysLogLoginAdminService")
    private IGenericService sysLogLoginAdminService;

    private final static String loginedUser = "com.simbest.boot.util.security.LoginUtils_LOGINED_USER";

    /**
     * 根据用户名，自动登录
     * @param username 用户名需要3DES、RSA或Mocha算法进行加密
     * @param appcode
     */
    public void manualLogin(String username, String appcode) {
        log.debug("通过用户名【{}】和应用编码【{}】进行系统自动登录", username, appcode);
        IAuthService authService = ApplicationContextProvider.getBean(IAuthService.class);
        IUser iUser = authService.findByKey(username, KeyType.username);
        GenericAuthentication auth = new GenericAuthentication(iUser, (UumsAuthenticationCredentials)null, iUser.getAuthorities());
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(auth);
        SecurityContextHolder.setContext(context);
    }

    /**
     * 根据用户名和密码，自动登录
     * @param username 用户名需要3DES、RSA或Mocha算法进行加密
     * @param password 密码需要由RSA加密
     * @param appcode
     */
    public void manualLogin(String username, String password, String appcode) {
        log.debug("通过用户名【{}】、密码【{}】和应用编码【{}】进行系统自动登录", username, password, appcode);
        String rawUsername = SecurityUtils.decryptorUserName(username);
        UumsAuthentication uumsAuthentication = new UumsAuthentication(rawUsername, UumsAuthenticationCredentials.builder()
                .password(password).appcode(appcode).build());
        AuthenticationManager authenticationManager = ApplicationContextProvider.getBean(AuthenticationManager.class);
        Authentication auth = authenticationManager.authenticate(uumsAuthentication);
        SecurityContext sc = SecurityContextHolder.getContext();
        sc.setAuthentication(auth);
    }

    /**
     * 管理员认证
     */
    public void adminLogin() {
        log.debug("通过管理账号【{}】进行系统自动登录", "hadmin");
        manualLogin("hadmin", appConfig.getAppcode());
    }

    /**
     * 框架中的LoginHandler在使用
     * @param request
     * @param authentication
     */
    public void recordLoginLog(HttpServletRequest request, Authentication authentication){
        if (authentication.getPrincipal() instanceof IUser) {
            IUser iUser = (IUser)authentication.getPrincipal();
            String applyUsername = null;
            if(null != authentication.getDetails()){
                if(authentication.getDetails() instanceof LoginWebAuthenticationDetails){
                    LoginWebAuthenticationDetails loginDetail = (LoginWebAuthenticationDetails) authentication.getDetails();
                    applyUsername = loginDetail.getApplyUsername();
                }
                else if(authentication.getDetails() instanceof LinkedHashMap){
                    LinkedHashMap<String, String> parameters = (LinkedHashMap) authentication.getDetails();
                    applyUsername = parameters.get(APPLY_USERNAME);
                }
            }
            recordLog(request, iUser, true, applyUsername);
        }
    }

    /**
     * 框架中的DefaultLogoutHandler在使用
     * @param request
     * @param authentication
     */
    public void recordLogoutLog(HttpServletRequest request, Authentication authentication){
        if (authentication.getPrincipal() instanceof IUser) {
            IUser iUser = (IUser)authentication.getPrincipal();
            String applyUsername = null;
            if(null != authentication.getDetails()){
                if(authentication.getDetails() instanceof LoginWebAuthenticationDetails){
                    LoginWebAuthenticationDetails loginDetail = (LoginWebAuthenticationDetails) authentication.getDetails();
                    applyUsername = loginDetail.getApplyUsername();
                }
                else if(authentication.getDetails() instanceof LinkedHashMap){
                    LinkedHashMap<String, String> parameters = (LinkedHashMap) authentication.getDetails();
                    applyUsername = parameters.get(APPLY_USERNAME);
                }
            }
            recordLog(request, iUser, false, applyUsername);
        }
    }

    /**
     * 接入4A认证，A4PasswordCheckController在用
     * @param request 登录或登出请求
     * @param iUser 用户信息
     * @param login 登录或登出
     * @param applyUsername 记录申请授权申请人账号
     */
    public void recordLog(HttpServletRequest request, IUser iUser, boolean login, String applyUsername){
        if (checkRecordLoginLog(request, iUser.getUsername(), applyUsername)) {
            SysLogLogin logLogin = SysLogLogin.builder()
                    .account(iUser.getUsername())
                    .loginEntry(ApplicationConstants.ZERO) //PC登录入口
                    .loginType(ApplicationConstants.ZERO)  //用户名登录方式
                    .isSuccess(true)
                    .ip(HostUtil.getClientIpAddress(request))
                    .trueName(iUser.getTruename())
                    .belongOrgName(iUser.getBelongOrgName())
                    .sessionid(request.getRequestedSessionId())
                    .remark(applyUsername)
                    .build();
            String uaStr = request.getHeader("User-Agent");
            if (StringUtils.isNotEmpty(uaStr)) {
                UserAgent ua = UserAgentUtil.parse(uaStr);
                logLogin.setOsFamily(ua.getPlatform().toString());
                logLogin.setOs(ua.getOs().toString());
                logLogin.setBrowserName(ua.getBrowser().toString());
                logLogin.setBrowserVersion(ua.getVersion());
                logLogin.setBrowserEngine(ua.getEngine().toString());
                logLogin.setBrowserEngineVersion(ua.getEngineVersion());
                logLogin.setIsMobile(ua.isMobile());
                logLogin.setServerip(appRuntime.getMyHost());
            }
            if(login) {
                logLogin.setLoginTime(DateUtil.getCurrent());
            }
            else{
                logLogin.setLogoutTime(DateUtil.getCurrent());
            }
            log.debug("记录登录日志RecordLoginLog【{}】", logLogin.toString());
            loginService.insert(logLogin);
        }
        else{
            SysLogLoginAdmin logLoginAdmin = SysLogLoginAdmin.builder()
                    .account(iUser.getUsername())
                    .loginEntry(ApplicationConstants.ZERO) //PC登录入口
                    .loginType(ApplicationConstants.ZERO)  //用户名登录方式
                    .isSuccess(true)
                    .ip(HostUtil.getClientIpAddress(request))
                    .trueName(iUser.getTruename())
                    .belongOrgName(iUser.getBelongOrgName())
                    .sessionid(request.getRequestedSessionId())
                    .remark(applyUsername)
                    .build();
            String uaStr = request.getHeader("User-Agent");
            if (StringUtils.isNotEmpty(uaStr)) {
                UserAgent ua = UserAgentUtil.parse(uaStr);
                logLoginAdmin.setOsFamily(ua.getPlatform().toString());
                logLoginAdmin.setOs(ua.getOs().toString());
                logLoginAdmin.setBrowserName(ua.getBrowser().toString());
                logLoginAdmin.setBrowserVersion(ua.getVersion());
                logLoginAdmin.setBrowserEngine(ua.getEngine().toString());
                logLoginAdmin.setBrowserEngineVersion(ua.getEngineVersion());
                logLoginAdmin.setIsMobile(ua.isMobile());
                logLoginAdmin.setServerip(appRuntime.getMyHost());
            }
            if(login) {
                logLoginAdmin.setLoginTime(DateUtil.getCurrent());
            }
            else{
                logLoginAdmin.setLogoutTime(DateUtil.getCurrent());
            }
            log.debug("记录登录日志RecordLoginLogAdmin【{}】", logLoginAdmin.toString());
            sysLogLoginAdminService.insert(logLoginAdmin);
        }
    }

    /**
     * 判断当前请求是否记录登录日志（也可以用于判断当前请求是否是北环或省公司开发运维人员）
     * @param request
     * @param username  登录账号
     * @param applyUsername 授权账号（暂时没有用到，主要还是判断是否是OA维护人员）
     * @return
     */
    public boolean checkRecordLoginLog(HttpServletRequest request, String username, String applyUsername){
        String clientIp = HostUtil.getClientIpAddress(request);
        //获取系统维护账号
        String excludeAccounts = appConfig.getExcludeAccounts();
        //获取系统维护终端IP地址段
        String[] excludeIps = appConfig.getExcludeIps().split(",");
        if(clientIp.equals("127.0.0.1") || clientIp.equals("localhost") || clientIp.equals("0:0:0:0:0:0:0:1")){
            return false;
        }
        // 判断IP地址是否匹配
        boolean clientIpMatch = false;
        for(String ipRegex: excludeIps){
            // 客户端终端IP匹配特殊OA维护IP或OA维护IP端
            if(clientIp.matches(ipRegex)){
                clientIpMatch = true;
                break;
            }
            else{
                continue;
            }
        }
        //客户端IP匹配的情况下，有可能是OA厂家人员登录
        if(clientIpMatch){
            //OA人员登录管理账号，记录登录日志
            if(username.matches(excludeAccounts)){
                return true;
            }
            //OA人员登录其他账号，不记录登录日志
            else{
                return false;
            }
        }
        //客户端IP不匹配OA终端的情况下，默认记录登录日志
        else {
            return true;
        }
    }

    /**
     * 记录登录账号
     * @param username
     */
    public void recordLoginUsername(String username){
        RedisUtil.getRedisTemplate().opsForSet().add(loginedUser, username);
    }

    /**
     * 记录登出账号
     * @param username
     */
    public void recordLogoutUsername(String username){
        RedisUtil.getRedisTemplate().opsForSet().remove(loginedUser, username);
    }

    /**
     * 获取所有已登录账号
     * @return
     */
    public Set<String> loadLoginUsername(){
        return RedisUtil.getRedisTemplate().opsForSet().members(loginedUser);
    }

    /**
     * 检查当前请求的IP地址是否是合法运维IP地址
     * @return
     */
    public static CheckIpIsDevOpsResult checkIpIsDevOps(String clientIp){
        boolean checkFlag;
        AppConfig appConfig = ApplicationContextProvider.getBean(AppConfig.class);
        //  app.login.ip.white.list=* (IP地址合法)
        if(!appConfig.getLoginWhiteIplist().isEmpty() && appConfig.getLoginWhiteIplist().size() == 1 && appConfig.getLoginWhiteIplist().get(0).equals(STAR)){
            checkFlag = true;
        }
        // app.login.ip.white.list=10.87.57.23,10.87.57.46, 10.87.57.68 (IP地址合法)
        else if(appConfig.getLoginWhiteIplist().contains(clientIp)){
            checkFlag = true;
        }
        else{
            checkFlag = false;
        }
        CheckIpIsDevOpsResult result = CheckIpIsDevOpsResult.builder().checkRet(checkFlag).whiteIps(String.join(COMMA, appConfig.getLoginWhiteIplist())).currentIp(clientIp).build();
        return result;
    }

    public static void main(String[] args) {
        String accountRegex = "sjbg|hadmin|hadmin1|hadmin2|hadmin3";
        System.out.println("sjbg".matches(accountRegex));
        System.out.println("sjbg1".matches(accountRegex));
        System.out.println("hadmin".matches(accountRegex));
        System.out.println("hadmin3".matches(accountRegex));
        System.out.println("hadmin4".matches(accountRegex));
        System.out.println("hadmi".matches(accountRegex));

        System.out.println("------------------------------------------------");

        String[] ipRegexs = "10.87.57.*,10.87.41.136,10.87.41.148".split(",");
        for(String ipRegex: ipRegexs){
            System.out.println("10.87.57.23".matches(ipRegex));
            System.out.println("10.87.57.1".matches(ipRegex));
            System.out.println("10.87.58.23".matches(ipRegex));
            System.out.println("10.87.41.136".matches(ipRegex));
            System.out.println("10.87.41.148".matches(ipRegex));
            System.out.println("10.87.41.150".matches(ipRegex));
            System.out.println("#########################");
        }


    }

}
