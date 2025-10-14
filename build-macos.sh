#!/bin/bash

# macOS Build Script - Creates .app bundle and .dmg installer

echo "Building CBA LMS Test for macOS"
echo ""

cd "$(dirname "$0")/frontend"

echo "Cleaning previous builds..."
mvn clean

echo ""
echo "Compiling and packaging..."
mvn package -DskipTests

echo ""
echo "Copying JAR to libs..."
cp target/library-management-frontend-1.0.0.jar target/libs/

echo ""
echo "Creating app bundle..."
jpackage \
  --name "CBA LMS Test" \
  --app-version 1.0.0 \
  --vendor "CBA" \
  --type app-image \
  --input target/libs \
  --main-jar library-management-frontend-1.0.0.jar \
  --main-class com.library.LibraryManagementApp \
  --dest target/dist \
  --mac-package-name "CBA LMS Test" \
  --mac-package-identifier "com.library.cba-lms-test" \
  --java-options '-Xmx512m'

echo ""
echo "Creating .dmg installer..."
jpackage \
  --name "CBA LMS Test" \
  --app-version 1.0.0 \
  --vendor "CBA" \
  --type dmg \
  --app-image "target/dist/CBA LMS Test.app" \
  --dest target/dist \
  --mac-package-name "CBA LMS Test"

cd ..

echo ""
echo "Build Complete"
echo ""
echo "Output: frontend/target/dist/"
echo "  - CBA LMS Test.app"
echo "  - CBA LMS Test-1.0.0.dmg"
echo ""
echo "Install: Open .dmg and drag to Applications"
echo "Note: Backend must run on http://localhost:8080"
