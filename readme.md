# Lively: An Intelligent Traditional Chinese Medicine Learning System

## System Overview
Lively is a comprehensive Traditional Chinese Medicine (TCM) learning platform that integrates gamified pedagogy with AI-assisted tutoring. The system facilitates the structured study of TCM literature through personalised learning pathways, progress tracking, and an adaptive recommendation system. Our mascot—a black-haired, arrow-tailed demon rabbit named "Fuzi"—embodies the platform’s vibrant and agile approach to knowledge acquisition, representing both the traditional wisdom of TCM and the innovative application of modern educational technology.
Fuzi is not only the name of a classic TCM herb but also encapsulates our system’s philosophy of "adding fun to learning." This mischievous demon rabbit guides users to maintain curiosity and an exploratory spirit within the rigorous study of TCM.

## Architecture Overview
The system employs a monolithic application architecture, maintaining a clear separation of concerns between backend services and the frontend presentation layer while leveraging the deployment advantages of a unified codebase.

### Backend Architecture
-   **Runtime Environment:** Java Development Kit 17
-   **Core Framework:** Spring Boot 3.x for application scaffolding and dependency management
-   **Security Layer:** Spring Security 6.x implementing a URL-based authorisation model
-   **Data Persistence:** Spring Data JPA with Hibernate as the ORM provider
-   **Relational Database:** MySQL 8.0+ utilising the `lively` schema

### Frontend Architecture
-   **Component Framework:** Vue 3 with the Composition API and TypeScript for type-safe development
-   **State Management:** Pinia stores for predictable state transitions
-   **Build System:** Vite for rapid development and optimised production builds
-   **Integration Pattern:** Compiled frontend assets are served from the Spring Boot application's resource directory, enabling seamless deployment.

### Complete Project Structure Analysis
```
lively/
├── logs/                                    # Application runtime logs
├── src/
│   ├── main/java/pers/eloyhere/lively
│   │   ├── annotation/                      # Custom security annotations
│   │   │   ├── Administrator.java           # Restricts access to admin endpoints
│   │   │   ├── Authenticated.java           # Requires valid authentication credentials
│   │   │   ├── Everyone.java                # Allows unrestricted public access
│   │   │   ├── Guest.java                   # Permits both authenticated and non-authenticated access
│   │   │   └── Unauthenticated.java         # Only allows non-authenticated requests
│   │   ├── authentication/                  # Complete security infrastructure
│   │   │   ├── entry/                       # Authentication entry point handlers
│   │   │   │   └── InvalidateAuthenticationEntryPoint.java
│   │   │   ├── filter/                      # Request filtering components
│   │   │   │   ├── handler/                 # Authentication result handlers
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
│   │   ├── component/                       # Application components
│   │   │   └── DataInitializer.java         # Database initialisation and data seeding
│   │   ├── configuration/                   # Spring configuration classes
│   │   │   ├── CrossOriginConfiguration.java # CORS policy configuration
│   │   │   ├── ResolverConfiguration.java   # Custom argument resolver registration
│   │   │   ├── ResourcesConfiguration.java  # Static resource handling
│   │   │   ├── SecurityConfiguration.java   # Core security framework configuration
│   │   │   └── Vue3Configuration.java       # Frontend integration and routing configuration
│   │   ├── controller/                      # REST API endpoint controllers
│   │   │   ├── book/                        # TCM literature management endpoints
│   │   │   ├── chat/                        # AI conversation interface endpoints
│   │   │   ├── consumer/                    # User management and authentication endpoints
│   │   │   ├── AnnouncementController.java  # System announcement publishing
│   │   │   ├── AuthenticationController.java # Authentication flow orchestration
│   │   │   └── BaseController.java          # Common controller utilities
│   │   ├── converter/                       # Custom type converters
│   │   │   └── StringBlobConverter.java     # String to BLOB conversion utilities
│   │   ├── entity/                          # JPA entity definitions
│   │   │   ├── book/                        # TCM literature domain entities
│   │   │   ├── chat/                        # Conversation and AI interaction entities
│   │   │   ├── consumer/                    # User and permission domain entities
│   │   │   ├── Announcement.java            # System announcement persistence model
│   │   │   └── BaseEntity.java              # Common entity properties and behaviour
│   │   ├── projection/                      # Spring Data projections
│   │   │   └── BaseProjection.java          # Common projection interface
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
│   │   │   ├── EntityArgumentResolver.java  # Resolves entities from request parameters
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
│   │   └── LivelyApplication.java           # Main application entry point
│   ├── resources/
│   │   ├── lively/                          # Integrated Vue 3 frontend project
│   │   │   ├── public/                      # Static assets not processed by Vite
│   │   │   ├── src/
│   │   │   │   ├── hooks/                   # Vue 3 composition functions
│   │   │   │   │   ├── entity.ts            # Entity interaction utilities
│   │   │   │   │   ├── network.ts           # HTTP client abstraction
│   │   │   │   │   ├── picture.ts           # Image processing utilities
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
│   │   │   │   │   ├── management/          # Management interfaces
│   │   │   │   │   │   ├── book/            # TCM literature management
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
│   │   │   │   │   ├── Home.vue             # Main dashboard
│   │   │   │   │   └── Management.vue       # Management dashboard
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
│   │   │   ├── background.jpeg              # Background image
│   │   │   ├── Close Eyes.png               # Mascot visual assets
│   │   │   ├── favicon.ico                  # Browser icon
│   │   │   ├── index.html                   # Fallback HTML entry point
│   │   │   └── smile.png                    # Additional mascot image
│   │   └── application.properties           # Spring Boot configuration
│   └── test/java/pers/eloyhere/lively/
│       └── LivelyApplicationTests.java      # Primary test suite
├── .gitattributes                           # Git file handling directives
├── .gitignore                              # Version control exclusions
├── mvnw                                    # Maven wrapper for Unix systems
├── mvnw.cmd                                # Maven wrapper for Windows systems
├── pom.xml                                 # Maven project configuration
└── readme.md                               # Project overview documentation
```

