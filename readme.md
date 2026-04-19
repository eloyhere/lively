# Lively: AI-Powered TCM Learning System

## Overview
Lively is a comprehensive Traditional Chinese Medicine (TCM) learning platform that integrates gamified pedagogical methodologies with intelligent artificial intelligence assistance. The system facilitates structured engagement with TCM literature through personalised learning pathways, progress tracking, and adaptive recommendation systems. Our mascot—a black-furred demon rabbit distinguished by an arrow-shaped tail—epitomises the platform's dynamic, spirited approach to knowledge acquisition, symbolising both the traditional wisdom of Chinese medicine and the innovative application of modern educational technology.

## Architectural Overview
The system employs a monolithic application architecture that maintains clear separation of concerns between backend services and frontend presentation layers, thereby ensuring maintainability while leveraging the deployment advantages of a unified codebase.

### Backend Architecture
- **Runtime Environment**: Java Development Kit 17
- **Core Framework**: Spring Boot 3.x for application scaffolding and dependency management
- **Security Layer**: Spring Security 6.x implementing a URL-based authorisation model
- **Data Persistence**: Spring Data JPA with Hibernate as the ORM provider
- **Relational Database**: MySQL 8.0+ with the `lively` schema

### Frontend Architecture
- **Component Framework**: Vue 3 with Composition API and TypeScript for type-safe development
- **State Management**: Pinia stores for predictable state transitions
- **Build System**: Vite for rapid development and optimised production builds
- **Integration Model**: Compiled frontend assets are served from the Spring Boot application's resource directory, enabling seamless deployment

