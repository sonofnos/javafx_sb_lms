#!/bin/bash

# Library Management System - Backend Startup Script

echo "Library Management System - Backend"
echo ""

# Navigate to backend directory
cd "$(dirname "$0")/backend"

echo "Starting Spring Boot backend on port 8080..."
echo ""

# Run Spring Boot application
mvn spring-boot:run

# Keep the terminal open if there's an error
if [ $? -ne 0 ]; then
    echo ""
    echo "Error: Backend failed to start!"
    echo "Press any key to exit..."
    read -n 1
fi
