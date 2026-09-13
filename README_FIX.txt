FIX FOR GITHUB ACTIONS

Replace this file in your repository:
app/build.gradle.kts

What was fixed:
- Removed the Secrets Gradle Plugin from app/build.gradle.kts.
- Removed the secrets { ... } block that required .env and .env.example.
- This directly fixes the GitHub Actions error:
  File .env.example could not be found.

After replacing the file:
1. Commit the change.
2. Open Actions.
3. Re-run Android CI.
