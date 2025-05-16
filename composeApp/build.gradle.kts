@file:OptIn(ExperimentalKotlinGradlePluginApi::class)

import org.jetbrains.compose.ExperimentalComposeLibrary
import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSetTree

plugins {
    alias(libs.plugins.multiplatform)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.compose)
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlinx.serialization)
    alias(libs.plugins.sqlDelight)
    alias(libs.plugins.apollo)
    alias(libs.plugins.buildconfig)
}

kotlin {
    jvmToolchain(11)
    androidTarget {
        //https://www.jetbrains.com/help/kotlin-multiplatform-dev/compose-test.html
        instrumentedTestVariant.sourceSetTree.set(KotlinSourceSetTree.test)
    }

    jvm()

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }

    sourceSets {
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
//            implementation(libs.androidx.material.icons.extended)
            implementation(libs.material.icons)
            implementation(libs.kermit)
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.ktor.client.serialization)
            implementation(libs.ktor.client.logging)
            implementation(libs.androidx.lifecycle.viewmodel)
            implementation(libs.androidx.lifecycle.runtime.compose)
            implementation(libs.androidx.navigation.composee)
            implementation(libs.kotlinx.serialization.json)
            implementation(libs.koin.core)
            implementation(libs.koin.compose)
            implementation(libs.coil)
            implementation(libs.coil.network.ktor)
            implementation(libs.kotlinx.datetime)
            implementation(libs.apollo.runtime)
            implementation(libs.apollo.normalized.cache) // optional for In-memory cache
            implementation(libs.apollo.normalized.cache.sqlite) // optional for SQLite cache
            // Rich text editor for Compose Multiplatform
            implementation(libs.compose.rich.editor)
            implementation(libs.multiplatform.markdown.renderer)
            implementation(libs.multiplatform.markdown.renderer.m3)
            implementation(libs.multiplatform.markdown.renderer.coil3)
        }

        commonTest.dependencies {
            implementation(kotlin("test"))
            @OptIn(ExperimentalComposeLibrary::class)
            implementation(compose.uiTest)
            implementation(libs.kotlinx.coroutines.test)
        }

        androidMain.dependencies {
            implementation(compose.uiTooling)
            implementation(libs.androidx.activityCompose)
            implementation(libs.kotlinx.coroutines.android)
            implementation(libs.ktor.client.okhttp)
            implementation(libs.sqlDelight.driver.android)
            implementation(libs.apollo.normalized.cache.sqlite) // optional for SQLite cache
        }

        jvmMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutines.swing)
            implementation(libs.ktor.client.okhttp)
            implementation(libs.sqlDelight.driver.sqlite)
            implementation(libs.apollo.normalized.cache.sqlite) // optional for SQLite cache
        }

        iosMain.dependencies {
            implementation(libs.ktor.client.darwin)
            implementation(libs.sqlDelight.driver.native)
            implementation(libs.apollo.normalized.cache.sqlite) // optional for SQLite cache
        }

    }
}

android {
    namespace = "com.droidslife.graphqldemo"
    compileSdk = 35

    defaultConfig {
        minSdk = 21
        targetSdk = 35

        applicationId = "com.droidslife.graphqldemo.androidApp"
        versionCode = 1
        versionName = "1.0.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
}

//https://developer.android.com/develop/ui/compose/testing#setup
dependencies {
    androidTestImplementation(libs.androidx.uitest.junit4)
    debugImplementation(libs.androidx.uitest.testManifest)
}

compose.desktop {
    application {
        mainClass = "MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "GraphQL Demo App"
            packageVersion = "1.0.0"

            linux {
                iconFile.set(project.file("desktopAppIcons/LinuxIcon.png"))
            }
            windows {
                iconFile.set(project.file("desktopAppIcons/WindowsIcon.ico"))
            }
            macOS {
                iconFile.set(project.file("desktopAppIcons/MacosIcon.icns"))
                bundleID = "com.droidslife.graphqldemo.desktopApp"
            }
        }
    }
}

sqldelight {
    databases {
        create("MyDatabase") {
            // Database configuration here.
            // https://cashapp.github.io/sqldelight
            packageName.set("com.droidslife.graphqldemo.db")
        }
    }
}

apollo {
    service("api") {
        // GraphQL configuration here.
        // https://www.apollographql.com/docs/kotlin/advanced/plugin-configuration/
        packageName.set("com.droidslife.graphqldemo.graphql")
    }
}

// BuildConfig configuration
buildConfig {
    // Package name for the generated BuildConfig class
    packageName("com.droidslife.graphqldemo.config")

    // Read GitHub token from environment variable or use a real token
    // For a real application, you should use an environment variable or a secure method to store the token
    // This is a sample token format - replace with your actual GitHub personal access token
    val githubToken = System.getenv("GITHUB_TOKEN") ?: "ghp_1234567890abcdefghijklmnopqrstuvwxyz"

    // Create a field for the GitHub token
    buildConfigField("String", "GITHUB_TOKEN", "\"$githubToken\"")
}
