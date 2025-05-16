package com.droidslife.graphqldemo.data.repository

import com.droidslife.graphqldemo.ApolloClientCache
import com.droidslife.graphqldemo.data.mappers.toReleaseDetail
import com.droidslife.graphqldemo.domain.model.GitHubRepository
import com.droidslife.graphqldemo.domain.model.GitHubRepositoryDetails
import com.droidslife.graphqldemo.domain.repository.GitHubRepository as GitHubRepositoryInterface
import com.droidslife.graphqldemo.graphql.RepositoryDetailsQuery
import com.droidslife.graphqldemo.graphql.SearchRepositoriesQuery
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * Implementation of the GitHubRepository interface that uses Apollo GraphQL client
 * to fetch data from the GitHub GraphQL API.
 */
class GitHubRepositoryImpl(
    private val apolloClientCache: ApolloClientCache
) : GitHubRepositoryInterface {

//    // Create a simple Ktor HTTP client for REST API calls
//    private val httpClient = HttpClient()

    /**
     * Search for repositories using the GitHub GraphQL API.
     *
     * @param query The search query string
     * @return A Flow of Result containing either a list of GitHubRepository objects or an exception
     */
    override fun searchRepositories(query: String): Flow<Result<List<GitHubRepository>>> = flow {
        try {
            val apolloClient = apolloClientCache.getApolloClient()
            val response = apolloClient.query(SearchRepositoriesQuery(query = query)).execute()

            if (response.hasErrors()) {
                val errorMessage = response.errors?.firstOrNull()?.message ?: "Unknown error occurred"
                emit(Result.failure(Exception(errorMessage)))
            } else {
                // Map the GraphQL response to our GitHubRepository data class
                val repositories = response.data?.search?.edges?.mapNotNull { edge ->
                    edge?.node?.onRepository?.let { repo ->
                        GitHubRepository(
                            name = repo.name,
                            owner = repo.owner.login,
                            description = repo.description,
                            stars = repo.stargazerCount,
                            forks = repo.forkCount,
                            language = repo.primaryLanguage?.name,
                            release = repo.latestRelease?.releaseFragment?.toReleaseDetail()
                        )
                    }
                } ?: emptyList()

                emit(Result.success(repositories))
            }
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    /**
     * Get detailed information about a specific repository using the GitHub GraphQL API.
     *
     * @param owner The owner of the repository
     * @param name The name of the repository
     * @return A Flow of Result containing either a GitHubRepositoryDetails object or an exception
     */
    override fun getRepositoryDetails(owner: String, name: String): Flow<Result<GitHubRepositoryDetails>> = flow {
        try {
            val apolloClient = apolloClientCache.getApolloClient()
            val response = apolloClient.query(RepositoryDetailsQuery(owner = owner, name = name)).execute()

            if (response.hasErrors()) {
                val errorMessage = response.errors?.firstOrNull()?.message ?: "Unknown error occurred"
                emit(Result.failure(Exception(errorMessage)))
            } else {
                val repoData = response.data?.repository
                if (repoData != null) {
                    val readmeContent =
                        repoData.obj1?.readmeContent ?: repoData.obj2?.readmeContent ?: repoData.obj3?.readmeContent
                        ?: repoData.obj4?.readmeContent ?: repoData.obj5?.readmeContent
                    val readme = GitHubRepositoryDetails.Readme(
                        readmeContent?.text ?: "No Readme.md Found"
                    )
                    // Map the GraphQL response to our GitHubRepositoryDetails data class
                    val repositoryDetails = GitHubRepositoryDetails(
                        id = repoData.id,
                        name = repoData.name,
                        nameWithOwner = repoData.nameWithOwner,
                        description = repoData.description,
                        url = repoData.url,
                        homepageUrl = repoData.homepageUrl,
                        stars = repoData.stargazerCount,
                        forks = repoData.forkCount,
                        isPrivate = repoData.isPrivate,
                        owner = GitHubRepositoryDetails.Owner(
                            login = repoData.owner.login,
                            avatarUrl = repoData.owner.avatarUrl,
                            url = repoData.owner.url
                        ),
                        primaryLanguage = repoData.primaryLanguage?.let {
                            GitHubRepositoryDetails.Language(
                                name = it.name,
                                color = it.color
                            )
                        },
                        languages = repoData.languages.nodes?.mapNotNull { node ->
                            node?.let {
                                GitHubRepositoryDetails.Language(
                                    name = it.name,
                                    color = it.color
                                )
                            }
                        } ?: emptyList(),
                        issues = repoData.issues.nodes?.mapNotNull { node ->
                            node?.let {
                                GitHubRepositoryDetails.Issue(
                                    number = it.number,
                                    title = it.title,
                                    url = it.url,
                                    state = it.state.toString(),
                                    createdAt = it.createdAt,
                                    author = it.author?.let { author ->
                                        GitHubRepositoryDetails.Author(
                                            login = author.login,
                                            avatarUrl = author.avatarUrl
                                        )
                                    }
                                )
                            }
                        } ?: emptyList(),
                        pullRequests = repoData.pullRequests.nodes?.mapNotNull { node ->
                            node?.let {
                                GitHubRepositoryDetails.PullRequest(
                                    number = it.number,
                                    title = it.title,
                                    url = it.url,
                                    state = it.state.toString(),
                                    createdAt = it.createdAt,
                                    author = it.author?.let { author ->
                                        GitHubRepositoryDetails.Author(
                                            login = author.login,
                                            avatarUrl = author.avatarUrl
                                        )
                                    }
                                )
                            }
                        } ?: emptyList(),
                        totalIssues = repoData.issues.totalCount,
                        totalPullRequests = repoData.pullRequests.totalCount,
                        readme = readme,
                        latestRelease = repoData.latestRelease?.releaseFragment?.toReleaseDetail(),
                        releases = repoData.releases.nodes?.mapNotNull { it?.releaseFragment?.toReleaseDetail() }
                            ?: emptyList()
                    )

                    emit(Result.success(repositoryDetails))
                } else {
                    emit(Result.failure(Exception("Repository not found")))
                }
            }
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    /**
     * Get the README content of a specific repository using the GitHub REST API.
     *
     * @param owner The owner of the repository
     * @param name The name of the repository
     * @return A Flow of Result containing either the README content as a string or an exception
     */
//    override fun getRepositoryReadme(owner: String, name: String): Flow<Result<String>> = flow {
//        try {
//            // Get the GitHub token from BuildConfig
//            val githubToken = BuildConfig.GITHUB_TOKEN
//
//            // Make a request to the GitHub REST API to get the README content
//            val response = httpClient.get("https://api.github.com/repos/$owner/$name/readme") {
//                headers {
//                    append("Authorization", "Bearer $githubToken")
//                    append("Accept", "application/vnd.github.raw")
//                }
//            }
//
//            // Check if the request was successful
//            if (response.status.value in 200..299) {
//                // Get the README content as a string
//                val readmeContent = response.bodyAsText()
//                emit(Result.success(readmeContent))
//            } else {
//                // If the request failed, emit a failure with an error message
//                emit(Result.failure(Exception("Failed to fetch README: ${response.status.value}")))
//            }
//        } catch (e: Exception) {
//            emit(Result.failure(e))
//        }
//    }
}
