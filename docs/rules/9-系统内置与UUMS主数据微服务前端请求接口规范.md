# 9-系统内置与UUMS主数据微服务前端请求接口规范

## 一、文档用途说明
本文档旨在说明主数据Web工具类的使用方法，为后端开发人员提供直接调用主数据接口的指导。文档包含了所有可用接口的详细说明、调用方法及参数说明，便于开发人员快速实现系统集成。

## 二、接口目录大纲

| 序号 | 接口名称 | 主要用途 |
|------|----------|----------|
| 1 | UumsSysAppConfigController | 应用配置相关接口 |
| 2 | UumsSysAppController | 应用决策群组相关接口 |
| 3 | UumsSysAppDecisionController | 应用决策项相关接口 |
| 4 | UumsSysAppGroupController | 应用群组关系相关接口 |
| 5 | UumsSysAppToMinController | 移动端应用相关接口 |
| 6 | UumsSysBlocController | 集团信息相关接口 |
| 7 | UumsSysCorpController | 企业信息相关接口 |
| 8 | UumsSysGroupController | 群组管理相关接口 |
| 9 | UumsSysUserGroupController | 用户群组关系管理相关接口 |
| 10 | UumsSysOrgController | 系统组织管理相关接口 |
| 11 | UumsSysOrgPermissionController | 系统组织权限管理相关接口 |
| 12 | UumsSysPermissionController | 系统权限管理相关接口 |
| 13 | UumsSysPositionController | 系统职位管理相关接口 |
| 14 | UumsSysRoleController | 系统角色管理，提供角色的基础CRUD操作、角色权限管理、角色状态管理等功能 |
| 15 | UumsSysRolePermissionController | 系统角色权限管理，提供角色与权限关联关系的管理功能 |
| 16 | UumsSysUserRoleController | 系统用户角色管理，提供用户与角色关联关系的管理功能 |
| 17 | UumsSysUserOrgController | 系统用户组织关联管理，提供用户与组织关联关系的管理功能 |
| 18 | UumsSysUserInfoController | 系统用户信息管理，提供用户基础信息的管理功能 |
| 19 | UumsSysUserPermissionController | 系统用户权限管理，提供用户与权限关联关系的管理功能 |

## 三、接口详细说明

### 1. UumsSysAppConfigController

**接口名称**：应用配置控制器  
**主要用途**：提供应用配置相关的操作接口

#### 方法清单

| 方法名称 | 方法说明 | 输入参数 | 输出结果 |
|----------|----------|-----------|-----------|
| findAppConfigByStyle | 根据接口类型获取app配置信息 | interfaceStyle: String (应用类型)<br>username: String (用户名)<br>appcode: String (应用code) | JsonResponse |

### 2. UumsSysAppController

**接口名称**：应用控制器  
**主要用途**：提供应用相关的基础操作接口

#### 方法清单

| 方法名称 | 方法说明 | 输入参数 | 输出结果 |
|----------|----------|-----------|-----------|
| findById | 根据appCode查询应用的消息 | appcode: String (应用code)<br>username: String (用户名) | JsonResponse |
| findAll | 单表条件查询（分页） | page: int (当前页码，必填，默认1)<br>size: int (每页数量，必填，默认10)<br>direction: String (排序规则asc/desc，必填)<br>properties: String (排序属性名称，必填)<br>appcode: String (应用编码)<br>sysAppMap: Map (查询条件) | JsonResponse |
| findAllNoPage | 获取全部应用职位列表无分页 | appcode: String (应用编码，必填)<br>sysAppMap: Map (查询条件，可选) | JsonResponse |

### 3. UumsSysAppDecisionController

**接口名称**：应用决策项控制器  
**主要用途**：提供应用决策项相关的操作接口

#### 方法清单

| 方法名称 | 方法说明 | 输入参数 | 输出结果 |
|----------|----------|-----------|-----------|
| findById | 根据id查看应用决策项信息 | id: String (权限ID)<br>appcode: String (应用code) | JsonResponse |
| findAll | 获取决策项列表（分页） | page: int (当前页码，必填，默认1)<br>size: int (每页数量，必填，默认10)<br>direction: String (排序规则asc/desc，必填)<br>properties: String (排序属性名称，必填)<br>appcode: String (应用编码)<br>map: Map (查询条件) | JsonResponse |
| findDecisions | 根据appcode以及其下的流程id及活动id获取其下全部决策信息 | appcode: String (当前应用appcode)<br>sysAppDecisionmap: Map (查询条件) | JsonResponse |
| findDecisionsByApp | 根据appCode查询某应用下流程的信息，不使用规则 | appcode: String (当前应用appcode) | JsonResponse |

