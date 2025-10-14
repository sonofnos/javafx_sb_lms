# GitHub Actions - Automated Builds

This directory contains GitHub Actions workflows for automated building and releasing of the CBA LMS Test application.

## Workflows

### `build-releases.yml` - Native Installer Builder

Automatically builds native installers for macOS and Windows.

**Triggers:**
- Push to `main` branch
- Pull requests to `main` branch
- Creating a version tag (e.g., `v1.0.0`)
- Manual workflow dispatch

**What it builds:**
- **macOS**: `.dmg` installer (~67 MB)
- **Windows**: `.msi` installer (~70 MB)

**Build artifacts are stored for 30 days**

## Prerequisites

> **⚠️ IMPORTANT**: The backend server must be running on `http://localhost:8080` before launching the frontend application. The installers only package the frontend - you need to start the backend separately.

To start the backend:
```bash
cd backend
mvn spring-boot:run
```

## How to Use

### Option 1: Automatic Build on Push

Simply push your code to the `main` branch:
```bash
git push origin main
```

The workflow will automatically build installers for both platforms.

### Option 2: Create a Release

To create a GitHub Release with downloadable installers:

1. **Create and push a version tag:**
   ```bash
   git tag v1.0.0
   git push origin v1.0.0
   ```

2. **GitHub Actions will:**
   - Build macOS and Windows installers
   - Create a GitHub Release
   - Attach the installers to the release
   - Generate release notes

3. **Download installers from:**
   - Go to your repository on GitHub
   - Click "Releases" (right sidebar)
   - Download the `.dmg` or `.msi` file

### Option 3: Manual Trigger

1. Go to your repository on GitHub
2. Click "Actions" tab
3. Select "Build Native Installers" workflow
4. Click "Run workflow" button
5. Select the branch
6. Click "Run workflow"

## Accessing Build Artifacts

After a workflow completes:

1. Go to "Actions" tab in your GitHub repository
2. Click on the completed workflow run
3. Scroll to "Artifacts" section at the bottom
4. Download:
   - `macos-installer` - Contains the `.dmg` file
   - `windows-installer` - Contains the `.msi` file

## Requirements

No additional setup required! The workflow uses:
- GitHub-hosted runners (free for public repositories)
- Java 17 (automatically installed)
- Maven (automatically cached)
- jpackage (included with JDK 17)

## Build Time

Typical build times:
- **macOS**: ~5-7 minutes
- **Windows**: ~5-7 minutes
- **Total**: ~10-15 minutes

## What Gets Built

Both installers include:
- The JavaFX frontend application
- Embedded Java Runtime (JRE)
- All required dependencies
- Native launchers
- Desktop shortcuts (optional)
- Start menu entries (Windows)

**Users do NOT need Java installed to run the application!**

## Example Release Workflow

```bash
# 1. Make your changes
git add .
git commit -m "Add new feature"

# 2. Push to main (builds artifacts)
git push origin main

# 3. When ready for release, create a tag
git tag v1.0.0
git push origin v1.0.0

# 4. Check GitHub Releases page for installers!
```

## Troubleshooting

### Build Failed?

Check the workflow logs:
1. Go to "Actions" tab
2. Click the failed workflow run
3. Expand the failed step to see error details

### Common Issues:

**Maven build fails:**
- Check `pom.xml` for syntax errors
- Ensure all dependencies are available

**jpackage fails:**
- Verify main class name is correct
- Check JAR file is created successfully

**Release not created:**
- Ensure you pushed a tag starting with `v` (e.g., `v1.0.0`)
- Check you have write permissions to the repository

## Customization

To customize the build:

1. Edit `.github/workflows/build-releases.yml`
2. Modify jpackage options
3. Change version numbers
4. Adjust artifact retention period
5. Commit and push changes

## Cost

For public repositories: **FREE** ✅
- Unlimited build minutes
- Unlimited artifact storage (30-day retention)

For private repositories:
- 2,000 free minutes/month
- After that, pay-per-use pricing

## Security

Build artifacts are:
- ✅ Scanned by GitHub
- ✅ Stored securely
- ✅ Only accessible to repository collaborators
- ✅ Available for download for 30 days

## Next Steps

After downloading installers:
- **macOS**: Open the `.dmg`, drag app to Applications
- **Windows**: Run the `.msi` installer

