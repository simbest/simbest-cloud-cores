# 8-系统内置与UUMS主数据微服务交互API接口规范

## 一、文档用途说明
本文档用于说明SIMBEST团队主数据系统提供的后端直接调用API工具类，帮助开发人员快速了解和使用这些API进行系统集成开发。文档包含接口目录大纲和详细说明，每个接口都提供了完整的方法清单及其使用说明。

## 二、接口目录大纲

| 序号 | 接口名称 | 主要用途 |
|------|----------|----------|
| 1 | ApiRequestHandle | API请求处理工具类，提供通用的请求处理方法 |
| 2 | UumsSysAppApi | 系统应用管理API，提供应用相关的增删改查功能 |
| 3 | UumsSysAppConfigApi | 系统应用配置API，管理应用配置信息 |
| 4 | UumsSysAppDecisionApi | 系统应用决策API，处理应用决策相关功能 |
| 5 | UumsSysAppGroupApi | 系统应用分组API，管理应用分组信息 |
| 6 | UumsSysBlocApi | 系统集团API，管理集团相关信息 |
| 7 | UumsSysCorpApi | 系统公司API，管理公司相关信息 |
| 8 | UumsSysGroupApi | 系统组织API，管理组织相关信息 |
| 9 | UumsSysUserGroupApi | 系统用户组API，管理用户组相关信息 |
| 10 | UumsSysOrgApi | 系统组织API，管理组织相关信息 |
| 11 | UumsSysOrgPermissionApi | 系统机构权限API，管理机构权限信息 |
| 12 | UumsSysPermissionApi | 系统权限API，管理权限相关信息 |
| 13 | UumsSysPositionApi | 系统职位管理API，管理职位相关信息 |
| 14 | UumsSysRoleApi | 系统角色API，管理角色相关信息 |
| 15 | UumsSysRolePermissionApi | 系统角色权限API，管理角色权限信息 |
| 16 | UumsSysDictValueApi | 系统字典值API，管理字典值相关信息 |
| 17 | UumsSysUserinfoApi | 系统用户信息API，管理用户基本信息 |
| 18 | UumsSysUserOrginfoApi | 系统用户组织信息API，管理用户组织关系 |
| 19 | UumsSysUserPermissionApi | 系统用户权限API，管理用户权限信息 |
| 20 | UumsSysUserRoleApi | 系统用户角色API，管理用户角色关系 |

## 三、接口详细说明

### 1. ApiRequestHandle
**接口名称**：ApiRequestHandle
**主要用途**：提供API请求处理的通用工具类，用于处理HTTP请求、响应等基础功能。

| 方法名称 | 方法说明 | 输入参数 | 输出结果 |
|----------|----------|----------|----------|
| handRemoteResponse | 处理API请求的响应结果，返回为对应的实体类 | JsonResponse response, Class<T> clazz | T |
| handRemoteTypeReferenceResponse | 处理API请求的响应结果，返回为对象实体列表 | JsonResponse response, TypeReference<T> listType | T |
| handRemoteNormalResponse | 内部方法，统一处理API响应结果 | JsonResponse response, Class<T> clazz, TypeReference<T> listType | T |

### 2. UumsSysAppApi
**接口名称**：UumsSysAppApi
**主要用途**：提供系统应用管理相关的API接口，包括应用的增删改查等操作。

| 方法名称 | 方法说明 | 输入参数 | 输出结果 |
|----------|----------|----------|----------|
| findById | 根据ID查找应用信息 | String id, String appcode | SimpleApp |
| findAll | 单表条件查询（分页） | int page, int size, String direction, String properties, String appcode, Map sysAppMap | JsonResponse |
| findAllNoPage | 获取全部应用列表（无分页） | String appcode, Map sysAppMap | JsonResponse |
| isHaveCode | 校验当前应用的appCode是否存在 | String appcode | Boolean |
| findAppByGroup | 查看多个或一个群组拥有哪些应用并分页 | String appcode, int page, int size, String direction, String properties, String ids | JsonResponse |
| findAppByGroupNoPage | 查看多个或一个群组拥有哪些应用不分页 | String appcode, String ids | Map<String,Set<SimpleApp>> |
| findAppByGroupPermission | 查询多个或一个群组拥有哪些应用的权限并分页 | String appcode, int page, int size, String direction, String properties, String ids | JsonResponse |
| findAppByGroupPermissionNoPage | 查询多个或一个群组拥有哪些应用的权限不分页 | String appcode, String ids | Map<String,Set<SimpleApp>> |
| findAppByUser | 查看当前用户所在的应用并分页 | String appcode, int page, int size, String direction, String properties | JsonResponse |
| findAppByUserNoPage | 查看当前用户所在的应用不分页 | String appcode | Map<String,Set<SimpleApp>> |
| findAppByUserPermission | 查看当前用户拥有哪些应用的权限并分页 | String appcode, int page, int size, String direction, String properties | JsonResponse |
| findAppByUserPermissionNoPage | 查看当前用户拥有哪些应用的权限不分页 | String appcode | Map<String,Set<SimpleApp>> |
| findAppByAppCode | 根据应用代码查找应用信息 | String appcode, String username | SimpleApp |

