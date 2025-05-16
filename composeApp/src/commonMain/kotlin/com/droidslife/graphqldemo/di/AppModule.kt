package com.droidslife.graphqldemo.di

import com.droidslife.graphqldemo.ApolloClientCache
import com.droidslife.graphqldemo.MyApolloInterceptor
import com.droidslife.graphqldemo.data.repository.GitHubRepositoryImpl
import com.droidslife.graphqldemo.domain.repository.GitHubRepository
import com.droidslife.graphqldemo.domain.usecase.SearchRepositoriesUseCase
import com.droidslife.graphqldemo.presentation.details.RepositoryDetailsViewModel
import com.droidslife.graphqldemo.presentation.search.SearchRepositoriesViewModel
import org.koin.dsl.module

/**
 * Koin module for the application.
 * This module provides all the dependencies needed by the application.
 */
val appModule = module {
    // Data Layer
    // Provide MyApolloInterceptor as a singleton
    single { MyApolloInterceptor() }

    // Provide ApolloClientCache as a singleton
    single { ApolloClientCache() }

    // Provide GitHubRepository implementation
    single<GitHubRepository> { GitHubRepositoryImpl(get()) }

    // Domain Layer
    // Provide SearchRepositoriesUseCase
    factory { SearchRepositoriesUseCase(get()) }

    // Presentation Layer
    // Provide SearchRepositoriesViewModel
    factory { SearchRepositoriesViewModel(get()) }

    // Provide RepositoryDetailsViewModel
    factory { RepositoryDetailsViewModel(get()) }
}
