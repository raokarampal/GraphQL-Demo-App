package com.droidslife.graphqldemo.presentation.details

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.droidslife.graphqldemo.domain.model.GitHubRepositoryDetails
import com.droidslife.graphqldemo.domain.repository.GitHubRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

/**
 * ViewModel for the repository details screen.
 * This class is responsible for fetching and managing the repository details data.
 */
class RepositoryDetailsViewModel(
    private val repository: GitHubRepository,
    private val coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.Main)
) {
    var state by mutableStateOf(RepositoryDetailsState())
        private set

    /**
     * Load repository details for the specified owner and repository name.
     */
    fun loadRepositoryDetails(owner: String, name: String) {
        state = state.copy(isLoading = true, errorMessage = null)

        repository.getRepositoryDetails(owner, name)
            .onEach { result ->
                result.fold(
                    onSuccess = { repositoryDetails ->
                        state = state.copy(
                            isLoading = false,
                            repositoryDetails = repositoryDetails,
                            errorMessage = null,
                        )

                        // After loading repository details, load the README content
//                        loadReadmeContent(owner, name)
                    },
                    onFailure = { error ->
                        state = state.copy(
                            isLoading = false,
                            errorMessage = error.message ?: "Unknown error occurred"
                        )
                    }
                )
            }
            .catch { error ->
                state = state.copy(
                    isLoading = false,
                    errorMessage = error.message ?: "Unknown error occurred"
                )
            }
            .launchIn(coroutineScope)
    }

    /**
     * Load README content for the specified owner and repository name.
     */
//    fun loadReadmeContent(owner: String, name: String) {
//        state = state.copy(isReadmeLoading = true, readmeError = null)
//
//        repository.getRepositoryReadme(owner, name)
//            .onEach { result ->
//                result.fold(
//                    onSuccess = { readmeContent ->
//                        state = state.copy(
//                            isReadmeLoading = false,
//                            readmeContent = readmeContent,
//                            readmeError = null
//                        )
//                    },
//                    onFailure = { error ->
//                        state = state.copy(
//                            isReadmeLoading = false,
//                            readmeError = error.message ?: "Failed to load README"
//                        )
//                    }
//                )
//            }
//            .catch { error ->
//                state = state.copy(
//                    isReadmeLoading = false,
//                    readmeError = error.message ?: "Failed to load README"
//                )
//            }
//            .launchIn(coroutineScope)
//    }
}

/**
 * State for the repository details screen.
 */
data class RepositoryDetailsState(
    val isLoading: Boolean = false,
    val repositoryDetails: GitHubRepositoryDetails? = null,
    val readmeContent: String? = null,
    val isReadmeLoading: Boolean = false,
    val readmeError: String? = null,
    val errorMessage: String? = null
)
