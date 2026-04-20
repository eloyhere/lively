 # Lively: 智能中医学习系统

## 系统概述
Lively 是一个全面的中医学习平台，将游戏化教学法与人工智能辅助相结合。该系统通过个性化学习路径、进度跟踪和自适应推荐系统，促进用户对中医文献的结构化学习。我们的吉祥物——一只拥有箭形尾巴的黑毛恶魔兔，名为"附子"，象征着平台对知识获取的活力与灵动态度，既体现了中医传统智慧，也代表了现代教育技术的创新应用。
附子既是传统中药名，也代表着我们系统"附加学习之趣"的理念，这只调皮的恶魔兔引导用户在严谨的中医学习中保持好奇与探索精神。

## 架构概述
系统采用单体应用架构，在保持后端服务和前端展示层明确关注点分离的同时，充分利用统一代码库的部署优势。

### 后端架构
- **运行时环境**: Java 开发工具包 17
- **核心框架**: Spring Boot 3.x，用于应用脚手架和依赖管理
- **安全层**: Spring Security 6.x，实现基于 URL 的授权模型
- **数据持久化**: Spring Data JPA，使用 Hibernate 作为 ORM 提供者
- **关系型数据库**: MySQL 8.0+，使用 `lively` 模式

### 前端架构
- **组件框架**: Vue 3，使用组合式 API 和 TypeScript 进行类型安全的开发
- **状态管理**: Pinia 存储，实现可预测的状态转换
- **构建系统**: Vite，用于快速开发和优化的生产构建
- **集成模式**: 编译后的前端资源从 Spring Boot 应用的资源目录提供，实现无缝部署

