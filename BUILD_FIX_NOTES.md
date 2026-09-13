# Cairo Files Android - CI build fixes

Changes applied:

1. Updated the Gradle wrapper from 8.9 to 9.3.1 to match Android Gradle Plugin 9.1.1 requirements.
2. Replaced `.env.example` as the Secrets Gradle Plugin fallback with tracked `secrets.defaults.properties`.
3. GitHub Actions now creates `.env` from the `GEMINI_API_KEY` repository secret when available, or from the safe fallback otherwise.
4. Removed the custom debug signing configuration that referenced a missing project-level `debug.keystore`; default Android debug signing is used.
5. GitHub Actions explicitly installs Android SDK Platform 36.1 and Build Tools 36.1.0 before building.

The CI build command remains a debug APK build: `./gradlew :app:assembleDebug --stacktrace`.
