#!/bin/bash

echo "Building JPetStore Modern Architecture..."

# Build the Spring Boot API
echo "Building Spring Boot API..."
cd jpetstore-api
./mvnw clean package -DskipTests
cd ..

# Build the Next.js Frontend
echo "Building Next.js Frontend..."
cd jpetstore-frontend
npm install
npm run build
cd ..

echo "Build completed successfully!"
echo ""
echo "To run the applications:"
echo "1. Start the API: cd jpetstore-api && java -jar target/jpetstore-api-1.0.0-SNAPSHOT.jar"
echo "2. Start the Frontend: cd jpetstore-frontend && npm start"
echo ""
echo "Or use Docker:"
echo "docker-compose up -d"