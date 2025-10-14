@echo off
REM Start Frontend Script for Library Management System (Windows)

echo Starting Library Management System Frontend...
echo.

cd frontend
echo Frontend connecting to backend at http://localhost:8080
echo JavaFX window will open shortly...
echo.

call mvn javafx:run
