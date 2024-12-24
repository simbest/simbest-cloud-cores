/*
 * 版权所有 © 北京晟壁科技有限公司 2008-2027。保留一切权利!
 */
package com.simbest.cloud.cores.config;

import jakarta.annotation.PostConstruct;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.simbest.cloud.cores.constants.ApplicationConstants.COMMA;
import static com.simbest.cloud.cores.constants.ApplicationConstants.SLASH;


/**
 * 用途：应用配置
 * 参考: https://segmentfault.com/a/1190000016941757
 *
 * @ConfigurationProperties(prefix = "doc")
 *
 * private boolean preferIpAddress;
 * private int maxConnections=0;
 * private int port;
 * private AuthInfo authInfo;
 * private List<String> whitelist;
 * private Map<String,String> converter;
 * private List<Person> defaultShareUsers;
 *
 * doc.prefer-ip-address=true
 * doc.port=8080
 * doc.max-connections=30
 * #doc.whitelist=192.168.0.1,192.168.0.2
 * # 这种等同于下面的doc.whitelist[0] doc.whitelist[1]
 * doc.whitelist[0]=192.168.0.1
 * doc.whitelist[1]=192.168.0.2
 * doc.default-share-users[0].name=jack
 * doc.default-share-users[0].age=18
 * doc.converter.a=xxConverter
 * doc.converter.b=xxConverter
 * doc.auth-info.username=user
 * doc.auth-info.password=password
 *
 * 作者: lishuyi
 * 时间: 2018/8/16  13:52
 */
@Slf4j
@Data
@Component
@RefreshScope
public class AppConfig {

//    @Value("${app.oa.portal.token:SIMBEST_SSO}")
//    private String mochaPortalToken;
    @Value("${logback.artifactId}")
    private String appcode;
    @Value("${app.host.port}")
    private String appHostPort;
    @Value("${app.cloud.auth.public.key:MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDI90D8sRwU5rwagj9fgNQAFH/Ws03jR+qYUUtA3iI05IaqDLrVDdvMQU446/6c+nyJBtdO3P95+dLg7UVQn1bQSj1wLWa5nuJvTh5paBe1XWZj/HmISTpq+OhyGKmX5xNRU96fDld03JyrgEbmHb9T8jks7g5FhKmZmLJBeRTpoQIDAQAB}")
    private String cloudPublicKey;
    @Value("${app.cloud.auth.private.key:MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAMj3QPyxHBTmvBqCP1+A1AAUf9azTeNH6phRS0DeIjTkhqoMutUN28xBTjjr/pz6fIkG107c/3n50uDtRVCfVtBKPXAtZrme4m9OHmloF7VdZmP8eYhJOmr46HIYqZfnE1FT3p8OV3TcnKuARuYdv1PyOSzuDkWEqZmYskF5FOmhAgMBAAECgYASt7S91GEhMTxj2627X2xfdYlSnfCrMo+PEENKD2ZwNri0LetY3KmUJKD8fD6CsHHY8WIsXjkNS09w06iZEb4sDf1PLluo+HJTZsRWBb7Wa+PEdVwVwjO8FFnsmO3G9E/ObWQdlxMLbJeXN1l4uqbhSpkO/aTo7o/CzbQLbxtyAQJBAOESrdrO1o6OW/ztwFqHv+n0FmN/knHiHjJt5ILZE8QJAqi/pxUIbXmIOIDhbFVE9Foq5arN70rn0uROavVanAkCQQDklJDZBEmPWfQRc+YHp5sVEwBhl9u+HlPRTIwfnNnxGaBRGHgCAWti9BRtFPK7aMLYYGngHCke4u9onH4kJvbZAkBRINGICHxwQEJKJkzqlPoJU5FqZgachDwMQ25V9/dW90R9HTAVtcb4QrDTS+4nwkYt5j6I1dhGcM+kudt1+yY5AkA7qZONMZNJuX79NzUpdlQCUi1dS9ftbdkO3l4MulIgnkG8KRjZ3Sj8cR0Lw9X/mL6S38eC2ZbaGv3GXmKGaS0xAkBXUk5oNzRizhKyZDPfuaCaKMKySVhY2si3OQkuRd7vBbuZT3hgIwQxSX6oti9DCjnxHWj6+Fu+gy7VoNQnx32U}")
    private String cloudPrivateKey;
    @Value("${server.servlet.context-path}")
    private String contextPath;
    @Value("${server.servlet.session.timeout}")
    private Integer sessionTimeout;
    @Value("${spring.servlet.multipart.max-file-size}")
    private String maxFileSize;
    @Value("${spring.session.cookie.path:}")
    private String cookiePath;
    // 是否开启验证码功能
    @Value("${app.captcha.enable}")
    private boolean isOpenValidateCode = true;
    @Value("${app.uums.address}")
    private String uumsAddress;
    // 是否开启心跳检测功能
    @Value("${app.heart.check.enable:false}")
    private boolean isOpenHeartCheck;
    @Value("${app.record.web.log:false}")
    private boolean isRecordWebLog;
    @Value("${app.uums.clean.user:true}")
    private boolean isCleanCacheUser;
    @Value("${app.uums.clean.user.time:0 0 5 * * ?}") //目前没用，UumsSysUserTask是写死的
    private String cleanCacheUserTime;
    @Value("${app.login.ip.white.list:localhost}")
    private List<String> loginWhiteIplist;
    @Value("${app.record.exclude.account:sjbg|hadmin|hadmin1|hadmin2|hadmin3}")
    private String excludeAccounts;
    @Value("${app.record.exclude.ip:10.87.57.*,10.87.41.139,10.87.41.140,10.87.41.141,10.87.41.142,10.87.41.144}")
    private String excludeIps;

