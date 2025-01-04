/*
 * 版权所有 © 北京晟壁科技有限公司 2008-2027。保留一切权利!
 */
package com.simbest.cloud.cores.config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.Maps;
import com.simbest.cloud.cores.component.distributed.lock.DistributedLockFactoryBean;
import com.simbest.cloud.cores.constants.ApplicationConstants;
import com.simbest.cloud.cores.enums.StoreLocation;
import com.simbest.cloud.cores.exception.Exceptions;
import com.simbest.cloud.cores.json.JacksonUtils;
import com.simbest.cloud.cores.redis.RedisUtil;
import com.simbest.cloud.cores.response.ApiRequestHandle;
import com.simbest.cloud.cores.response.JsonResponse;
import com.simbest.cloud.cores.sys.model.SysDictValue;
import com.simbest.cloud.cores.utils.encrypt.Des3Encryptor;
import com.simbest.cloud.cores.utils.files.AppFileSftpUtil;
import io.lettuce.core.ClientOptions;
import io.lettuce.core.SocketOptions;
import io.lettuce.core.cluster.ClusterClientOptions;
import io.lettuce.core.cluster.ClusterTopologyRefreshOptions;
import jakarta.annotation.PreDestroy;
import jakarta.annotation.Resource;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.CacheErrorHandler;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.env.MapPropertySource;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.*;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;
//import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.time.Duration;
import java.util.*;

import static com.simbest.cloud.cores.constants.ApplicationConstants.*;
import static com.simbest.cloud.cores.sys.model.SysDictValue.SYS_CONFIG;


/**
 * 用途：Redis 和 RedissonClient 配置信息
 * 作者: lishuyi
 * 时间: 2018/5/1  18:56
 */
@Slf4j
@Configuration
@EnableCaching
//@EnableRedisHttpSession
public class RedisConfiguration extends CachingConfigurerSupport {

    public enum RedisConfigType {
        propertiesRedis,  ftpRedis, sftpRedis, dictValueRedis
    }

    @Autowired //@Resource
    private AppConfig appConfig;

    @Autowired
    private RedisKeyGenerator redisKeyGenerator;

    private Des3Encryptor encryptor = new Des3Encryptor();

    private ApiRequestHandle<SysDictValue> sysDictValueApiHandle = new ApiRequestHandle();

    private ApiRequestHandle<List<SysDictValue>> sysDictValueApiListHandle = new ApiRequestHandle();

    @Getter
    private RedissonClient redissonClient;

    @Getter
    private String redisClusterNodes;

//    @Bean
//    public JedisPoolConfig jedisPoolConfig() {
//        JedisPoolConfig poolConfig = new JedisPoolConfig();
//        poolConfig.setMaxTotal(100);
//        poolConfig.setTestOnBorrow(true);
//        poolConfig.setTestOnReturn(true);
//        return poolConfig;
//    }

