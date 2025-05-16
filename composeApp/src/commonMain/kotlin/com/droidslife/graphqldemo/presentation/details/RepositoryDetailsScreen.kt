package com.droidslife.graphqldemo.presentation.details

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.mikepenz.markdown.coil3.Coil3ImageTransformerImpl
import com.mikepenz.markdown.m3.Markdown
import org.koin.compose.koinInject

/**
 * Composable for the repository details screen.
 * This screen displays detailed information about a GitHub repository.
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun RepositoryDetailsScreen(
    owner: String,
    name: String,
    onBackClick: () -> Unit = {}
) {
    val viewModel = koinInject<RepositoryDetailsViewModel>()
    val state = viewModel.state
    val uriHandler = LocalUriHandler.current

    // Load repository details when the screen is first composed
    androidx.compose.runtime.LaunchedEffect(owner, name) {
        viewModel.loadRepositoryDetails(owner, name)
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp)
    ) {
        // Loading indicator
        if (state.isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        // Error message
        state.errorMessage?.let {
            Text(
                text = it,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }

        // Repository details
        state.repositoryDetails?.let { repo ->
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                item {
                    // Repository name and owner
                    Text(
                        text = repo.nameWithOwner,
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Description
                    repo.description?.let {
                        Text(
                            text = it,
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    }

                    // Stats row
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "⭐ ${repo.stars}",
                            style = MaterialTheme.typography.bodyMedium
                        )

                        Text(
                            text = "🍴 ${repo.forks}",
                            style = MaterialTheme.typography.bodyMedium
                        )

                        repo.primaryLanguage?.let {
                            Text(
                                text = it.name,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }

                    // Links

                    FlowRow(
                        modifier = Modifier.padding(8.dp),
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                    ) {
                        OutlinedButton(onClick = { uriHandler.openUri(repo.url) }) { Text("Repo URL") }

                        repo.homepageUrl?.let {
                            OutlinedButton(
                                onClick = { uriHandler.openUri(it) }
                            ) {
                                Text("Homepage")
                            }

                        }
                        OutlinedButton(
                            onClick = { uriHandler.openUri("https://github.com/${repo.owner.login}") }
                        ) {
                            Text("Owner")
                        }
                        repo.latestRelease?.let {
                            OutlinedButton(
                                onClick = { uriHandler.openUri(it.url) }
                            ) {
                                Text(it.name)
                            }
                        }
                    }


                    // Languages
                    if (repo.languages.isNotEmpty()) {
                        Card(
                            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp)
                            ) {
                                Text(
                                    text = "Languages",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                FlowRow {
                                    repo.languages.forEach { language ->
//                                        AssistChip(onClick = {},
//                                            modifier = Modifier.padding(horizontal = 4.dp),
//                                            label = {
                                            Text(
                                                text = "• ${language.name}",
                                                style = MaterialTheme.typography.bodyMedium,
                                                modifier = Modifier.padding(horizontal = 4.dp),
                                            )
//                                        })
                                    }
                                }
                            }
                        }
                    }

                    // Issues
                    if (repo.issues.isNotEmpty()) {
                        Card(
                            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp)
                            ) {
                                Text(
                                    text = "Open Issues (${repo.totalIssues})",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )

                                Spacer(modifier = Modifier.height(8.dp))

                                repo.issues.forEach { issue ->
                                    TextButton(
                                        onClick = { uriHandler.openUri(issue.url) },
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Text(
                                            text = "#${issue.number}: ${issue.title}",
                                            style = MaterialTheme.typography.bodyMedium
                                        )
                                    }
                                }
                            }
                        }
                    }

                    // Pull Requests
                    if (repo.pullRequests.isNotEmpty()) {
                        Card(
                            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp)
                            ) {
                                Text(
                                    text = "Open Pull Requests (${repo.totalPullRequests})",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )

                                Spacer(modifier = Modifier.height(8.dp))

                                repo.pullRequests.forEach { pr ->
                                    TextButton(
                                        onClick = { uriHandler.openUri(pr.url) },
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Text(
                                            text = "#${pr.number}: ${pr.title}",
                                            style = MaterialTheme.typography.bodyMedium
                                        )
                                    }
                                }
                            }
                        }
                    }

                    // README
                    Card(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text(
                                text = "README",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            // Show loading indicator while README is loading
                            if (state.isReadmeLoading) {
                                Box(
                                    modifier = Modifier.fillMaxWidth().height(200.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    CircularProgressIndicator()
                                }
                            }
                            // Show error message if README loading failed
                            else if (state.readmeError != null) {
                                Text(
                                    text = state.readmeError,
                                    color = MaterialTheme.colorScheme.error,
                                    modifier = Modifier.padding(vertical = 8.dp)
                                )
                            }
                            // Show README content if available
                            else {
                                Markdown(
                                    state.repositoryDetails.readme.text,
                                    imageTransformer = Coil3ImageTransformerImpl,
                                )
//                                val richTextState = rememberRichTextState()
//                                richTextState.config.linkColor = MaterialTheme.colorScheme.primary
//
//                                // Set the markdown content
//                                richTextState.setMarkdown(state.readmeContent)
//
//                                RichText(
//                                    state = richTextState,
//                                    modifier = Modifier.fillMaxWidth()
//                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
