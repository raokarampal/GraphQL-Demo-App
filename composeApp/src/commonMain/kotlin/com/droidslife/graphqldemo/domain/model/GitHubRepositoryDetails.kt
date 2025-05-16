package com.droidslife.graphqldemo.domain.model

/**
 * Domain model representing detailed information about a GitHub repository.
 * This is a clean model that doesn't depend on any external libraries or frameworks.
 */
data class GitHubRepositoryDetails(
    val id: String,
    val name: String,
    val nameWithOwner: String,
    val description: String?,
    val url: String,
    val homepageUrl: String?,
    val stars: Int,
    val forks: Int,
    val isPrivate: Boolean,
    val owner: Owner,
    val primaryLanguage: Language?,
    val languages: List<Language>,
    val issues: List<Issue>,
    val pullRequests: List<PullRequest>,
    val totalIssues: Int,
    val totalPullRequests: Int,
    val readme: Readme,
    val latestRelease: ReleaseDetail?,
    val releases : List<ReleaseDetail>
) {
    /**
     * Owner of the repository
     */
    data class Owner(
        val login: String,
        val avatarUrl: String,
        val url: String
    )

    /**
     * Programming language used in the repository
     */
    data class Language(
        val name: String,
        val color: String?
    )

    /**
     * Issue in the repository
     */
    data class Issue(
        val number: Int,
        val title: String,
        val url: String,
        val state: String,
        val createdAt: String,
        val author: Author?
    )

    /**
     * Pull request in the repository
     */
    data class PullRequest(
        val number: Int,
        val title: String,
        val url: String,
        val state: String,
        val createdAt: String,
        val author: Author?
    )

    /**
     * Author of an issue or pull request
     */
    data class Author(
        val login: String,
        val avatarUrl: String
    )

    data class Readme(
        val text: String,
    )
}