### Comprehensive Project Structure Analysis
```
lively/
├── logs/                                    # Application runtime logs
├── src/
│   ├── main/java/pers/eloyhere/lively
│   │   ├── annotation/                      # Custom security annotations
│   │   │   ├── Administrator.java           # Restricts access to administrative endpoints
│   │   │   ├── Authenticated.java           # Requires valid authentication credentials
│   │   │   ├── Everyone.java                # Permits unrestricted public access
│   │   │   ├── Guest.java                   # Allows both authenticated and unauthenticated access
│   │   │   └── Unauthenticated.java         # Permits only unauthenticated requests
│   │   ├── authentication/                  # Comprehensive security infrastructure
│   │   │   ├── entry/                       # Authentication entry point handlers
│   │   │   │   └── InvalidateAuthenticationEntryPoint.java
│   │   │   ├── filter/                      # Request filtering components
│   │   │   │   ├── handler/                 # Authentication outcome handlers
│   │   │   │   │   ├── LivelyAuthenticationFailureHandler.java
│   │   │   │   │   └── LivelyAuthenticationSuccessHandler.java
│   │   │   │   ├── LivelyAuthenticationDetailsSource.java
│   │   │   │   └── LivelyUsernamePasswordAuthenticationFilter.java
│   │   │   ├── granter/                     # Authentication grant providers
│   │   │   │   └── Granter.java
│   │   │   ├── provider/                    # Authentication provider implementations
│   │   │   │   └── UsernamePasswordAuthenticationProvider.java
│   │   │   └── strategy/                    # Session management strategies
│   │   │       └── InvalidateSessionStrategy.java
│   │   ├── component/                       # Application-wide components
│   │   │   └── DataInitializer.java         # Database seeding and initialisation logic
│   │   ├── configuration/                   # Spring configuration classes
│   │   │   ├── CrossOriginConfiguration.java # CORS policy configuration
│   │   │   ├── ResolverConfiguration.java   # Custom argument resolver registration
│   │   │   ├── ResourcesConfiguration.java  # Static resource handling
│   │   │   ├── SecurityConfiguration.java   # Core security framework configuration
│   │   │   └── Vue3Configuration.java       # Frontend integration and routing
│   │   ├── controller/                      # REST API endpoint controllers
│   │   │   ├── book/                        # TCM literature management endpoints
│   │   │   ├── chat/                        # AI conversation interface endpoints
│   │   │   ├── consumer/                  # User management and authentication endpoints
│   │   │   ├── AnnouncementController.java  # System announcement dissemination
│   │   │   ├── AuthenticationController.java # Authentication flow orchestration
│   │   │   └── BaseController.java          # Common controller utilities
│   │   ├── converter/                       # Custom type converters
│   │   │   └── StringBlobConverter.java     # String-BLOB interconversion utilities
│   │   ├── entity/                          # JPA entity definitions
│   │   │   ├── book/                        # TCM literature domain entities
│   │   │   ├── chat/                        # Conversation and AI interaction entities
│   │   │   ├── consumer/                    # User and permission domain entities
│   │   │   ├── Announcement.java            # System announcement persistence model
│   │   │   └── BaseEntity.java              # Common entity attributes and behaviours
│   │   ├── projection/                      # Spring Data projections
│   │   │   └── BaseProjection.java          # Common projection interfaces
│   │   ├── repository/                      # Spring Data repository interfaces
│   │   │   ├── book/                        # TCM literature data access
│   │   │   │   ├── BookRepository.java
│   │   │   │   └── ChapterRepository.java
│   │   │   ├── chat/                        # Conversation data access
│   │   │   │   ├── ChatRepository.java
│   │   │   │   └── MessageRepository.java
│   │   │   ├── consumer/                    # User and permission data access
│   │   │   │   ├── AuthorityRepository.java
│   │   │   │   ├── ConsumerRepository.java
│   │   │   │   ├── InvitationRepository.java
│   │   │   │   ├── RoleRepository.java
│   │   │   │   └── TokenRepository.java
│   │   │   ├── AnnouncementRepository.java
│   │   │   └── BaseRepository.java          # Common repository operations
│   │   ├── resolver/                        # Custom handler method argument resolvers
│   │   │   ├── EntityArgumentResolver.java  # Entity resolution from request parameters
│   │   │   └── UUIDArgumentResolver.java    # UUID type conversion and validation
│   │   ├── service/                         # Business logic layer
│   │   │   ├── authentication/              # Authentication services
│   │   │   │   └── LivelyPersistentTokenBasedRememberMeServices.java
│   │   │   ├── book/                        # TCM literature business logic
│   │   │   │   ├── BookService.java
│   │   │   │   └── ChapterService.java
│   │   │   ├── chat/                        # Conversation management
│   │   │   │   ├── ChatService.java
│   │   │   │   └── MessageService.java
│   │   │   ├── consumer/                    # User and permission management
│   │   │   │   ├── AuthorityService.java
│   │   │   │   ├── ConsumerService.java
│   │   │   │   ├── InvitationService.java
│   │   │   │   ├── RoleService.java
│   │   │   │   └── TokenService.java
│   │   │   ├── AnnouncementService.java
│   │   │   └── BaseService.java             # Common service operations
│   │   └── LivelyApplication.java           # Primary application entry point
│   ├── resources/
│   │   ├── lively/                          # Integrated Vue 3 frontend project
│   │   │   ├── public/                      # Static assets not processed by Vite
│   │   │   ├── src/
│   │   │   │   ├── hooks/                   # Vue 3 composition functions
│   │   │   │   │   ├── entity.ts            # Entity interaction utilities
│   │   │   │   │   ├── network.ts           # HTTP client abstraction
│   │   │   │   │   ├── picture.ts           # Image handling utilities
│   │   │   │   │   └── url.ts               # URL construction and manipulation
│   │   │   │   ├── interaction/             # Frontend data interaction layer
│   │   │   │   │   ├── entity.ts            # TypeScript entity definitions
│   │   │   │   │   └── service.ts           # Frontend service abstractions
│   │   │   │   ├── plugins/                 # Vue plugin integrations
│   │   │   │   │   └── element.ts           # Element Plus component library
│   │   │   │   ├── router/                  # Vue Router configuration
│   │   │   │   │   └── index.ts             # Route definitions and guards
│   │   │   │   ├── stores/                  # Pinia state management
│   │   │   │   │   ├── authentication.ts    # Authentication state
│   │   │   │   │   └── counter.ts           # Example store implementation
│   │   │   │   ├── views/                   # Page-level Vue components
│   │   │   │   │   ├── authentication/      # Authentication interfaces
│   │   │   │   │   │   ├── Account.vue      # User account management
│   │   │   │   │   │   └── Profile.vue      # User profile management
│   │   │   │   │   ├── management/          # Administrative interfaces
│   │   │   │   │   │   ├── book/            # TCM literature administration
│   │   │   │   │   │   │   ├── Book.vue
│   │   │   │   │   │   │   └── Chapter.vue
│   │   │   │   │   │   ├── chat/            # Conversation management
│   │   │   │   │   │   │   ├── Chat.vue
│   │   │   │   │   │   │   └── Message.vue
│   │   │   │   │   │   └── consumer/        # User management
│   │   │   │   │   │   │   ├── Authority.vue
│   │   │   │   │   │   │   ├── Consumer.vue
│   │   │   │   │   │   │   ├── Invitation.vue
│   │   │   │   │   │   │   ├── Role.vue
│   │   │   │   │   │   │   └── Token.vue
│   │   │   │   │   ├── Authentication.vue   # Login and registration
│   │   │   │   │   ├── Home.vue             # Primary dashboard
│   │   │   │   │   └── Management.vue       # Administrative dashboard
│   │   │   │   ├── App.vue                  # Root application component
│   │   │   │   ├── main.ts                  # Application entry point
│   │   │   │   └── style.css                # Global styles
│   │   │   ├── .gitignore                   # Frontend-specific Git exclusions
│   │   │   ├── env.d.ts                     # TypeScript environment definitions
│   │   │   ├── index.html                   # HTML entry point
│   │   │   ├── package.json                 # NPM dependencies and scripts
│   │   │   ├── package-lock.json            # Dependency lock file
│   │   │   ├── README.md                    # Frontend documentation
│   │   │   ├── tsconfig.app.json            # Application TypeScript configuration
│   │   │   ├── tsconfig.json                # Base TypeScript configuration
│   │   │   ├── tsconfig.node.json           # Node.js TypeScript configuration
│   │   │   └── vite.config.ts               # Vite build configuration
│   │   ├── static/                          # Spring Boot static resources directory
│   │   │   ├── assets/                      # Compiled frontend assets
│   │   │   ├── background.jpeg              # Background imagery
│   │   │   ├── Close Eyes.png               # Mascot visual assets
│   │   │   ├── favicon.ico                  # Browser icon
│   │   │   ├── index.html                   # Fallback HTML entry point
│   │   │   └── smile.png                    # Additional mascot imagery
│   │   └── application.properties           # Spring Boot configuration
│   └── test/java/pers/eloyhere/lively/
│       └── LivelyApplicationTests.java      # Primary test suite
├── .gitattributes                           # Git file handling directives
├── .gitignore                              # Version control exclusions
├── mvnw                                    # Maven wrapper for Unix systems
├── mvnw.cmd                                # Maven wrapper for Windows
├── pom.xml                                 # Maven project configuration
└── readme.md                               # Project overview documentation
```

