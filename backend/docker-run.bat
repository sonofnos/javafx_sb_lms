@echo off
REM Docker Run Script for Backend (Windows)

setlocal

set CONTAINER_NAME=library-backend
set IMAGE_NAME=library-backend:1.0.0
set HOST_PORT=8080
set CONTAINER_PORT=8080

echo Running Library Management System Backend Container...
echo.

REM Check if Docker is installed
docker --version >nul 2>&1
if %ERRORLEVEL% NEQ 0 (
    echo ERROR: Docker is not installed. Please install Docker Desktop first.
    exit /b 1
)

REM Check if container already exists
docker ps -a --format "{{.Names}}" | findstr /X "%CONTAINER_NAME%" >nul
if %ERRORLEVEL% EQU 0 (
    echo Container '%CONTAINER_NAME%' already exists.
    
    REM Check if it's running
    docker ps --format "{{.Names}}" | findstr /X "%CONTAINER_NAME%" >nul
    if !ERRORLEVEL! EQU 0 (
        echo Container is already running.
        docker ps --filter "name=%CONTAINER_NAME%" --format "table {{.Names}}\t{{.Status}}\t{{.Ports}}"
        echo.
        echo Access the API at: http://localhost:%HOST_PORT%/api/books
        exit /b 0
    ) else (
        echo Starting existing container...
        docker start %CONTAINER_NAME%
        echo Container started successfully!
        echo.
        docker ps --filter "name=%CONTAINER_NAME%" --format "table {{.Names}}\t{{.Status}}\t{{.Ports}}"
        echo.
        echo Access the API at: http://localhost:%HOST_PORT%/api/books
        exit /b 0
    )
)

REM Check if port is already in use (Windows)
netstat -ano | findstr ":%HOST_PORT%" | findstr "LISTENING" >nul
if %ERRORLEVEL% EQU 0 (
    echo ERROR: Port %HOST_PORT% is already in use.
    echo Please stop the service using this port or choose a different port.
    exit /b 1
)

REM Check if image exists
docker images --format "{{.Repository}}:{{.Tag}}" | findstr /X "%IMAGE_NAME%" >nul
if %ERRORLEVEL% NEQ 0 (
    echo Docker image '%IMAGE_NAME%' not found.
    echo Building the image first...
    call docker-build.bat
    if !ERRORLEVEL! NEQ 0 exit /b 1
    echo.
)

REM Run the container
echo Starting new container: %CONTAINER_NAME%
echo.

docker run -d ^
    --name %CONTAINER_NAME% ^
    -p %HOST_PORT%:%CONTAINER_PORT% ^
    -e SPRING_PROFILES_ACTIVE=docker ^
    -e SPRING_H2_CONSOLE_ENABLED=true ^
    %IMAGE_NAME%

if %ERRORLEVEL% NEQ 0 (
    echo ERROR: Failed to start container.
    exit /b 1
)

REM Wait a moment for container to start
timeout /t 2 /nobreak >nul

REM Check container status
docker ps --filter "name=%CONTAINER_NAME%" --format "{{.Names}}" | findstr /X "%CONTAINER_NAME%" >nul
if %ERRORLEVEL% EQU 0 (
    echo Container started successfully!
    echo.
    docker ps --filter "name=%CONTAINER_NAME%" --format "table {{.Names}}\t{{.Status}}\t{{.Ports}}"
    echo.
    echo Library Management System Backend is running!
    echo.
    echo Access points:
    echo   - REST API: http://localhost:%HOST_PORT%/api/books
    echo   - H2 Console: http://localhost:%HOST_PORT%/h2-console
    echo.
    echo Useful commands:
    echo   - View logs: docker logs -f %CONTAINER_NAME%
    echo   - Stop container: docker stop %CONTAINER_NAME%
    echo   - Remove container: docker rm %CONTAINER_NAME%
) else (
    echo ERROR: Failed to start container. Check logs:
    echo   docker logs %CONTAINER_NAME%
    exit /b 1
)

endlocal