### 3. UumsSysAppConfigApi
**接口名称**：UumsSysAppConfigApi
**主要用途**：提供系统应用配置管理相关的API接口，包括应用配置信息的查询等操作。

| 方法名称 | 方法说明 | 输入参数 | 输出结果 |
|----------|----------|----------|----------|
| findAppConfigByStyle | 根据接口类型获取app配置信息 | String interfaceStyle, String username, String appcode | SimpleConfig |

### 4. UumsSysAppDecisionApi
**接口名称**：UumsSysAppDecisionApi
**主要用途**：提供系统应用决策管理相关的API接口，包括应用决策信息的查询等操作。

| 方法名称 | 方法说明 | 输入参数 | 输出结果 |
|----------|----------|----------|----------|
| findById | 根据ID查找应用决策信息 | String id, String appcode | SimpleAppDecision |
| findAll | 单表条件查询（分页） | int page, int size, String direction, String properties, String appcode, Map sysAppDecisionMap | JsonResponse |
| findDecisions | 根据appcode以及其下的流程id及活动id获取其下全部决策信息 | String appcode, Map sysAppDecisionMap | List<SimpleAppDecision> |
| findDecisionsNoSession | 根据appcode以及其下的流程id及活动id获取其下全部决策信息（不需要session） | String appcode, Map sysAppDecisionMap | List<SimpleAppDecision> |
| findDecisionsByApp | 根据appCode查询某应用下流程的信息（不使用规则） | String appcode | List<SimpleAppDecision> |
| findDecisionsByAppWfmg | 根据appCode查询某应用下流程的信息（工作流管理，不使用规则） | String appcode | Set<Map<String,Object>> |

### 5. UumsSysAppGroupApi
**接口名称**：UumsSysAppGroupApi
**主要用途**：提供系统应用分组管理相关的API接口，包括应用分组信息的查询等操作。

| 方法名称 | 方法说明 | 输入参数 | 输出结果 |
|----------|----------|----------|----------|
| findById | 根据ID查找应用分组信息 | String id, String appcode | JsonResponse |
| findAll | 单表条件查询（分页） | int page, int size, String direction, String properties, String appcode, Map sysAppGroupMap | JsonResponse |
| ifHasPermission | 根据用户名以及应用code来查看此用户是否拥有使用应用的权限 | String username, String appcode | Boolean |

### 6. UumsSysBlocApi
**接口名称**：UumsSysBlocApi
**主要用途**：提供系统集团管理相关的API接口，包括集团信息的查询等操作。

| 方法名称 | 方法说明 | 输入参数 | 输出结果 |
|----------|----------|----------|----------|
| findById | 根据集团id查找集团信息 | String blocid, String appcode | IBloc |

### 7. UumsSysCorpApi
**接口名称**：UumsSysCorpApi
**主要用途**：提供系统企业信息管理相关的API接口，包括企业信息的查询等操作。

| 方法名称 | 方法说明 | 输入参数 | 输出结果 |
|----------|----------|----------|----------|
| findById | 根据企业id查找企业信息 | String corpid, String appcode | ICorp |

### 8. UumsSysGroupApi
**接口名称**：UumsSysGroupApi
**主要用途**：提供系统群组管理相关的API接口，包括群组信息的增删改查等操作。

