@echo off
REM Build Script for Library Management System (Windows)
REM Builds both backend and frontend projects

echo Building Library Management System
echo.

echo [1/2] Building Backend...
cd backend
call mvn clean install -DskipTests
if %ERRORLEVEL% NEQ 0 (
    echo ERROR: Backend build failed!
    exit /b 1
)
echo Backend build completed successfully!
echo.

echo [2/2] Building Frontend...
cd ..\frontend
call mvn clean install -DskipTests
if %ERRORLEVEL% NEQ 0 (
    echo ERROR: Frontend build failed!
    exit /b 1
)
echo Frontend build completed successfully!
echo.

echo Build completed successfully!
echo.
echo To start the backend: start-backend.bat
echo To start the frontend: start-frontend.bat
echo.

cd ..
