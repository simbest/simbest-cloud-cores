# 1-组织人员RBAC与群组决策项定义规范

本文档详细说明了系统中用户组织相关的所有模型类的字段规范。

## 目录
1. [IApp - 应用信息接口](#iapp---应用信息接口)
2. [IAppDecision - 应用决策接口](#iappdecision---应用决策接口)
3. [IAuthService - 统一认证接口](#iauthservice---统一认证接口)
4. [IBloc - 集团信息接口](#ibloc---集团信息接口)
5. [IConfig - 应用配置信息接口](#iconfig---应用配置信息接口)
6. [ICorp - 企业信息接口](#icorp---企业信息接口)
7. [IGroup - 群组信息接口](#igroup---群组信息接口)
8. [IOrg - 组织信息接口](#iorg---组织信息接口)
9. [IPermission - 权限信息接口](#ipermission---权限信息接口)
10. [IPosition - 职位信息接口](#iposition---职位信息接口)
11. [IRole - 角色信息接口](#irole---角色信息接口)
12. [IUser - 用户信息接口](#iuser---用户信息接口)
13. [IUserOrg - 用户组织关系接口](#iuserorg---用户组织关系接口)
14. [MySimpleGrantedAuthority - 权限控制类](#mysimplegrantedauthority---权限控制类)
15. [SimpleApp - 应用信息实现类](#simpleapp---应用信息实现类)
16. [SimpleAppDecision - 应用决策实体类](#simpleappdecision---应用决策实体类)
17. [SimpleBloc - 业务集团实体类](#simplebloc---业务集团实体类)
18. [SimpleConfig - 配置实体类](#simpleconfig---配置实体类)
19. [SimpleCorp - 企业实体类](#simplecorp---企业实体类)
20. [SimpleGroup - 用户组实体类](#simplegroup---用户组实体类)
21. [SimpleModel - 基础模型类](#simplemodel---基础模型类)
22. [SimpleOrg - 组织机构实体类](#simpleorg---组织机构实体类)
23. [SimplePermission - 权限实体类](#simplepermission---权限实体类)
24. [SimplePosition - 职位实体类](#simpleposition---职位实体类)
25. [SimpleRole - 角色实体类](#simplerole---角色实体类)
26. [SimpleUser - 用户实体类](#simpleuser---用户实体类)
27. [SimpleUserOrg - 用户组织关系实体类](#simpleuserorg---用户组织关系实体类)
28. [UserOrgTree - 用户组织树实体类](#userorgtree---用户组织树实体类)

## 详细规范

### IApp - 应用信息接口

**文件类型**：接口类  
**文件路径**：src/main/java/com/simbest/boot/security/IApp.java

#### 字段说明

| 字段名 | 类型 | 说明 |
|--------|------|------|
| id | String | 应用唯一标识 |
| receiveName | String | 接收名称 |
| appCode | String | 应用编码 |
| appName | String | 应用名称 |
| accessUrl | String | 访问URL |
| deployUrl | String | 部署URL |
| clicks | Integer | 点击次数 |
| todoOpen | Boolean | 是否打开待办标志 |
| appDescription | String | 应用描述 |
| appFirmName | String | 应用厂商名称 |
| appFirmTel | String | 应用厂商电话 |
| appFirmMail | String | 应用厂商邮箱 |
| appCmName | String | 应用联系人姓名 |
| appCmTel | String | 应用联系人电话 |
| appCmMail | String | 应用联系人邮箱 |
| appCmCode | String | 应用联系人编码 |
| appTypeDictValue | String | 应用类型字典值 |
| appLevel | String | 应用级别 |
| isSendMsg | Boolean | 是否发送消息标志 |
| isControlCorp | Boolean | 是否控制企业标志 |

### IAppDecision - 应用决策接口

**文件类型**：接口类  
**文件路径**：src/main/java/com/simbest/boot/security/IAppDecision.java

#### 字段说明

| 字段名 | 类型 | 说明 |
|--------|------|------|
| id | String | 决策唯一标识 |
| groupId | String | 组ID |
| appCode | String | 应用编码 |
| processDefId | String | 流程定义ID |
| processDefName | String | 流程定义名称 |
| activityDefId | String | 活动定义ID |
| activityDefName | String | 活动定义名称 |
| decisionId | String | 决策ID |
| decisionName | String | 决策名称 |
| opinion | String | 意见内容 |
| spare2 | String | 备用字段2内容 |
| decisionConfig | String | 决策配置信息 |
| decisionType | String | 决策类型 |

### IAuthService - 统一认证接口

**文件类型**：接口类  
**文件路径**：src/main/java/com/simbest/boot/security/IAuthService.java

#### 枚举类型

**KeyType** - 用户关键字类型枚举：
- username：登录名
- employeeNumber：人员编号
- preferredMobile：手机号码
- email：邮箱
- openid：微信openid
- unionid：微信unionid
- reserve1：保留关键字

#### 方法说明

| 方法名 | 参数 | 返回类型 | 说明 |
|--------|------|----------|------|
| findByKey | keyword: String, keyType: KeyType | IUser | 根据关键字查询用户身份信息 |
| findUserPermissionByAppcode | username: String, appcode: String | Set<? extends IPermission> | 为应用追加用户定制权限 |
| checkUserAccessApp | username: String, appcode: String | boolean | 判断用户是否可以访问应用 |
| customUserForApp | iUser: IUser, appcode: String | IUser | 为应用定制用户信息 |
| changeUserSessionByCorp | newuser: IUser | void | 根据用户信息更新用户Session |
| updateUserOpenidAndUnionid | preferredMobile: String, openid: String, unionid: String, appcode: String | int | 通过手机号码查找用户并绑定微信信息 |
| createUser | keyword: String, keytype: KeyType, appcode: String, user: SimpleUser | IUser | 提供应用向主数据创建用户的接口 |
| updateUser | keyword: String, keytype: KeyType, appcode: String, user: SimpleUser | IUser | 提供应用向主数据更新用户的接口 |

### IBloc - 集团信息接口

**文件类型**：接口类  
**文件路径**：src/main/java/com/simbest/boot/security/IBloc.java

#### 字段说明

| 字段名 | 类型 | 说明 |
|--------|------|------|
| id | String | 集团唯一标识 |
| blocCode | String | 集团编码 |
| blocName | String | 集团名称 |
| blocDesc | String | 集团简介 |

### IConfig - 应用配置信息接口

**文件类型**：接口类  
**文件路径**：src/main/java/com/simbest/boot/security/IConfig.java

#### 字段说明

| 字段名 | 类型 | 说明 |
|--------|------|------|
| id | String | 配置唯一标识 |
| address | String | 地址信息 |
| interfaceStyle | String | 接口风格 |
| isOpen | Boolean | 是否开放标志 |
| isInner | Boolean | 是否内部标志 |
| provider | String | 提供者信息 |

### ICorp - 企业信息接口

**文件类型**：接口类  
**文件路径**：src/main/java/com/simbest/boot/security/ICorp.java

#### 字段说明

| 字段名 | 类型 | 说明 |
|--------|------|------|
| id | String | 企业唯一标识 |
| blocId | String | 集团信息ID |
| corpCode | String | 企业编码 |
| corpName | String | 企业名称 |
| corpShortName | String | 企业简称 |
| corpDesc | String | 企业简介 |
| corpContent | String | 主体信息 |
| corpMail | String | 企业邮箱 |
| corpTel | String | 企业电话 |
| corpLegalPerson | String | 企业法人 |
| postcode | String | 邮政编码 |
| address | String | 企业地址 |
| industry | String | 服务行业 |
| registerLocation | String | 注册地址 |
| belongArea | String | 所在地区 |
| webUrl | String | 官方网站URL |
| linkMan | String | 联系人 |
| linkManPhone | String | 联系电话 |
| linkManMail | String | 联系邮箱 |

### IGroup - 群组信息接口

**文件类型**：接口类  
**文件路径**：src/main/java/com/simbest/boot/security/IGroup.java

#### 字段说明

| 字段名 | 类型 | 说明 |
|--------|------|------|
| id | String | 群组唯一标识 |
| name | String | 群组名称 |
| sid | String | 群组安全标识符 |
| fid | String | 群组父级标识符 |
| levels | String | 群组级别 |
| groupType | String | 群组类型 |
| description | String | 群组描述 |
| refSid | String | 引用安全标识符 |
| displayOrder | Integer | 显示顺序 |

### IOrg - 组织信息接口

**文件类型**：接口类  
**文件路径**：src/main/java/com/simbest/boot/security/IOrg.java

#### 字段说明

| 字段名 | 类型 | 说明 |
|--------|------|------|
| id | String | 组织唯一标识 |
| orgCode | String | 组织编码 |
| orgName | String | 组织名称 |
| parentOrgCode | String | 父级组织编码 |
| displayOrder | Integer | 显示顺序 |
| displayName | String | 显示名称 |
| belongCompanyCode | String | 所属公司编码 |
| belongDepartmentCode | String | 所属部门编码 |
| levelDictValue | String | 级别字典值 |
| reserve1 | String | 预留字段1 |
| reserve2 | String | 预留字段2 |
| reserve3 | String | 预留字段3 |
| reserve4 | String | 预留字段4 |
| reserve5 | String | 预留字段5 |
| styleDictValue | String | 样式字典值 |
| companyTypeDictValue | String | 公司类型字典值 |
| erpId | String | 此部门在ERP-HR中对应的组织编码 |
| corpId | String | 企业ID |
| isCorpRoot | Boolean | 是否为企业根组织 |

### IPermission - 权限信息接口

**文件类型**：接口类  
**文件路径**：src/main/java/com/simbest/boot/security/IPermission.java

#### 字段说明

| 字段名 | 类型 | 说明 |
|--------|------|------|
| id | String | 权限唯一标识 |
| permissionCode | String | 权限编码 |
| description | String | 权限描述 |
| url | String | 权限对应的URL |
| icon | String | 权限对应的图标 |
| menuLevel | Integer | 菜单级别 |
| displayOrder | Integer | 显示顺序 |
| type | String | 权限类型 |
| parentId | String | 父级权限ID |
| remark | String | 备注信息 |

### IPosition - 职位信息接口

**文件类型**：接口类  
**文件路径**：src/main/java/com/simbest/boot/security/IPosition.java

#### 字段说明

| 字段名 | 类型 | 说明 |
|--------|------|------|
| id | String | 职位唯一标识 |
| positionName | String | 职位名称 |
| positionCompany | String | 职位所属公司 |
| positionCode | Integer | 职位编码 |
| positionType | Integer | 职位类型 |

### IRole - 角色信息接口

**文件类型**：接口类  
**文件路径**：src/main/java/com/simbest/boot/security/IRole.java

#### 字段说明

| 字段名 | 类型 | 说明 |
|--------|------|------|
| id | String | 角色唯一标识 |
| roleCode | String | 角色编码 |
| roleName | String | 角色名称 |
| isApplicationRole | Boolean | 是否为应用角色 |
| displayOrder | Integer | 显示顺序 |

### IUser - 用户信息接口

**文件类型**：接口类  
**文件路径**：src/main/java/com/simbest/boot/security/IUser.java

#### 字段说明

| 字段名 | 类型 | 说明 |
|--------|------|------|
| id | String | 用户唯一标识 |
| username | String | OA账号 |
| password | String | 密码 |
| truename | String | 真实姓名 |
| nickname | String | 昵称 |
| displayOrder | Integer | 显示顺序 |
| email | String | 邮箱地址 |
| preferredMobile | String | 主数据规范首选移动电话 |
| telephoneNumber | String | 主数据规范用户办公电话 |
| genderDictValue | Integer | 性别(1：男 2：女 3：未知) |
| isCmcc | Boolean | 是否是公司内部用户 |
| photo | String | 照片 |
| userType | Integer | 用户类型 |
| openid | String | 微信OpenID |
| unionid | String | 微信UnionID |
| positionLevel | Integer | 职位等级 |
| employeeNumber | String | 员工编码 |
| currentBloc | String | 当前集团ID |
| currentBlocCode | String | 当前集团编码 |
| currentCorp | String | 当前企业ID |
| currentCorpCode | String | 当前企业编码 |
| belongCompanyCode | String | 所属公司编码 |
| belongCompanyName | String | 所属公司名称 |
| belongCompanyCodeParent | String | 所属上级公司编码 |
| belongCompanyNameParent | String | 所属上级公司名称 |
| belongCompanyTypeDictValue | String | 所属公司类型(01省公司,02分公司,03县公司) |
| belongCompanyTypeDictDesc | String | 所属公司类型描述 |
| belongDepartmentCode | String | 所属部门编码 |
| belongDepartmentName | String | 所属部门名称 |
| belongOrgCode | String | 所属组织编码 |
| belongOrgName | String | 所属组织名称 |
| reserve1 | String | 预留字段1 |
| reserve2 | String | 预留字段2 |
| reserve3 | String | 预留字段3 |
| reserve4 | String | 用户登录密码 |
| reserve5 | String | 预留字段5 |

### IUserOrg - 用户组织关系接口

**文件类型**：接口类  
**文件路径**：src/main/java/com/simbest/boot/security/IUserOrg.java

#### 字段说明

| 字段名 | 类型 | 说明 |
|--------|------|------|
| id | String | 用户组织关系唯一标识 |
| orgCode | String | 组织编码 |
| username | String | 用户名 |
| positionId | String | 职位ID |
| displayOrder | Integer | 显示顺序 |
| status | String | 状态信息 |

### MySimpleGrantedAuthority - 权限控制类

**文件类型**：普通类  
**文件路径**：src/main/java/com/simbest/boot/security/MySimpleGrantedAuthority.java

#### 字段说明

| 字段名 | 类型 | 说明 |
|--------|------|------|
| authority | String | 权限标识(在Spring Security中代表一个权限的字符串) |

### SimpleApp - 应用信息实现类

**文件类型**：普通类  
**文件路径**：src/main/java/com/simbest/boot/security/SimpleApp.java

#### 字段说明

| 字段名 | 类型 | 说明 |
|--------|------|------|
| id | String | 应用ID |
| receiveName | String | 接收名称 |
| appCode | String | 应用编码 |
| appName | String | 应用名称 |
| accessUrl | String | 访问URL |
| deployUrl | String | 部署URL |
| clicks | Integer | 点击次数 |
| todoOpen | Boolean | 是否打开待办 |
| appDescription | String | 应用描述 |
| appFirmName | String | 应用厂商名称 |
| appFirmTel | String | 应用厂商电话 |
| appFirmMail | String | 应用厂商邮箱 |
| appCmName | String | 应用联系人姓名 |
| appCmTel | String | 应用联系人电话 |
| appCmMail | String | 应用联系人邮箱 |
| appCmCode | String | 应用联系人编码 |
| appTypeDictValue | String | 应用类型字典值 |
| appLevel | String | 应用级别 |
| isSendMsg | Boolean | 是否发送消息标志 |
| isControlCorp | Boolean | 是否控制企业标志 |

### SimpleAppDecision - 应用决策实体类

**文件类型**：普通类

**文件路径**：src/main/java/com/simbest/boot/security/SimpleAppDecision.java

**字段说明**：

| 字段名 | 类型 | 说明 |
|--------|------|------|
| id | String | 决策ID |
| groupId | String | 组ID |
| appCode | String | 应用编码 |
| processDefId | String | 流程定义ID |
| processDefName | String | 流程定义名称 |
| activityDefId | String | 活动定义ID |
| activityDefName | String | 活动定义名称 |
| decisionId | String | 决策ID |
| decisionName | String | 决策名称 |
| opinion | String | 意见 |
| spare2 | String | 备用字段2 |
| decisionConfig | String | 决策配置 |
| decisionType | String | 决策类型 |

### SimpleBloc - 业务集团实体类

**文件类型**：普通类

**文件路径**：src/main/java/com/simbest/boot/security/SimpleBloc.java

**字段说明**：

| 字段名 | 类型 | 说明 |
|--------|------|------|
| id | String | 集团ID |
| blocCode | String | 集团编码 |
| blocName | String | 集团名称 |
| blocDesc | String | 集团简介 |

### SimpleConfig - 配置实体类

**文件类型**：普通类

**文件路径**：src/main/java/com/simbest/boot/security/SimpleConfig.java

**字段说明**：

| 字段名 | 类型 | 说明 |
|--------|------|------|
| id | String | 配置ID |
| address | String | 地址信息 |
| interfaceStyle | String | 接口风格 |
| isOpen | Boolean | 是否开放标志 |
| isInner | Boolean | 是否内部标志 |
| provider | String | 提供者信息 |

### SimpleCorp - 企业实体类

**文件类型**：普通类

**文件路径**：src/main/java/com/simbest/boot/security/SimpleCorp.java

**字段说明**：

| 字段名 | 类型 | 说明 |
|--------|------|------|
| id | String | 企业ID |
| blocId | String | 集团ID |
| corpCode | String | 企业编码 |
| corpName | String | 企业名称 |
| corpShortName | String | 企业简称 |
| corpDesc | String | 企业简介 |
| corpContent | String | 主体信息 |
| corpMail | String | 企业邮箱 |
| corpTel | String | 企业电话 |
| corpLegalPerson | String | 企业法人 |
| postcode | String | 邮政编码 |
| address | String | 企业地址 |
| industry | String | 服务行业 |
| registerLocation | String | 注册地址 |
| belongArea | String | 所在地区 |
| webUrl | String | 官方网站 |
| linkMan | String | 联系人 |
| linkManPhone | String | 联系电话 |
| linkManMail | String | 联系邮箱 |

### SimpleGroup - 用户组实体类

**文件类型**：普通类

**文件路径**：src/main/java/com/simbest/boot/security/SimpleGroup.java

**字段说明**：

| 字段名 | 类型 | 说明 |
|--------|------|------|
| id | String | 群组ID |
| name | String | 群组名称 |
| sid | String | 群组SID |
| fid | String | 群组FID |
| levels | String | 群组级别 |
| groupType | String | 群组类型 |
| description | String | 群组描述 |
| refSid | String | 引用SID |
| displayOrder | Integer | 显示顺序 |

### SimpleModel - 基础模型类

**文件类型**：普通类

**文件路径**：src/main/java/com/simbest/boot/security/SimpleModel.java

**类说明**：
SimpleModel是一个基础模型类，实现了Serializable和Comparable接口。它为其他模型类提供了基本的对象比较、哈希计算和字符串表示等功能。

**方法说明**：

| 方法名 | 返回类型 | 说明 |
|--------|----------|------|
| toString() | String | 返回对象的字符串表示形式，使用Apache Commons Lang的ToStringBuilder实现反射式toString |
| equals(Object o) | boolean | 比较此对象与指定对象是否相等，使用Apache Commons Lang的EqualsBuilder实现反射式equals |
| hashCode() | int | 返回该对象的哈希码值，使用Apache Commons Lang的HashCodeBuilder实现反射式hashCode |
| compareTo(Object obj) | int | 将此对象与指定的对象进行比较，使用Apache Commons Lang的CompareToBuilder实现反射式比较 |

### SimpleOrg - 组织机构实体类

**文件类型**：普通类

**文件路径**：src/main/java/com/simbest/boot/security/SimpleOrg.java

**字段说明**：

| 字段名 | 类型 | 说明 |
|--------|------|------|
| id | String | 组织ID |
| orgCode | String | 组织编码 |
| orgName | String | 组织名称 |
| parentOrgCode | String | 父级组织编码 |
| displayOrder | Integer | 显示顺序 |
| displayName | String | 显示名称 |
| belongCompanyCode | String | 所属公司编码 |
| belongDepartmentCode | String | 所属部门编码 |
| levelDictValue | String | 组织级别（级别字典值） |
| reserve1 | String | 预留字段1（预留扩展字段1） |
| reserve2 | String | 预留字段2（预留扩展字段2） |
| reserve3 | String | 预留字段3（预留扩展字段3） |
| reserve4 | String | 预留字段4（预留扩展字段4） |
| reserve5 | String | 预留字段5（预留扩展字段5） |
| styleDictValue | String | 样式字典值（即主数据规范style，对应sys_dict_value的value字段，自定义属性，定义组织形态） |
| companyTypeDictValue | String | 公司类型字典值（所属公司类型，省公司为01、地市分公司为02、县/市区分公司为03） |
| erpId | String | ERP ID（此部门在ERP-HR中对应的组织编码） |
| corpId | String | 企业ID |
| isCorpRoot | Boolean | 是否为企业根组织标志 |

### SimplePermission - 权限实体类

**文件类型**：普通类

**文件路径**：src/main/java/com/simbest/boot/security/SimplePermission.java

**字段说明**：

| 字段名 | 类型 | 说明 |
|--------|------|------|
| id | String | 权限ID |
| permissionCode | String | 权限编码 |
| description | String | 权限描述 |
| url | String | URL（权限对应的URL） |
| icon | String | 图标（权限对应的图标） |
| menuLevel | Integer | 菜单级别 |
| displayOrder | Integer | 显示顺序 |
| type | String | 权限类型 |
| parentId | String | 父级权限ID |
| remark | String | 备注（备注信息） |
| authority | String | 权限标识（在Spring Security中代表一个权限的字符串） |

### SimplePosition - 职位实体类

**文件类型**：普通类

**文件路径**：src/main/java/com/simbest/boot/security/SimplePosition.java

**字段说明**：

| 字段名 | 类型 | 说明 |
|--------|------|------|
| id | String | 职位ID |
| positionName | String | 职位名称 |
| positionCompany | String | 职位所属公司 |
| positionCode | Integer | 职位编码 |
| positionType | Integer | 职位类型 |

### SimpleUser - 用户实体类

**文件类型**：普通类

**文件路径**：src/main/java/com/simbest/boot/security/SimpleUser.java

**字段说明**：

| 字段名 | 类型 | 说明 |
|--------|------|------|
| id | String | 用户ID |
| username | String | 用户名（即主数据规范uid，引用自inetOrgPerson，定义用户登录ID） |
| password | String | 用户密码 |
| truename | String | 用户真实姓名（即主数据规范cn，定义用户中文姓名） |
| nickname | String | 用户昵称 |
| email | String | 用户邮箱 |
| preferredMobile | String | 首选移动电话（即主数据规范首选移动电话） |
| telephoneNumber | String | 办公电话（即主数据规范用户办公电话） |
| genderDictValue | Integer | 性别字典值（即主数据规范gender，1：男 2：女 3：未知） |
| isCmcc | Boolean | 是否是公司内部用户标志 |
| startTime | LocalDateTime | 用户开始生效时间 |
| endTime | LocalDateTime | 用户结束生效时间 |
| status | String | 用户状态（定义用户帐号的状态，例如："0"表示正常状态） |
| passwordModifiedDate | LocalDateTime | 密码修改时间 |
| displayOrder | Integer | 显示顺序 |
| employeeNumber | String | 员工编码（与人力资源系统中的员工号一致） |
| duty | String | 职务（采用"<组织编码>-<职务编码>"形式） |
| positionLevel | Integer | 职位等级（1-11级，详见代码注释） |
| employeeTypeDictValue | Integer | 员工类型字典值 |
| employeeTypeDictDesc | String | 员工类型字典描述 |
| photo | String | 用户照片（照片路径或数据） |
| userType | Integer | 用户类型 |
| openid | String | 微信OpenID |
| unionid | String | 微信UnionID |
| enabled | Boolean | 是否可用标志 |
| accountNonExpired | Boolean | 账户是否过期标志 |
| accountNonLocked | Boolean | 账户是否锁定标志 |
| credentialsNonExpired | Boolean | 密码是否过期标志 |
| currentBloc | String | 当前集团 |
| currentBlocCode | String | 当前集团编码 |
| authBlocs | Set<SimpleBloc> | 授权集团集合 |
| currentCorp | String | 当前企业 |
| currentCorpCode | String | 当前企业编码 |
| authCorps | Set<SimpleCorp> | 授权企业集合 |
| authOrgs | Set<SimpleOrg> | 授权组织集合 |
| authPositions | Set<SimplePosition> | 授权职位集合 |
| authRoles | Set<SimpleRole> | 授权角色集合 |
| authPermissions | Set<SimplePermission> | 授权权限集合 |
| authorities | Set<MySimpleGrantedAuthority> | 授权集合 |
| authUserOrgs | Set<SimpleUserOrg> | 授权用户组织集合 |
| belongCompanyCode | String | 所属公司编码 |
| belongCompanyName | String | 所属公司名称 |
| belongCompanyCodeParent | String | 所属上级公司编码 |
| belongCompanyNameParent | String | 所属上级公司名称 |
| belongCompanyTypeDictValue | String | 所属公司类型字典值 |
| belongCompanyTypeDictDesc | String | 所属公司类型字典描述 |
| belongDepartmentCode | String | 所属部门编码 |
| belongDepartmentName | String | 所属部门名称 |
| belongOrgCode | String | 所属组织编码 |
| belongOrgName | String | 所属组织名称 |
| reserve1 | String | 预留字段1 |
| reserve2 | String | 预留字段2 |
| reserve3 | String | 预留字段3 |
| reserve4 | String | 预留字段4 |
| reserve5 | String | 预留字段5 |

**主要方法说明**：

| 方法名 | 返回类型 | 说明 |
|--------|----------|------|
| isAccountNonExpired() | boolean | 判断账户是否未过期 |
| isAccountNonLocked() | boolean | 判断账户是否未锁定 |
| isCredentialsNonExpired() | boolean | 判断密码是否未过期 |
| isEnabled() | boolean | 判断账户是否可用 |
| addAppPositions() | void | 添加应用职位集合 |
| addAppRoles() | void | 添加应用角色集合 |
| addAppPermissions() | void | 添加应用权限集合 |
| addAppAuthorities() | void | 添加应用授权集合 |

### SimpleUserOrg - 用户组织关系实体类

**文件类型**：普通类

**文件路径**：src/main/java/com/simbest/boot/security/SimpleUserOrg.java

**字段说明**：

| 字段名 | 类型 | 说明 |
|--------|------|------|
| id | String | 用户组织关系ID |
| orgCode | String | 组织编码 |
| username | String | 用户名 |
| positionId | String | 职位ID |
| displayOrder | Integer | 显示顺序 |
| status | String | 状态（状态信息） |

### UserOrgTree - 用户组织树实体类

**文件类型**：普通类

**文件路径**：src/main/java/com/simbest/boot/security/UserOrgTree.java

**字段说明**：

| 字段名 | 类型 | 说明 |
|--------|------|------|
| id | String | 树节点ID |
| name | String | 树节点名称 |
| parentId | String | 父节点ID |
| displayOrder | Integer | 显示顺序 |
| treeType | String | 树类型 |
| treeLevel | Integer | 树层级 |
| defaultSelectUser | Boolean | 是否默认选中用户标志 |
| orgDisplayName | String | 组织显示名称 |
| cancelSelectUser | String | 是否可以取消选中标志 |

</rewritten_file> 