| 方法名称 | 方法说明 | 输入参数 | 输出结果 |
|----------|----------|----------|----------|
| findById | 根据ID查找群组信息 | String id, String appcode | SimpleGroup |
| findAll | 单表条件查询（分页） | int page, int size, String direction, String properties, String appcode, Map sysGroupMap | JsonResponse |
| getAll | 获取所有群组信息（分页） | int page, int size, Map sysGroupMap | JsonResponse |
| isHaveCode | 校验群组的sid是否存在 | String sid, String appcode | Boolean |
| findParentGroup | 根据sid查看父群组信息 | String appcode, String sid | SimpleGroup |
| findAllSonGroup | 查看一个群组下的全部子群组不分页 | String appcode, String sid | List<SimpleGroup> |
| findSonGroups | 查看群组的下一级子群组不分页 | String appcode, String sid | List<SimpleGroup> |
| findGroupByAppNoPage | 根据应用编码、流程定义ID、环节定义ID或者决策定义ID查询群组信息不分页 | String appcode, int page, int size, String direction, String properties, Map sysAppDecisionMap | List<SimpleGroup> |
| findGroupByUsername | 根据用户名查询群组信息（分页） | String appcode, int page, int size, String direction, String properties | JsonResponse |
| findGroupByUsernameNoPage | 根据用户名查询群组信息（不分页） | String appcode | List<SimpleGroup> |
| findGroupByPermission | 根据权限查询群组信息（分页） | String appcode, int page, int size, String direction, String properties | JsonResponse |
| findGroupByPermissionNoPage | 根据权限查询群组信息（不分页） | String appcode | List<SimpleGroup> |
| findDataPermission | 查询数据权限 | String appcode | List<SimpleGroup> |
| addGroup | 添加群组 | String appcode, SimpleGroup simpleGroup | SimpleGroup |
| delGroup | 删除群组 | String appcode, SimpleGroup simpleGroup | boolean |
| updateGroup | 更新群组 | String appcode, SimpleGroup simpleGroup | SimpleGroup |
| findGroupsType | 查询群组类型 | 无 | JsonResponse |
| deleteById | 根据ID删除群组 | String id, String sid | JsonResponse |

### 9. UumsSysUserGroupApi
**接口名称**：UumsSysUserGroupApi
**主要用途**：提供系统用户群组管理相关的API接口，包括用户与群组关联关系的管理等操作。

| 方法名称 | 方法说明 | 输入参数 | 输出结果 |
|----------|----------|----------|----------|
| create | 新增人员群组信息 | String username, String groupId, String appcode | JsonResponse |
| findByGroupId | 根据群组ID查找群组信息 | String sid | JsonResponse |
| delete | 根据群组人员关联表中的ID删除关联信息 | Map sysUserGroup | JsonResponse |
| createGroupUsers | 批量创建群组用户 | String groupSid, String orgCodes, String groupSids, String usernames, String userBussinessConfig | JsonResponse |
| deleteById | 根据ID删除群组用户关联信息 | String id | JsonResponse |

### 10. UumsSysOrgApi
**接口名称**：UumsSysOrgApi
**主要用途**：提供系统组织管理相关的API接口，包括组织信息的基础CRUD操作、组织树结构查询和管理、父子组织关系维护等功能。

