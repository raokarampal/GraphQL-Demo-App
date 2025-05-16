# GitHub GraphQL API Demo App


This is a Kotlin Multiplatform project with Compose UI that demonstrates how to use the GitHub GraphQL API with Apollo Kotlin.

## Features

- Search for GitHub repositories using the GitHub GraphQL API
- View repository details including stars, forks, primary language and latest releases
- Dark/light theme support
- Cross-platform (Android, iOS, Desktop)

## Before running!
 - check your system with [KDoctor](https://github.com/Kotlin/kdoctor)
 - install JDK 17 or higher on your machine
 - add `local.properties` file to the project root and set a path to Android SDK there
 - set up a GitHub Personal Access Token (see below)

## GitHub Personal Access Token

To use the GitHub GraphQL API, you need a personal access token:

1. Go to [GitHub Settings > Developer settings > Personal access tokens > Tokens (classic)](https://github.com/settings/tokens)
2. Click "Generate new token" and select "Generate new token (classic)"
3. Give your token a name and select the following scopes:
   - `repo` (Full control of private repositories)
   - `read:user` (Read all user profile data)
   - `user:email` (Access user email addresses)
4. Click "Generate token" and copy the token
5. Open `composeApp/build.gradle.kts`
6. Replace `githubToken` with your actual token:

```kotlin
    // For a real application, you should use an environment variable or a secure method to store the token
    // This is a sample token format - replace it with your actual GitHub personal access token
    val githubToken = System.getenv("GITHUB_TOKEN") ?: "ghp_1234567890abcdefghijklmnopqrstuvwxyz"
```
or 

## Running the App

### Android
To run the application on an android device / emulator:  
 - open project in Android Studio and run an imported android run configuration

To build the application bundle:
 - run `./gradlew :composeApp:assembleDebug`
 - find `.apk` file in `composeApp/build/outputs/apk/debug/composeApp-debug.apk`
Run android UI tests on the connected device: `./gradlew :composeApp:connectedDebugAndroidTest`

### Desktop
Run the desktop application: `./gradlew :composeApp:run`
Run desktop UI tests: `./gradlew :composeApp:jvmTest`

### iOS
To run the application on an iPhone device / simulator:
 - Open `iosApp/iosApp.xcproject` in Xcode and run standard configuration
 - Or use [Kotlin Multiplatform Mobile plugin](https://plugins.jetbrains.com/plugin/14936-kotlin-multiplatform-mobile) for Android Studio
Run iOS simulator UI tests: `./gradlew :composeApp:iosSimulatorArm64Test`

## Project Structure

- `composeApp/src/commonMain/kotlin/com/droidslife/graphqldemo/` - Main application code
- `composeApp/src/commonMain/graphql/` - GraphQL schema and queries
- `composeApp/src/commonMain/kotlin/com/droidslife/graphqldemo/MyApolloInterceptor.kt` - Apollo interceptor for GitHub API authentication
- `composeApp/src/commonMain/kotlin/com/droidslife/graphqldemo/ApolloClientCache.kt` - Apollo client configuration

## How It Works

1. The app uses Apollo Kotlin to interact with the GitHub GraphQL API
2. GraphQL schema and queries are defined in the `graphql` directory
3. Apollo generates type-safe Kotlin code for the GraphQL operations
4. The app uses these generated classes to make requests to the GitHub API
5. The `MyApolloInterceptor` adds the necessary authentication headers to each request
6. The UI displays the results in a user-friendly way

## GraphQL Queries

The app includes several GraphQL queries:

- `ViewerQuery` - Gets information about the currently authenticated user
- `SearchRepositoriesQuery` - Searches for repositories based on a query
- `RepositoryDetailsQuery` - Gets detailed information about a specific repository

## 📦 Dependencies

This project uses the following libraries:

- [Kotlinx Coroutines](https://github.com/Kotlin/kotlinx.coroutines) – Asynchronous programming for Kotlin
- [Ktor Client](https://ktor.io/docs/getting-started-ktor-client.html) – HTTP client for Kotlin Multiplatform
- [Kotlinx Serialization](https://github.com/Kotlin/kotlinx.serialization) – JSON serialization for Kotlin
- [Koin](https://insert-koin.io/) – Dependency injection for Kotlin
- [Coil](https://coil-kt.github.io/coil/) – Image loading library for Android backed by Kotlin Coroutines
- [Apollo GraphQL](https://www.apollographql.com/docs/kotlin/) – GraphQL client for Kotlin Multiplatform
- [Compose Rich Editor](https://github.com/your-org/compose-rich-editor) – Rich text editor for Jetpack Compose
- [Multiplatform Markdown Renderer](https://github.com/your-org/multiplatform-markdown-renderer) – Markdown rendering for Kotlin Multiplatform


## Resources

- [GitHub GraphQL API Documentation](https://docs.github.com/en/graphql)
- [GitHub GraphQL Explorer](https://docs.github.com/en/graphql/guides/using-the-explorer)
- [Apollo Kotlin Documentation](https://www.apollographql.com/docs/kotlin/)