### 完整项目结构分析
```
lively/
├── logs/                                    # 应用程序运行时日志
├── src/
│   ├── main/java/pers/eloyhere/lively
│   │   ├── annotation/                      # 自定义安全注解
│   │   │   ├── Administrator.java           # 限制对管理员端点的访问
│   │   │   ├── Authenticated.java           # 需要有效认证凭证
│   │   │   ├── Everyone.java                # 允许无限制的公共访问
│   │   │   ├── Guest.java                   # 允许认证和非认证访问
│   │   │   └── Unauthenticated.java         # 仅允许非认证请求
│   │   ├── authentication/                  # 完整安全基础设施
│   │   │   ├── entry/                       # 认证入口点处理器
│   │   │   │   └── InvalidateAuthenticationEntryPoint.java
│   │   │   ├── filter/                      # 请求过滤组件
│   │   │   │   ├── handler/                 # 认证处理结果处理器
│   │   │   │   │   ├── LivelyAuthenticationFailureHandler.java
│   │   │   │   │   └── LivelyAuthenticationSuccessHandler.java
│   │   │   │   ├── LivelyAuthenticationDetailsSource.java
│   │   │   │   └── LivelyUsernamePasswordAuthenticationFilter.java
│   │   │   ├── granter/                     # 认证授权提供者
│   │   │   │   └── Granter.java
│   │   │   ├── provider/                    # 认证提供者实现
│   │   │   │   └── UsernamePasswordAuthenticationProvider.java
│   │   │   └── strategy/                    # 会话管理策略
│   │   │       └── InvalidateSessionStrategy.java
│   │   ├── component/                       # 应用程序组件
│   │   │   └── DataInitializer.java         # 数据库初始化和数据种子
│   │   ├── configuration/                   # Spring 配置类
│   │   │   ├── CrossOriginConfiguration.java # CORS 策略配置
│   │   │   ├── ResolverConfiguration.java   # 自定义参数解析器注册
│   │   │   ├── ResourcesConfiguration.java  # 静态资源处理
│   │   │   ├── SecurityConfiguration.java   # 核心安全框架配置
│   │   │   └── Vue3Configuration.java       # 前端集成和路由配置
│   │   ├── controller/                      # REST API 端点控制器
│   │   │   ├── book/                        # 中医文献管理端点
│   │   │   ├── chat/                        # AI 对话接口端点
│   │   │   ├── consumer/                    # 用户管理和认证端点
│   │   │   ├── AnnouncementController.java  # 系统公告发布
│   │   │   ├── AuthenticationController.java # 认证流程编排
│   │   │   └── BaseController.java          # 通用控制器工具
│   │   ├── converter/                       # 自定义类型转换器
│   │   │   └── StringBlobConverter.java     # 字符串与 BLOB 互转工具
│   │   ├── entity/                          # JPA 实体定义
│   │   │   ├── book/                        # 中医文献领域实体
│   │   │   ├── chat/                        # 对话和 AI 交互实体
│   │   │   ├── consumer/                    # 用户和权限领域实体
│   │   │   ├── Announcement.java            # 系统公告持久化模型
│   │   │   └── BaseEntity.java              # 通用实体属性和行为
│   │   ├── projection/                      # Spring Data 投影
│   │   │   └── BaseProjection.java          # 通用投影接口
│   │   ├── repository/                      # Spring Data 仓库接口
│   │   │   ├── book/                        # 中医文献数据访问
│   │   │   │   ├── BookRepository.java
│   │   │   │   └── ChapterRepository.java
│   │   │   ├── chat/                        # 对话数据访问
│   │   │   │   ├── ChatRepository.java
│   │   │   │   └── MessageRepository.java
│   │   │   ├── consumer/                    # 用户和权限数据访问
│   │   │   │   ├── AuthorityRepository.java
│   │   │   │   ├── ConsumerRepository.java
│   │   │   │   ├── InvitationRepository.java
│   │   │   │   ├── RoleRepository.java
│   │   │   │   └── TokenRepository.java
│   │   │   ├── AnnouncementRepository.java
│   │   │   └── BaseRepository.java          # 通用仓库操作
│   │   ├── resolver/                        # 自定义处理方法参数解析器
│   │   │   ├── EntityArgumentResolver.java  # 从请求参数解析实体
│   │   │   └── UUIDArgumentResolver.java    # UUID 类型转换和验证
│   │   ├── service/                         # 业务逻辑层
│   │   │   ├── authentication/              # 认证服务
│   │   │   │   └── LivelyPersistentTokenBasedRememberMeServices.java
│   │   │   ├── book/                        # 中医文献业务逻辑
│   │   │   │   ├── BookService.java
│   │   │   │   └── ChapterService.java
│   │   │   ├── chat/                        # 对话管理
│   │   │   │   ├── ChatService.java
│   │   │   │   └── MessageService.java
│   │   │   ├── consumer/                    # 用户和权限管理
│   │   │   │   ├── AuthorityService.java
│   │   │   │   ├── ConsumerService.java
│   │   │   │   ├── InvitationService.java
│   │   │   │   ├── RoleService.java
│   │   │   │   └── TokenService.java
│   │   │   ├── AnnouncementService.java
│   │   │   └── BaseService.java             # 通用服务操作
│   │   └── LivelyApplication.java           # 主应用程序入口点
│   ├── resources/
│   │   ├── lively/                          # 集成的 Vue 3 前端项目
│   │   │   ├── public/                      # 未经 Vite 处理的静态资源
│   │   │   ├── src/
│   │   │   │   ├── hooks/                   # Vue 3 组合式函数
│   │   │   │   │   ├── serialization.ts            # 实体交互工具
│   │   │   │   │   ├── network.ts           # HTTP 客户端抽象
│   │   │   │   │   ├── picture.ts           # 图片处理工具
│   │   │   │   │   └── url.ts               # URL 构造和操作
│   │   │   │   ├── interaction/             # 前端数据交互层
│   │   │   │   │   ├── serialization.ts            # TypeScript 实体定义
│   │   │   │   │   └── service.ts           # 前端服务抽象
│   │   │   │   ├── plugins/                 # Vue 插件集成
│   │   │   │   │   └── element.ts           # Element Plus 组件库
│   │   │   │   ├── router/                  # Vue Router 配置
│   │   │   │   │   └── index.ts             # 路由定义和守卫
│   │   │   │   ├── stores/                  # Pinia 状态管理
│   │   │   │   │   ├── authentication.ts    # 认证状态
│   │   │   │   │   └── counter.ts           # 示例存储实现
│   │   │   │   ├── views/                   # 页面级 Vue 组件
│   │   │   │   │   ├── authentication/      # 认证界面
│   │   │   │   │   │   ├── Account.vue      # 用户账户管理
│   │   │   │   │   │   └── Profile.vue      # 用户资料管理
│   │   │   │   │   ├── management/          # 管理界面
│   │   │   │   │   │   ├── book/            # 中医文献管理
│   │   │   │   │   │   │   ├── Book.vue
│   │   │   │   │   │   │   └── Chapter.vue
│   │   │   │   │   │   ├── chat/            # 对话管理
│   │   │   │   │   │   │   ├── Chat.vue
│   │   │   │   │   │   │   └── Message.vue
│   │   │   │   │   │   └── consumer/        # 用户管理
│   │   │   │   │   │   │   ├── Authority.vue
│   │   │   │   │   │   │   ├── Consumer.vue
│   │   │   │   │   │   │   ├── Invitation.vue
│   │   │   │   │   │   │   ├── Role.vue
│   │   │   │   │   │   │   └── Token.vue
│   │   │   │   │   ├── Authentication.vue   # 登录和注册
│   │   │   │   │   ├── Home.vue             # 主仪表板
│   │   │   │   │   └── Management.vue       # 管理仪表板
│   │   │   │   ├── App.vue                  # 根应用程序组件
│   │   │   │   ├── main.ts                  # 应用入口点
│   │   │   │   └── style.css                # 全局样式
│   │   │   ├── .gitignore                   # 前端特定 Git 排除
│   │   │   ├── env.d.ts                     # TypeScript 环境定义
│   │   │   ├── index.html                   # HTML 入口点
│   │   │   ├── package.json                 # NPM 依赖和脚本
│   │   │   ├── package-lock.json            # 依赖锁文件
│   │   │   ├── README.md                    # 前端文档
│   │   │   ├── tsconfig.app.json            # 应用 TypeScript 配置
│   │   │   ├── tsconfig.json                # 基础 TypeScript 配置
│   │   │   ├── tsconfig.node.json           # Node.js TypeScript 配置
│   │   │   └── vite.config.ts               # Vite 构建配置
│   │   ├── static/                          # Spring Boot 静态资源目录
│   │   │   ├── assets/                      # 编译后的前端资源
│   │   │   ├── background.jpeg              # 背景图片
│   │   │   ├── Close Eyes.png               # 吉祥物视觉资源
│   │   │   ├── favicon.ico                  # 浏览器图标
│   │   │   ├── index.html                   # 后备 HTML 入口点
│   │   │   └── smile.png                    # 额外吉祥物图片
│   │   └── application.properties           # Spring Boot 配置
│   └── test/java/pers/eloyhere/lively/
│       └── LivelyApplicationTests.java      # 主测试套件
├── .gitattributes                           # Git 文件处理指令
├── .gitignore                              # 版本控制排除
├── mvnw                                    # Unix 系统的 Maven 包装器
├── mvnw.cmd                                # Windows 系统的 Maven 包装器
├── pom.xml                                 # Maven 项目配置
└── readme.md                               # 项目概述文档
```