### 4. UumsSysAppGroupController

**接口名称**：应用群组关系控制器  
**主要用途**：提供应用群组关系相关的操作接口

#### 方法清单

| 方法名称 | 方法说明 | 输入参数 | 输出结果 |
|----------|----------|-----------|-----------|
| findById | 根据id查看应用决策群组信息 | id: String (权限ID)<br>appcode: String (应用code) | JsonResponse |
| findAll | 获取权限列表（分页） | page: int (当前页码，必填，默认1)<br>size: int (每页数量，必填，默认10)<br>direction: String (排序规则asc/desc，必填)<br>properties: String (排序属性名称，必填)<br>appcode: String (应用编码)<br>map: Map (查询条件) | JsonResponse |
| ifHasPermission | 根据用户名以及应用code来查看此用户是否拥有使用应用的权限 | username: String (用户名)<br>appcode: String (应用编码) | JsonResponse |

### 5. UumsSysAppToMinController

**接口名称**：移动端应用控制器  
**主要用途**：提供移动端应用相关的API接口

#### 方法清单

| 方法名称 | 方法说明 | 输入参数 | 输出结果 |
|----------|----------|-----------|-----------|
| findById | 根据appCode查询应用信息 | appcode: String (应用编码，可选)<br>username: String (用户名，可选) | JsonResponse |

### 6. UumsSysBlocController

**接口名称**：集团信息控制器  
**主要用途**：提供集团基础信息的管理功能

#### 方法清单

| 方法名称 | 方法说明 | 输入参数 | 输出结果 |
|----------|----------|-----------|-----------|
| findById | 根据ID查询集团信息 | id: String (集团ID)<br>appcode: String (应用编码) | JsonResponse |

### 7. UumsSysCorpController

**接口名称**：企业信息控制器  
**主要用途**：提供企业基础信息的管理功能

#### 方法清单

| 方法名称 | 方法说明 | 输入参数 | 输出结果 |
|----------|----------|-----------|-----------|
| findById | 根据ID查询企业信息 | id: String (企业ID)<br>appcode: String (应用编码) | JsonResponse |

### 8. UumsSysGroupController

**接口名称**：群组管理控制器  
**主要用途**：提供群组相关的管理功能

#### 方法清单

| 方法名称 | 方法说明 | 输入参数 | 输出结果 |
|----------|----------|-----------|-----------|
| findAll | 分页查询群组信息 | page: int (当前页码，默认1)<br>size: int (每页大小，默认10)<br>direction: String (排序方向，可选)<br>properties: String (排序字段，可选)<br>appcode: String (应用编码，可选)<br>sysGroupMap: Map (查询条件) | JsonResponse |
| getAll | 简单分页查询群组信息 | page: int (当前页码，默认1)<br>size: int (每页大小，默认10)<br>sysGroupMap: Map (查询条件) | JsonResponse |
| findDataPermission | 查询用户的数据权限 | appcode: String (应用编码，可选) | JsonResponse |
| addGroup | 创建新群组 | appcode: String (应用编码)<br>simpleGroup: SimpleGroup (群组信息对象) | JsonResponse |
| delGroup | 删除群组 | appcode: String (应用编码)<br>simpleGroup: SimpleGroup (群组信息对象) | JsonResponse |
| updateGroup | 更新群组信息 | appcode: String (应用编码)<br>simpleGroup: SimpleGroup (群组信息对象) | JsonResponse |
| findGroupsType | 获取所有群组类型 | - | JsonResponse |
| deleteById | 根据ID删除群组 | id: String (群组ID)<br>sid: String (会话ID) | JsonResponse |

### 9. UumsSysUserGroupController

**接口名称**：用户群组关系管理控制器  
**主要用途**：提供用户与群组关系的管理功能

#### 方法清单

