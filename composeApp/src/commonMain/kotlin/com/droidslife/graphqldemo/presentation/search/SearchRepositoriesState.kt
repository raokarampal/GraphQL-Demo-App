package com.droidslife.graphqldemo.presentation.search

import com.droidslife.graphqldemo.domain.model.GitHubRepository

/**
 * UI state for the repository search screen.
 * This class represents the state of the UI at any given time.
 */
data class SearchRepositoriesState(
    val searchQuery: String = "kotlin",
    val repositories: List<GitHubRepository> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)