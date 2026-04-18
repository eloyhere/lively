# Lively: AI-Powered TCM Learning System

## Overview
Lively is a comprehensive Traditional Chinese Medicine (TCM) learning platform inspired by gamified language learning methodologies. The system integrates intelligent AI assistance with structured learning pathways, enabling users to engage with TCM literature, track their progress, and receive personalised learning recommendations. Our mascot, a black-furred demon rabbit with an arrow-shaped tail, symbolises the system's dynamic and spirited approach to knowledge acquisition.

## Architecture Overview
The system follows a monolithic application structure with clear separation between backend and frontend components:

### Backend Architecture (Java)
- **Runtime Environment**: JDK 17
- **Core Framework**: Spring Boot
- **Security Layer**: Spring Security with URL-based permission model
- **Data Persistence**: Spring Data JPA with MySQL
- **Database**: `lively` schema

### Frontend Architecture
- **Framework**: Vue 3 with TypeScript
- **State Management**: Pinia
- **Build Integration**: Compiled files reside in `resources/static` directory

### Project Structure
```
lively/
├── backend/ (Java Spring Boot)
│   ├── src/main/resources/
│   │   ├── static/ (compiled frontend assets)
│   │   └── vue-project/ (source Vue 3 project)
└── database/
    └── lively.sql
```

## Core Features

### Intelligent Learning System
- **Duolingo-inspired Learning Paths**: Progressive difficulty levels with spaced repetition algorithms
- **Book Upload Functionality**: Support for multiple TCM text formats with OCR processing
- **Dynamic Content Discovery**: "Guess You'll Like" recommendation engine
- **Personalised Dashboards**: Learning analytics and progress tracking

### AI-Powered Assistance
- **Conversational AI Interface**: Natural language interaction for concept clarification
- **Proactive Learning Reminders**: Context-aware study notifications
- **Knowledge Structure Visualisation**: Automated generation of:
    - Mind maps for conceptual relationships
    - Flowcharts for procedural knowledge
    - Learning step sequences based on proficiency assessment

## Security Model
The system implements a robust permission-based access control system:

### Permission Structure
```
User → (has many) → Roles → (have many) → Permissions
```

### Authorisation Mechanism
- Each backend endpoint URL represents a discrete permission
- Roles aggregate related permissions for logical grouping
- Authentication requires validation of URL-associated permissions only
- Spring Security interceptors validate requests against user's permission set

## Database Configuration
- **Database Name**: `lively`
- **Tables Include**:
    - `users`: User profiles and credentials
    - `roles`: Permission groupings
    - `permissions`: URL-access mappings
    - `learning_materials`: Uploaded TCM resources
    - `progress_tracking`: User learning metrics
    - `ai_interactions`: Conversation histories
    - `knowledge_graphs`: Generated visualisations

## Installation & Execution

### Prerequisites
- JDK 17 or later
- MySQL 8.0+
- Node.js 18+ (for frontend development)
- Maven 3.8+

### Backend Setup
1. **Database Initialisation**:
   ```sql
   CREATE DATABASE lively CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
   ```

2. **Configuration**:
   Update `application.properties`:
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/lively
   spring.datasource.username=your_username
   spring.datasource.password=your_password
   ```

3. **Build & Launch**:
   ```bash
   mvn clean package
   java -jar target/lively-1.0.0.jar
   ```

### Frontend Development
1. **Navigate to frontend directory**:
   ```bash
   cd src/main/resources/vue-project
   ```

2. **Install dependencies**:
   ```bash
   npm install
   ```

3. **Development mode**:
   ```bash
   npm run serve
   ```

4. **Production build** (deploys to `resources/static`):
   ```bash
   npm run build
   ```

## System Flow
1. **User Authentication**: Spring Security validates credentials
2. **Permission Resolution**: User's role-based permissions are cached
3. **Request Interception**: Each API call is validated against permission set
4. **Learning Interface**: Vue 3 SPA provides responsive learning environment
5. **AI Integration**: Learning patterns trigger intelligent interventions
6. **Progress Sync**: JPA entities persist learning states

## Key Technical Decisions
1. **Unified Project Structure**: Simplified deployment with embedded frontend resources
2. **URL-Centric Permissions**: Simplified authorisation logic with direct URL-to-permission mapping
3. **TypeScript Adoption**: Enhanced frontend reliability with type-safe Vue 3 components
4. **JPA Repositories**: Reduced boilerplate with Spring Data's repository abstraction
5. **Pinia State Management**: Predictable frontend state transitions

## Development Notes
- Backend services run on port 8080 by default
- Frontend development server uses port 3000 with proxy to backend
- Hot-reload is available during frontend development
- Production builds are automatically served from embedded static resources

## Support & Documentation
- API documentation available at `/swagger-ui.html` when running
- Frontend component documentation generated via TypeDoc
- Database schema visualisation in `/docs/database-diagram.pdf`

---

*Lively combines centuries-old TCM wisdom with modern learning technology, guided by our mischievous demon rabbit companion who reminds us that learning should be both profound and playful.*