| 方法名称 | 方法说明 | 输入参数 | 输出结果 |
|----------|----------|-----------|-----------|
| create | 将用户添加到指定群组 | username: String (用户名)<br>groupId: String (群组ID)<br>appcode: String (应用编码) | JsonResponse |
| delete | 删除用户群组关系 | sysUserGroup: Map (删除条件) | JsonResponse |
| deleteById | 根据ID删除用户群组关系 | id: String (用户群组关系ID) | JsonResponse |
| createGroupUsers | 批量添加用户到指定群组 | groupSid: String (目标群组SID)<br>orgCodes: String (组织机构代码列表，逗号分隔)<br>groupSids: String (源群组ID列表，逗号分隔)<br>usernames: String (用户名列表，逗号分隔)<br>userBussinessConfig: String (用户业务配置) | JsonResponse |

### 10. UumsSysOrgController

**接口名称**：系统组织管理控制器  
**主要用途**：提供组织相关的管理功能

#### 方法清单

| 方法名称 | 方法说明 | 输入参数 | 输出结果 |
|----------|----------|-----------|-----------|
| findById | 根据id查询组织信息 | id: String (组织ID) | JsonResponse |
| create | 新增一个组织 | sysOrgInfoFull: Map (组织信息) | JsonResponse |
| findAll | 获取组织信息列表并分页 | page: int (当前页码，必填，默认1)<br>size: int (每页数量，必填，默认10)<br>direction: String (排序规则asc/desc)<br>properties: String (排序属性名称)<br>sysOrgInfoFull: Map (查询条件) | JsonResponse |
| findSonByParentOrgId | 查看某个父组织的子组织 | appcode: String (应用编码)<br>orgCode: String (组织编码) | JsonResponse |
| findSonByParentOrgIdTree | 查看某个父组织的子组织(树形结构) | appcode: String (应用编码)<br>mapOrg: Map (查询条件) | JsonResponse |
| findRootAndNextRoot | 获取根组织及其下一级组织 | appcode: String (应用编码) | JsonResponse |
| findPOrgAndCityOrg | 获取省公司及地市分公司信息 | appcode: String (应用编码) | JsonResponse |
| findPOrgAnd18CityOrg | 获取省公司及18个地市分公司 | appcode: String (应用编码) | JsonResponse |
| findCityDeapartmentAndCountyCompany | 获取市公司部门及下属县公司 | appcode: String (应用编码) | JsonResponse |
| findOrgByUserMap | 根据用户规则查询组织 | appcode: String (应用编码)<br>userMap: Map (用户规则) | JsonResponse |
| findRootByCorpId | 查询企业根节点 | appcode: String (应用编码)<br>corpId: String (企业ID) | JsonResponse |
| findOrgTreeFromCorp | 查询用户所在组织树 | appcode: String (应用编码)<br>corpMap: Map (查询条件) | JsonResponse |
| findRuleOrgTree | 查询组织的上下级关系树 | appcode: String (应用编码)<br>orgMap: Map (查询条件) | JsonResponse |
| findListByOrgCode | 根据组织编码查询组织列表 | orgCode: String (组织编码)<br>appcode: String (应用编码) | JsonResponse |
| findParentBySon | 查询子组织的父组织 | orgCode: String (组织编码)<br>appcode: String (应用编码) | JsonResponse |
| findOrgCodeOrgNameDim | 模糊查询组织列表 | page: int (当前页码)<br>size: int (每页数量)<br>direction: String (排序方向)<br>properties: String (排序字段)<br>parentOrgCode: String (父组织编码)<br>orgCode: String (组织编码)<br>orgName: String (组织名称)<br>companyTypeDictValue: String (公司类型) | JsonResponse |
| updateOrgCustom | 更新组织信息 | sysOrgInfoFull: Map (组织信息) | JsonResponse |
| deleteOrgCustom | 删除组织信息 | id: String (组织ID) | JsonResponse |
| queryOrgByPermissionId | 查询菜单授权的组织 | permissionId: String (菜单ID) | JsonResponse |

### 11. UumsSysOrgPermissionController

**接口名称**：系统组织权限管理控制器  
**主要用途**：提供组织权限相关的管理功能

#### 方法清单

| 方法名称 | 方法说明 | 输入参数 | 输出结果 |
|----------|----------|-----------|-----------|
| updateListByPermissionId | 修改组织权限信息 | permissionId: String (权限ID)<br>orgCodes: List<String> (组织编码列表) | JsonResponse |

