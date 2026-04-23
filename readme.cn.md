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
lively/                                           # 主项目根目录
├── logs/                                         # 应用程序运行时和操作日志
├── src/
│   ├── main/java/pers/eloyhere/lively
│   │   ├── annotation/                           # 用于细粒度访问控制的自定义安全注解
│   │   │   ├── Administrator.java                # 限制端点仅供拥有管理员权限的用户访问的注解
│   │   │   ├── Authenticated.java                # 要求用户必须通过有效认证才能访问的注解
│   │   │   ├── Everyone.java                     # 允许无限制公开访问、绕过安全检查的注解
│   │   │   ├── Guest.java                        # 允许已认证和未认证（访客）用户访问的注解
│   │   │   └── Unauthenticated.java              # 限制访问，仅允许未认证请求的注解
│   │   ├── aspect/
│   │   │   └── ClientAspect.java                 # 用于处理客户端横切关注点的面向切面编程（AOP）切面
│   │   ├── authentication/                       # 完整的Spring Security基础设施、配置和自定义扩展
│   │   │   ├── entry/                            # 用于处理安全异常的身份验证入口点处理器
│   │   │   │   └── InvalidateAuthenticationEntryPoint.java # 处理无效或过期身份验证尝试的入口点
│   │   │   ├── filter/                           # 用于身份验证和授权请求处理的自定义过滤器
│   │   │   │   ├── handler/                      # 认证结果处理器
│   │   │   │   │   ├── LivelyAuthenticationFailureHandler.java # 身份验证失败时执行的逻辑
│   │   │   │   │   └── LivelyAuthenticationSuccessHandler.java # 身份验证成功时执行的逻辑
│   │   │   │   ├── LivelyAuthenticationDetailsSource.java # 提供身份验证请求的附加详情
│   │   │   │   └── LivelyUsernamePasswordAuthenticationFilter.java # 自定义的用户名/密码身份验证过滤器
│   │   │   ├── granter/                          # 用于扩展认证机制的自定义认证授权提供者
│   │   │   │   └── Granter.java                  # 自定义认证授权器的基础接口/类
│   │   │   ├── provider/                         # 身份验证提供者实现
│   │   │   │   └── UsernamePasswordAuthenticationProvider.java # 自定义的用户名/密码身份验证提供者
│   │   │   └── strategy/                         # 会话管理和失效策略
│   │   │       └── InvalidateSessionStrategy.java # 定义使用户会话失效的策略
│   │   ├── component/                            # Spring管理的应用程序组件和Bean
│   │   │   └── DataInitializer.java              # 用于在应用启动时初始化数据库并填充种子数据的组件
│   │   ├── configuration/                        # Spring Boot和框架配置类
│   │   │   ├── CrossOriginConfiguration.java     # 跨源资源共享（CORS）策略配置
│   │   │   ├── ResolverConfiguration.java        # 向Spring MVC注册自定义处理器方法参数解析器
│   │   │   ├── ResourcesConfiguration.java       # 提供静态资源和缓存策略的配置
│   │   │   ├── SecurityConfiguration.java        # 核心Spring Security框架配置和规则定义
│   │   │   ├── WebSocketConfiguration.java       # WebSocket端点注册和消息代理配置
│   │   │   └── Vue3Configuration.java           # Vue 3前端集成配置，包括路由和历史模式回退
│   │   ├── controller/                           # RESTful API端点控制器（MVC）
│   │   │   ├── book/                             # 中医文献管理端点
│   │   │   │   ├── BookController.java
│   │   │   │   └── ChapterController.java
│   │   │   ├── chat/                             # AI驱动的对话聊天和消息接口端点
│   │   │   │   ├── ChatController.java
│   │   │   │   └── MessageController.java
│   │   │   ├── consumer/                         # 用户、角色、权限和系统管理端点
│   │   │   │   ├── AuthorityController.java
│   │   │   │   ├── ConsumerController.java
│   │   │   │   ├── InvitationController.java
│   │   │   │   ├── MenuControllerController.java
│   │   │   │   ├── RoleControllerController.java
│   │   │   │   ├── RouteControllerController.java
│   │   │   │   └── TokenControllerController.java
│   │   │   ├── AnnouncementController.java       # 发布和管理全系统公告的端点
│   │   │   ├── AuthenticationController.java    # 协调认证流程（登录、注销、注册）的控制器
│   │   │   ├── BaseController.java               # 通用控制器工具、常量和其他控制器的基类
│   │   │   └── ResourcesController.java         # 用于提供应用程序资源的控制器
│   │   ├── converter/                            # 用于Spring MVC数据绑定的自定义类型转换器
│   │   │   └── StringBlobConverter.java          # 用于String到SQL BLOB类型转换的转换器工具
│   │   ├── entity/                               # 代表领域模型的JPA实体类定义
│   │   │   ├── book/                             # 中医文献领域实体
│   │   │   │   ├── Book.java
│   │   │   │   └── Chapter.java
│   │   │   ├── chat/                             # 对话和AI交互领域实体
│   │   │   │   ├── Chat.java
│   │   │   │   └── Message.java
│   │   │   ├── consumer/                         # 用户、认证和授权领域实体
│   │   │   │   ├── Authority.java
│   │   │   │   ├── Consumer.java
│   │   │   │   ├── Invitation.java
│   │   │   │   ├── Menu.java
│   │   │   │   ├── Role.java
│   │   │   │   ├── Route.java
│   │   │   │   └── Token.java
│   │   │   ├── Announcement.java                 # 用于持久化系统公告的实体
│   │   │   └── BaseEntity.java                   # 定义公共属性（ID、时间戳）和行为的抽象基实体
│   │   ├── projection/                           # 用于存储库查询的Spring Data JPA投影接口
│   │   │   └── BaseProjection.java               # 投影的通用基础接口
│   │   ├── repository/                           # 用于数据持久化的Spring Data JPA存储库接口
│   │   │   ├── book/                             # 中医文献实体的数据访问接口
│   │   │   │   ├── BookRepository.java
│   │   │   │   └── ChapterRepository.java
│   │   │   ├── chat/                             # 对话实体的数据访问接口
│   │   │   │   ├── ChatRepository.java
│   │   │   │   └── MessageRepository.java
│   │   │   ├── consumer/                         # 用户和权限实体的数据访问接口
│   │   │   │   ├── AuthorityRepository.java
│   │   │   │   ├── ConsumerRepository.java
│   │   │   │   ├── InvitationRepository.java
│   │   │   │   ├── MenuRepository.java
│   │   │   │   ├── RoleRepository.java
│   │   │   │   ├── RouteRepository.java
│   │   │   │   └── TokenRepository.java
│   │   │   ├── AnnouncementRepository.java
│   │   │   └── BaseRepository.java               # 通用存储库操作和泛型基础接口
│   │   ├── resolver/                             # 自定义Spring MVC处理器方法参数解析器
│   │   │   ├── EntityArgumentResolver.java       # 从请求参数直接解析并注入实体实例
│   │   │   └── UUIDArgumentResolver.java         # 从请求中解析、转换和验证UUID参数
│   │   ├── service/                              # 业务逻辑和服务层实现
│   │   │   ├── authentication/                   # 身份验证相关的业务服务
│   │   │   │   └── LivelyPersistentTokenBasedRememberMeServices.java # 自定义的"记住我"认证服务
│   │   │   ├── book/                             # 中医文献管理的业务逻辑服务
│   │   │   │   ├── BookService.java
│   │   │   │   └── ChapterService.java
│   │   │   ├── chat/                             # 对话和消息管理的业务逻辑服务
│   │   │   │   ├── ChatService.java
│   │   │   │   └── MessageService.java
│   │   │   ├── consumer/                         # 用户、角色和权限管理的业务逻辑服务
│   │   │   │   ├── AuthorityService.java
│   │   │   │   ├── ConsumerService.java
│   │   │   │   ├── InvitationService.java
│   │   │   │   ├── MenuService.java
│   │   │   │   ├── RoleService.java
│   │   │   │   ├── RouteService.java
│   │   │   │   └── TokenService.java
│   │   │   ├── AnnouncementService.java
│   │   │   └── BaseService.java                  # 提供通用CRUD操作的抽象基础服务
│   │   └── LivelyApplication.java                # Spring Boot应用程序主入口点和引导类
│   ├── resources/
│   │   ├── lively/                               # 集成的Vue 3单页应用（SPA）前端项目
│   │   │   ├── public/                           # 无需Vite处理、直接提供的静态资源
│   │   │   ├── src/
│   │   │   │   ├── component/
│   │   │   │   │   ├── AvatarCard.vue            # 用户头像显示和下拉菜单组件
│   │   │   │   │   ├── Modulize.vue              # 用于CRUD操作的抽象、可复用组件，集成搜索、表格和表单
│   │   │   │   │   └── Toolbar.vue               # 带有常用操作的通用工具栏组件
│   │   │   │   ├── declaration/                  # TypeScript类型和接口声明
│   │   │   │   │   ├── component.ts
│   │   │   │   │   ├── entity.ts                 # 对应后端JPA实体的类型声明
│   │   │   │   │   ├── modulize.ts               # 抽象CRUD组件的类型声明
│   │   │   │   │   └── serialization.ts          # 数据序列化和反序列化的类型声明
│   │   │   │   ├── hooks/                        # Vue 3组合式API自定义钩子（组合式函数）
│   │   │   │   │   ├── datetime.ts               # 日期和时间格式化、处理工具函数
│   │   │   │   │   ├── network.ts                # 封装GET、POST、PUT、DELETE请求的HTTP客户端函数
│   │   │   │   │   ├── picture.ts                # 图片转换、压缩和处理工具函数
│   │   │   │   │   ├── serialization.ts          # JSON序列化和反序列化工具函数
│   │   │   │   │   ├── url.ts                    # URL构建、解析和处理工具函数
│   │   │   │   │   └── utility.ts                # 通用工具和辅助函数
│   │   │   │   ├── interaction/                  # 前端数据交互和服务抽象层
│   │   │   │   │   ├── serialization.ts          # 用于DTO的序列化/反序列化逻辑和TypeScript接口定义
│   │   │   │   │   └── service.ts                # 与后端API通信的前端服务抽象
│   │   │   │   ├── plugins/                      # Vue插件初始化和配置
│   │   │   │   │   └── element.ts                # Element Plus UI组件库的配置和设置
│   │   │   │   ├── router/                       # 用于前端路由的Vue Router配置
│   │   │   │   │   └── index.ts                  # 路由定义、导航守卫和路由器实例
│   │   │   │   ├── stores/                       # Pinia状态管理存储
│   │   │   │   │   ├── authentication.ts         # 用于管理用户认证状态、令牌和操作的存储
│   │   │   │   │   └── counter.ts                # 示例演示存储（可替换或删除）
│   │   │   │   ├── views/                        # 页面级Vue组件（路由）
│   │   │   │   │   ├── authentication/           # 用户认证和账户管理视图
│   │   │   │   │   │   ├── Account.vue           # 用户账户管理和设置视图
│   │   │   │   │   │   └── Profile.vue           # 用户资料查看和编辑界面
│   │   │   │   │   ├── management/               # 管理和系统管理视图
│   │   │   │   │   │   ├── book/                 # 中医文献管理界面
│   │   │   │   │   │   │   ├── Book.vue
│   │   │   │   │   │   │   └── Chapter.vue
│   │   │   │   │   │   ├── chat/                 # 对话历史和管理界面
│   │   │   │   │   │   │   ├── Chat.vue
│   │   │   │   │   │   │   └── Message.vue
│   │   │   │   │   │   ├── consumer/             # 用户、角色和系统配置管理界面
│   │   │   │   │   │   │   ├── Authority.vue
│   │   │   │   │   │   │   ├── Consumer.vue
│   │   │   │   │   │   │   ├── Invitation.vue
│   │   │   │   │   │   │   ├── Menu.vue
│   │   │   │   │   │   │   ├── Role.vue
│   │   │   │   │   │   │   ├── Route.vue
│   │   │   │   │   │   │   └── Token.vue
│   │   │   │   │   │   ├── Announcement.vue      # 查看和管理系统公告的界面
│   │   │   │   │   │   ├── Index.vue             # 管理部分的索引或主页
│   │   │   │   │   │   ├── Online.vue            # 用于监控用户在线状态和会话日志的视图
│   │   │   │   │   │   └── Operation.vue         # 用于检查系统操作和审计日志的视图
│   │   │   │   │   ├── Authentication.vue        # 登录和用户注册页面
│   │   │   │   │   ├── Home.vue                  # 认证后的主应用程序仪表板
│   │   │   │   │   └── Management.vue            # 中央管理仪表板
│   │   │   │   ├── App.vue                       # 根Vue应用组件和布局包装器
│   │   │   │   ├── main.ts                       # 前端应用程序入口点，用于挂载Vue应用
│   │   │   │   └── style.css                     # 全局CSS样式和主题
│   │   │   ├── .gitignore                        # 前端特定的Git版本控制排除项
│   │   │   ├── env.d.ts                          # 用于Vite环境变量的TypeScript环境声明
│   │   │   ├── index.html                        # Vue 3应用的主要HTML入口点
│   │   │   ├── package.json                      # NPM项目配置、依赖项和脚本
│   │   │   ├── package-lock.json                 # 依赖项锁定文件，确保可复现的安装
│   │   │   ├── README.md                         # 前端项目特定的文档和指南
│   │   │   ├── tsconfig.app.json                 # 应用源代码的TypeScript配置
│   │   │   ├── tsconfig.json                     # 基础TypeScript配置文件
│   │   │   ├── tsconfig.node.json                # Node.js工具和构建脚本的TypeScript配置
│   │   │   └── vite.config.ts                    # 用于开发和生产的Vite构建工具配置
│   │   ├── static/                               # Spring Boot静态资源目录（从根路径'/'提供）
│   │   │   ├── assets/                           # 构建过程生成的已编译前端资源（CSS、JS）
│   │   │   ├── background.jpeg                   # 应用程序的背景图片资源
│   │   │   ├── Close Eyes.png                    # 吉祥物或品牌图片资源
│   │   │   ├── favicon.ico                       # 浏览器网站图标
│   │   │   ├── index.html                        # 回退HTML入口点（通常用于SPA路由）
│   │   │   └── smile.png                         # 额外的吉祥物或品牌图片资源
│   │   └── application.properties                # 主要的Spring Boot应用程序配置（数据源、服务器等）
│   └── test/java/pers/eloyhere/lively/
│       └── LivelyApplicationTests.java           # 应用程序的主要集成和单元测试套件
├── .gitattributes                                # Git文件处理指令（如行尾、合并策略）
├── .gitignore                                    # 针对生成文件、依赖项和本地环境的版本控制排除项
├── mvnw                                          # 适用于Unix系统（Linux/macOS）的Maven包装器可执行脚本
├── mvnw.cmd                                      # 适用于Windows系统的Maven包装器可执行脚本
├── pom.xml                                       # 定义依赖项、构建和项目元数据的Maven项目对象模型（POM）文件
└── readme.md                                     # 完整的项目概述、设置说明和使用文档
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