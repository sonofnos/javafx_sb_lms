#!/bin/bash

# Docker Run Script for Library Management System Backend
# This script runs the Docker container for the backend

set -e

# Color codes for output
GREEN='\033[0;32m'
BLUE='\033[0;34m'
YELLOW='\033[1;33m'
RED='\033[0;31m'
NC='\033[0m' # No Color

# Configuration
CONTAINER_NAME="library-backend"
IMAGE_NAME="library-backend:1.0.0"
HOST_PORT="8080"
CONTAINER_PORT="8080"

echo "Running Library Management System Backend Container..."
echo ""

# Check if Docker is installed
if ! command -v docker &> /dev/null; then
    echo -e "${RED} Docker is not installed. Please install Docker first.${NC}"
    exit 1
fi

# Check if container already exists
if docker ps -a --format '{{.Names}}' | grep -q "^${CONTAINER_NAME}$"; then
    echo -e "${YELLOW}  Container '${CONTAINER_NAME}' already exists.${NC}"
    
    # Check if it's running
    if docker ps --format '{{.Names}}' | grep -q "^${CONTAINER_NAME}$"; then
        echo -e "${GREEN} Container is already running.${NC}"
        echo ""
        docker ps --filter "name=${CONTAINER_NAME}" --format "table {{.Names}}\t{{.Status}}\t{{.Ports}}"
        echo ""
        echo -e "${BLUE}Access the API at: http://localhost:${HOST_PORT}/api/books${NC}"
        exit 0
    else
        echo "Starting existing container..."
        docker start "${CONTAINER_NAME}"
        echo -e "${GREEN} Container started successfully!${NC}"
        echo ""
        docker ps --filter "name=${CONTAINER_NAME}" --format "table {{.Names}}\t{{.Status}}\t{{.Ports}}"
        echo ""
        echo -e "${BLUE}Access the API at: http://localhost:${HOST_PORT}/api/books${NC}"
        exit 0
    fi
fi

# Check if port is already in use
if lsof -Pi :${HOST_PORT} -sTCP:LISTEN -t >/dev/null 2>&1; then
    echo -e "${RED} Port ${HOST_PORT} is already in use.${NC}"
    echo "Please stop the service using this port or choose a different port."
    echo ""
    echo "To kill the process using port ${HOST_PORT}:"
    echo "  lsof -ti:${HOST_PORT} | xargs kill -9"
    exit 1
fi

# Check if image exists
if ! docker images --format '{{.Repository}}:{{.Tag}}' | grep -q "^${IMAGE_NAME}$"; then
    echo -e "${YELLOW}  Docker image '${IMAGE_NAME}' not found.${NC}"
    echo "Building the image first..."
    ./docker-build.sh
    echo ""
fi

# Run the container
echo -e "${BLUE}Starting new container: ${CONTAINER_NAME}${NC}"
echo ""

docker run -d \
    --name "${CONTAINER_NAME}" \
    -p "${HOST_PORT}:${CONTAINER_PORT}" \
    -e SPRING_PROFILES_ACTIVE=docker \
    -e SPRING_H2_CONSOLE_ENABLED=true \
    "${IMAGE_NAME}"

# Wait a moment for container to start
sleep 2

# Check container status
if docker ps --filter "name=${CONTAINER_NAME}" --format '{{.Names}}' | grep -q "^${CONTAINER_NAME}$"; then
    echo -e "${GREEN} Container started successfully!${NC}"
    echo ""
    docker ps --filter "name=${CONTAINER_NAME}" --format "table {{.Names}}\t{{.Status}}\t{{.Ports}}"
    echo ""
    echo -e "${BLUE} Library Management System Backend is running!${NC}"
    echo ""
    echo "Access points:"
    echo "  • REST API: http://localhost:${HOST_PORT}/api/books"
    echo "  • H2 Console: http://localhost:${HOST_PORT}/h2-console"
    echo ""
    echo "Useful commands:"
    echo "  • View logs: docker logs -f ${CONTAINER_NAME}"
    echo "  • Stop container: docker stop ${CONTAINER_NAME}"
    echo "  • Remove container: docker rm ${CONTAINER_NAME}"
else
    echo -e "${RED} Failed to start container. Check logs:${NC}"
    echo "  docker logs ${CONTAINER_NAME}"
    exit 1
fi
