package com.droidslife.graphqldemo

import android.app.Application
import com.droidslife.graphqldemo.di.appModule
import org.koin.core.context.startKoin

/**
 * Custom Application class for the GraphQL Demo App.
 * This class initializes Koin for dependency injection when the app starts.
 */
class GraphQLDemoApp : Application() {
    override fun onCreate() {
        super.onCreate()

        // Initialize Koin for dependency injection
        startKoin {
            // Load the app module
            modules(appModule)
        }
    }
}
