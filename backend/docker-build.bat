@echo off
REM Docker Build Script for Backend (Windows)

echo Building Library Management System Backend Docker Image...
echo.

cd backend

REM Check if Docker is installed
docker --version >nul 2>&1
if %ERRORLEVEL% NEQ 0 (
    echo ERROR: Docker is not installed. Please install Docker Desktop first.
    exit /b 1
)

REM Check if Docker daemon is running
docker info >nul 2>&1
if %ERRORLEVEL% NEQ 0 (
    echo ERROR: Docker daemon is not running. Please start Docker Desktop.
    exit /b 1
)

echo Building Docker image: library-backend:1.0.0
echo.

docker build ^
    --tag library-backend:1.0.0 ^
    --tag library-backend:latest ^
    .

if %ERRORLEVEL% NEQ 0 (
    echo.
    echo ERROR: Docker build failed!
    exit /b 1
)

echo.
echo Docker image built successfully!
echo.
echo Image Information:
docker images library-backend --format "table {{.Repository}}\t{{.Tag}}\t{{.Size}}\t{{.CreatedAt}}"

echo.
echo To run the container:
echo   docker run -d -p 8080:8080 --name library-backend library-backend:1.0.0
echo.
echo Or use Docker Compose:
echo   cd ..
echo   docker-compose up -d
echo.

cd ..
