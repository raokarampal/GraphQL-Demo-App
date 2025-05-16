package com.droidslife.graphqldemo.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * A dialog that displays information about the GitHub token.
 * This dialog is shown when the app is run with a dummy token or when the user clicks the info button.
 */
@Composable
fun GitHubTokenInfoDialog(
    onDismissRequest: () -> Unit,
    onConfirm: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = {
            Text(
                text = "GitHub GraphQL API Demo",
                style = MaterialTheme.typography.titleMedium
            )
        },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "This app demonstrates how to use the GitHub GraphQL API with Apollo Kotlin.",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Text(
                    text = "The app is now fetching real repositories from GitHub using GraphQL. For production use, replace the token in build.gradle.kts with your own.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = onConfirm
            ) {
                Text("OK")
            }
        }
    )
}