#!/bin/bash

# Library Management System - Frontend Startup Script

echo "Library Management System - Frontend"
echo ""

# Navigate to frontend directory
cd "$(dirname "$0")/frontend"

echo "Starting JavaFX frontend application..."
echo "Note: Make sure the backend is running on http://localhost:8080"
echo ""

# Run JavaFX application
mvn javafx:run

# Keep the terminal open if there's an error
if [ $? -ne 0 ]; then
    echo ""
    echo "Error: Frontend failed to start!"
    echo "Press any key to exit..."
    read -n 1
fi
