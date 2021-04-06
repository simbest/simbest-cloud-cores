/*
 * 版权所有 © 北京晟壁科技有限公司 2008-2027。保留一切权利!
 */
package com.simbest.cloud.cores.security.auth.config;

import com.simbest.cloud.cores.constants.ApplicationConstants;
import com.simbest.cloud.cores.security.auth.entrypoints.AccessDeniedEntryPoint;
import com.simbest.cloud.cores.security.auth.filters.CaptchaAuthenticationFilter;
import com.simbest.cloud.cores.security.auth.filters.CustomAbstractAuthenticationProcessingFilter;
import com.simbest.cloud.cores.security.auth.filters.RestCaptchaAuthenticationFilter;
import com.simbest.cloud.cores.security.auth.filters.RestRsaAuthenticationFilter;
import com.simbest.cloud.cores.security.auth.filters.RestUumsAuthenticationFilter;
import com.simbest.cloud.cores.security.auth.filters.RsaAuthenticationFilter;
import com.simbest.cloud.cores.security.auth.filters.UumsAuthenticationFilter;
import com.simbest.cloud.cores.security.auth.handles.AccessDeniedForbiddenHandler;
import com.simbest.cloud.cores.security.auth.handles.FailedLoginRedirectHandler;
import com.simbest.cloud.cores.security.auth.handles.FailedLoginRestHandler;
import com.simbest.cloud.cores.security.auth.handles.SuccessLoginRedirectHandler;
import com.simbest.cloud.cores.security.auth.handles.SuccessLoginRestHandler;
import com.simbest.cloud.cores.security.auth.handles.SuccessLogoutRedirectHandler;
import com.simbest.cloud.cores.security.auth.handles.SuccessLogoutRestHandler;
import com.simbest.cloud.cores.security.auth.providers.GenericAuthenticationChecker;
import com.simbest.cloud.cores.security.auth.service.IAuthUserCacheService;
import com.simbest.cloud.cores.utils.encrypt.RsaEncryptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.session.FindByIndexNameSessionRepository;
import org.springframework.session.security.SpringSessionBackedSessionRegistry;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Map;

import static com.simbest.cloud.cores.constants.ApplicationConstants.LOGIN_ERROR_PAGE;
import static com.simbest.cloud.cores.constants.ApplicationConstants.LOGIN_PAGE;
import static com.simbest.cloud.cores.constants.ApplicationConstants.REST_LOGIN_PAGE;
import static com.simbest.cloud.cores.constants.ApplicationConstants.REST_UUMS_LOGIN_PAGE;
import static com.simbest.cloud.cores.constants.ApplicationConstants.UUMS_LOGIN_PAGE;

/**
 * 用途：认证中心服务器安全配置
 * 作者: lishuyi@simbest.com.cn
 * 时间: 2021/3/5  17:09
 *
 */
//libeixiao
//@ConditionalOnProperty(name = "app.security.auth", havingValue = "server")
@Slf4j
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Order(100)
public class FormSecurityConfigurer extends WebSecurityConfigurerAdapter {

    @Autowired
    private ApplicationContext appContext;

    @Autowired
    private SuccessLoginRedirectHandler successLoginRedirectHandler;

    @Autowired
    private SuccessLoginRestHandler successLoginRestHandler;

    @Autowired
    private FailedLoginRedirectHandler failedLoginRedirectHandler;

    @Autowired
    private FailedLoginRestHandler FailedLoginRestHandler;

    @Autowired
    private SuccessLogoutRedirectHandler successLogoutRedirectHandler;

    @Autowired
    private SuccessLogoutRestHandler successLogoutRestHandler;

    @Autowired
    private AccessDeniedForbiddenHandler accessDeniedForbiddenHandler;

    @Autowired
    private RsaEncryptor rsaEncryptor;

    @Autowired
    private IAuthUserCacheService authUserCacheService;

    @Autowired
    private GenericAuthenticationChecker genericAuthenticationChecker;

    @Autowired
    private FindByIndexNameSessionRepository sessionRepository;

    @Bean
    public SpringSessionBackedSessionRegistry sessionRegistry() {
        return new SpringSessionBackedSessionRegistry(sessionRepository);
    }

