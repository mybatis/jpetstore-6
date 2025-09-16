#!/bin/bash
# Build script for JPetStore modernization project

echo "========================================="
echo "Building JPetStore Modernization Project"
echo "========================================="

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Function to check if command was successful
check_status() {
    if [ $? -eq 0 ]; then
        echo -e "${GREEN}✓ $1 successful${NC}"
    else
        echo -e "${RED}✗ $1 failed${NC}"
        exit 1
    fi
}

# Build Spring Boot API
echo ""
echo -e "${YELLOW}Building Spring Boot API...${NC}"
cd jpetstore-api
mvn clean package -DskipTests
check_status "Spring Boot API build"
cd ..

# Build Next.js Frontend
echo ""
echo -e "${YELLOW}Building Next.js Frontend...${NC}"
cd jpetstore-frontend
npm install
check_status "Next.js dependencies installation"
npm run build
check_status "Next.js build"
cd ..

# Build Legacy App (if needed)
echo ""
echo -e "${YELLOW}Building Legacy App...${NC}"
mvn clean package -DskipTests
check_status "Legacy app build"

echo ""
echo -e "${GREEN}=========================================${NC}"
echo -e "${GREEN}All services built successfully!${NC}"
echo -e "${GREEN}=========================================${NC}"
echo ""
echo "To run the services:"
echo "  1. Legacy app:     mvn cargo:run -Dcargo.servlet.port=8080"
echo "  2. Spring Boot API: cd jpetstore-api && mvn spring-boot:run"
echo "  3. Next.js app:     cd jpetstore-frontend && npm run dev"
