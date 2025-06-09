# SIMBEST团队代码编程助手，熟悉SIMBEST团队的开发规约，需求开发过程中优先检索开发规约中的各类规范进行使用，不重复造轮子，从而帮助用户快速实现需求变更。所有交互请使用中文回答。

## 术语说明

### 各微服务术语
- uums：统一用户微服务，负责账号登录认证；以及人员、组织、权限、角色、职位、人员群组、应用审批决策项等基础信息维护
- nmsg：统一短信微服务
- ntodo：统一待办微服务
- ntydl：统一代理微服务
- iportal：统一门户微服务

## 领域对象实体、持久层、服务层、控制器层代码生成
- 严格遵循《Simbest_Java_Gen.mdc》的规范要求，并严格参考文件中Examples示例生成

## 人员、组织、权限、角色、职位、人员群组、应用审批决策项的字段定义规范
- 严格遵循docs目录下《1-组织人员RBAC与群组决策项定义规范.md》- 定义系统中后端内部、前端与后端、微服务之间的统一用户字段规范

## 领域对象继承关系与字段规范
- 严格遵循docs目录下《2-框架统一领域对象字段规范.md》 - 定义GenericModel、SystemModel、LogicModel、WfFormModel、ActTaskInstModel基类

## 持久层接口定义规范
- 严格遵循docs目录下《3-框架统一持久层接口规范.md》 - 定义Repository层继承关系和接口规范
- 首先，构建org.springframework.data.jpa.domain.Specification查询条件，并复用框架Repository层的查询方法，不允许重复发明轮子，除非用户特殊要求
- 其次，通过JPQL构建复杂的查询语句进行查询，从而实现异构数据库的兼容性保障
- 最后，对于业务级实体对象，除非用户特殊要求，所有查询条件必须判断业务级实体对象enable为真且removedTime为空

## 服务层接口定义规范
- 严格遵循docs目录下《4-框架统一服务层接口规范.md》 - 定义Service层继承关系和接口规范
- 对于查询操作，检查《4-框架统一服务层接口规范.md》所提供方法，通过构建Specification完成查询，不允许重复发明轮子，除非用户特殊要求

## 控制器层接口定义规范
- 严格遵循docs目录下《5-框架统一控制器层接口规范.md》 - 定义Controller层继承关系和接口规范
- 对于前端接口的端点地址，检查《5-框架统一控制器层接口规范.md》所提供方法，不允许重复发明轮子，除非用户特殊要求

## 框架统一通用工具能力规范
- 严格遵循docs目录下《6-框架统一通用工具类接口规范.md》 - 定义各种封装工具类的方法和使用场景

## 框架统一文件、附件、数据字典、数据字典值使用规范
- 严格遵循docs目录下《7-文件与附件及数据字典接口规范.md》 - 定义文件、附件、数据字典、数据字典值的方法和使用场景

## 后端与主数据交互API规范
- 严格遵循docs目录下《8-系统内置与UUMS主数据微服务交互API接口规范.md》 - 定义后端与UUMS统一用户微服务交互的方法和使用场景

## 前端与主数据交互API规范
- 严格遵循docs目录下《9-系统内置与UUMS主数据微服务前端请求接口规范.md》- 定义前端与UUMS统一用户微服务交互的方法和使用场景

## 统一流程引擎接口定义规范
- 严格遵循docs目录下《10-框架统一流程引擎接口规范.md》 - 定义统一流程引擎抽象接口定义

## Flowable6工作流审批流转接口规范
- 严格遵循docs目录下《11-Flowable6工作流审批流转接口规范.md》- 定义Flowable6具体流程引擎实现的接口定义

## 安全规范
- 严格遵循等保2.0三级要求
- 所有外部接口必须进行认证和鉴权
- 敏感数据必须加密存储
- SQL注入防护
- XSS防护
- CSRF防护

## API文档
- 必须使用Swagger注解
- 接口文档实时更新
- 在线文档地址：http://localhost:8080/{{appcode}}/swagger-ui.html

## 重要组件使用规范

### 异常捕获统一要求
- 捕获异常后，至少应该保证第一行编写log.error("类名+方法名+异常信息"); 第二行编写Exceptions.printException(e);

### 后端获取当前登陆人信息
```
	//获取当前登录人详细信息
    IUser iUser = SecurityUtils.getCurrentUser();
	
	//获取当前登录人OA账号
	String currentUserName = SecurityUtils.getCurrentUserName();
```

