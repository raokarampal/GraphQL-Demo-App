package com.droidslife.graphqldemo.presentation.search

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.droidslife.graphqldemo.domain.usecase.SearchRepositoriesUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

/**
 * ViewModel for the repository search screen.
 * This class handles the business logic for the search screen and manages its state.
 */
class SearchRepositoriesViewModel(
    private val searchRepositoriesUseCase: SearchRepositoriesUseCase,
    private val coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.Main)
) {
    var state by mutableStateOf(SearchRepositoriesState())
        private set

    init {
        // Search for repositories when the ViewModel is created
        searchRepositories(state.searchQuery)
    }

    /**
     * Update the search query and trigger a search if the query is not blank.
     *
     * @param query The new search query
     */
    fun onSearchQueryChanged(query: String) {
        state = state.copy(searchQuery = query)
    }

    /**
     * Trigger a search for repositories using the current search query.
     */
    fun onSearchClicked() {
        if (state.searchQuery.isNotBlank()) {
            searchRepositories(state.searchQuery)
        }
    }

    /**
     * Search for repositories using the provided query.
     *
     * @param query The search query
     */
    private fun searchRepositories(query: String) {
        state = state.copy(isLoading = true, errorMessage = null)

        searchRepositoriesUseCase(query)
            .onEach { result ->
                result.fold(
                    onSuccess = { repositories ->
                        state = state.copy(
                            repositories = repositories,
                            isLoading = false,
                            errorMessage = null
                        )
                    },
                    onFailure = { error ->
                        state = state.copy(
                            repositories = emptyList(),
                            isLoading = false,
                            errorMessage = error.message ?: "Unknown error occurred"
                        )
                    }
                )
            }
            .catch { error ->
                state = state.copy(
                    repositories = emptyList(),
                    isLoading = false,
                    errorMessage = error.message ?: "Unknown error occurred"
                )
            }
            .launchIn(coroutineScope)
    }
}