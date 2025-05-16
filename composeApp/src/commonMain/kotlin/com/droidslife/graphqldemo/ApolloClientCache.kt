package com.droidslife.graphqldemo

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.ApolloRequest
import com.apollographql.apollo.api.Operation
import com.apollographql.apollo.cache.normalized.api.MemoryCacheFactory
import com.apollographql.apollo.cache.normalized.normalizedCache
import com.apollographql.apollo.network.http.HttpNetworkTransport
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

/**
 * Apollo client cache for GitHub GraphQL API.
 * This class manages the Apollo client instance and provides methods to access it.
 */
class ApolloClientCache : KoinComponent {
    private val mutex = Mutex()
    private val interceptor: MyApolloInterceptor by inject()
    private var apolloClient: ApolloClient? = null

    // GitHub GraphQL API endpoint
    private val githubGraphQLEndpoint = "https://api.github.com/graphql"

    /**
     * Gets the Apollo client instance, creating it if it doesn't exist.
     * Thread-safe using a mutex to prevent multiple instances from being created.
     */
    suspend fun getApolloClient(): ApolloClient {
        if (apolloClient == null) {
            mutex.withLock {
                if (apolloClient == null) {
                    apolloClient = createApolloClient()
                }
            }
        }
        return apolloClient!!
    }

    /**
     * Creates a new Apollo client configured for the GitHub GraphQL API.
     */
    private fun createApolloClient(): ApolloClient {
        return ApolloClient.Builder()
            .networkTransport(
                HttpNetworkTransport.Builder()
                    .serverUrl(githubGraphQLEndpoint)
                    .build()
            )
            .normalizedCache(
                MemoryCacheFactory(maxSizeBytes = 10 * 1024 * 1024) // 10MB cache
            )
            .addInterceptor(interceptor)
            .build()
    }

    /**
     * Clears the Apollo client cache.
     * This is useful when you want to refresh data or when the authentication state changes.
     */
    suspend fun clearCache() {
        // In Apollo 4.x, we need to recreate the client to clear the cache
        mutex.withLock {
            apolloClient = null
        }
    }
}