### 12. UumsSysPermissionController

**接口名称**：系统权限管理控制器  
**主要用途**：提供权限和菜单相关的管理功能

#### 方法清单

| 方法名称 | 方法说明 | 输入参数 | 输出结果 |
|----------|----------|-----------|-----------|
| findById | 查询权限信息 | id: String (权限ID)<br>appcode: String (应用编码) | JsonResponse |
| findAllNoPage | 获取全部权限信息列表不分页 | appcode: String (应用编码)<br>simplePermissionMap: Map (查询条件) | JsonResponse |
| findPerCustom | 自定义查询菜单 | paramMap: Map (参数对象) | JsonResponse |
| findAllPermissions | 查询应用下所有菜单 | - | JsonResponse |
| createCustom | 添加指定应用下的菜单 | sysPermission: SimplePermission (权限信息) | JsonResponse |
| update | 更新菜单信息 | sysPermission: SimplePermission (权限信息) | JsonResponse |
| updateSysPermission | 启用/禁用菜单 | id: String (权限ID)<br>status: String (状态) | JsonResponse |
| deleteById | 删除菜单 | id: String (权限ID) | JsonResponse |

### 13. UumsSysPositionController

**接口名称**：系统职位管理控制器  
**主要用途**：提供职位相关的管理功能

#### 方法清单

| 方法名称 | 方法说明 | 输入参数 | 输出结果 |
|----------|----------|-----------|-----------|
| create | 新增职务信息 | map: Map (职务信息) | JsonResponse |
| update | 修改职位信息 | map: Map (职务信息) | JsonResponse |
| deleteByIdCustom | 删除职位 | id: String (职位ID) | JsonResponse |
| findPositionAndUser | 根据职务id查所有用户 | page: int (当前页码，默认1)<br>size: int (每页数量，默认10)<br>positionId: String (职务ID)<br>truename: String (人员中文名称) | JsonResponse |
| findById | 查询职位信息 | id: String (职位ID)<br>appcode: String (应用编码) | JsonResponse |
| findAll | 获取职位列表 | page: int (当前页码，默认1)<br>size: int (每页数量，默认10)<br>direction: String (排序规则)<br>properties: String (排序属性)<br>appcode: String (应用编码)<br>map: Map (查询条件) | JsonResponse |
| findPositionByUsername | 根据用户名查询其职务 | username: String (用户名)<br>appcode: String (应用编码) | JsonResponse |
| findPositionOrgcodeAndUsername | 查询某一组织下全部用户的职务 | orgCode: String (组织编码)<br>appcode: String (应用编码) | JsonResponse |
| findAllToTree | 查询全部职务(树形结构) | - | JsonResponse |
| findPositionCustom | 查询所有职务支持模糊查询职务名称 | orgCode: String (组织编码)<br>companyTypeDictValue: String (公司类型)<br>map: Map (查询条件) | JsonResponse |

### 14. UumsSysRoleController 系统角色管理接口
提供角色相关的REST API接口，包括基础的CRUD操作、角色权限管理、角色状态管理等功能。

#### 14.1 findById 查询角色信息
- 接口说明：根据角色ID查询角色详细信息
- 请求方式：POST
- 请求URL：/uums/sys/role/findById 或 /sys/uums/role/findById
- 请求参数：
  - id：角色ID（Integer，必填）
  - appcode：应用编码（String，必填）
- 返回结果：JsonResponse，包含角色信息

#### 14.2 findRoleNameIsARoleDim 角色列表分页查询
- 接口说明：用于管理后台角色列表页查询，支持角色名模糊查询并分页
- 请求方式：POST
- 请求URL：/uums/sys/role/findRoleNameIsARoleDim 或 /sys/uums/role/findRoleNameIsARoleDim/sso 或 /api/findRoleNameIsARoleDim/sso
- 请求参数：
  - page：当前页码（int，默认1）
  - size：每页数量（int，默认10）
  - direction：排序规则（String，可选，asc/desc）
  - properties：排序属性（String，可选）
  - map：角色名查询条件（Map，请求体）
- 返回结果：JsonResponse，包含分页的角色列表