    /**
     * @see RedisClusterConfiguration
     * @return
     */
    @Bean
    public RedisClusterConfiguration redisClusterConfiguration(){
        Map<String, Object> source = Maps.newHashMap();
        source.put("spring.redis.cluster.nodes", redisClusterNodes);
        source.put("spring.redis.cluster.max-redirects", appConfig.getRedisMaxRedirects());
        return new RedisClusterConfiguration(new MapPropertySource("RedisClusterConfiguration", source));
    }

//    private LettuceClientConfiguration getLettuceClientConfiguration(GenericObjectPoolConfig genericObjectPoolConfig) {
    private LettuceClientConfiguration getLettuceClientConfiguration() {
        /*
        ClusterTopologyRefreshOptions配置用于开启自适应刷新和定时刷新。如自适应刷新不开启，Redis集群变更时将会导致连接异常！
         */
        ClusterTopologyRefreshOptions topologyRefreshOptions = ClusterTopologyRefreshOptions.builder()
                //开启所有自适应刷新，MOVED，ASK，PERSISTENT都会触发
                .enableAllAdaptiveRefreshTriggers()
                // 自适应刷新超时时间(默认30秒)
                .adaptiveRefreshTriggersTimeout(Duration.ofSeconds(30)) //默认关闭开启后时间为30秒
                // 开周期刷新
                .enablePeriodicRefresh(Duration.ofSeconds(60))  // 默认关闭开启后时间为60秒 ClusterTopologyRefreshOptions.DEFAULT_REFRESH_PERIOD 60  .enablePeriodicRefresh(Duration.ofSeconds(2)) = .enablePeriodicRefresh().refreshPeriod(Duration.ofSeconds(2))
                .build();

        GenericObjectPoolConfig genericObjectPoolConfig = new GenericObjectPoolConfig();
        genericObjectPoolConfig.setMaxTotal(appConfig.getRedisPoolMaxTotal()); //最大连接数
        genericObjectPoolConfig.setMaxIdle(appConfig.getRedisPoolMaxIdle()); //最大空闲连接数
        genericObjectPoolConfig.setMinIdle(appConfig.getRedisPoolMinIdle());  //最小空闲连接数

        return LettucePoolingClientConfiguration.builder()
                .poolConfig(genericObjectPoolConfig)
                .clientOptions(ClusterClientOptions.builder()
                        .topologyRefreshOptions(topologyRefreshOptions)
                        .disconnectedBehavior(ClientOptions.DisconnectedBehavior.REJECT_COMMANDS)
                        .autoReconnect(true)
                        .socketOptions(SocketOptions.builder().keepAlive(true).build())
                        .validateClusterNodeMembership(false)
                        .build()).build();
    }

//    @Bean
//    public RedisConnectionFactory redisConnectionFactory() {
//        JedisConnectionFactory factory;
//        if (clusterNodes.split(ApplicationConstants.COMMA).length == 1) {
//            factory = new JedisConnectionFactory(jedisPoolConfig());
//            factory.setHostName(clusterNodes.split(ApplicationConstants.COLON)[0]);
//            factory.setPort(Integer.valueOf(clusterNodes.split(ApplicationConstants.COLON)[1]));
//        } else {
//            factory = new JedisConnectionFactory(redisClusterConfiguration(), jedisPoolConfig());
//        }
//        factory.setPassword(password);
//        factory.setUsePool(true);
//        return factory;
//    }

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        LettuceConnectionFactory factory = null;
        try {
            RedisConfigType redisConfigTypeEnum = Enum.valueOf(RedisConfigType.class, appConfig.getRedisConfigType());
            Assert.notNull(redisConfigTypeEnum, "Redis配置类型不能为空");
            log.info("缓存配置信息"+redisConfigTypeEnum.name());
            if(RedisConfigType.propertiesRedis.equals(redisConfigTypeEnum)){
                log.info("基于配置文件读取Redis配置");
                redisClusterNodes = appConfig.getRedisClusterNodes();
            }
            else if(RedisConfigType.dictValueRedis.equals(redisConfigTypeEnum)){
                log.info("基于数据库读取Redis配置");
                log.debug("即将通过UUMS主数据【{}】读取Redis配置项【{}】的Redis节点信息", appConfig.getUumsAddress(), appConfig.getRedisConfigTypeRedis());
                SysDictValue sysDictValue = SysDictValue.builder().dictType(SYS_CONFIG).name(appConfig.getRedisConfigTypeRedis()).build();
                String loginuser = StringUtils.replace(encryptor.encrypt(ADMINISTRATOR), "+", "%2B");

                String uumsUrl = appConfig.getUumsAddress() + "/sys/dictValue/sso/findAllNoPage?loginuser="+loginuser+"&appcode="+UUMS_APPCODE;
                HttpUriRequest request = RequestBuilder.post().setUri(uumsUrl)
                        .setEntity(new StringEntity(JacksonUtils.obj2json(sysDictValue), ContentType.APPLICATION_JSON)).build();
                CloseableHttpClient httpClient = HttpClients.createDefault();
                CloseableHttpResponse response = httpClient.execute(request);
                String resp = EntityUtils.toString(response.getEntity(), UTF_8);
                JsonResponse jsonResponse = JacksonUtils.json2obj(resp, JsonResponse.class);
//                JsonResponse jsonResponse = HttpClient.textBody(uumsUrl)
//                        .json(JacksonUtils.obj2json(sysDictValue))
//                        .asBean(JsonResponse.class);

                List<SysDictValue> sysDictValueList = sysDictValueApiListHandle.handRemoteTypeReferenceResponse(jsonResponse, new TypeReference<List<SysDictValue>>(){});
                Assert.notEmpty(sysDictValueList, String.format("通过字典类型%s和字典值名称%s，无法读取REDIS配置",SYS_CONFIG, appConfig.getRedisConfigTypeRedis()));
                SysDictValue redisDv = sysDictValueList.get(ZERO);
                Assert.notNull(redisDv, "REDIS节点配置不能为空！");
                redisClusterNodes = redisDv.getValue();
            }
            else {
                log.info("基于FTP文件读取Redis配置");
                AppFileSftpUtil appFileSftpUtil = new AppFileSftpUtil();
                appFileSftpUtil.setUsername(appConfig.getRedisFtpUsername());
                appFileSftpUtil.setPassword(appConfig.getRedisFtpPassword());
                appFileSftpUtil.setHost(appConfig.getRedisFtpHost());
                appFileSftpUtil.setPort(appConfig.getRedisFtpPort());
                appFileSftpUtil.setKeyFilePath(appConfig.getRedisFtpKeyFile());
                appFileSftpUtil.setPassphrase(appConfig.getRedisFtpPassphrase());
                if(RedisConfigType.ftpRedis.equals(redisConfigTypeEnum)){
                    appFileSftpUtil.setServerUploadLocation(StoreLocation.ftp);
                }
                if(RedisConfigType.sftpRedis.equals(redisConfigTypeEnum)){
                    appFileSftpUtil.setServerUploadLocation(StoreLocation.sftp);
                }
                redisClusterNodes = new String(appFileSftpUtil.download2Byte(appConfig.getRedisFtpNodeConfigDirectory(),
                        appConfig.getRedisFtpNodeConfigFile()));
            }
            redisClusterNodes = StringUtils.trimAllWhitespace(redisClusterNodes);
            Assert.notNull(redisClusterNodes, "REDIS节点配置不能为空！");
            log.info("*************************Redis加载配置节点START******************************");
            log.info("Redis节点为【{}】", redisClusterNodes);
            log.info("*************************Redis加载配置节点END********************************");
            if (redisClusterNodes.split(ApplicationConstants.COMMA).length == 1) {
                RedisStandaloneConfiguration standaloneConfig = new RedisStandaloneConfiguration();
                standaloneConfig.setHostName(redisClusterNodes.split(ApplicationConstants.COLON)[ZERO]);
                standaloneConfig.setPort(Integer.valueOf(redisClusterNodes.split(ApplicationConstants.COLON)[ONE]));
                standaloneConfig.setPassword(RedisPassword.of(appConfig.getRedisPassword()));
                standaloneConfig.setDatabase(0);
                factory = new LettuceConnectionFactory(standaloneConfig);
            } else {
                // 不会自动探测刷新Redis集群状态，一旦集群某个节点宕机，整个集群不可用，抛出RedisConnectionException异常，并可能发生ERR max number of clients reached 和 Error: Connection reset by peer
//                RedisClusterConfiguration clusterConfig = redisClusterConfiguration();
//                clusterConfig.setPassword(RedisPassword.of(config.getRedisPassword()));
//                factory = new LettuceConnectionFactory(clusterConfig);

//                GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
//                poolConfig.setMaxIdle(8);
//                poolConfig.setMinIdle(2);
//                poolConfig.setMaxTotal(0);
//                factory = new LettuceConnectionFactory(clusterConfig, getLettuceClientConfiguration(poolConfig));

                // 自动探测刷新Redis集群状态，某个集群节点宕机，不影响整个集群运作
                Set<RedisNode> redisNodes = new HashSet<>();
                Arrays.asList(redisClusterNodes.split(ApplicationConstants.COMMA)).stream().forEach(address -> {
                    String redisHost = address.split(":")[0].trim();
                    int redisPort = Integer.valueOf(address.split(":")[1]);
                    redisNodes.add(new RedisNode(redisHost, redisPort));
                });
                RedisClusterConfiguration clusterConfig = new RedisClusterConfiguration();
                clusterConfig.setClusterNodes(redisNodes);
                clusterConfig.setPassword(RedisPassword.of(appConfig.getRedisPassword()));
                clusterConfig.setMaxRedirects(Integer.parseInt(appConfig.getRedisMaxRedirects()));
                factory = new LettuceConnectionFactory(clusterConfig, getLettuceClientConfiguration());
            }
        }
        catch (Exception e){
            log.error("加载Redis配置发生错误，请检查配置文件");
            Exceptions.printException(e);
        }
        return factory;
    }