| 方法名称 | 方法说明 | 输入参数 | 输出结果 |
|----------|----------|----------|----------|
| create | 新增组织信息 | Map<String, Object> simpleOrg | JsonResponse |
| findById | 根据ID查找组织信息 | String id, String appcode | SimpleOrg |
| findAll | 单表条件查询（分页） | int page, int size, String direction, String properties, String appcode, Map sysOrgMap | JsonResponse |
| findAllNoPage | 单表条件查询（不分页） | String appcode, Map sysOrgMap | JsonResponse |
| findListByOrgCode | 根据组织code查询组织信息 | String appcode, String orgCode | SimpleOrg |
| findSonByParentOrgId | 查看某个父组织的子组织 | String appcode, String orgCode | List<SimpleOrg> |
| findSonByParentOrgIdTree | 查看某个父组织的子组织（返回树形结构） | String appcode, Map<String, Object> mapOrg | List<UserOrgTree> |
| findAllOrgByParentoCode | 查看某个父组织的全部子组织 | String appcode, String orgCode | List<SimpleOrg> |
| findParentBySon | 根据子组织查父组织 | String appcode, String orgCode | SimpleOrg |
| findRoot | 查找根组织 | String appcode | SimpleOrg |
| findRootAndNext | 查找根组织和下一级组织 | String appcode | List<SimpleOrg> |
| findOrgByAppCode | 根据应用编码查找组织 | String appcode | SimpleOrg |
| findPOrgAndCityOrg | 查找省级组织和市级组织 | String appcode | List<SimpleOrg> |
| findPOrgAnd18CityOrg | 查找省级组织和18个市级组织 | String appcode | List<SimpleOrg> |
| findCityDeapartmentAndCountyCompany | 查找市级部门和县级公司 | String appcode | List<SimpleOrg> |
| findOrgByUserMap | 根据用户信息查找组织 | String appcode, Map userMap | List<SimpleOrg> |
| findRootByCorpId | 根据企业ID查找根组织 | String appcode, String corpId | SimpleOrg |
| findUserTreeFromCorp | 根据企业信息查找用户树 | String appcode, Map<String, Object> corpMap | List<UserOrgTree> |
| findRuleOrgTree | 查找规则组织树 | String appcode, Map<String, Object> orgMap | List<UserOrgTree> |
| findOrgCodeOrgNameDim | 组织编码和名称模糊查询 | int page, int size, String direction, String properties, String parentOrgCode, String orgCode, String orgName, String companyTypeDictValue | JsonResponse |
| updateOrgCustom | 更新组织信息 | Map<String, Object> simpleOrg | JsonResponse |
| deleteOrgCustom | 删除组织信息 | String id | JsonResponse |
| queryOrgByPermissionId | 根据权限ID查询组织 | String permissionId | JsonResponse |

### 11. UumsSysOrgPermissionApi
**接口名称**：UumsSysOrgPermissionApi
**主要用途**：提供系统组织权限管理相关的API接口，包括组织权限的批量更新操作、基于权限ID的组织权限管理、组织代码与权限的关联管理等功能。

| 方法名称 | 方法说明 | 输入参数 | 输出结果 |
|----------|----------|----------|----------|
| updateListByPermissionId | 修改组织权限信息（先删除角色所有权限，再重新添加） | String permissionId, List<String> orgCodes | JsonResponse |

### 12. UumsSysPermissionApi
**接口名称**：UumsSysPermissionApi
**主要用途**：提供系统权限管理相关的API接口，包括权限信息的基础CRUD操作、按角色和职位查询权限、权限状态管理、应用级别的权限管理等功能。

| 方法名称 | 方法说明 | 输入参数 | 输出结果 |
|----------|----------|----------|----------|
| findById | 根据ID查找权限信息 | String id, String appcode | SimplePermission |
| findAll | 单表条件查询（分页） | int page, int size, String direction, String properties, String appcode, Map sysPermissionMap | JsonResponse |
| findAllNoPage | 单表条件查询（不分页） | String appcode, Map simplePermissionMap | List<SimplePermission> |
| findRoleAppPermission | 根据角色和应用查询其下的权限 | String roleName, String appcode | List<SimplePermission> |
| findPositionAppPermission | 根据职位和应用查询其下的权限 | String positionName, String appcode | List<SimplePermission> |
| findPerCustom | 自定义权限查询 | Map sysPermissionMap | JsonResponse |
| findAllPermissions | 根据指定应用查询该应用下的所有菜单 | 无 | JsonResponse |
| createCustom | 添加指定应用下的菜单 | SimplePermission simplePermission | JsonResponse |
| update | 更新菜单信息 | SimplePermission simplePermission | JsonResponse |
| updateSysPermissionStatus | 更新权限状态 | String id, String status | JsonResponse |
| deleteById | 根据ID删除权限 | String id | JsonResponse |

### 13. UumsSysPositionApi
**接口名称**：UumsSysPositionApi
**主要用途**：提供系统职位管理相关的API接口，包括职位信息的基础CRUD操作、按用户名和组织代码查询职位、职位树形结构查询、职位与用户关联管理等功能。

