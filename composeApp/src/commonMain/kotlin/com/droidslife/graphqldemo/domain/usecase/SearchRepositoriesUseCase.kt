package com.droidslife.graphqldemo.domain.usecase

import com.droidslife.graphqldemo.domain.model.GitHubRepository
import com.droidslife.graphqldemo.domain.repository.GitHubRepository as GitHubRepositoryInterface
import kotlinx.coroutines.flow.Flow

/**
 * Use case for searching GitHub repositories.
 * This class encapsulates the business logic for searching repositories.
 */
class SearchRepositoriesUseCase(
    private val repository: GitHubRepositoryInterface
) {
    /**
     * Execute the use case to search for repositories.
     *
     * @param query The search query string
     * @return A Flow of Result containing either a list of GitHubRepository objects or an exception
     */
    operator fun invoke(query: String): Flow<Result<List<GitHubRepository>>> {
        return repository.searchRepositories(query)
    }
}