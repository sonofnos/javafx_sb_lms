# GitHub Actions - Automated Builds

## Workflows

### `build-releases.yml`

Builds native installers for macOS and Windows.

**Triggers:**

- Push to `main`
- Pull requests
- Version tags (e.g., `v1.0.0`)
- Manual dispatch

**Output:**

- macOS: `.dmg` installer
- Windows: `.msi` installer
- Artifacts stored for 30 days

## Prerequisites

> **IMPORTANT**: Backend must run on `http://localhost:8080` before launching the frontend. Installers only package the frontend.

Start backend:

```bash
cd backend
mvn spring-boot:run
```

## Usage

### Automatic Build

Push to `main`:

```bash
git push origin main
```

Installers build automatically for both platforms.

### Create Release

Push a version tag:

```bash
git tag v1.0.0
git push origin v1.0.0
```

GitHub Actions will:

- Build macOS and Windows installers
- Create GitHub Release
- Attach installers
- Generate release notes

Download from: Repository → Releases

### Manual Build

1. Go to repository on GitHub
2. Actions tab → Build Native Installers
3. Run workflow → Select branch → Run

## Download Artifacts

From completed workflow:

1. Actions tab → Select workflow run
2. Scroll to Artifacts section
3. Download `macos-installer` or `windows-installer`

## Requirements

None. Workflow uses:

- GitHub-hosted runners
- Java 17 (auto-installed)
- Maven (auto-cached)
- jpackage (included in JDK 17)

## Build Time

- macOS: ~5-7 minutes
- Windows: ~5-7 minutes

## Installer Contents

Both include:

- JavaFX frontend application
- Embedded Java Runtime (JRE)
- All dependencies
- Native launchers
- Desktop shortcuts (optional)
- Start menu entries (Windows)

Users don't need Java installed.

## Example Workflow

```bash
# Make changes
git add .
git commit -m "Add feature"

# Build artifacts
git push origin main

# Create release
git tag v1.0.0
git push origin v1.0.0
```

## Troubleshooting

**Build failed?**
Actions tab → Failed run → Expand step for details

**Maven build fails:**

- Check `pom.xml` syntax
- Verify dependencies available

**jpackage fails:**

- Verify main class name
- Check JAR created successfully

**Release not created:**

- Tag must start with `v`
- Check write permissions

## Customization

Edit `.github/workflows/build-releases.yml`:

- Modify jpackage options
- Change version numbers
- Adjust artifact retention

## Installation

- **macOS**: Open `.dmg`, drag to Applications
- **Windows**: Run `.msi` installer