#### 14.3 findAll 获取角色列表
- 接口说明：获取角色信息列表并分页
- 请求方式：POST
- 请求URL：/uums/sys/role/findAll 或 /sys/uums/role/findAll
- 请求参数：
  - page：当前页码（int，默认1）
  - size：每页数量（int，默认10）
  - direction：排序规则（String，可选，asc/desc）
  - properties：排序属性（String，可选）
  - appcode：应用编码（String，必填）
  - map：查询条件（Map，请求体）
- 返回结果：JsonResponse，包含分页的角色列表

#### 14.4 isHaveCode 校验角色编码
- 接口说明：校验roleCode是否已存在
- 请求方式：POST
- 请求URL：/uums/sys/role/isHaveCode 或 /sys/uums/role/isHaveCode
- 请求参数：
  - appcode：应用编码（String，必填）
  - roleCode：角色编码（String，必填）
- 返回结果：JsonResponse，包含校验结果

#### 14.5 addRole 新增角色
- 接口说明：创建新的角色
- 请求方式：POST
- 请求URL：/uums/sys/role/addRole 或 /sys/uums/role/addRole
- 请求参数：
  - appcode：应用编码（String，必填）
  - simpleRole：角色对象（SimpleRole，请求体）
- 返回结果：JsonResponse，包含新创建的角色信息

#### 14.6 delRole 删除角色
- 接口说明：删除指定的角色
- 请求方式：POST
- 请求URL：/uums/sys/role/delRole 或 /sys/uums/role/delRole
- 请求参数：
  - appcode：应用编码（String，必填）
  - simpleRole：角色对象（SimpleRole，请求体）
- 返回结果：JsonResponse，包含删除操作结果

#### 14.7 updateRole 修改角色
- 接口说明：更新角色信息
- 请求方式：POST
- 请求URL：/uums/sys/role/updateRole 或 /sys/uums/role/updateRole
- 请求参数：
  - appcode：应用编码（String，必填）
  - simpleRole：角色对象（SimpleRole，请求体）
- 返回结果：JsonResponse，包含更新后的角色信息

#### 14.8 queryRoleByPermissionId 查询授权角色
- 接口说明：根据菜单ID查询被授权的角色
- 请求方式：POST
- 请求URL：/uums/sys/role/queryRoleByPermissionId 或 /sys/uums/role/queryRoleByPermissionId/sso 或 /api/queryRoleByPermissionId
- 请求参数：
  - permissionId：菜单ID（String，可选）
- 返回结果：JsonResponse，包含授权角色列表

#### 14.9 updateSysRole 更新角色状态
- 接口说明：更新角色状态（启用/禁用）
- 请求方式：POST
- 请求URL：/uums/sys/role/updateSysRole 或 /sys/uums/role/updateSysRole/sso
- 请求参数：
  - id：角色ID（String，必填）
  - status：状态值（String，必填）
- 返回结果：JsonResponse，包含更新操作结果

### 15. UumsSysRolePermissionController 系统角色权限管理接口
提供角色权限关联的REST API接口，包括管理角色与权限的关联关系、批量更新角色的权限配置、权限分配和回收等功能。

#### 15.1 updateListByPermissionId 更新角色权限
- 接口说明：修改角色权限信息，先删除角色所有权限，再重新添加
- 请求方式：POST
- 请求URL：/uums/sys/role/permission/updateListByPermissionId 或 /sys/uums/role/permission/updateListByPermissionId/sso 或 /api/updateListByPermissionId
- 请求参数：
  - permissionId：权限ID（String，必填）
  - roleCodes：角色编码列表（List<String>，请求体）
- 返回结果：JsonResponse，包含更新操作结果

### 16. UumsSysUserRoleController 系统用户角色管理接口
提供用户与角色关联的REST API接口，包括管理用户与角色的关联关系、批量分配和移除用户角色、角色成员管理等功能。

#### 16.1 deleteByIdCustom 删除用户角色
- 接口说明：删除用户角色关联信息
- 请求方式：POST
- 请求URL：/uums/sys/user/role/deleteByIdCustom 或 /sys/uums/user/role/sso/deleteByIdCustom 或 /api/deleteByIdCustom
- 请求参数：
  - id：用户角色ID（String，必填）
- 返回结果：JsonResponse，包含删除操作结果

