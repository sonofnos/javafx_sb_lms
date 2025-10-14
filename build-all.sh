#!/bin/bash

# Build All Script

echo "Building All Components"
echo ""

# Backend
echo "Building Backend..."
cd "$(dirname "$0")/backend"
mvn clean install

if [ $? -ne 0 ]; then
    echo "Backend build failed"
    exit 1
fi

echo ""
echo "Backend build complete"
echo ""

# Frontend
echo "Building Frontend..."
cd ../frontend
mvn clean install

if [ $? -ne 0 ]; then
    echo "Frontend build failed"
    exit 1
fi

echo ""
echo "Build complete"
echo ""
echo "Run application:"
echo "1. ./start-backend.sh"
echo "2. ./start-frontend.sh"
echo ""