| 方法名称 | 方法说明 | 输入参数 | 输出结果 |
|----------|----------|----------|----------|
| findById | 根据ID查询职位信息 | String id, String appcode | SimplePosition |
| findAll | 获取职位信息列表并分页 | int page, int size, String direction, String properties, String appcode, Map sysPositionMap | JsonResponse |
| findAllNoPage | 获取所有职位信息（不分页） | String appcode, Map sysPositionMap | List<SimplePosition> |
| findPositionByUsername | 根据用户名查询其职务信息 | String username, String appcode | List<SimplePosition> |
| findPositionOrgcodeAndUsername | 查询指定组织下所有用户的职务信息 | String orgCode, String appcode | Map<String,List<SimplePosition>> |
| findAllToTree | 查询所有职务并以树形结构返回 | 无 | JsonResponse |
| create | 创建新的职务信息 | Map map | JsonResponse |
| update | 更新职务信息 | Map map | JsonResponse |
| deleteByIdCustom | 根据ID删除职务信息 | String id | JsonResponse |
| findPositionAndUser | 查询职位和用户关联信息 | int page, int size, String positionId, String truename | JsonResponse |
| findPositionCustom | 自定义职位查询 | String orgCode, String companyTypeDictValue, Map<String, Object> map | JsonResponse |

### 14. UumsSysRoleApi
**接口名称**：UumsSysRoleApi
**主要用途**：提供系统角色管理相关的API接口，包括角色信息的基础CRUD操作、按用户名和职位名查询角色、角色代码校验、角色与权限的关联管理等功能。

| 方法名称 | 方法说明 | 输入参数 | 输出结果 |
|----------|----------|----------|----------|
| findById | 根据ID查询角色信息 | String id, String appcode | SimpleRole |
| findAll | 获取角色信息列表并分页 | int page, int size, String direction, String properties, String appcode, Map sysRoleMap | JsonResponse |
| findRoleNameIsARoleDim | 根据角色名和业务角色标识进行模糊查询 | int page, int size, String direction, String properties, Map map | JsonResponse |
| isHaveCode | 校验角色代码是否已存在 | String appcode, String roleCode | Boolean |
| findRoleAppointByUsername | 查询指定用户的所有角色 | Map<String, Object> mapParam, String appcode | List<SimpleRole> |
| findRoleByUsername | 查询当前用户的所有角色 | String appcode | List<SimpleRole> |
| findRoleByPositionName | 查询指定职位的所有角色 | String appcode, String positionName | List<SimpleRole> |
| findRoleByRoleCode | 根据角色代码查询角色信息 | String appcode, String roleCode | SimpleRole |
| addRole | 创建新的角色 | String appcode, SimpleRole simpleRole | SimpleRole |
| delRole | 删除角色 | String appcode, SimpleRole simpleRole | boolean |
| updateRole | 更新角色信息 | String appcode, SimpleRole simpleRole | SimpleRole |
| queryRoleByPermissionId | 根据权限ID查询角色 | String permissionId | JsonResponse |
| updateSysRole | 更新角色状态 | String id, String status | JsonResponse |

### 15. UumsSysRolePermissionApi
**接口名称**：UumsSysRolePermissionApi
**主要用途**：提供系统角色权限管理相关的API接口，用于管理角色与权限之间的关联关系。

| 方法名称 | 方法说明 | 输入参数 | 输出结果 |
|----------|----------|----------|----------|
| updateListByPermissionId | 修改角色权限信息（先删除角色所有权限，再重新添加） | String permissionId, List<String> roleCodes | JsonResponse |

### 17. UumsSysUserinfoApi
**接口名称**：UumsSysUserinfoApi
**主要用途**：提供系统用户信息管理相关的API接口，包括用户信息的基础CRUD操作、用户认证、密码管理、用户组织关系管理等功能。

