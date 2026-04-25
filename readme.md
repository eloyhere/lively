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
lively/                                           # Main project root directory
├── logs/                                         # Application runtime and operational logs
├── src/
│   ├── main/java/pers/eloyhere/lively
│   │   ├── annotation/                           # Custom security annotations for fine-grained access control
│   │   │   ├── Administrator.java                # Annotation restricting endpoint access to users possessing administrative privileges
│   │   │   ├── Authenticated.java                # Annotation mandating valid user authentication for access
│   │   │   ├── Everyone.java                     # Annotation permitting unrestricted public access, bypassing security checks
│   │   │   ├── Guest.java                        # Annotation permitting access to both authenticated and non-authenticated (guest) users
│   │   │   └── Unauthenticated.java              # Annotation restricting access exclusively to non-authenticated requests
│   │   ├── aspect/
│   │   │   ├── BaseAspect.java
│   │   │   ├── ClientAspect.java                 # Aspect-oriented programming (AOP) aspect for client-side cross-cutting concerns
│   │   │   ├── OperationAspect.java
│   │   │   └── VisitAspect.java
│   │   ├── authentication/                       # Complete Spring Security infrastructure, configuration, and custom extensions
│   │   │   ├── entry/                            # Authentication entry point handlers for processing security exceptions
│   │   │   │   └── InvalidateAuthenticationEntryPoint.java # Entry point for handling invalid or expired authentication attempts
│   │   │   ├── filter/                           # Custom filters for authentication and authorisation request processing
│   │   │   │   ├── handler/                      # Handlers for post-authentication results
│   │   │   │   │   ├── LivelyAuthenticationFailureHandler.java # Logic executed upon authentication failure
│   │   │   │   │   └── LivelyAuthenticationSuccessHandler.java # Logic executed upon successful authentication
│   │   │   │   ├── LivelyAuthenticationDetailsSource.java # Provides additional authentication request details
│   │   │   │   └── LivelyUsernamePasswordAuthenticationFilter.java # Custom username/password authentication filter
│   │   │   ├── granter/                          # Custom authentication grant providers for extended authentication mechanisms
│   │   │   │   └── Granter.java                  # Base interface/class for custom authentication granters
│   │   │   ├── provider/                         # Authentication provider implementations
│   │   │   │   └── UsernamePasswordAuthenticationProvider.java # Custom provider for username/password authentication
│   │   │   └── strategy/                         # Strategies for session management and invalidation
│   │   │       └── InvalidateSessionStrategy.java # Defines strategy for invalidating user sessions
│   │   ├── component/                            # Spring-managed application components and beans
│   │   │   └── DataInitializer.java              # Component for initialising database with seed data upon application startup
│   │   ├── configuration/                        # Spring Boot and framework configuration classes
│   │   │   ├── CrossOriginConfiguration.java     # Cross-Origin Resource Sharing (CORS) policy configuration
│   │   │   ├── ResolverConfiguration.java        # Registers custom handler method argument resolvers with Spring MVC
│   │   │   ├── ResourcesConfiguration.java       # Configuration for serving static resources and caching policies
│   │   │   ├── SecurityConfiguration.java        # Core Spring Security framework configuration and rule definitions
│   │   │   ├── WebSocketConfiguration.java       # WebSocket endpoint registration and message broker configuration
│   │   │   └── Vue3Configuration.java           # Frontend integration configuration for Vue 3, including routing and history mode fallback
│   │   ├── controller/                           # RESTful API endpoint controllers (MVC)
│   │   │   ├── book/                             # Traditional Chinese Medicine (TCM) literature management endpoints
│   │   │   │   ├── BookController.java
│   │   │   │   └── ChapterController.java
│   │   │   ├── chat/                             # AI-powered conversational chat and messaging interface endpoints
│   │   │   │   ├── ChatController.java
│   │   │   │   └── MessageController.java
│   │   │   ├── consumer/                         # User, role, permission, and system management endpoints
│   │   │   │   ├── AuthorityController.java
│   │   │   │   ├── ConsumerController.java
│   │   │   │   ├── InvitationController.java
│   │   │   │   ├── MenuControllerController.java
│   │   │   │   ├── RoleControllerController.java
│   │   │   │   ├── RouteControllerController.java
│   │   │   │   └── TokenControllerController.java
│   │   │   ├── AnnouncementController.java       # Endpoints for publishing and managing system-wide announcements
│   │   │   ├── AuthenticationController.java    # Orchestrates authentication flows (login, logout, registration)
│   │   │   ├── BaseController.java               # Common controller utilities, constants, and base class for other controllers
│   │   │   └── ResourcesController.java         # Controller for serving application resources
│   │   ├── converter/                            # Custom type converters for Spring MVC data binding
│   │   │   └── StringBlobConverter.java          # Converter utilities for String to SQL BLOB type conversion
│   │   ├── entity/                               # JPA entity class definitions representing the domain model
│   │   │   ├── book/                             # TCM literature domain entities
│   │   │   │   ├── Book.java
│   │   │   │   └── Chapter.java
│   │   │   ├── chat/                             # Conversation and AI interaction domain entities
│   │   │   │   ├── Chat.java
│   │   │   │   └── Message.java
│   │   │   ├── consumer/                         # User, authentication, and authorisation domain entities
│   │   │   │   ├── Authority.java
│   │   │   │   ├── Consumer.java
│   │   │   │   ├── Invitation.java
│   │   │   │   ├── Menu.java
│   │   │   │   ├── Role.java
│   │   │   │   ├── Route.java
│   │   │   │   └── Token.java
│   │   │   ├── Announcement.java                 # Entity for persisting system announcements
│   │   │   └── BaseEntity.java                   # Abstract base entity defining common properties (ID, timestamps) and behaviour
│   │   ├── projection/                           # Spring Data JPA projection interfaces for repository queries
│   │   │   └── BaseProjection.java               # Common base interface for projections
│   │   ├── repository/                           # Spring Data JPA repository interfaces for data persistence
│   │   │   ├── book/                             # Data access interfaces for TCM literature entities
│   │   │   │   ├── BookRepository.java
│   │   │   │   └── ChapterRepository.java
│   │   │   ├── chat/                             # Data access interfaces for conversation entities
│   │   │   │   ├── ChatRepository.java
│   │   │   │   └── MessageRepository.java
│   │   │   ├── consumer/                         # Data access interfaces for user and permission entities
│   │   │   │   ├── AuthorityRepository.java
│   │   │   │   ├── ConsumerRepository.java
│   │   │   │   ├── InvitationRepository.java
│   │   │   │   ├── MenuRepository.java
│   │   │   │   ├── RoleRepository.java
│   │   │   │   ├── RouteRepository.java
│   │   │   │   └── TokenRepository.java
│   │   │   ├── AnnouncementRepository.java
│   │   │   └── BaseRepository.java               # Common repository operations and base interface with generic types
│   │   ├── resolver/                             # Custom Spring MVC handler method argument resolvers
│   │   │   ├── EntityArgumentResolver.java       # Resolves and injects entity instances directly from request parameters
│   │   │   └── UUIDArgumentResolver.java         # Resolves, converts, and validates UUID parameters from requests
│   │   ├── service/                              # Business logic and service layer implementations
│   │   │   ├── authentication/                   # Authentication-related business services
│   │   │   │   └── LivelyPersistentTokenBasedRememberMeServices.java # Custom "remember-me" authentication service
│   │   │   ├── book/                             # Business logic services for TCM literature management
│   │   │   │   ├── BookService.java
│   │   │   │   └── ChapterService.java
│   │   │   ├── chat/                             # Business logic services for conversation and message management
│   │   │   │   ├── ChatService.java
│   │   │   │   └── MessageService.java
│   │   │   ├── consumer/                         # Business logic services for user, role, and permission management
│   │   │   │   ├── AuthorityService.java
│   │   │   │   ├── ConsumerService.java
│   │   │   │   ├── InvitationService.java
│   │   │   │   ├── MenuService.java
│   │   │   │   ├── RoleService.java
│   │   │   │   ├── RouteService.java
│   │   │   │   └── TokenService.java
│   │   │   ├── AnnouncementService.java
│   │   │   └── BaseService.java                  # Abstract base service providing common CRUD operations
│   │   └── LivelyApplication.java                # Main Spring Boot application entry point and bootstrap class
│   ├── resources/
│   │   ├── lively/                               # Integrated Vue 3 single-page application (SPA) frontend project
│   │   │   ├── public/                           # Static assets served directly without Vite processing
│   │   │   ├── src/
│   │   │   │   ├── component/
│   │   │   │   │   ├── AvatarCard.vue            # User avatar display and dropdown menu component
│   │   │   │   │   ├── Modulize.vue              # Abstract, reusable component for CRUD operations with integrated search, table, and forms
│   │   │   │   │   └── Toolbar.vue               # Reusable toolbar component with common actions
│   │   │   │   ├── declaration/                  # TypeScript type and interface declarations
│   │   │   │   │   ├── component.ts
│   │   │   │   │   ├── entity.ts                 # Type declarations corresponding to backend JPA entities
│   │   │   │   │   ├── modulize.ts               # Type declarations for the abstract CRUD component
│   │   │   │   │   └── serialization.ts          # Type declarations for data serialisation and deserialisation
│   │   │   │   ├── hooks/                        # Vue 3 Composition API custom hooks (composables)
│   │   │   │   │   ├── datetime.ts               # Date and time formatting and manipulation utilities
│   │   │   │   │   ├── network.ts                # HTTP client functions abstracting GET, POST, PUT, DELETE requests
│   │   │   │   │   ├── picture.ts                # Image conversion, compression, and manipulation utilities
│   │   │   │   │   ├── serialization.ts          # JSON serialisation and deserialisation utilities
│   │   │   │   │   ├── url.ts                    # URL construction, parsing, and manipulation utilities
│   │   │   │   │   └── utility.ts                # General-purpose utility and helper functions
│   │   │   │   ├── interaction/                  # Frontend data interaction and service abstraction layer
│   │   │   │   │   ├── serialization.ts          # Serialisation/deserialisation logic and TypeScript interface definitions for DTOs
│   │   │   │   │   └── service.ts                # Frontend service abstractions for communicating with backend APIs
│   │   │   │   ├── plugins/                      # Vue plugin initialisation and configuration
│   │   │   │   │   └── element.ts                # Configuration and setup for the Element Plus UI component library
│   │   │   │   ├── router/                       # Vue Router configuration for frontend routing
│   │   │   │   │   └── index.ts                  # Route definitions, navigation guards, and router instance
│   │   │   │   ├── stores/                       # Pinia state management stores
│   │   │   │   │   ├── authentication.ts         # Store for managing user authentication state, tokens, and actions
│   │   │   │   │   └── counter.ts                # Example demonstration store (may be replaced or removed)
│   │   │   │   ├── views/                        # Page-level Vue components (routes)
│   │   │   │   │   ├── authentication/           # User authentication and account management views
│   │   │   │   │   │   ├── Account.vue           # User account management and settings view
│   │   │   │   │   │   └── Profile.vue           # User profile viewing and editing interface
│   │   │   │   │   ├── management/               # Administrative and system management views
│   │   │   │   │   │   ├── book/                 # TCM literature management interface
│   │   │   │   │   │   │   ├── Book.vue
│   │   │   │   │   │   │   └── Chapter.vue
│   │   │   │   │   │   ├── chat/                 # Conversation history and management interface
│   │   │   │   │   │   │   ├── Chat.vue
│   │   │   │   │   │   │   └── Message.vue
│   │   │   │   │   │   ├── consumer/             # User, role, and system configuration management interface
│   │   │   │   │   │   │   ├── Authority.vue
│   │   │   │   │   │   │   ├── Consumer.vue
│   │   │   │   │   │   │   ├── Invitation.vue
│   │   │   │   │   │   │   ├── Menu.vue
│   │   │   │   │   │   │   ├── Role.vue
│   │   │   │   │   │   │   ├── Route.vue
│   │   │   │   │   │   │   └── Token.vue
│   │   │   │   │   │   ├── Announcement.vue      # Interface for viewing and managing system announcements
│   │   │   │   │   │   ├── Index.vue             # Index or landing page for the management section
│   │   │   │   │   │   ├── Online.vue            # View for monitoring user online status and session logs
│   │   │   │   │   │   └── Operation.vue         # View for inspecting system operation and audit logs
│   │   │   │   │   ├── Authentication.vue        # Login and user registration page
│   │   │   │   │   ├── Home.vue                  # Main application dashboard post-authentication
│   │   │   │   │   └── Management.vue            # Central administrative dashboard
│   │   │   │   ├── App.vue                       # Root Vue application component and layout wrapper
│   │   │   │   ├── main.ts                       # Frontend application entry point, mounting the Vue app
│   │   │   │   └── style.css                     # Global CSS styles and theming
│   │   │   ├── .gitignore                        # Frontend-specific Git version control exclusions
│   │   │   ├── env.d.ts                          # TypeScript ambient declarations for Vite environment variables
│   │   │   ├── index.html                        # Primary HTML entry point for the Vue 3 application
│   │   │   ├── package.json                      # NPM project configuration, dependencies, and scripts
│   │   │   ├── package-lock.json                 # Dependency lock file ensuring reproducible installations
│   │   │   ├── README.md                         # Frontend project-specific documentation and guides
│   │   │   ├── tsconfig.app.json                 # TypeScript configuration for the application source code
│   │   │   ├── tsconfig.json                     # Base TypeScript configuration file
│   │   │   ├── tsconfig.node.json                # TypeScript configuration for Node.js tooling and build scripts
│   │   │   └── vite.config.ts                    # Vite build tool configuration for development and production
│   │   ├── static/                               # Spring Boot static resources directory (served from root '/')
│   │   │   ├── assets/                           # Compiled frontend assets (CSS, JS) generated by the build process
│   │   │   ├── background.jpeg                   # Background image asset for the application
│   │   │   ├── Close Eyes.png                    # Mascot or branding image asset
│   │   │   ├── favicon.ico                       # Browser favicon
│   │   │   ├── index.html                        # Fallback HTML entry point (typically for SPA routing)
│   │   │   └── smile.png                         # Additional mascot or branding image asset
│   │   └── application.properties                # Primary Spring Boot application configuration (datasource, server, etc.)
│   └── test/java/pers/eloyhere/lively/
│       └── LivelyApplicationTests.java           # Primary integration and unit test suite for the application
├── .gitattributes                                # Git file handling directives (e.g., line endings, merge strategies)
├── .gitignore                                    # Version control exclusions for generated files, dependencies, and local environments
├── mvnw                                          # Maven wrapper executable script for Unix-based systems (Linux/macOS)
├── mvnw.cmd                                      # Maven wrapper executable script for Windows systems
├── pom.xml                                       # Maven Project Object Model (POM) file defining dependencies, build, and project metadata
└── readme.md                                     # Comprehensive project overview, setup instructions, and usage documentation
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
