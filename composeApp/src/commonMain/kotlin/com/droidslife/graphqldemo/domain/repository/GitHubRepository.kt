package com.droidslife.graphqldemo.domain.repository

import com.droidslife.graphqldemo.domain.model.GitHubRepository
import com.droidslife.graphqldemo.domain.model.GitHubRepositoryDetails
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for GitHub operations.
 * This interface defines the contract for accessing GitHub data,
 * regardless of the data source (API, cache, etc.).
 */
interface GitHubRepository {
    /**
     * Search for repositories using a query string.
     *
     * @param query The search query string
     * @return A Flow of Result containing either a list of GitHubRepository objects or an exception
     */
    fun searchRepositories(query: String): Flow<Result<List<GitHubRepository>>>

    /**
     * Get detailed information about a specific repository.
     *
     * @param owner The owner of the repository
     * @param name The name of the repository
     * @return A Flow of Result containing either a GitHubRepositoryDetails object or an exception
     */
    fun getRepositoryDetails(owner: String, name: String): Flow<Result<GitHubRepositoryDetails>>

    /**
     * Get the README content of a specific repository.
     *
     * @param owner The owner of the repository
     * @param name The name of the repository
     * @return A Flow of Result containing either the README content as a string or an exception
     */
//    fun getRepositoryReadme(owner: String, name: String): Flow<Result<String>>
}
