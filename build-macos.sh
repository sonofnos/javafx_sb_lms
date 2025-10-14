#!/bin/bash

# Build script for macOS native application
# Creates a .app bundle and .dmg installer

echo "=================================="
echo "Building CBA LMS Test for macOS"
echo "=================================="
echo ""

# Navigate to frontend directory
cd "$(dirname "$0")/frontend"

echo "Step 1: Cleaning previous builds..."
mvn clean

echo ""
echo "Step 2: Compiling and packaging..."
mvn package -DskipTests

echo ""
echo "Step 3: Copying main JAR to libs folder..."
cp target/library-management-frontend-1.0.0.jar target/libs/

echo ""
echo "Step 4: Creating macOS application bundle..."
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
echo "Step 5: Creating macOS .dmg installer..."
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
echo "=================================="
echo "✅ Build Complete!"
echo "=================================="
echo ""
echo "Output location: frontend/target/dist/"
echo ""
echo "Files created:"
echo "  - CBA LMS Test.app (macOS application bundle)"
echo "  - CBA LMS Test-1.0.0.dmg (macOS installer)"
echo ""
echo "To install: Open the .dmg file and drag the app to Applications"
echo ""
echo "Note: Backend must be running at http://localhost:8080"