#### 16.2 createRoleUsers 角色添加用户
- 接口说明：为指定角色批量添加用户
- 请求方式：POST
- 请求URL：/uums/sys/user/role/createRoleUsers 或 /sys/uums/user/role/createRoleUsers/sso 或 /api/createRoleUsers
- 请求参数：
  - roleId：角色ID（String，可选）
  - usernames：OA账号列表，多个账号用逗号分隔（String，可选）
- 返回结果：JsonResponse，包含添加操作结果

### 17. UumsSysUserOrgController 系统用户组织关联管理接口
提供用户与组织关联的REST API接口，包括基础的CRUD操作、用户组织关系管理、分页和后台管理功能、用户所属组织信息查询等功能。

#### 17.1 findAllForAdmin 管理后台查询
- 接口说明：用于管理后台查询用户、组织、职务的管理关系
- 请求方式：POST
- 请求URL：/uums/sys/user/org/findAllForAdmin 或 /sys/uums/user/org/findAllForAdmin/sso 或 /api/findAllForAdmin
- 请求参数：
  - page：当前页码（int，默认1）
  - size：每页数量（int，默认10）
  - direction：排序规则（String，可选，asc/desc）
  - properties：排序属性（String，可选）
  - userOrg：用户组织对象（SimpleUserOrg，请求体）
- 返回结果：JsonResponse，包含分页的用户组织关系列表

#### 17.2 getOrgInfoByUsername 查询用户组织
- 接口说明：查询人员所属组织信息
- 请求方式：POST
- 请求URL：/uums/sys/user/org/getOrgInfoByUsername 或 /sys/uums/user/org/getOrgInfoByUsername/sso 或 /api/getOrgInfoByUsername
- 请求参数：
  - username：用户名（String，必填）
- 返回结果：JsonResponse，包含用户所属组织信息

#### 17.3 create 新增关联
- 接口说明：新增用户组织关联信息
- 请求方式：POST
- 请求URL：/uums/sys/user/org/create 或 /sys/uums/user/org/create
- 请求参数：
  - sysUserOrg：用户组织对象（SimpleUserOrg，请求体）
- 返回结果：JsonResponse，包含新创建的关联信息

#### 17.4 findById 查询关联
- 接口说明：根据ID查询用户组织信息
- 请求方式：POST
- 请求URL：/uums/sys/user/org/findById 或 /sys/uums/user/org/findById/sso 或 /api/findById
- 请求参数：
  - id：用户组织ID（String，必填）
- 返回结果：JsonResponse，包含用户组织关联信息

#### 17.5 update 更新关联
- 接口说明：修改用户组织关联信息
- 请求方式：POST
- 请求URL：/uums/sys/user/org/update 或 /sys/uums/user/org/update/sso 或 /api/update
- 请求参数：
  - sysUserOrg：用户组织对象（SimpleUserOrg，请求体）
- 返回结果：JsonResponse，包含更新后的关联信息

#### 17.6 deleteById 删除关联
- 接口说明：删除用户组织关联信息
- 请求方式：POST
- 请求URL：/uums/sys/user/org/deleteById 或 /sys/uums/user/org/deleteById/sso 或 /api/deleteById
- 请求参数：
  - id：用户组织ID（String，必填）
- 返回结果：JsonResponse，包含删除操作结果

### 18. UumsSysUserInfoController 系统用户信息管理接口
提供用户信息相关的REST API接口，包括基础的CRUD操作、用户权限和角色管理、用户组织和职位关联、用户密码管理、分页和多维度查询、用户缓存管理等功能。

#### 18.1 findUserByRole 根据角色查询用户
- 接口说明：根据角色获取用户信息并分页
- 请求方式：POST
- 请求URL：/uums/sys/userinfo/findUserByRole 或 /sys/uums/userinfo/findUserByRole/sso 或 /api/findUserByRole/sso
- 请求参数：
  - page：当前页码（int，默认1）
  - size：每页数量（int，默认10）
  - direction：排序规则（String，可选，asc/desc）
  - properties：排序属性（String，可选）
  - roleId：角色ID（String，可选）
- 返回结果：JsonResponse，包含分页的用户列表

