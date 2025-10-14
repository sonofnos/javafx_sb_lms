@echo off
REM Start Backend Script for Library Management System (Windows)

echo Starting Library Management System Backend...
echo.

cd backend
echo Backend starting on http://localhost:8080
echo Press Ctrl+C to stop the backend
echo.

call mvn spring-boot:run
