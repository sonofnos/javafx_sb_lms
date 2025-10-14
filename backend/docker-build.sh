#!/bin/bash

# Docker Build Script - Library Management System Backend

set -e

echo "Building Docker Image for Backend"
echo ""

GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m'

IMAGE_NAME="library-backend"
IMAGE_TAG="1.0.0"
IMAGE_FULL="${IMAGE_NAME}:${IMAGE_TAG}"
IMAGE_LATEST="${IMAGE_NAME}:latest"

# Check Docker
if ! command -v docker &> /dev/null; then
    echo "Docker not installed"
    exit 1
fi

if ! docker info &> /dev/null; then
    echo "Docker daemon not running"
    exit 1
fi

echo "Building ${IMAGE_FULL}..."
echo ""

docker build \
    --tag "${IMAGE_FULL}" \
    --tag "${IMAGE_LATEST}" \
    --build-arg BUILD_DATE="$(date -u +'%Y-%m-%dT%H:%M:%SZ')" \
    .

echo ""
echo -e "${GREEN}Build successful${NC}"
echo ""

echo "Image Information:"
docker images "${IMAGE_NAME}" --format "table {{.Repository}}\t{{.Tag}}\t{{.Size}}\t{{.CreatedAt}}"

echo ""
echo -e "${YELLOW}Run container:${NC}"
echo "  docker run -d -p 8080:8080 --name library-backend ${IMAGE_FULL}"
echo ""
echo -e "${YELLOW}Or use Docker Compose:${NC}"
echo "  cd .. && docker-compose up -d"
echo ""