    @Value("${app.a4.logserver.on:false}")
    private Boolean a4LogserverOn;
    @Value("${app.a4.logserver.ip:10.96.23.1}")
    private String a4LogserverIp;
    @Value("${app.a4.logserver.port:22}")
    private String a4LogserverPort;
    @Value("${app.a4.logserver.account:host}")
    private String a4LogserverAccount;
    @Value("${app.a4.logserver.password:5*QCKxJM}")
    private String a4LogserverPassword;
    @Value("${app.a4.logserver.filePath:/home/aiuap/gather/}")   //需要应用侧进行配置，如：/home/aiuap/gather/MGDICT/
    private String a4LogserverFilePath;
    @Value("#{'${app.a4.sendFile.fail.noticers:sjbg,hadmin2}'.split(',')}")
    private List<String>  a4LogSendFailNoticers;


    @Value("${app.allowed.origins:*}")
    private String allowedOrigins;
    public static final String uploadTmpFileDir = "springboottmp";
    private String uploadTmpFileLocation;
    @Value("${app.security.white.hosts}")
    private String whiteHostList;
    @Value("${app.swagger.address}")
    private String swaggerUrl;


    @Value("${thread.core.pool.size:10}")
    private int threadCorePoolSize;
    @Value("${thread.max.pool.size:20}")
    private int threadMaxPoolSize;
    @Value("${thread.queue.capacity:200}")
    private int threadQueueCapacity;
    @Value("${thread.keep.alive.seconds:60}")
    private int threadKeepAliveSeconds;


    @Value("${spring.datasource.url}")
    private String datasourceUrl;
    @Value("${spring.datasource.username}")
    private String datasourceUsername;
    @Value("${spring.datasource.password}")
    private String datasourcePassword;
    @Value("${spring.datasource.driver-class-name}")
    private String datasourceDriverClassName;
    @Value("${spring.datasource.druid.initial-size}")
    private Integer datasourceDruidInitialSize;
    @Value("${spring.datasource.druid.min-idle}")
    private Integer datasourceDruidMinIdle;
    @Value("${spring.datasource.druid.max-active}")
    private Integer datasourceDruidMaxActive;
    @Value("${spring.datasource.druid.max-wait}")
    private Integer datasourceDruidMaxWait;
    @Value("${spring.datasource.druid.time-between-eviction-runs-millis}")
    private Integer datasourceDruidTimeBetweenEvictionRunsMillis;
    @Value("${spring.datasource.druid.min-evictable-idle-time-millis}")
    private Integer datasourceDruidMinEvictableIdleTimeMillis;
    @Value("${spring.datasource.druid.validation-query}")
    private String datasourceDruidValidationQuery;
    @Value("${spring.datasource.druid.test-while-idle}")
    private Boolean datasourceDruidTestWhileIdle;
    @Value("${spring.datasource.druid.test-on-borrow}")
    private Boolean datasourceDruidTestOnBorrow;
    @Value("${spring.datasource.druid.test-on-return}")
    private Boolean datasourceDruidTestOnReturn;