## 核心教学功能

### 智能学习系统
- **游戏化学习路径**: 受语言学习平台启发的渐进难度系统，实现间隔重复算法以优化知识保留
- **多格式文本集成**: 全面的中医文献支持，包含扫描手稿的光学字符识别功能
- **自适应推荐引擎**: "猜你喜欢"系统，采用协同过滤和基于内容的算法
- **个性化分析仪表板**: 全面的学习指标，带可视化进度跟踪和能力评估

### AI 驱动的认知辅助
- **对话式学习界面**: 自然语言处理，用于上下文概念澄清和苏格拉底式对话
- **主动参与系统**: 基于学习模式和知识衰减曲线的上下文感知学习提醒
- **知识结构可视化**: 自动生成认知构件：
    - 说明理论关系的概念思维导图
    - 诊断和治疗路径的程序流程图
    - 基于持续能力评估的个性化学习序列

## 安全架构
系统实现了精细的层级授权模型，在细粒度控制和行政简洁性之间取得平衡。

### 权限层次结构
```
用户 → (分配给) → 角色 → (授予访问) → 权限
```

### 授权机制
- 每个后端端点 URL 构成一个离散的权限实体
- 角色将相关权限聚合为逻辑管理分组
- 认证流程根据持久化用户仓库验证凭证
- 授权拦截器根据用户的聚合权限集验证每个请求
- 权限-URL 映射消除了访问控制决策的歧义