| 方法名称 | 方法说明 | 输入参数 | 输出结果 |
|----------|----------|----------|----------|
| insertReserve4 | 插入reserve4字段 | String username, String password | JsonResponse |
| create | 新增用户信息 | String keyword, IAuthService.KeyType keytype, String appcode, SimpleUser simpleUser | SimpleUser |
| createNew | 新增用户信息（新版本） | String keyword, IAuthService.KeyType keytype, String appcode, SimpleUser simpleUser | SimpleUser |
| update | 更新用户信息 | String keyword, IAuthService.KeyType keytype, String appcode, SimpleUser simpleUser | SimpleUser |
| updateNew | 更新用户信息（新版本） | String keyword, IAuthService.KeyType keytype, String appcode, SimpleUser simpleUser | SimpleUser |
| findById | 根据ID查找用户信息 | String id, String appcode | SimpleUser |
| findAll | 单表条件查询（分页） | int page, int size, String direction, String properties, String appcode, Map sysUserinfoMap | JsonResponse |
| findAllNoPage | 单表条件查询（不分页） | String appcode, Map simpleUserMap | List<SimpleUser> |
| findByUsernameFromCurrent | 根据用户名查找当前用户信息 | String username, String appcode | SimpleUser |
| findByUsername | 根据用户名查找用户信息 | String username, String appcode | SimpleUser |
| findUsernameByOrgAndPosition | 根据组织和职位查找用户名 | String loginUser, String orgCode, String positionIds, String appcode | String |
| findUserByGroup | 根据群组查找用户 | int page, int size, String direction, String properties, String appcode, String groupSid, Map<String,Object> params | JsonResponse |
| findUserByOrg | 根据组织查找用户 | int page, int size, String direction, String properties, String appcode, String orgCode | JsonResponse |
| findUserByPosition | 根据职位查找用户 | int page, int size, String direction, String properties, String appcode, String positionName | JsonResponse |
| findUserByPositionNoPage | 根据职位查找用户（不分页） | String appcode, String positionId | List<SimpleUser> |
| findUserByRoleNoPage | 根据角色查找用户（不分页） | String roleId, String appcode | List<SimpleUser> |
| findUserByRole | 根据角色查找用户 | int page, int size, String direction, String properties, String appcode, String roleId | JsonResponse |
| findUserByDecisionNoPage | 根据决策查找用户（不分页） | String appcode, Map sysAppDecisionmap | JsonResponse |
| findUserByDecisionNoPageNoSession | 根据决策查找用户（不分页，无会话） | String appcode, Map sysAppDecisionmap | JsonResponse |
| findUserByDecisionNoPageGrouping | 根据决策查找用户（分组） | String appcode, Map sysAppDecisionmap | JsonResponse |
| findUserByDecisionNoPageNoSessionGrouping | 根据决策查找用户（分组，无会话） | String appcode, Map sysAppDecisionmap | JsonResponse |
| findUserByUsernameNoPage | 根据用户名查找用户（不分页） | String appcode, String username | JsonResponse |
| findUserByUsernameNoPageNoSession | 根据用户名查找用户（不分页，无会话） | String appcode, String loginUser, String username | JsonResponse |
| findOneStep | 查找一步用户树 | String appcode, String orgCode, Map<String,Object> extraValueMap, String currentUserCode | List<UserOrgTree> |
| findRoleNameIsARoleDim | 根据角色名和用户信息模糊查询 | int page, int size, String direction, String properties, String appcode, String truename, String username, String preferredMobile, String employeeNumber, String email, String orgCode | JsonResponse |
| checkUserAccessApp | 检查用户是否有权限访问应用 | String username, String appcode | Boolean |
| checkUserAccessAppNoSession | 检查用户是否有权限访问应用（无会话） | String username, String appcode | Boolean |
| findPermissionByAppUser | 查找用户的应用权限 | String username, String appcode | Set<SimplePermission> |
| findPermissionByAppUserNoSession | 查找用户的应用权限（无会话） | String username, String appcode | Set<SimplePermission> |
| findByKey | 根据关键字查找用户 | String keyword, IAuthService.KeyType keytype, String appcode | SimpleUser |
| findUserInfoByGroupSidNoPage | 根据群组ID查找用户信息（不分页） | String appcode, String groupSid | Set<Map<String,Object>> |
| changeUserPassword | 修改用户密码 | String username, String rsaPassword, String appcode | JsonResponse |
| changeMyPassword | 修改当前用户密码 | String oldRsaPassword, String newRsaPassword, String appcode | JsonResponse |
| findUserByGroupSort | 根据群组查找用户（排序） | String groupId, String appcode | JsonResponse |
| findAllInfosUnderOrg | 查找组织下的所有用户信息 | String orgCode, String appcode | JsonResponse |
| findAllOrgUserByOrgs | 根据组织查找所有用户 | String appcode, Map sysAppDecisionmap | JsonResponse |
| findAllUserByOrgCode | 根据组织代码查找所有用户 | String orgCode, String appcode | Set<Map<String,Object>> |
| findAllInfosUnderOrgTogether | 查找组织下的所有用户信息（包含子组织） | String orgCode, String appcode | JsonResponse |
| findUserOrgDim | 用户组织维度查询 | int page, int size, String direction, String properties, String appcode, String orgCode, String searchFields | JsonResponse |
| findUserTreeBz | 查找用户树 | Map<String,Object> mapParam, String appcode | List<UserOrgTree> |
| findUserIncludeExtensionByOrgCode | 根据组织代码查找用户（包含扩展信息） | int page, int size, String direction, String properties, Map<String,Object> mapParam, String appcode | Map<String,Object> |
| findLeaderShip | 查找领导信息 | String appcode | JsonResponse |
| findUserFromCorpType | 根据公司类型查找用户 | Map<String,Object> mapParam, String appcode | Set<SimpleUser> |
| findUserFromPerId | 根据权限ID查找用户 | int page, int size, String id, String appcode | JsonResponse |
| findThLeUser | 查找特殊用户 | String appcode | Set<String> |
| findDimUserTree | 查找维度用户树 | Map<String,Object> mapParam, String appcode | Set<UserOrgTree> |
| insertUserInOrgNormal | 在组织中插入用户 | Map<String,Object> mapParam, String appcode | JsonResponse |
| updateUserInOrgNormal | 更新组织中的用户 | Map<String,Object> mapParam, String appcode | JsonResponse |
| deleteUserInOrgNormal | 删除组织中的用户 | Map<String,Object> mapParam, String appcode | JsonResponse |
| findAllUsersNoPage | 查找所有用户（不分页） | String appcode | JsonResponse |
| CleanUserCacheByFindTimUserInfos | 清理用户缓存 | String startTime, String endTime | Map<String,Object> |
| queryUserByPermissionId | 根据权限ID查询用户 | String permissionId | JsonResponse |

