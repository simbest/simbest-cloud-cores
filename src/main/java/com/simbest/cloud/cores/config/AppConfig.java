package com.simbest.cloud.cores.config;

import static com.simbest.cloud.cores.constants.ApplicationConstants.COMMA;
import static com.simbest.cloud.cores.constants.ApplicationConstants.SLASH;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

import jakarta.annotation.PostConstruct;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
@Configuration
@RefreshScope
public class AppConfig {
    @Value("${app.oa.portal.token:}")
    private String mochaPortalToken;
    @Value("${logback.artifactId}")
    private String appcode;
    @Value("${app.host.port}")
    private String appHostPort;
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
    @Value("${app.gateway.address:${app.host.port}/gateway}")
    private String gatewayAddress;
    // 是否开启心跳检测功能
    @Value("${app.heart.check.enable:false}")
    private boolean isOpenHeartCheck;
    @Value("${app.record.web.log:false}")
    private boolean isRecordWebLog;
    @Value("${app.uums.clean.user:true}")
    private boolean isCleanCacheUser;
    @Value("${app.uums.clean.user.time:0 0 5 * * ?}") // 目前没用，UumsSysUserTask是写死的
    private String cleanCacheUserTime;
    @Value("${app.login.ip.white.list:localhost}")
    private List<String> loginWhiteIplist;
    @Value("${app.record.exclude.account:}")
    private String excludeAccounts;
    @Value("${app.record.exclude.ip:}")
    private String excludeIps;

    @Value("${app.a4.logserver.on:false}")
    private Boolean a4LogserverOn;
    @Value("${app.a4.logserver.ip:}")
    private String a4LogserverIp;
    @Value("${app.a4.logserver.port:22}")
    private String a4LogserverPort;
    @Value("${app.a4.logserver.account:host}")
    private String a4LogserverAccount;
    @Value("${app.a4.logserver.password:}")
    private String a4LogserverPassword;
    public static final String DEFAULT_4A_LOGFILE_PATH = "/home/aiuap/gather/";
    @Value("${app.a4.logserver.filePath:/home/aiuap/gather/}") // 需要应用侧进行配置，如：/home/aiuap/gather/MGDICT/
    private String a4LogserverFilePath;
    @Value("#{'${app.a4.sendFile.fail.noticers:}'.split(',')}")
    private List<String> a4LogSendFailNoticers;

    @Value("${app.sso.force.time:false}")
    private Boolean ssoForceTime;
    @Value("${app.sso.salt:}")
    private String ssoSalt;
    @Value("${app.sso.time:1}")
    private Integer ssoTime; // 默认1分钟，即单点支持前后2分钟
    @Value("${app.allowed.origins:*}") // app.allowed.origins=http://iportal.ha.cmcc
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

    // 简单实时短信接口配置
    @Value("${app.sms.account:}")
    private String smsAccount;
    @Value("${app.sms.token:}")
    private String smsToken;
    @Value("${app.sms.appId:}")
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

    //@Value("${app.cloud.auth.public.key:MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDI90D8sRwU5rwagj9fgNQAFH/Ws03jR+qYUUtA3iI05IaqDLrVDdvMQU446/6c+nyJBtdO3P95+dLg7UVQn1bQSj1wLWa5nuJvTh5paBe1XWZj/HmISTpq+OhyGKmX5xNRU96fDld03JyrgEbmHb9T8jks7g5FhKmZmLJBeRTpoQIDAQAB}")
    @Value("${app.cloud.auth.public.key:}")
    private String cloudPublicKey;