    /**
     * 配置匹配路径
     *
     * @param web WebSecurity
     * @throws Exception 异常
     */
    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers("/css/**");
        web.ignoring().antMatchers("/js/**");
        web.ignoring().antMatchers("/fonts/**");
        web.ignoring().antMatchers("/img/**");
        web.ignoring().antMatchers("/images/**");
        web.ignoring().antMatchers("/resources/**");
        web.ignoring().antMatchers("/h2-console/**");
        web.ignoring().antMatchers("/captcha/**");
        web.ignoring().antMatchers("/wssocket/**","/wstopic/**","/wsqueue/**","/wsclient/**");
        //allow Swagger URL to be accessed without authentication
        web.ignoring().antMatchers("/v2/api-docs", //swagger api json
                "/swagger-resources/configuration/ui", //用来获取支持的动作
                "/swagger-resources", //用来获取api-docs的URI
                "/swagger-resources/configuration/security", //安全选项
                "/swagger-ui.html");
        web.ignoring().antMatchers(
                "/webjars/**",
                "/favicon.ico",
                "/**/*.html",
                "/**/*.htm",
                "/**/*.css",
                "/**/*.js",
                "/**/*.txt",
                "/**/*.eot",
                "/**/*.svg",
                "/**/*.ttf",
                "/**/*.woff"
        );
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .addFilterBefore(captchaAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(restCaptchaAuthenticationFilter(), CaptchaAuthenticationFilter.class)
                .addFilterBefore(uumsAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(restUumsAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilterAt(rsaAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(restRsaAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .authorizeRequests()
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                .antMatchers(HttpMethod.OPTIONS).permitAll()//跨域请求会先进行一次options请求
                .antMatchers(ApplicationConstants.ROOT_PAGE, ApplicationConstants.WELCOME_PAGE, ApplicationConstants.ERROR_PAGE, LOGIN_PAGE, ApplicationConstants.LOGOUT_PAGE).permitAll()  // 主页、欢迎页、错误页、登陆页、登出页可以匿名访问
                .antMatchers("/h2-console/**", "/html/**").permitAll()  // 都可以访问
                .antMatchers("/httpauth/**", "/**/anonymous/**", "/services/**", "/wx/**").permitAll()  // 都可以访问
                .antMatchers("/action/**").hasRole("USER")   // 需要相应的角色才能访问
                // 需要相应的角色才能访问, 后台管理和javamelody监控
                .antMatchers("/sys/admin/**", "/monitoring/**").hasAnyRole("ADMIN", "SUPER")
                .anyRequest().authenticated()
                .and().formLogin().successHandler(successLoginRedirectHandler) // 成功登入后，重定向到首页
                .loginPage(LOGIN_PAGE).failureUrl(LOGIN_ERROR_PAGE) // 自定义登录界面
                .failureHandler(failedLoginRedirectHandler) //记录登录错误日志，并自定义登录错误提示信息
                .and().logout().logoutSuccessHandler(successLogoutRedirectHandler) // 成功登出后，重定向到登陆页
                .and().exceptionHandling().authenticationEntryPoint(new AccessDeniedEntryPoint()) //无权限返回JSON数据
                .accessDeniedHandler(accessDeniedForbiddenHandler) //无权限返回JSON数据
                .and().headers().frameOptions().sameOrigin()
                .and().csrf().disable().cors().and()

                .sessionManagement().sessionFixation().newSession().invalidSessionUrl(LOGIN_PAGE).maximumSessions(1)
//                .sessionManagement().invalidSessionUrl(LOGIN_PAGE).maximumSessions(5)
                .maxSessionsPreventsLogin(true)
                .sessionRegistry(sessionRegistry())
                .expiredUrl(LOGIN_PAGE);

        Map<String, CustomAbstractAuthenticationProcessingFilter> auths = appContext.getBeansOfType(CustomAbstractAuthenticationProcessingFilter.class);
        for(CustomAbstractAuthenticationProcessingFilter filter : auths.values()){
            log.debug("系统将注册定义过滤器【{}】", filter.getClass());
            http.addFilterAfter(filter, UumsAuthenticationFilter.class);
        }

    }

    /**
     * 验证码
     * @return CaptchaAuthenticationFilter
     * @throws Exception
     */
    @Bean
    public CaptchaAuthenticationFilter captchaAuthenticationFilter() throws Exception {
        CaptchaAuthenticationFilter filter = new CaptchaAuthenticationFilter(
                new OrRequestMatcher(
//                        new AntPathRequestMatcher("/*login", RequestMethod.POST.name())
                        new AntPathRequestMatcher(UUMS_LOGIN_PAGE, RequestMethod.POST.name()),
                        new AntPathRequestMatcher(LOGIN_PAGE, RequestMethod.POST.name())
                ));
        filter.setAuthenticationManager(authenticationManagerBean());
        //跳至登陆页，提醒验证码错误
        filter.setAuthenticationFailureHandler(new SimpleUrlAuthenticationFailureHandler(LOGIN_ERROR_PAGE));
        return filter;
    }

    /** 验证码
     * @return RestCaptchaAuthenticationFilter
     * @throws Exception
     */
    @Bean
    public RestCaptchaAuthenticationFilter restCaptchaAuthenticationFilter() throws Exception {
        RestCaptchaAuthenticationFilter filter = new RestCaptchaAuthenticationFilter(
                new OrRequestMatcher(
                        new AntPathRequestMatcher(REST_UUMS_LOGIN_PAGE, RequestMethod.POST.name()),
                        new AntPathRequestMatcher(REST_LOGIN_PAGE, RequestMethod.POST.name()),
                        new AntPathRequestMatcher("/oauth/token", RequestMethod.POST.name())
                ));
        filter.setAuthenticationManager(authenticationManagerBean());
        filter.setAuthenticationFailureHandler(accessDeniedForbiddenHandler);
        return filter;
    }


    /**
     * 基于数据库的主数据登录认证拦截器，拦截/login请求
     * @return RsaAuthenticationFilter
     * @throws Exception
     */
    @Bean
    public RsaAuthenticationFilter rsaAuthenticationFilter() throws Exception {
        RsaAuthenticationFilter filter = new RsaAuthenticationFilter();
        filter.setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher(LOGIN_PAGE, RequestMethod.POST.name()));
        filter.setAuthenticationManager(authenticationManagerBean());
        //记录成功登录日志
        filter.setAuthenticationSuccessHandler(successLoginRedirectHandler);
        //记录失败登录次数
        filter.setAuthenticationFailureHandler(failedLoginRedirectHandler);
        filter.setEncryptor(rsaEncryptor);
        filter.setAuthUserCacheService(authUserCacheService);
        filter.setGenericAuthenticationChecker(genericAuthenticationChecker);
        return filter;
    }


    /**
     * REST方式，基于数据库的主数据登录认证拦截器，拦截/restlogin请求
     * @return RestRsaAuthenticationFilter
     * @throws Exception
     */
    @Bean
    public RestRsaAuthenticationFilter restRsaAuthenticationFilter() throws Exception {
        RestRsaAuthenticationFilter filter = new RestRsaAuthenticationFilter();
        filter.setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher(REST_LOGIN_PAGE, RequestMethod.POST.name()));
        filter.setAuthenticationManager(authenticationManagerBean());
        //记录成功登录日志
        filter.setAuthenticationSuccessHandler(successLoginRestHandler);
        //记录失败登录次数
        filter.setAuthenticationFailureHandler(FailedLoginRestHandler);
        filter.setEncryptor(rsaEncryptor);
        filter.setAuthUserCacheService(authUserCacheService);
        filter.setGenericAuthenticationChecker(genericAuthenticationChecker);
        return filter;
    }
    /**
     * 通过UUMS认证的应用认证拦截器，拦截/uumslogin请求（WEB方式）
     * @return UumsAuthenticationFilter
     * @throws Exception
     */
    @Bean
    public UumsAuthenticationFilter uumsAuthenticationFilter() throws Exception {
        UumsAuthenticationFilter filter = new UumsAuthenticationFilter(new AntPathRequestMatcher(UUMS_LOGIN_PAGE, RequestMethod.POST.name()));
        filter.setAuthenticationManager(authenticationManagerBean());
        //记录成功登录日志
        filter.setAuthenticationSuccessHandler(successLoginRedirectHandler);
        //记录失败登录次数
        filter.setAuthenticationFailureHandler(failedLoginRedirectHandler);
        return filter;
    }

    /**
     * 通过UUMS认证的应用认证拦截器，拦截/restuumslogin请求（REST方式）
     * @return RestUumsAuthenticationFilter
     * @throws Exception
     */
    @Bean
    public RestUumsAuthenticationFilter restUumsAuthenticationFilter() throws Exception {
        RestUumsAuthenticationFilter filter = new RestUumsAuthenticationFilter(new AntPathRequestMatcher(REST_UUMS_LOGIN_PAGE, RequestMethod.POST.name()));
        filter.setAuthenticationManager(authenticationManagerBean());
        //记录成功登录日志
        filter.setAuthenticationSuccessHandler(successLoginRestHandler);
        //记录失败登录次数
        filter.setAuthenticationFailureHandler(FailedLoginRestHandler);
        return filter;
    }

//    /**
//     * 普通Web认证
//     * @param http
//     * @throws Exception
//     */
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http
//                .authorizeRequests()
//                .antMatchers("/login/**","/oauth/**").permitAll()
//                .anyRequest().authenticated()
//                .and()
//                .formLogin().permitAll()
//                .and()
//                .csrf().disable();
//    }


//    /**
//     * 配合EnableOAuth2Sso的Client认证-REST方式
//     * @param http
//     * @throws Exception
//     */
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http
//                .requestMatchers()
//                .antMatchers("/login", "/logout", "/oauth/**")
//                .and().authorizeRequests().anyRequest().authenticated()
//                .and().formLogin().successHandler(new RestSuccessLoginHandler()).failureHandler(new FailedLoginRestDefaultHandler())
//                .and().logout().logoutSuccessHandler(new SuccessLogoutRestDefaultHandler())
//                .and().exceptionHandling().authenticationEntryPoint(new AccessDeniedEntryPoint())
//                .accessDeniedHandler(new FailedAccessDeniedHandler())
//                .and().headers().frameOptions().sameOrigin()
//                .and().csrf().disable();
//    }


//    /**
//     * 配合EnableOAuth2Sso的Client认证-重定向方式
//     * @param http
//     * @throws Exception
//     */
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http.requestMatchers()
//                .antMatchers("/login", "/oauth/**")
//                .and()
//                .authorizeRequests().anyRequest().authenticated()
//                .and()
//                .formLogin()
//                .and()
//                .csrf().disable();
//    }

    /**
     * 向外暴露Spring Security的AuthenticationManager
     * @return AuthenticationManager
     * @throws Exception
     */
    @Bean(name = BeanIds.AUTHENTICATION_MANAGER)
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

}
