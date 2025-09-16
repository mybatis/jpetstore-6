#!/bin/bash

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

echo "========================================="
echo -e "${BLUE}JPetStore Development Status Check${NC}"
echo "========================================="
echo ""

# Check Backend (Port 8080)
echo -e "${YELLOW}Backend API Status:${NC}"
if lsof -Pi :8080 -sTCP:LISTEN -t >/dev/null 2>&1; then
    echo -e "${GREEN}✓ Spring Boot API is running on port 8080${NC}"
    echo "  - API URL: http://localhost:8080/api"
    echo "  - H2 Console: http://localhost:8080/h2-console"
else
    echo -e "${RED}✗ Spring Boot API is not running${NC}"
    echo "  To start: cd jpetstore-api && ./mvnw spring-boot:run"
fi
echo ""

# Check Frontend (Port 3000)
echo -e "${YELLOW}Frontend Status:${NC}"
if lsof -Pi :3000 -sTCP:LISTEN -t >/dev/null 2>&1; then
    echo -e "${GREEN}✓ Next.js Frontend is running on port 3000${NC}"
    echo "  - URL: http://localhost:3000"
else
    echo -e "${RED}✗ Next.js Frontend is not running${NC}"
    echo "  To start: cd jpetstore-frontend && npm run dev"
fi
echo ""

# Check Node modules
echo -e "${YELLOW}Frontend Dependencies:${NC}"
if [ -d "jpetstore-frontend/node_modules" ]; then
    echo -e "${GREEN}✓ Node modules are installed${NC}"
else
    echo -e "${RED}✗ Node modules not installed${NC}"
    echo "  To install: cd jpetstore-frontend && npm install"
fi
echo ""

# Quick start instructions
echo "========================================="
echo -e "${BLUE}Quick Start Commands:${NC}"
echo "========================================="
echo ""
echo "1. Run everything at once:"
echo "   ./run-dev.sh"
echo ""
echo "2. Run individually:"
echo "   Backend:  cd jpetstore-api && ./mvnw spring-boot:run"
echo "   Frontend: cd jpetstore-frontend && npm run dev"
echo ""
echo "3. Stop all services:"
echo "   Press Ctrl+C in the terminal running the services"
echo ""
echo "========================================="
