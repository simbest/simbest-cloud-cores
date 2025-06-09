# Simbest Cloud Cores

## 项目简介

Simbest Cloud Cores 是北京晟壁科技有限公司开发的云平台核心组件库，提供了微服务架构下的通用功能模块和基础设施支持。

## 🚀 快速开始

### 环境要求

- JDK 8+
- Maven 3.6+
- Git

### 快速构建运行

```bash
# 1. 克隆项目
git clone https://github.com/simbest/simbest-cloud-cores.git
cd simbest-cloud-cores

# 2. 编译项目
mvn compile -Dmaven.test.skip=true -Puat

# 3. 打包项目
mvn clean package -Dmaven.test.skip=true -Puat

# 4. 运行应用
java -jar target/robot.jar

# 5. 生成API文档
javadoc -d docs/apis -sourcepath src/main/java -subpackages com.simbest -encoding UTF-8 -charset UTF-8 -author -version -use -windowtitle "Simbest Cloud Cores API Documentation" -doctitle "Simbest Cloud Cores API Documentation" -header "Simbest Cloud Cores" -footer "Simbest Cloud Cores" -Xdoclint:none
```

## 主要功能模块

### 🏗️ 基础架构

- **基础模型层** (`com.simbest.cloud.cores.base.model`) - 提供通用、系统、业务、流程四级实体模型
- **数据访问层** (`com.simbest.cloud.cores.base.repository`) - 基于 JPA 的通用数据访问接口
- **服务层** (`com.simbest.cloud.cores.base.service`) - 通用业务服务接口和实现
- **控制器层** (`com.simbest.cloud.cores.base.web.controller`) - RESTful API 控制器基类

### 🔐 安全认证

- **认证令牌** (`com.simbest.cloud.cores.security.authtokens`) - 多种认证方式支持
- **安全配置** (`com.simbest.cloud.cores.security.config`) - Spring Security 配置
- **安全工具** (`com.simbest.cloud.cores.security.utils`) - 登录、权限验证工具

### 🛠️ 系统管理

- **系统模型** (`com.simbest.cloud.cores.sys.model`) - 文件管理、日志记录、字典管理等
- **系统服务** (`com.simbest.cloud.cores.sys.service`) - 系统级服务接口
- **系统控制器** (`com.simbest.cloud.cores.sys.web`) - 系统管理 API

### 🔧 工具组件

- **分布式锁** (`com.simbest.cloud.cores.component.distributed.lock`) - Redis/Redisson 分布式锁
- **加密工具** (`com.simbest.cloud.cores.utils.encrypt`) - 多种加密算法支持
- **文件处理** (`com.simbest.cloud.cores.utils.files`) - 文件上传、下载、SFTP 等
- **HTTP 客户端** (`com.simbest.cloud.cores.utils.http`) - HTTP 请求工具
- **验证码** (`com.simbest.cloud.cores.utils.captcha`) - 图形验证码生成

### 📊 数据处理

- **JSON 处理** (`com.simbest.cloud.cores.json`) - Jackson 配置和工具
- **Excel 处理** (`com.simbest.cloud.cores.office`) - Excel 导入导出
- **Redis 缓存** (`com.simbest.cloud.cores.redis`) - Redis 操作工具

### ⚙️ 配置管理

- **应用配置** (`com.simbest.cloud.cores.config`) - 应用级配置管理
- **常量定义** (`com.simbest.cloud.cores.constants`) - 系统常量
- **枚举类型** (`com.simbest.cloud.cores.enums`) - 业务枚举

## 技术栈

- **Spring Boot 3.2.0** - 应用框架
- **Spring Security 6.2.0** - 安全框架
- **Spring Data JPA 3.2.0** - 数据访问
- **Redis/Redisson** - 缓存和分布式锁
- **MySQL/Oracle** - 数据库支持
- **Jackson** - JSON 处理
- **Lombok** - 代码简化
- **Hutool** - Java 工具库

## 项目结构

```
src/main/java/com/simbest/
├── boot/                           # Boot工具包
│   └── util/distribution/id/       # 分布式ID生成
├── cloud/                          # 云平台核心
│   ├── cores/                      # 核心功能
│   │   ├── annotations/            # 注解
│   │   ├── base/                   # 基础层
│   │   ├── component/              # 组件
│   │   ├── config/                 # 配置
│   │   ├── security/               # 安全
│   │   ├── sys/                    # 系统管理
│   │   └── utils/                  # 工具类
│   └── exclude/                    # 排除包
docs/                               # 文档目录
├── apis/                          # API文档
└── prompts/                       # 开发规约
```

