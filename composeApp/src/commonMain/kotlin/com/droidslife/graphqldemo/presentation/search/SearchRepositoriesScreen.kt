package com.droidslife.graphqldemo.presentation.search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.outlined.Info

import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton

import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.droidslife.graphqldemo.config.BuildConfig
import com.droidslife.graphqldemo.domain.model.GitHubRepository
import com.droidslife.graphqldemo.presentation.components.GitHubTokenInfoDialog
import com.droidslife.graphqldemo.presentation.components.RepositoryItem
import com.droidslife.graphqldemo.presentation.details.RepositoryDetailsScreen
import com.droidslife.graphqldemo.theme.LocalThemeIsDark
import com.droidslife.graphqldemo.util.WindowSize
import com.droidslife.graphqldemo.util.WindowWidthSizeClass
import graphql_demo_app.composeapp.generated.resources.*
import org.jetbrains.compose.resources.vectorResource
import org.koin.compose.koinInject


/**
 * Composable for the repository search screen.
 * This screen allows users to search for GitHub repositories and displays the results.
 */
@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)
@Composable
fun SearchRepositoriesScreen() {
    val viewModel = koinInject<SearchRepositoriesViewModel>()
    val state = viewModel.state

    // State for showing the GitHub token info dialog
    var showTokenInfoDialog by remember { mutableStateOf(false) }

    // State for the selected repository
    var selectedRepository by remember { mutableStateOf<GitHubRepository?>(null) }

    // Check if the token is the fake token
    val isDummyToken = remember { BuildConfig.GITHUB_TOKEN == "ghp_1234567890abcdefghijklmnopqrstuvwxyz" }

    // Show the dialog if the token is the fake token or if there's an error related to authentication
    val showDialogForAuthError = state.errorMessage?.contains("Bad credentials", ignoreCase = true) == true

    // Show the dialog automatically if it's a fake token or auth error
    LaunchedEffect(isDummyToken, showDialogForAuthError) {
        if (isDummyToken || showDialogForAuthError) {
            showTokenInfoDialog = true
        }
    }

    // Show the GitHub token info dialog if needed
    if (showTokenInfoDialog) {
        GitHubTokenInfoDialog(
            onDismissRequest = { showTokenInfoDialog = false },
            onConfirm = { showTokenInfoDialog = false }
        )
    }
    // Use WindowSize to determine the layout based on screen width
    WindowSize { windowWidthSizeClass ->
    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    if (windowWidthSizeClass == WindowWidthSizeClass.COMPACT && selectedRepository != null){
                        IconButton(onClick = {
                            selectedRepository = null
                        }){
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                        }
                    }
                },
                title = { Text("GitHub GraphQL Demo") },
                actions = {
                    // Theme toggle button
                    var isDark by LocalThemeIsDark.current
                    val themeIcon = remember(isDark) {
                        if (isDark) Res.drawable.ic_light_mode
                        else Res.drawable.ic_dark_mode
                    }
                    val themeDescription = if (isDark) "Switch to Light Mode" else "Switch to Dark Mode"

                    IconToggleButton(
                        checked = isDark,
                        onCheckedChange = { isDark = it }
                    ) {
                        Icon(
                            vectorResource(themeIcon),
                            contentDescription = themeDescription
                        )
                    }

                    // Info button
                    IconButton(onClick = { showTokenInfoDialog = true }) {
                        Icon(
                            Icons.Filled.Info,
                            contentDescription = "GitHub Token Info"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->

            when (windowWidthSizeClass) {
                // Mobile layout - show list, then details on click
                WindowWidthSizeClass.COMPACT -> {
                    if (selectedRepository != null) {
                        RepositoryDetailsScreen(
                            owner = selectedRepository!!.owner,
                            name = selectedRepository!!.name,
                            onBackClick = { selectedRepository = null }
                        )
                    } else {
                        // Show list screen
                        RepositoryListContent(
                            state = state,
                            viewModel = viewModel,
                            onRepositoryClick = { selectedRepository = it },
                            modifier = Modifier.padding(paddingValues).padding(16.dp)
                        )
                    }
                }

                // Tablet/Desktop layout - show list and details side by side
                WindowWidthSizeClass.MEDIUM, WindowWidthSizeClass.EXPANDED -> {
                    Row(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
                        // List panel (1/3 of the screen)
                        Box(modifier = Modifier.weight(3f).fillMaxHeight()) {
                            RepositoryListContent(
                                state = state,
                                viewModel = viewModel,
                                onRepositoryClick = { selectedRepository = it },
                                modifier = Modifier.padding(16.dp)
                            )
                        }

                        // Divider
                        HorizontalDivider(
                            modifier = Modifier.width(1.dp).fillMaxHeight(),
                            thickness = DividerDefaults.Thickness,
                            color = DividerDefaults.color
                        )

                        // Details panel (2/3 of the screen)
                        Box(modifier = Modifier.weight(4f).fillMaxHeight()) {
                            if (selectedRepository != null) {
                                RepositoryDetailsScreen(
                                    owner = selectedRepository!!.owner,
                                    name = selectedRepository!!.name
                                )
                            } else {
                                // Placeholder when no repository is selected
                                Box(
                                    modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "Select a repository to view details",
                                        style = MaterialTheme.typography.titleMedium
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

/**
 * Composable for the repository list content.
 * This is extracted to avoid duplication between mobile and desktop layouts.
 */
@Composable
private fun RepositoryListContent(
    state: SearchRepositoriesState,
    viewModel: SearchRepositoriesViewModel,
    onRepositoryClick: (GitHubRepository) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxSize()) {
        // Search bar
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = state.searchQuery,
                onValueChange = { viewModel.onSearchQueryChanged(it) },
                modifier = Modifier.weight(1f),
                label = { Text("Search repositories") },
                singleLine = true,
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(
                    onSearch = { viewModel.onSearchClicked() }
                )
            )

            Spacer(modifier = Modifier.width(8.dp))

            Button(
                onClick = { viewModel.onSearchClicked() },
                enabled = !state.isLoading && state.searchQuery.isNotBlank()
            ) {
                Text("Search")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Loading indicator
        if (state.isLoading) {
            Box(
                modifier = Modifier.fillMaxWidth(),
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

        // Repository list
        Text(
            text = "Found ${state.repositories.size} repositories",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(vertical = 8.dp)
        )

        LazyColumn(
            modifier = Modifier.weight(1f)
        ) {
            items(state.repositories) { repo ->
                Box(
                    modifier = Modifier.clickable { onRepositoryClick(repo) }
                ) {
                    RepositoryItem(repo)
                }
            }
        }

        // GitHub link
        val uriHandler = LocalUriHandler.current
        TextButton(
            onClick = { uriHandler.openUri("https://github.com/apollographql/apollo-kotlin") },
            modifier = Modifier.align(Alignment.End)
        ) {
            Text("Apollo Kotlin")
        }
    }
}
