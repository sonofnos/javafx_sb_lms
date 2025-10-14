#!/bin/bash

# Frontend Startup Script

echo "Starting Frontend"
echo ""

cd "$(dirname "$0")/frontend"

echo "JavaFX frontend application..."
echo "Note: Backend must run on http://localhost:8080"
echo ""

mvn javafx:run

if [ $? -ne 0 ]; then
    echo ""
    echo "Error: Frontend failed to start"
    echo "Press any key to exit..."
    read -n 1
fi