## Core Pedagogical Features

### Intelligent Learning System
- **Gamified Learning Pathways**: Progressive difficulty progression inspired by language acquisition platforms, implementing spaced repetition algorithms for optimal knowledge retention
- **Multiformat Text Integration**: Comprehensive TCM literature support with optical character recognition capabilities for scanned manuscripts
- **Adaptive Recommendation Engine**: "Guess You'll Like" system employing collaborative filtering and content-based algorithms
- **Personalised Analytics Dashboards**: Comprehensive learning metrics with visual progress tracking and proficiency assessments

### AI-Powered Cognitive Assistance
- **Conversational Learning Interface**: Natural language processing for contextual concept clarification and Socratic dialogue
- **Proactive Engagement System**: Context-aware study notifications based on learning patterns and knowledge decay curves
- **Knowledge Structure Visualisation**: Automated generation of cognitive artefacts:
    - Conceptual mind maps illustrating theoretical relationships
    - Procedural flowcharts for diagnostic and treatment pathways
    - Personalised learning sequences derived from continuous proficiency assessment

## Security Architecture
The system implements a sophisticated, hierarchical authorisation model that balances granular control with administrative simplicity.

### Permission Hierarchy
```
User → (assigned to) → Roles → (granting access to) → Permissions
```

### Authorisation Mechanism
- Each backend endpoint URL constitutes a discrete permission entity
- Roles aggregate related permissions into logical administrative groupings
- Authentication workflows validate credentials against the persistent user repository
- Authorisation interceptors validate each request against the user's aggregated permission set
- The permission-URL mapping eliminates ambiguity in access control decisions