#### 18.2 create 新增用户
- 接口说明：不登录新增用户信息
- 请求方式：POST
- 请求URL：/uums/sys/userinfo/create 或 /sys/uums/userinfo/create
- 请求参数：
  - keyword：用户名（String，可选）
  - keyType：用户名类型（KeyType，可选）
  - appcode：应用编码（String，可选）
  - simpleUser：用户对象（SimpleUser，请求体，可选）
- 返回结果：JsonResponse，包含新创建的用户信息

#### 18.3 update/updateUser 更新用户
- 接口说明：不登录更新用户信息
- 请求方式：POST
- 请求URL：/uums/sys/userinfo/update 或 /uums/sys/userinfo/updateUser 或 /sys/uums/userinfo/updateUser/sso 或 /api/updateUser
- 请求参数：
  - keyword：用户名（String，可选）
  - keyType：用户名类型（KeyType，可选）
  - appcode：应用编码（String，可选）
  - simpleUser：用户对象（SimpleUser，请求体，可选）
- 返回结果：JsonResponse，包含更新后的用户信息

#### 18.4 findAll/findAllNoPage 查询用户列表
- 接口说明：获取用户信息列表（支持分页和不分页两种方式）
- 请求方式：POST
- 请求URL：/uums/sys/userinfo/findAll 或 /uums/sys/userinfo/findAllNoPage
- 请求参数：
  - page：当前页码（int，默认1，仅分页接口）
  - size：每页数量（int，默认10，仅分页接口）
  - direction：排序规则（String，可选，asc/desc，仅分页接口）
  - properties：排序属性（String，可选，仅分页接口）
  - appcode：应用编码（String，必填）
  - map/simpleUserMap：查询条件（Map，请求体）
- 返回结果：JsonResponse，包含用户列表

#### 18.5 findByUsername 用户名查询
- 接口说明：根据用户名查询用户信息
- 请求方式：POST
- 请求URL：/uums/sys/userinfo/findByUsername
- 请求参数：
  - username：用户名（String，必填）
  - appcode：应用编码（String，必填）
- 返回结果：JsonResponse，包含用户信息

#### 18.6 findUsernameByOrgAndPosition 组织职位查询
- 接口说明：根据部门以及职位查询所有的人的用户名
- 请求方式：POST
- 请求URL：/uums/sys/userinfo/findUsernameByOrgAndPosition
- 请求参数：
  - loginUser：要验证的用户（String，必填）
  - orgCode：组织编码（String，必填）
  - positionIds：职位ID（String，必填）
  - appcode：应用编码（String，必填）
- 返回结果：JsonResponse，包含用户名列表

#### 18.7 findUserByOrg 组织查询用户
- 接口说明：根据组织orgcode获取用户并分页
- 请求方式：POST
- 请求URL：/uums/sys/userinfo/findUserByOrg
- 请求参数：
  - page：当前页码（int，默认1）
  - size：每页数量（int，默认10）
  - direction：排序规则（String，可选，asc/desc）
  - properties：排序属性（String，可选）
  - appcode：应用编码（String，必填）
  - orgCode：组织编码（String，必填）
- 返回结果：JsonResponse，包含分页的用户列表

#### 18.8 checkUserAccessApp 权限检查
- 接口说明：检测用户是否有app的权限
- 请求方式：POST
- 请求URL：/uums/sys/userinfo/checkUserAccessApp 或 /uums/sys/userinfo/checkUserAccessAppNoSession
- 请求参数：
  - username：用户名（String，可选）
  - appcode：应用编码（String，可选）
- 返回结果：JsonResponse，包含权限检查结果

#### 18.9 findPermissionByAppUser 查询用户权限
- 接口说明：查询某个人在某一应用下的全部权限
- 请求方式：POST
- 请求URL：/uums/sys/userinfo/findPermissionByAppUser 或 /uums/sys/userinfo/findPermissionByAppUserNoSession
- 请求参数：
  - username/loginuser：用户名/RSA加密后的用户名（String，可选）
  - appcode：应用编码（String，可选）
- 返回结果：JsonResponse，包含用户权限列表