### 前端获取当前登陆人信息
```
	//POST请求获取当前登录人详细信息
	//1. 需要cookie+session的接口
	/getCurrentUser
	//2. SSO单点登录接口
	/getCurrentUser/sso?loginuser=加密串&username=username明文&appcode=nma
	//3. API访问接口
	/getCurrentUser/api?access_token={OAuth2访问令牌}
```

### 获取当前项目编码，即获取appcode
```
	//注入读取系统配置工具
    @Resource
    private AppConfig appConfig;
	//获取当前系统项目编码appcode
	String appcode = appConfig.getAppcode();
```

### 读取Nacos配置中心参数变量
```
	//注入读取配置的工具类
    @Resource
    private ExtraConfig extraConfig;
	//读取Nacos配置，如获取app.audit.url参数变量值
	String auditUrl = extraConfig.getValue("app.audit.url");
```

### 获取当前环境信息，即获取profile
```
	//注入读取系统Context上下文工具
    @Autowired
    private SpringContextUtil springContextUtil;
	//获取当前系统环境信息profile
	String activeProfile = springContextUtil.getActiveProfile();
```

### 文件附件管理能力
- com.simbest.boot.sys.model.SysFile 是统一文件附件管理对象实体，记录了文件的上传记录、位置信息、文件名称、大小等重要信息
- 各领域对象中的出现文件字段、附件字段均为此对象
- com.simbest.boot.util.AppFileUtil 具体完成文件附件上传、下载的工具类
- com.simbest.boot.sys.service.ISysFileService 提供控制器Controller操作，用于完成文件附件的上传下载和数据库维护
- 更多操作详见《6-框架统一通用工具类接口说明.md》
```
	// 引入文件读写工具类
	import com.simbest.boot.sys.service.ISysFileService;
	// 实现文件读写操作，如：本地保存文件
	SysFile sysFile = sysFileService.uploadProcessFile(file, AUDIT_FILE_UPLOAD, result.getId(), null);
```

### 短信发送能力
```
	// 引入文件读写工具类
	import com.simbest.boot.cmcc.nmsg.MsgPostOperatorService;
	// 构建ShrotMsg对象，发送短信
	public Boolean postMsg(ShrotMsg shrotMsg)
```

### 基于REDIS处理缓存的能力
```
	// 引入Redis工具类
	import com.simbest.boot.util.redis.RedisUtil;
	// 实现Redis缓存处理，如设置某key的值为value，并且设置TTL失效时间
	RedisUtil.set(String key, String value, int seconds)
```

- 更多Redis交互能力详见《6-框架统一通用工具类接口说明》的方法说明

### 定时任务执行能力
- 统一集成cores包的AbstractTaskSchedule
- 在nacos中配置Scheduled注解的cron执行表达式
- 在execute()方法中执行定时任务逻辑
- 以下为标准模板
```
@Slf4j
@Component
public class {{TaskName}}Task extends AbstractTaskSchedule {

    @Autowired
    public {{TaskName}}Task(AppRuntimeMaster master, SysTaskExecutedLogRepository repository) {
        super(master, repository);
    }

    @Scheduled(cron = "${{cron任务执行表达式}}")
    public void checkAndExecute() {
        super.checkAndExecute(true);
    }

    @Override
    public String execute() {
        return "";
    }
}
```

### 外部远程接口或重要逻辑，需要基于Spring-Retry完善重试机制
```
parent包已经加入了spring-retry的依赖，外部远程接口或重要逻辑实现时在代码中可直接使用。默认每分钟重试1次，共计尝试5次。超过五次之后，重试记录入库报错，并且结合上述继承AbstractTaskSchedule的能力，再构建一个定时任务，每5分钟执行一次失败的重试，并且基于MsgPostOperatorService的postMsg方法推送短信至干系人。干系人在Nacos配置中心定义，并通过ExtraConfig获取干系人手机号码配置
```

## Flowable6工作流相关

### 流程对象关联规则
- 流程级领域对象必须与ActTaskInstModel关联
- 关联字段映射：
  - businessKey 对应 pmInsId（主单据ID）
  - assignee 对应办理人OA账号
  - taskDefinitionKey 对应任务环节信息
  - taskCreateTime 对应任务创建时间

---

更多详细信息请参考项目Swagger在线文档：[Swagger UI](http://localhost:8080/{{appcode}}/swagger-ui.html)