## Database Schema Configuration
- **Primary Database**: `lively` schema with UTF8MB4 character encoding
- **Core Entity Tables**:
    - `consumers`: User profiles with authentication credentials
    - `roles`: Permission grouping entities with hierarchical relationships
    - `authorities`: URL-permission mappings with HTTP method specifications
    - `books` and `chapters`: TCM literature repository with metadata
    - `learning_progress`: Temporal learning metrics and proficiency tracking
    - `chat_sessions` and `messages`: AI interaction histories with contextual metadata
    - `knowledge_graphs`: Persisted visualisation structures and relationship mappings

## Installation and Deployment Procedures

### Prerequisite Requirements
- Java Development Kit 17 or subsequent versions
- MySQL Community Edition 8.0+ with InnoDB storage engine
- Node.js 18+ LTS for frontend dependency management
- Apache Maven 3.8+ for Java project orchestration

### Backend Initialisation Sequence
1. **Database Schema Creation**:
   ```sql
   CREATE DATABASE lively 
   CHARACTER SET utf8mb4 
   COLLATE utf8mb4_unicode_ci;
   ```

2. **Application Configuration**:
   Amend `application.properties` with environment-specific values:
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/lively?useSSL=false&serverTimezone=UTC
   spring.datasource.username=deployment_user
   spring.datasource.password=secure_credential
   spring.jpa.hibernate.ddl-auto=validate
   ```

3. **Application Packaging and Execution**:
   ```bash
   mvn clean package -DskipTests
   java -jar target/lively-1.0.0.jar
   ```

### Frontend Development Workflow
1. **Frontend Environment Navigation**:
   ```bash
   cd src/main/resources/lively
   ```

2. **Dependency Installation**:
   ```bash
   npm install
   ```

3. **Development Server Initialisation** (with hot reload):
   ```bash
   npm run dev
   ```

4. **Production Artefact Generation** (deploys to Spring Boot's static resources):
   ```bash
   npm run build
   ```

## System Operational Flow
1. **Credential Validation**: Spring Security authentication providers validate user credentials against persisted data
2. **Permission Resolution**: Upon successful authentication, the user's role-based permissions are cached for efficient authorisation
3. **Request Interception**: Each API invocation triggers authorisation validation against the user's permission set
4. **Learning Interface Delivery**: The Vue 3 single-page application provides a responsive, component-based learning environment
5. **Intelligent Intervention**: Learning patterns and proficiency assessments trigger contextual AI assistance
6. **Progress Persistence**: JPA entities synchronise learning states and interaction histories with the persistent data store

## Foundational Technical Decisions
1. **Unified Project Architecture**: Simplified deployment model with frontend resources embedded within the Spring Boot application, eliminating cross-origin complexities
2. **URL-Centric Authorisation Model**: Simplified permission management through direct URL-to-permission mapping, reducing administrative overhead
3. **TypeScript Adoption**: Enhanced frontend reliability through compile-time type checking and improved developer tooling
4. **Repository Abstraction Pattern**: Reduced data access boilerplate through Spring Data JPA's repository interfaces
5. **Composition-Based State Management**: Predictable frontend state transitions through Pinia's reactive store architecture
6. **Build Tool Integration**: Vite's rapid development server coupled with optimised production bundling

## Development Environment Specifications
- Backend services default to port 8080 with configurable alternatives
- Frontend development server utilises port 3000 with proxy configuration to backend services
- Hot module replacement is enabled during frontend development sessions
- Production builds are automatically served from embedded static resources without additional configuration

## Support and Documentation Resources
- Interactive API documentation available at `/swagger-ui.html` when the application is operational
- Frontend component documentation generated through TypeDoc configuration
- Database schema visualisations and entity-relationship diagrams in the documentation directory
- Comprehensive logging configuration with structured JSON output for production environments

---

*Lively harmonises the centuries-old wisdom of Traditional Chinese Medicine with contemporary educational technology, guided by our mischievous demon rabbit companion—a reminder that profound learning is most effective when approached with curiosity and playfulness.*