#### 18.10 changeUserPassword/changeMyPassword 密码管理
- 接口说明：修改用户密码/修改我的密码
- 请求方式：POST
- 请求URL：/uums/sys/userinfo/changeUserPassword 或 /uums/sys/userinfo/changeMyPassword
- 请求参数：
  - username：用户名（String，仅changeUserPassword需要）
  - rsaPassword：RSA加密的密码（String，仅changeUserPassword需要）
  - oldRsaPassword：RSA加密的原始密码（String，仅changeMyPassword需要）
  - newRsaPassword：RSA加密的新密码（String，仅changeMyPassword需要）
  - appCode：应用编码（String，必填）
- 返回结果：JsonResponse，包含密码修改结果

#### 18.11 findUserByGroup 群组查询用户
- 接口说明：根据群组获取用户
- 请求方式：POST
- 请求URL：/uums/sys/userinfo/findUserByGroup 或 /sys/uums/userinfo/findUserByGroup/sso 或 /api/findUserByGroup
- 请求参数：
  - page：当前页码（int，默认1）
  - size：每页数量（int，默认10）
  - direction：排序规则（String，可选，asc/desc）
  - properties：排序属性（String，可选）
  - groupSid：群组ID（String，可选）
  - map：查询条件（Map，请求体，可选）
- 返回结果：JsonResponse，包含分页的用户列表

#### 18.12 findAllInfosUnderOrg 组织下级查询
- 接口说明：获取某一个组织下的组织和人
- 请求方式：POST
- 请求URL：/uums/sys/userinfo/findAllInfosUnderOrg
- 请求参数：
  - orgCode：组织编码（String，必填）
  - appCode：应用编码（String，必填）
- 返回结果：JsonResponse，包含组织和人员信息

#### 18.13 findUserByPositionNoPage 职位查询用户
- 接口说明：根据职位名获取用户不分页
- 请求方式：POST
- 请求URL：/uums/sys/userinfo/findUserByPositionNoPage
- 请求参数：
  - orgCode：组织编码（String，必填）
  - positionId：多个职位ID（String，必填）
- 返回结果：JsonResponse，包含用户列表

#### 18.14 findUserOrgDim 组织用户模糊查询
- 接口说明：根据组织（精确）以及用户oa账号、用户名、手机号（模糊）获取用户并分页
- 请求方式：POST
- 请求URL：/uums/sys/userinfo/findUserOrgDim
- 请求参数：
  - page：当前页码（int，默认1）
  - size：每页数量（int，默认10）
  - direction：排序规则（String，可选，asc/desc）
  - properties：排序属性（String，可选）
  - appcode：应用编码（String，可选）
  - orgCode：组织编码（String，可选）
  - searchFields：搜索内容（String，可选）
- 返回结果：JsonResponse，包含分页的用户列表

#### 18.15 用户管理操作
- insertUserInOrgNormal：新增组织下的用户
- updateUserInOrgNormal：修改组织下的用户
- deleteUserInOrgNormal：删除组织下的用户
请求方式：POST
请求URL：/uums/sys/userinfo/{operation} 或 /sys/uums/userinfo/{operation}/sso
请求参数：
  - mapParam：操作参数（Map，请求体，可选）
  - appcode：应用编码（String，可选）
返回结果：JsonResponse，包含操作结果

#### 18.16 缓存管理
- CleanUserCacheByFindTimUserInfos：清理人员变更缓存
请求方式：POST
请求URL：/uums/sys/userinfo/CleanUserCacheByFindTimUserInfos 或 /sys/uums/userinfo/CleanUserCacheByFindTimUserInfos/sso
请求参数：
  - startTime：开始时间（String，必填）
  - endTime：结束时间（String，必填）
返回结果：JsonResponse，包含清理结果

### 19. UumsSysUserPermissionController 系统用户权限管理接口
提供用户权限管理的REST API接口，包括管理用户与权限的关联关系、批量更新用户的权限配置、权限分配和回收、用户权限的动态调整等功能。

#### 19.1 updateListByPermissionId 更新用户权限
- 接口说明：修改用户权限信息，先删除用户所有权限，再重新添加
- 请求方式：POST
- 请求URL：/uums/sys/user/permission/updateListByPermissionId 或 /sys/uums/user/permission/updateListByPermissionId/sso 或 /api/updateListByPermissionId
- 请求参数：
  - permissionId：权限ID（String，必填）
  - orgCodes：组织编码列表（List<String>，请求体）
- 返回结果：JsonResponse，包含更新操作结果 