//    @Bean
//    public RedisCacheConfiguration redisCacheConfiguration() {
//        return RedisCacheConfiguration
//                .defaultCacheConfig()
//                .serializeKeysWith(
//                        RedisSerializationContext
//                                .SerializationPair
//                                .fromSerializer(new StringRedisSerializer()))
//                .serializeValuesWith(RedisSerializationContext.
//                                SerializationPair.
//                                fromSerializer(new JdkSerializationRedisSerializer(this.getClass().getClassLoader())))
//                //默认1小时超时
//                .entryTtl(Duration.ofSeconds(3600));
//    }


    @Bean
    @Override
    public CacheManager cacheManager() {
        // 初始化一个RedisCacheWriter
        RedisCacheWriter cacheWriter = RedisCacheWriter.nonLockingRedisCacheWriter(redisConnectionFactory());
        // 设置默认过期时间：60 分钟
        RedisCacheConfiguration defaultCacheConfig = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofSeconds(appConfig.getSessionTimeout()))
                //.prefixKeysWith("cache:key:uums:") //无法区分不同对象相同id时的key
                // .disableCachingNullValues()
                // 使用注解时的序列化、反序列化
                .serializeKeysWith(RedisSerializationContext
                        .SerializationPair.fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(RedisSerializationContext
                        .SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()));
        Map<String, RedisCacheConfiguration> initialCacheConfigurations = new HashMap<>();
        return new RedisCacheManager(cacheWriter, defaultCacheConfig, initialCacheConfigurations);
    }

    @Bean
    @Qualifier("redisTemplate")
    public <T> RedisTemplate<String, T> redisTemplate() {
        /**
         * 解决分离项目报空指针问题
         * 参考：https://www.jianshu.com/p/32d38a7fd20a
         */
        ClassLoader classLoader = this.getClass().getClassLoader();
        RedisTemplate<String, T> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory());
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new JdkSerializationRedisSerializer(classLoader));
        template.setHashKeySerializer(new JdkSerializationRedisSerializer(classLoader));
        template.setHashValueSerializer(new JdkSerializationRedisSerializer(classLoader));
        template.setDefaultSerializer(new JdkSerializationRedisSerializer(classLoader));
        template.afterPropertiesSet();
        return template;
    }

    /**
     * 自定义Key生成策略
     * @return
     */
    @Bean
    @Override
    public KeyGenerator keyGenerator() {
        return redisKeyGenerator;
    }

    @Bean
    @Override
    public CacheErrorHandler errorHandler() {
        CacheErrorHandler cacheErrorHandler = new CacheErrorHandler() {
            /**
             * 从缓存读取数据报错时，不作处理，由数据库提供服务
             * @param e
             * @param cache
             * @param key
             */
            @Override
            public void handleCacheGetError(RuntimeException e, Cache cache, Object key) {
                if(e instanceof RedisConnectionFailureException){
                    log.warn("redis 丢失连接 connection:",e);
                    return;
                }
                throw e;
            }

            /**
             * 向缓存写入数据报错时，不作处理，由数据库提供服务
             * @param e
             * @param cache
             * @param key
             * @param value
             */
            @Override
            public void handleCachePutError(RuntimeException e, Cache cache, Object key, Object value) {
                if(e instanceof RedisConnectionFailureException){
                    log.warn("redis 丢失连接 connection:",e);
                    return;
                }
                throw e;
            }

            /**
             * 删除缓存报错时，抛出异常
             * @param e
             * @param cache
             * @param key
             */
            @Override
            public void handleCacheEvictError(RuntimeException e, Cache cache, Object key) {
                log.error("handleCacheEvictError缓存时异常---key：-"+key+"异常信息:"+e);
                throw e;
            }

            /**
             * 清理缓存报错时，抛出异常
             * @param e
             * @param cache
             */
            @Override
            public void handleCacheClearError(RuntimeException e, Cache cache) {
                log.error("清除缓存时异常---：-"+"异常信息:"+e);
                throw e;
            }
        };
        return cacheErrorHandler;
    }

    @Bean(destroyMethod = "shutdown")
    public RedissonClient redissonClient() {
        Config redissonConfig = new Config();
        if (redisClusterNodes.split(ApplicationConstants.COMMA).length == 1) {
            redissonConfig.useSingleServer().setAddress("redis://"+redisClusterNodes)
            .setPassword(appConfig.getRedisPassword());
        } else {
            String[] nodes = redisClusterNodes.split(ApplicationConstants.COMMA);
            for(int i=0; i<nodes.length; i++){
                nodes[i] = "redis://"+ nodes[i];
            }
            redissonConfig.useClusterServers()
                    .setScanInterval(2000) // cluster state scan interval in milliseconds
                    .setPassword(appConfig.getRedisPassword())
                    .addNodeAddress(nodes);
//                    .addNodeAddress("redis://10.92.80.70:26379", "redis://10.92.80.70:26389", "redis://10.92.80.70:26399")
//                    .addNodeAddress("redis://10.92.80.71:26379", "redis://10.92.80.71:26389", "redis://10.92.80.71:26399");
        }
        redissonClient = Redisson.create(redissonConfig);
        log.debug("Congratulations------------------------------------------------Redis 进程实例已创建成功");
        return redissonClient;
    }

    @Bean
    @DependsOn("redissonClient")
    public DistributedLockFactoryBean distributeLockTemplate(){
        DistributedLockFactoryBean d = new DistributedLockFactoryBean();
        d.setMode("SINGLE");
        return d;
    }

    @PreDestroy
    public void destroy() {
        if(null != redissonClient) {
            log.debug("清理分布式事务锁START................................");
            RedisUtil.cleanRedisLock();
            log.debug("清理分布式事务锁END................................");
//            redissonClient.shutdown(); //RedisConfiguration.redissonClient()申明创建出来的RedissonClient的shutdown执行真正的销毁redissonClient
            log.debug("Congratulations------------------------------------------------Redis 进程实例已销毁成功");
        }
    }



}