### 18. UumsSysUserOrginfoApi
**接口名称**：UumsSysUserOrginfoApi
**主要用途**：提供系统用户组织信息管理相关的API接口，包括用户与组织关联关系的基础CRUD操作、用户组织信息的查询等功能。

| 方法名称 | 方法说明 | 输入参数 | 输出结果 |
|----------|----------|----------|----------|
| findAllNoPage | 单表条件查询（不分页） | String appcode, Map simpleUserOrgMap | List<SimpleUserOrg> |
| findAllForAdmin | 查找所有用户信息（管理员） | int page, int size, String direction, String properties, SimpleUserOrg userOrg | JsonResponse |
| getOrgInfoByUsername | 根据用户名查询用户组织信息 | String username | JsonResponse |
| create | 创建用户组织关联信息 | SimpleUserOrg sysUserOrg | JsonResponse |
| findById | 根据ID查询用户组织信息 | String id | JsonResponse |
| update | 更新用户组织关联信息 | SimpleUserOrg sysUserOrg | JsonResponse |
| deleteById | 根据ID删除用户组织关联信息 | String id | JsonResponse |

### 19. UumsSysUserPermissionApi
**接口名称**：UumsSysUserPermissionApi
**主要用途**：提供系统用户权限管理相关的API接口，用于管理用户与权限之间的关联关系。

| 方法名称 | 方法说明 | 输入参数 | 输出结果 |
|----------|----------|----------|----------|
| updateListByPermissionId | 修改用户权限信息（先删除用户所有权限，再重新添加） | String permissionId, List<String> userCodes | JsonResponse |

### 20. UumsSysUserRoleApi
**接口名称**：UumsSysUserRoleApi
**主要用途**：提供系统用户角色管理相关的API接口，用于管理用户与角色之间的关联关系。

| 方法名称 | 方法说明 | 输入参数 | 输出结果 |
|----------|----------|----------|----------|
| updateRoleUser | 更新应用下的角色里的人 | Map<String,Object> mapParam, String appcode | Integer |
| deleteByIdCustom | 删除用户角色信息 | String id | JsonResponse |
| createRoleUsers | 指定角色新增人员 | String roleId, String usernames | JsonResponse |

## 四、总结
本文档详细说明了SIMBEST团队主数据系统提供的20个后端直接调用API工具类，包括：
1. 基础请求处理工具类（ApiRequestHandle）
2. 应用管理相关API（UumsSysAppApi等）
3. 组织管理相关API（UumsSysOrgApi等）
4. 用户管理相关API（UumsSysUserinfoApi等）
5. 权限管理相关API（UumsSysPermissionApi等）
6. 角色管理相关API（UumsSysRoleApi等）

每个API接口都提供了详细的方法说明，包括方法名称、功能描述、输入参数和输出结果。这些API接口可以帮助开发人员快速实现与主数据系统的集成，提高开发效率。