    @Value("${spring.redis.config.type:propertiesRedis}")
    private String redisConfigType;
    @Value("${spring.redis.config.type.redis:}")
    private String redisConfigTypeRedis;
    @Value("${spring.redis.config.ftp.username:}")
    private String redisFtpUsername;
    @Value("${spring.redis.config.ftp.password:}")
    private String redisFtpPassword;
    @Value("${spring.redis.config.ftp.host:}")
    private String redisFtpHost;
    @Value("${spring.redis.config.ftp.port:22}")
    private int redisFtpPort;
    @Value("${spring.redis.config.ftp.keyFile:}")
    private String redisFtpKeyFile;
    @Value("${spring.redis.config.ftp.passphrase:}")
    private String redisFtpPassphrase;
    @Value("${spring.redis.config.ftp.node.config.directory:}")
    private String redisFtpNodeConfigDirectory;
    @Value("${spring.redis.config.ftp.node.config.file:}")
    private String redisFtpNodeConfigFile;
    @Value("${spring.redis.cluster.nodes:}")
    private String redisClusterNodes;
    @Value("${spring.redis.cluster.password}")
    private String redisPassword;
    @Value("${spring.redis.cluster.max-redirects}")
    private String redisMaxRedirects;
    @Value("${spring.session.redis.namespace}")
    private String redisNamespace;
    @Value("${spring.cache.redis.key-prefix}")
    private String redisKeyPrefix;
    @Value("${spring.cache.redis.lock.wait.seconds:3}")
    private int redisLockWaitSeconds;
    @Value("${spring.cache.redis.lock.release.seconds:60}")
    private int redisLockReleaseSeconds;
    @Value("${spring.cache.redis.pool.MaxTotal:100}")
    private int redisPoolMaxTotal;
    @Value("${spring.cache.redis.pool.MaxIdle:50}")
    private int redisPoolMaxIdle;
    @Value("${spring.cache.redis.pool.MinIdle:5}")
    private int redisPoolMinIdle;


    @Value("${app.file.upload.location}")
    private String uploadLocation;
    @Value("${app.file.upload.path}")
    private String uploadPath;
    @Value("${app.nginx.enable:false}")
    private boolean ngEnable;
    @Value("${app.nginx.custom.upload.flag:false}")
    private String ngCustomUploadFlag;
    @Value("${app.nginx.custom.upload.path:null}")
    private String ngCustomUploadPath;
    @Value("${app.file.dir.ymd.flag:true}")
    private String fileDirYmdFlag;

    //简单实时短信接口配置
    @Value("${app.sms.account:8a48b5515018a0f40150467da6134cddsim}")
    private String smsAccount;
    @Value("${app.sms.token:6cde887f4355445fa2c16f9fb073fbf7be}")
    private String smsToken;
    @Value("${app.sms.appId:8a48b5515018a0f4015046d3765c4ea3st}")
    private String smsAppId;
    @Value("${app.sms.templateId:408992008}")
    private String smsTemplateId;

    @Value("${jar.encrypt.key.config:H}")
    private String licenseKeyCon;

    @Value("${spring.data.mongodb.uri:}")
    private String mongodbUrl;
    @Value("${spring.data.mongodb.database:}")
    private String mongodbName;
    @Value("${spring.data.mongodb.username:}")
    private String mongodbUserName;
    @Value("${spring.data.mongodb.password:}")
    private String mongodbPassword;



