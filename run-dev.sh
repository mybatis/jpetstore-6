#!/bin/bash

# JPetStore Development Runner Script
# This script starts both the backend API and frontend with hot reload

echo "========================================="
echo "   JPetStore Development Environment"
echo "========================================="
echo ""

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Function to check if a command exists
command_exists() {
    command -v "$1" >/dev/null 2>&1
}

# Function to kill process on port
kill_port() {
    local port=$1
    if lsof -Pi :$port -sTCP:LISTEN -t >/dev/null 2>&1; then
        echo -e "${YELLOW}Killing process on port $port...${NC}"
        lsof -ti:$port | xargs kill -9 2>/dev/null
        sleep 2
    fi
}

# Check prerequisites
echo "Checking prerequisites..."

if ! command_exists java; then
    echo -e "${RED}Error: Java is not installed${NC}"
    exit 1
fi

if ! command_exists node; then
    echo -e "${RED}Error: Node.js is not installed${NC}"
    exit 1
fi

if ! command_exists npm; then
    echo -e "${RED}Error: npm is not installed${NC}"
    exit 1
fi

# Check Java version
JAVA_VERSION=$(java -version 2>&1 | head -n 1 | cut -d'"' -f2 | cut -d'.' -f1)
if [ "$JAVA_VERSION" -lt 21 ]; then
    echo -e "${YELLOW}Warning: Java 21 or higher is recommended (found Java $JAVA_VERSION)${NC}"
fi

echo -e "${GREEN}✓ All prerequisites met${NC}"
echo ""

# Clean up any existing processes
echo "Cleaning up existing processes..."
kill_port 8080
kill_port 3000

# Function to run backend
run_backend() {
    echo -e "${GREEN}Starting Spring Boot API...${NC}"
    cd jpetstore-api
    
    # Check if Maven wrapper exists
    if [ -f "./mvnw" ]; then
        echo "Using Maven wrapper..."
        ./mvnw spring-boot:run
    else
        echo "Using system Maven..."
        mvn spring-boot:run
    fi
}

# Function to run frontend
run_frontend() {
    echo -e "${GREEN}Starting Next.js Frontend...${NC}"
    cd jpetstore-frontend
    
    # Install dependencies if node_modules doesn't exist
    if [ ! -d "node_modules" ]; then
        echo "Installing frontend dependencies..."
        npm install
    fi
    
    npm run dev
}

# Create a cleanup function
cleanup() {
    echo ""
    echo -e "${YELLOW}Shutting down services...${NC}"
    kill_port 8080
    kill_port 3000
    exit 0
}

# Set up trap to catch Ctrl+C
trap cleanup INT

# Start services in background
echo "========================================="
echo "Starting services..."
echo "========================================="
echo ""

# Start backend in background
(run_backend) &
BACKEND_PID=$!

# Wait a bit for backend to start
echo "Waiting for backend to initialize..."
sleep 5

# Start frontend in background
(run_frontend) &
FRONTEND_PID=$!

echo ""
echo "========================================="
echo -e "${GREEN}Services are starting up!${NC}"
echo "========================================="
echo ""
echo "📦 Backend API: http://localhost:8080"
echo "   - H2 Console: http://localhost:8080/h2-console"
echo "   - API Docs: http://localhost:8080/api"
echo ""
echo "🎨 Frontend: http://localhost:3000"
echo ""
echo "========================================="
echo -e "${YELLOW}Press Ctrl+C to stop all services${NC}"
echo "========================================="
echo ""

# Keep script running and wait for both processes
wait $BACKEND_PID $FRONTEND_PID
