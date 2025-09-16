# JPetStore Development Setup Guide

## Project Structure

This project consists of three main components:

1. **Legacy JPetStore** (`/src`) - Original MyBatis/Spring MVC application
2. **Modern API** (`/jpetstore-api`) - Spring Boot REST API with hot reload
3. **Modern Frontend** (`/jpetstore-frontend`) - Next.js application with hot reload

## Prerequisites

- Java 21
- Node.js 18+ and npm
- Maven 3.8+

## Quick Start

### Option 1: Run Everything with One Command

```bash
# Run the provided script
./run-dev.sh
```

### Option 2: Run Components Individually

#### 1. Backend API (Spring Boot)
```bash
cd jpetstore-api
./mvnw spring-boot:run
```
- API will run on: http://localhost:8080
- H2 Console: http://localhost:8080/h2-console
- Hot reload is enabled via Spring DevTools

#### 2. Frontend (Next.js)
```bash
cd jpetstore-frontend
npm install  # First time only
npm run dev
```
- Frontend will run on: http://localhost:3000
- Hot reload is enabled via Next.js Fast Refresh

#### 3. Legacy Application (Optional)
```bash
# From root directory
./mvnw clean package cargo:run
```
- Legacy app will run on: http://localhost:8080/jpetstore

## Development Features

### Backend Hot Reload (Spring Boot DevTools)
- **Automatic Restart**: The application restarts when classpath files change
- **LiveReload**: Browser refresh when resources change
- **Property Defaults**: Development-friendly defaults

### Frontend Hot Reload (Next.js)
- **Fast Refresh**: Instant feedback for React component changes
- **Turbopack**: Faster builds and hot module replacement
- **Error Overlay**: Development error display

## API Endpoints

### Base URL: http://localhost:8080/api

- `GET /api/categories` - List all categories
- `GET /api/categories/{id}` - Get category by ID
- `GET /api/products` - List all products
- `GET /api/products/{id}` - Get product by ID
- `GET /api/items` - List all items
- `GET /api/items/{id}` - Get item by ID

## Database Access

### H2 Console
- URL: http://localhost:8080/h2-console
- JDBC URL: `jdbc:h2:mem:testdb`
- Username: `sa`
- Password: (leave empty)

## Troubleshooting

### Port Already in Use
If ports 8080 or 3000 are already in use:

```bash
# Kill process on port 8080
lsof -ti:8080 | xargs kill -9

# Kill process on port 3000
lsof -ti:3000 | xargs kill -9
```

### Maven Build Issues
```bash
# Clean and rebuild
cd jpetstore-api
./mvnw clean install
```

### Node Module Issues
```bash
# Clean install
cd jpetstore-frontend
rm -rf node_modules package-lock.json
npm install
```

## Development Workflow

1. **Make Backend Changes**
   - Edit Java files in `jpetstore-api/src`
   - Spring DevTools will auto-restart the application
   - Check console for compilation errors

2. **Make Frontend Changes**
   - Edit React/TypeScript files in `jpetstore-frontend/src`
   - Next.js will hot reload the changes
   - Check browser console for errors

3. **Test Integration**
   - Frontend calls backend API endpoints
   - Use browser DevTools Network tab to debug API calls
   - Check both frontend and backend logs

## VS Code Extensions Recommended

- Java Extension Pack
- Spring Boot Extension Pack
- ES7+ React/Redux/React-Native snippets
- Tailwind CSS IntelliSense
- Prettier - Code formatter

## Environment Variables

### Backend (.env or application.yml)
```yaml
server:
  port: 8080
spring:
  h2:
    console:
      enabled: true
  datasource:
    url: jdbc:h2:mem:testdb
```

### Frontend (.env.local)
```
NEXT_PUBLIC_API_URL=http://localhost:8080/api
```

## Build for Production

### Backend
```bash
cd jpetstore-api
./mvnw clean package
java -jar target/jpetstore-api-1.0.0-SNAPSHOT.jar
```

### Frontend
```bash
cd jpetstore-frontend
npm run build
npm start
```

## Docker Support

Use the provided `docker-compose.yaml` for containerized development:

```bash
docker-compose up
```

This will start both the backend and frontend with proper networking.
