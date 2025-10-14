#!/bin/bash

# Docker Build Script for Library Management System Backend
# This script builds the Docker image for the backend

set -e

echo "🐳 Building Library Management System Backend Docker Image..."
echo ""

# Color codes for output
GREEN='\033[0;32m'
BLUE='\033[0;34m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Configuration
IMAGE_NAME="library-backend"
IMAGE_TAG="1.0.0"
IMAGE_FULL="${IMAGE_NAME}:${IMAGE_TAG}"
IMAGE_LATEST="${IMAGE_NAME}:latest"

# Check if Docker is installed
if ! command -v docker &> /dev/null; then
    echo "❌ Docker is not installed. Please install Docker first."
    exit 1
fi

# Check if Docker daemon is running
if ! docker info &> /dev/null; then
    echo "❌ Docker daemon is not running. Please start Docker."
    exit 1
fi

echo -e "${BLUE}Building Docker image: ${IMAGE_FULL}${NC}"
echo ""

# Build the Docker image
docker build \
    --tag "${IMAGE_FULL}" \
    --tag "${IMAGE_LATEST}" \
    --build-arg BUILD_DATE="$(date -u +'%Y-%m-%dT%H:%M:%SZ')" \
    .

echo ""
echo -e "${GREEN}✅ Docker image built successfully!${NC}"
echo ""

# Display image information
echo -e "${BLUE}Image Information:${NC}"
docker images "${IMAGE_NAME}" --format "table {{.Repository}}\t{{.Tag}}\t{{.Size}}\t{{.CreatedAt}}"

echo ""
echo -e "${YELLOW}To run the container:${NC}"
echo "  docker run -d -p 8080:8080 --name library-backend ${IMAGE_FULL}"
echo ""
echo -e "${YELLOW}Or use Docker Compose:${NC}"
echo "  cd .."
echo "  docker-compose up -d"
echo ""