## Core Pedagogical Features

### Intelligent Learning System
-   **Gamified Learning Pathways:** A progressive difficulty system inspired by language learning platforms, implementing spaced repetition algorithms to optimise knowledge retention.
-   **Multi-format Text Integration:** Comprehensive TCM literature support, including OCR functionality for scanned manuscripts.
-   **Adaptive Recommendation Engine:** A "You Might Like" system employing collaborative filtering and content-based algorithms.
-   **Personalised Analytics Dashboard:** Comprehensive learning metrics with visualised progress tracking and competency assessment.

### AI-Powered Cognitive Assistance
-   **Conversational Learning Interface:** Natural language processing for contextual concept clarification and Socratic dialogue.
-   **Active Engagement System:** Context-aware learning reminders based on study patterns and knowledge decay curves.
-   **Knowledge Structure Visualisation:** Automated generation of cognitive artefacts:
    -   Conceptual mind maps illustrating theoretical relationships.
    -   Procedural flowcharts for diagnosis and treatment pathways.
    -   Personalised learning sequences based on ongoing competency assessment.

## Security Architecture
The system implements a refined, tiered authorisation model that balances fine-grained control with administrative simplicity.

### Permission Hierarchy
```
User → (Assigned to) → Role → (Grants access to) → Authority
```

### Authorisation Mechanism
-   Each backend endpoint URL constitutes a discrete authority entity.
-   Roles aggregate related authorities into logical administrative groups.
-   The authentication process validates credentials against a persistent user repository.
-   An authorisation interceptor validates each request against the user's aggregated set of authorities.
-   The authority-URL mapping eliminates ambiguity in access control decisions.

