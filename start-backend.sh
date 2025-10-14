#!/bin/bash

# Backend Startup Script

echo "Starting Backend"
echo ""

cd "$(dirname "$0")/backend"

echo "Spring Boot backend on port 8080..."
echo ""

mvn spring-boot:run

if [ $? -ne 0 ]; then
    echo ""
    echo "Error: Backend failed to start"
    echo "Press any key to exit..."
    read -n 1
fi
