@echo off
REM Build script for Windows native application
REM Creates a .exe launcher and .msi installer

echo ==================================
echo Building CBA LMS Test for Windows
echo ==================================
echo.

REM Navigate to frontend directory
cd "%~dp0frontend"

echo Step 1: Cleaning previous builds...
call mvn clean

echo.
echo Step 2: Compiling and packaging...
call mvn package -DskipTests

echo.
echo Step 3: Copying main JAR to libs folder...
copy target\library-management-frontend-1.0.0.jar target\libs\

echo.
echo Step 4: Creating Windows application...
jpackage ^
  --name "CBA LMS Test" ^
  --app-version 1.0.0 ^
  --vendor "CBA" ^
  --type app-image ^
  --input target\libs ^
  --main-jar library-management-frontend-1.0.0.jar ^
  --main-class com.library.LibraryManagementApp ^
  --dest target\dist ^
  --win-dir-chooser ^
  --win-menu ^
  --win-shortcut ^
  --java-options "-Xmx512m"

echo.
echo Step 5: Creating Windows .msi installer...
jpackage ^
  --name "CBA LMS Test" ^
  --app-version 1.0.0 ^
  --vendor "CBA" ^
  --type msi ^
  --app-image "target\dist\CBA LMS Test" ^
  --dest target\dist ^
  --win-dir-chooser ^
  --win-menu ^
  --win-shortcut

cd ..

echo.
echo ==================================
echo Build Complete!
echo ==================================
echo.
echo Output location: frontend\target\dist\
echo.
echo Files created:
echo   - CBA LMS Test\ (Windows application folder)
echo   - CBA LMS Test-1.0.0.msi (Windows installer)
echo.
echo To install: Double-click the .msi file
echo.
echo Note: Backend must be running at http://localhost:8080
echo.
pause
