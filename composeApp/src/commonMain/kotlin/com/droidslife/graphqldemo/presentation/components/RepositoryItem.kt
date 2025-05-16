package com.droidslife.graphqldemo.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.droidslife.graphqldemo.domain.model.GitHubRepository

/**
 * Composable for displaying a repository item.
 * This is a reusable UI component that can be used in different screens.
 */
@Composable
fun RepositoryItem(repo: GitHubRepository) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Box {
            Row(
                modifier = Modifier.align(Alignment.TopEnd) .padding(16.dp),
            ) {
                repo.release?.let {
                    Text(
                        text = it.name,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.primary,
                    )
                }
            }
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "${repo.owner}/${repo.name}",
                    style = MaterialTheme.typography.titleMedium
                )

                repo.description?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodyMedium,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "⭐ ${repo.stars}",
                        style = MaterialTheme.typography.bodySmall
                    )

                    Text(
                        text = "🍴 ${repo.forks}",
                        style = MaterialTheme.typography.bodySmall
                    )

                    repo.language?.let {
                        Text(
                            text = it,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
        }
    }
}