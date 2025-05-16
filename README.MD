# GitHub GraphQL API Demo App

[![LinkedIn](https://img.shields.io/badge/LinkedIn-0077B5?style=for-the-badge&logo=linkedin&logoColor=white)](https://linkedin.com/in/raokarampal)

This is a Kotlin Multiplatform project with Compose UI that demonstrates how to use the GitHub GraphQL API with Apollo Kotlin.

## Features

- Search for GitHub repositories using the GitHub GraphQL API
- View repository details including stars, forks, and primary language
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
5. Open `composeApp/src/commonMain/kotlin/com/droidslife/graphqldemo/MyApolloInterceptor.kt`
6. Replace `YOUR_GITHUB_TOKEN` with your actual token:

```kotlin
private val githubToken: String = "YOUR_GITHUB_TOKEN"
```

**Important**: Never commit your token to version control. In a real application, you would store this securely.

## Running the App

### Android
To run the application on android device/emulator:  
 - open project in Android Studio and run imported android run configuration

To build the application bundle:
 - run `./gradlew :composeApp:assembleDebug`
 - find `.apk` file in `composeApp/build/outputs/apk/debug/composeApp-debug.apk`
Run android UI tests on the connected device: `./gradlew :composeApp:connectedDebugAndroidTest`

### Desktop
Run the desktop application: `./gradlew :composeApp:run`
Run desktop UI tests: `./gradlew :composeApp:jvmTest`

### iOS
To run the application on iPhone device/simulator:
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

## Resources

- [GitHub GraphQL API Documentation](https://docs.github.com/en/graphql)
- [GitHub GraphQL Explorer](https://docs.github.com/en/graphql/guides/using-the-explorer)
- [Apollo Kotlin Documentation](https://www.apollographql.com/docs/kotlin/)