## 数据库模式配置
- **主数据库**: `lively` 模式，使用 UTF8MB4 字符编码
- **核心实体表**:
    - `consumers`: 包含认证凭证的用户资料
    - `roles`: 具有层级关系的权限分组实体
    - `authorities`: 带有 HTTP 方法规范的 URL-权限映射
    - `books` 和 `chapters`: 带元数据的中医文献存储库
    - `learning_progress`: 时序学习指标和能力跟踪
    - `chat_sessions` 和 `messages`: 带上下文元数据的 AI 交互历史
    - `knowledge_graphs`: 持久化的可视化结构和关系映射

## 安装和部署流程

### 先决条件要求
- Java 开发工具包 17 或更高版本
- MySQL 社区版 8.0+，使用 InnoDB 存储引擎
- Node.js 18+ LTS，用于前端依赖管理
- Apache Maven 3.8+，用于 Java 项目编排

### 后端初始化序列
1. **数据库模式创建**:
   ```sql
   CREATE DATABASE lively 
   CHARACTER SET utf8mb4 
   COLLATE utf8mb4_unicode_ci;
   ```

2. **应用程序配置**:
   使用环境特定值修改 `application.properties`:
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/lively?useSSL=false&serverTimezone=UTC
   spring.datasource.username=部署用户
   spring.datasource.password=安全凭证
   spring.jpa.hibernate.ddl-auto=validate
   ```

3. **应用程序打包和执行**:
   ```bash
   mvn clean package -DskipTests
   java -jar target/lively-1.0.0.jar
   ```

### 前端开发工作流程
1. **前端环境导航**:
   ```bash
   cd src/main/resources/lively
   ```

2. **依赖安装**:
   ```bash
   npm install
   ```

3. **开发服务器初始化** (支持热重载):
   ```bash
   npm run dev
   ```

4. **生产制品生成** (部署到 Spring Boot 的静态资源):
   ```bash
   npm run build
   ```

## 系统操作流程
1. **凭证验证**: Spring Security 认证提供者根据持久化数据验证用户凭证
2. **权限解析**: 认证成功后，用户的基于角色的权限被缓存以进行高效授权
3. **请求拦截**: 每个 API 调用都会触发针对用户权限集的授权验证
4. **学习界面交付**: Vue 3 单页应用程序提供响应式的、基于组件的学习环境
5. **智能干预**: 学习模式和能力评估触发上下文相关的 AI 辅助
6. **进度持久化**: JPA 实体将学习状态和交互历史与持久化数据存储同步

## 基础技术决策
1. **统一项目架构**: 前端资源嵌入 Spring Boot 应用的简化部署模型，消除跨域复杂性
2. **URL 中心授权模型**: 通过直接的 URL-权限映射简化权限管理，减少管理开销
3. **TypeScript 采用**: 通过编译时类型检查和改进的开发者工具，增强前端可靠性
4. **仓库抽象模式**: 通过 Spring Data JPA 的仓库接口减少数据访问的样板代码
5. **基于组合的状态管理**: 通过 Pinia 的响应式存储架构实现可预测的前端状态转换
6. **构建工具集成**: Vite 的快速开发服务器与优化的生产捆绑相结合

## 开发环境规格
- 后端服务默认使用端口 8080，支持可配置的备用端口
- 前端开发服务器使用端口 3000，并配置代理到后端服务
- 前端开发会话期间启用热模块替换
- 生产构建无需额外配置即可从嵌入式静态资源自动提供

## 支持和文档资源
- 应用程序运行时可在 `/swagger-ui.html` 获得交互式 API 文档
- 通过 TypeDoc 配置生成前端组件文档
- 文档目录中包含数据库模式可视化和实体关系图
- 生产环境的全面日志配置，带结构化 JSON 输出

---

*Lively 将中医的千年智慧与现代教育技术相融合，由我们调皮的恶魔兔伙伴附子引导——它提醒我们，带着好奇心和趣味性的深刻学习最为有效。*