## 开发规约

项目遵循 SIMBEST 团队开发规约，详见 `docs/prompts/` 目录：

- `Simbest_Java_Gen.md` - 代码生成规范
- `Simbest_Java_Dev.md` - 开发规范

## API 文档生成

### 生成命令

在项目根目录执行以下命令生成 API 文档：

```bash
javadoc -d docs/apis \
        -sourcepath src/main/java \
        -subpackages com.simbest \
        -encoding UTF-8 \
        -charset UTF-8 \
        -author \
        -version \
        -use \
        -windowtitle "Simbest Cloud Cores API Documentation" \
        -doctitle "Simbest Cloud Cores API Documentation" \
        -header "Simbest Cloud Cores" \
        -footer "Simbest Cloud Cores" \
        -Xdoclint:none
```

### 命令参数说明

| 参数                        | 说明                          |
| --------------------------- | ----------------------------- |
| `-d docs/apis`              | 指定输出目录                  |
| `-sourcepath src/main/java` | 源代码路径                    |
| `-subpackages com.simbest`  | 递归处理 com.simbest 包及子包 |
| `-encoding UTF-8`           | 源文件编码                    |
| `-charset UTF-8`            | 输出文档编码                  |
| `-author`                   | 包含@author 标签              |
| `-version`                  | 包含@version 标签             |
| `-use`                      | 创建类和包使用页面            |
| `-windowtitle`              | 浏览器窗口标题                |
| `-doctitle`                 | 文档标题                      |
| `-header`                   | 页面头部信息                  |
| `-footer`                   | 页面底部信息                  |
| `-Xdoclint:none`            | 禁用文档检查                  |

### 查看文档

生成完成后，在浏览器中打开 `docs/apis/index.html` 即可查看 API 文档。

## 构建和部署

### 1. 编译工程

#### CMD 命令

```cmd
mvn compile -Dmaven.test.skip=true -Puat
```

#### PowerShell 命令

```powershell
mvn compile "-Dmaven.test.skip=true" "-Puat"
```

### 2. 测试工程

```bash
mvn test
```

### 3. 分析类库依赖

```bash
# 生成依赖树并输出到文件
mvn dependency:tree > tree.txt
```

### 4. Maven 基础命令

```bash
# 安装到本地仓库
mvn install

# 部署到远程仓库
mvn deploy
```

### 5. 打包

#### 测试环境打包

**Linux/Mac:**

```bash
mvn clean package -Dmaven.test.skip=true -Puat
```

**Windows PowerShell:**

```powershell
mvn clean package '-Dmaven.test.skip=true' '-Puat'
```

#### 生产环境打包

```bash
mvn clean package -Dmaven.test.skip=true -Pprd
```

### 6. 运行应用

#### 本地环境运行

```bash
# 默认端口运行
java -jar target/robot.jar

# 指定端口运行
java -jar target/robot.jar --server.port=7000
```

#### 测试环境后台运行

```bash
# 切换到应用目录
cd simbestboot

# 后台运行应用
nohup java -jar robot.jar --server.port=7000 > /dev/null 2>&1 &

# 查看日志
tailf boot_app_logs/robot/log_debug.log
```

### 7. 环境配置说明

#### Maven Profile 配置

项目支持多环境配置，通过 Maven Profile 进行环境切换：

| Profile | 环境     | 说明                    |
| ------- | -------- | ----------------------- |
| `-Puat` | 测试环境 | User Acceptance Testing |
| `-Pprd` | 生产环境 | Production              |
| 默认    | 开发环境 | Development             |

#### 常用参数说明

| 参数                     | 说明         |
| ------------------------ | ------------ |
| `-Dmaven.test.skip=true` | 跳过测试     |
| `--server.port=7000`     | 指定应用端口 |
| `nohup ... &`            | 后台运行     |
| `> /dev/null 2>&1`       | 重定向输出   |

### 8. 日志管理

#### 日志文件位置

```
boot_app_logs/robot/
├── log_debug.log      # 调试日志
├── log_error.log      # 错误日志
└── log_info.log       # 信息日志
```

#### 常用日志命令

```bash
# 实时查看日志
tailf boot_app_logs/robot/log_debug.log

# 查看最后100行日志
tail -100 boot_app_logs/robot/log_debug.log

# 搜索错误日志
grep "ERROR" boot_app_logs/robot/log_error.log
```

### 依赖引用

在其他项目中引用此组件：

```xml
<dependency>
    <groupId>com.simbest.cloud</groupId>
    <artifactId>simbest-cloud-cores</artifactId>
    <version>1.0</version>
</dependency>
```