    @PostConstruct
    public void init() {
        log.info("************************************应用配置START**************************************************");

        log.info("应用注册代码【{}】", appcode);
        log.info("应用访问地址【{}】", appHostPort);
        log.info("应用访问上下文【{}】", contextPath);
        log.info("应用超时时间【{}】秒", sessionTimeout);
        log.info("应用上传文件大小限制【{}】", maxFileSize);
        log.info("应用Cookie路径【{}】", cookiePath);
        log.info("应用请求主数据地址【{}】", uumsAddress);
        log.info("应用接口文档地址【{}】", String.format("%s%s/swagger-ui.html", appHostPort, contextPath));
        log.info("应用请求接收访问登录页地址的IP白名单为【{}】", String.join(COMMA, loginWhiteIplist));
        log.info("应用登录验证码开启状态【{}】", isOpenValidateCode);
        log.info("应用心跳定时器开关打开状态【{}】", isOpenHeartCheck ? true : false);
        log.info("记录Web请求日志状态【{}】", isRecordWebLog ? true : false);
        log.info("4A生成审计日志文件开关状态【{}】", a4LogserverOn ? true : false);
        log.info("4A上传审计日志文件服务器路径地址【{}】", a4LogserverFilePath);
        log.info("清理用户缓存开关【{}】", isCleanCacheUser ? true : false);
        log.info("清理用户缓存定时任务执行周期【{}】", cleanCacheUserTime);
        log.info("跨域访问列表【{}】", allowedOrigins);
        uploadTmpFileLocation = System.getProperty("user.dir").concat(SLASH).concat(uploadTmpFileDir).concat(contextPath);
        log.info("应用临时文件上传目录为【{}】", uploadTmpFileLocation);
        log.info("应用获准访问白名单【{}】", whiteHostList);
        log.info("************************************应用配置END**************************************************");
        log.info("");
        log.info("------------------------------------多线程配置START--------------------------------------------------");
        log.info("多线程核心线程数【{}】", threadCorePoolSize);
        log.info("多线程最大线程数【{}】", threadMaxPoolSize);
        log.info("多线程缓冲队列【{}】", threadQueueCapacity);
        log.info("多线程空闲时间【{}】", threadKeepAliveSeconds);
        log.info("------------------------------------多线程配置END--------------------------------------------------");
        log.info("");
        log.info("====================================数据库配置START==================================================");
        log.info("数据库URL【{}】", datasourceUrl);
        log.info("数据库账号【{}】", datasourceUsername);
        log.info("数据库密码【{}】", datasourcePassword);
        log.info("====================================数据库配置END==================================================");
        log.info("");
        log.info("====================================MongoDB数据库配置START==================================================");
        log.info("MongoDB数据库URL【{}】", mongodbUrl);
        log.info("MongoDB数据库【{}】", mongodbName);
        log.info("MongoDB数据库账号【{}】", mongodbUserName);
        log.info("MongoDB数据库密码【{}】", mongodbPassword);
        log.info("====================================MongoDB数据库配置END==================================================");
        log.info("");
        log.info("^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^REDIS缓存配置START^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
        log.info("Redis配置方式【{}】", redisConfigType);
        RedisConfiguration.RedisConfigType redisConfigTypeEnum = Enum.valueOf(RedisConfiguration.RedisConfigType.class, redisConfigType);
        if(RedisConfiguration.RedisConfigType.dictValueRedis.equals(redisConfigTypeEnum) ) {
            log.info("Redis主数据中配置项为【{}】", redisConfigTypeRedis);
        }
        else if(RedisConfiguration.RedisConfigType.ftpRedis.equals(redisConfigTypeEnum) ||
                RedisConfiguration.RedisConfigType.sftpRedis.equals(redisConfigTypeEnum) ) {
            log.info("Redis (S)FTP用户名【{}】", redisFtpUsername);
            log.info("Redis (S)FTP密码【{}】", redisFtpPassword);
            log.info("Redis (S)FTP主机【{}】", redisFtpHost);
            log.info("Redis (S)FTP端口【{}】", redisFtpPort);
            log.info("Redis (S)FTP私钥文件【{}】", redisFtpKeyFile);
            log.info("Redis (S)FTP私钥密码【{}】", redisFtpPassphrase);
            log.info("Redis (S)FTP节点配置目录【{}】", redisFtpNodeConfigDirectory);
            log.info("Redis (S)FTP节点配置文件【{}】", redisFtpNodeConfigFile);
        }
        log.info("Redis密码【{}】", redisPassword);
        log.info("Redis重定向次数【{}】", redisMaxRedirects);
        log.info("Redis缓存空间前缀【{}】", redisNamespace);
        log.info("Redis缓存Key键前缀【{}】", redisKeyPrefix);
        log.info("Redis缓存默认等待加锁时间【{}】秒", redisLockWaitSeconds);
        log.info("Redis缓存默认加锁后释放时间【{}】秒", redisLockReleaseSeconds);
        log.info("Redis缓存连接池最大连接数【{}】个", redisPoolMaxTotal);
        log.info("Redis缓存连接池最大空闲连接数【{}】个", redisPoolMaxIdle);
        log.info("Redis缓存连接池最小空闲连接数【{}】个", redisPoolMinIdle);
        log.info("^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^REDIS缓存配置END^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
        log.info("");
        log.info("####################################文件存储配置START##################################################");
        log.info("应用文件上传方式【{}】", uploadLocation);
        log.info("应用上传文件路径【{}】", uploadPath);
        log.info("应用下载文件启用Nginx映射状态【{}】", ngEnable);
        if(ngEnable) {
            log.info("Nginx代理暴露文件自定义上传位置启动状态【{}】", ngCustomUploadFlag);
            log.info("Nginx代理暴露文件自定义上传位置路径地址【{}】", ngCustomUploadPath);
        }
        log.info("####################################文件存储配置END##################################################");
        log.info("");
    }



}