    @PostConstruct
    public void init() {
        log.info("************************************应用配置START**************************************************");
        log.info("应用注册代码【{}】", appcode);
        log.info("应用访问地址【{}】", appHostPort);
        log.info("应用访问上下文【{}】", contextPath);
        log.info("应用门户单点加密令牌【{}】", mochaPortalToken);
        log.info("应用超时时间【{}】秒", sessionTimeout);
        log.info("应用上传文件大小限制【{}】", maxFileSize);
        log.info("应用请求主数据地址【{}】", uumsAddress);
        log.info("应用请求统一网关地址【{}】", gatewayAddress);
        log.info("应用接口文档地址【{}】", String.format("%s%s/swagger-ui.html", appHostPort, contextPath));
        log.info("应用请求接收访问登录页地址的IP白名单为【{}】", String.join(COMMA, loginWhiteIplist));
        log.info("应用登录验证码开启状态【{}】", isOpenValidateCode);
        log.info("应用心跳定时器开关打开状态【{}】", isOpenHeartCheck ? true : false);
        log.info("记录Web请求日志状态【{}】", isRecordWebLog ? true : false);
        log.info("4A生成审计日志文件开关状态【{}】", a4LogserverOn ? true : false);
        log.info("4A上传审计日志文件服务器路径地址【{}】", a4LogserverFilePath);
        log.info("清理用户缓存开关【{}】", isCleanCacheUser ? true : false);
        log.info("清理用户缓存定时任务执行周期【{}】", cleanCacheUserTime);
        log.info("SSO单点认证强制启用时间戳【{}】", ssoForceTime);
        log.info("SSO单点认证加密盐值【{}】", ssoSalt);
        log.info("SSO单点认证时间间隔【{}】分钟", ssoTime);
        log.info("Cors跨域请求访问列表【{}】", allowedOrigins);
        uploadTmpFileLocation = System.getProperty("user.dir").concat(SLASH).concat(uploadTmpFileDir)
                .concat(contextPath);
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
        log.info(
                "====================================MongoDB数据库配置START==================================================");
        log.info("MongoDB数据库URL【{}】", mongodbUrl);
        log.info("MongoDB数据库【{}】", mongodbName);
        log.info("MongoDB数据库账号【{}】", mongodbUserName);
        log.info("MongoDB数据库密码【{}】", mongodbPassword);
        log.info(
                "====================================MongoDB数据库配置END==================================================");
        log.info("");
        log.info(
                "^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^REDIS缓存配置START^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
        log.info("Redis配置方式【{}】", redisConfigType);
        // RedisConfiguration.RedisConfigType redisConfigTypeEnum =
        // Enum.valueOf(RedisConfiguration.RedisConfigType.class, redisConfigType);
        // if(RedisConfiguration.RedisConfigType.dictValueRedis.equals(redisConfigTypeEnum)
        // ) {
        // log.info("Redis主数据中配置项为【{}】", redisConfigTypeRedis);
        // }
        // else
        // if(RedisConfiguration.RedisConfigType.ftpRedis.equals(redisConfigTypeEnum) ||
        // RedisConfiguration.RedisConfigType.sftpRedis.equals(redisConfigTypeEnum) ) {
        // log.info("Redis (S)FTP用户名【{}】", redisFtpUsername);
        // log.info("Redis (S)FTP密码【{}】", redisFtpPassword);
        // log.info("Redis (S)FTP主机【{}】", redisFtpHost);
        // log.info("Redis (S)FTP端口【{}】", redisFtpPort);
        // log.info("Redis (S)FTP私钥文件【{}】", redisFtpKeyFile);
        // log.info("Redis (S)FTP私钥密码【{}】", redisFtpPassphrase);
        // log.info("Redis (S)FTP节点配置目录【{}】", redisFtpNodeConfigDirectory);
        // log.info("Redis (S)FTP节点配置文件【{}】", redisFtpNodeConfigFile);
        // }
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
        if (ngEnable) {
            log.info("Nginx代理暴露文件自定义上传位置启动状态【{}】", ngCustomUploadFlag);
            log.info("Nginx代理暴露文件自定义上传位置路径地址【{}】", ngCustomUploadPath);
        }
        log.info("####################################文件存储配置END##################################################");
        log.info("");
    }
}
