#!/bin/bash

# Library Management System - Build All Script

echo "Library Management System - Build All"
echo ""

# Build Backend
echo "Building Backend..."
cd "$(dirname "$0")/backend"
mvn clean install

if [ $? -ne 0 ]; then
    echo "Backend build failed!"
    exit 1
fi

echo ""
echo "Backend build completed successfully!"
echo ""

# Build Frontend
echo "Building Frontend..."
cd ../frontend
mvn clean install

if [ $? -ne 0 ]; then
    echo "Frontend build failed!"
    exit 1
fi

echo ""
echo "Build completed successfully!"
echo ""
echo "To run the application:"
echo "1. Start backend: ./start-backend.sh"
echo "2. Start frontend: ./start-frontend.sh"
echo ""
