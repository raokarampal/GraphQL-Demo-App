package com.droidslife.graphqldemo

import com.apollographql.apollo.ApolloClient
import kotlinx.coroutines.runBlocking
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertNotNull
import kotlin.test.assertNotSame
import kotlin.test.assertSame

class ApolloClientCacheTest {
    private lateinit var apolloClientCache: ApolloClientCache
    private lateinit var interceptor: MyApolloInterceptor

    @BeforeTest
    fun setup() {
        interceptor = MyApolloInterceptor()

        // Set up Koin for dependency injection
        startKoin {
            modules(
                module {
                    single { interceptor }
                }
            )
        }

        apolloClientCache = ApolloClientCache()
    }

    @AfterTest
    fun tearDown() {
        stopKoin()
    }

    @Test
    fun testGetApolloClient() = runBlocking {
        // Get the Apollo client instance
        val client1 = apolloClientCache.getApolloClient()

        // Verify that it's not null
        assertNotNull(client1)

        // Get the Apollo client instance again
        val client2 = apolloClientCache.getApolloClient()

        // Verify that the same instance is returned
        assertSame(client1, client2)

        // Clear the cache
        apolloClientCache.clearCache()

        // Get the Apollo client instance again
        val client3 = apolloClientCache.getApolloClient()

        // Verify that a new instance is returned
        assertNotSame(client1, client3)
    }
}
