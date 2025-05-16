package com.droidslife.graphqldemo.domain.model

/**
 * Domain model representing a GitHub repository.
 * This is a clean model that doesn't depend on any external libraries or frameworks.
 */
data class GitHubRepository(
    val name: String,
    val owner: String,
    val description: String?,
    val stars: Int,
    val forks: Int,
    val language: String?,
    val release: ReleaseDetail?,
)

data class ReleaseDetail(
    val name:String,
    val tagName:String,
    val url: String,
    val isLatest: Boolean,
    val description: String?,
    val releaseDate: String,
    val updatedAt: String,
)