## Database Schema Configuration
-   **Primary Database:** The `lively` schema, using UTF8MB4 character encoding.
-   **Core Entity Tables:**
    -   `consumers`: User profiles containing authentication credentials.
    -   `roles`: Permission-grouping entities with hierarchical relationships.
    -   `authorities`: URL-permission mappings with HTTP method specifications.
    -   `books` and `chapters`: TCM literature repositories with metadata.
    -   `learning_progress`: Time-series learning metrics and competency tracking.
    -   `chat_sessions` and `messages`: AI interaction history with contextual metadata.
    -   `knowledge_graphs`: Persisted visual structure and relationship mappings.

## Installation and Deployment Workflow

### Prerequisite Requirements
-   Java Development Kit 17 or later
-   MySQL Community Edition 8.0+, utilising the InnoDB storage engine
-   Node.js 18+ LTS for frontend dependency management
-   Apache Maven 3.8+ for Java project orchestration

### Backend Initialisation Sequence
1.  **Database Schema Creation:**
    ```sql
    CREATE DATABASE lively
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;
    ```
2.  **Application Configuration:**
    Modify `application.properties` with environment-specific values:
    ```properties
    spring.datasource.url=jdbc:mysql://localhost:3306/lively?useSSL=false&serverTimezone=UTC
    spring.datasource.username=deployment_user
    spring.datasource.password=secure_credentials
    spring.jpa.hibernate.ddl-auto=validate
    ```
3.  **Application Packaging and Execution:**
    ```bash
    mvn clean package -DskipTests
    java -jar target/lively-1.0.0.jar
    ```

### Frontend Development Workflow
1.  **Navigate to Frontend Environment:**
    ```bash
    cd src/main/resources/lively
    ```
2.  **Dependency Installation:**
    ```bash
    npm install
    ```
3.  **Development Server Initialisation** (with hot-reload support):
    ```bash
    npm run dev
    ```
4.  **Production Artefact Generation** (for deployment to Spring Boot static resources):
    ```bash
    npm run build
    ```

## System Operational Flow
1.  **Credential Verification:** Spring Security authentication providers validate user credentials against persistent data.
2.  **Permission Resolution:** Upon successful authentication, the user's role-based authorities are cached for efficient authorisation.
3.  **Request Interception:** Each API call triggers authorisation validation against the user's authority set.
4.  **Learning Interface Delivery:** The Vue 3 single-page application provides a reactive, component-based learning environment.
5.  **Intelligent Intervention:** Learning patterns and competency assessments trigger context-relevant AI assistance.
6.  **Progress Persistence:** JPA entities synchronise learning states and interaction histories with the persistent data store.

## Foundational Technical Decisions
1.  **Unified Project Architecture:** A simplified deployment model with frontend resources embedded within the Spring Boot application, eliminating cross-origin complexity.
2.  **URL-Centric Authorisation Model:** Simplified permission management through direct URL-authority mapping, reducing administrative overhead.
3.  **TypeScript Adoption:** Enhanced frontend reliability via compile-time type checking and improved developer tooling.
4.  **Repository Abstraction Pattern:** Reduced boilerplate code for data access via Spring Data JPA repository interfaces.
5.  **Composition-Based State Management:** Predictable frontend state transitions via Pinia's reactive store architecture.
6.  **Build Tool Integration:** Vite's rapid development server combined with optimised production bundling.

## Development Environment Specifications
-   Backend service defaults to port 8080, with configurable alternative ports supported.
-   Frontend development server uses port 3000, configured to proxy to the backend service.
-   Hot Module Replacement is enabled during frontend development sessions.
-   Production builds are automatically served from embedded static resources without additional configuration.

## Support and Documentation Resources
-   Interactive API documentation is available at `/swagger-ui.html` during application runtime.
-   Frontend component documentation is generated via TypeDoc configuration.
-   Database schema visualisations and entity-relationship diagrams are included in the documentation directory.
-   Comprehensive logging configuration for production environments, with structured JSON output.

---

*Lively merges the millennia-old wisdom of Traditional Chinese Medicine with modern educational technology, guided by our mischievous demon rabbit companion Fuzi—who reminds us that profound learning is most effective when approached with curiosity and a sense of fun.*
