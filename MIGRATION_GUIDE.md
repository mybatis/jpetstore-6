# JPetStore Migration Guide

## Overview
This guide explains how to migrate from the legacy JPetStore Java 17 application to the modern architecture with separate API and frontend services.

## Architecture Changes

### Before (Legacy)
- Monolithic Java web application using Stripes framework
- JSP-based frontend
- MyBatis for data access
- Single WAR deployment

### After (Modern)
- **jpetstore-api**: Spring Boot REST API (Java 21)
- **jpetstore-frontend**: Next.js React application (TypeScript)
- Microservices architecture
- Modern development practices

## Migration Steps

### 1. Build the Applications

```bash
# Build both API and frontend
./build-all.sh

# Or build individually:
cd jpetstore-api && ./mvnw clean package
cd jpetstore-frontend && npm install && npm run build
```

### 2. Run the Applications

#### Option A: Run Locally
```bash
# Terminal 1: Start the API
cd jpetstore-api
java -jar target/jpetstore-api-1.0.0-SNAPSHOT.jar

# Terminal 2: Start the frontend
cd jpetstore-frontend
npm start
```

#### Option B: Use Docker
```bash
# Run modern stack
docker-compose up jpetstore-api jpetstore-frontend

# Run legacy app (for comparison)
docker-compose up jpetstore-legacy

# Run all services
docker-compose up
```

### 3. Access the Applications

- **Modern Frontend**: http://localhost:3000
- **Modern API**: http://localhost:8081/api
- **Legacy App**: http://localhost:8080/jpetstore
- **API Documentation**: http://localhost:8081/api/catalog/categories

## Key Differences

### Data Access
- **Legacy**: MyBatis with XML mappers
- **Modern**: Spring Data JPA with repositories

### Frontend
- **Legacy**: JSP pages with server-side rendering
- **Modern**: React SPA with client-side rendering

### API
- **Legacy**: Stripes actions returning JSP views
- **Modern**: REST endpoints returning JSON

### Database
- **Legacy**: HSQLDB with custom schema
- **Modern**: H2 in-memory database with JPA entities

## API Endpoints

### Categories
- `GET /api/catalog/categories` - List all categories
- `GET /api/catalog/categories/{id}` - Get category by ID
- `GET /api/catalog/categories/{id}/products` - Get products in category

### Products
- `GET /api/catalog/products/{id}` - Get product by ID
- `GET /api/catalog/products/search?keyword={keyword}` - Search products
- `GET /api/catalog/products/{id}/items` - Get items for product

### Items
- `GET /api/catalog/items/{id}` - Get item by ID

## Development Workflow

### API Development
```bash
cd jpetstore-api
./mvnw spring-boot:run
```

### Frontend Development
```bash
cd jpetstore-frontend
npm run dev
```

## Testing

### API Testing
```bash
# Test categories endpoint
curl http://localhost:8081/api/catalog/categories

# Test search
curl "http://localhost:8081/api/catalog/products/search?keyword=dog"
```

### Frontend Testing
- Open http://localhost:3000
- Click on categories to browse products
- Verify API integration works

## Migration Benefits

1. **Separation of Concerns**: API and frontend are independent
2. **Modern Tech Stack**: Spring Boot 3.x, React 19, Java 21
3. **Better Developer Experience**: Hot reload, TypeScript, modern tooling
4. **Scalability**: Services can be scaled independently
5. **API-First**: RESTful API can be consumed by multiple clients

## Next Steps

1. Add authentication and user management
2. Implement shopping cart functionality
3. Add order management
4. Implement proper error handling
5. Add comprehensive testing
6. Set up CI/CD pipelines
7. Add monitoring and logging

## Troubleshooting

### Common Issues

1. **CORS Errors**: Ensure API CORS configuration allows frontend origin
2. **Database Issues**: Check H2 console at http://localhost:8081/api/h2-console
3. **Port Conflicts**: Ensure ports 3000, 8080, 8081 are available

### Logs
- API logs: Check Spring Boot console output
- Frontend logs: Check browser developer console
- Database: Access H2 console for SQL debugging