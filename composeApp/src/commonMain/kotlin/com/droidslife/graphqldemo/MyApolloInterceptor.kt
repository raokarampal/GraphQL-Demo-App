package com.droidslife.graphqldemo

import com.apollographql.apollo.api.ApolloRequest
import com.apollographql.apollo.api.ApolloResponse
import com.apollographql.apollo.api.Operation
import com.apollographql.apollo.interceptor.ApolloInterceptor
import com.apollographql.apollo.interceptor.ApolloInterceptorChain
import com.droidslife.graphqldemo.config.BuildConfig
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.emitAll
import org.koin.core.component.KoinComponent

/**
 * Apollo interceptor for GitHub GraphQL API authentication.
 * This interceptor adds the necessary authorization header with a GitHub personal access token.
 * The token is configured during build time from the GITHUB_TOKEN environment variable.
 */
class MyApolloInterceptor : ApolloInterceptor, KoinComponent {
    // Use the GitHub token from BuildConfig
    // This token is set during build time from the environment variable
    private val githubToken: String = BuildConfig.GITHUB_TOKEN

    override fun <D : Operation.Data> intercept(
        request: ApolloRequest<D>,
        chain: ApolloInterceptorChain
    ): Flow<ApolloResponse<D>> = flow {
        // Create a new request with the Authorization header
        val authorizedRequest = request.newBuilder()
            .addHttpHeader("Authorization", "Bearer $githubToken")
            .build()

        // Proceed with the modified request
        emitAll(chain.proceed(authorizedRequest))
    }
}
