package com.droidslife.graphqldemo

import androidx.compose.runtime.Composable
import coil3.ImageLoader
import coil3.compose.setSingletonImageLoaderFactory
import coil3.request.crossfade
import com.droidslife.graphqldemo.presentation.search.SearchRepositoriesScreen
import com.droidslife.graphqldemo.theme.AppTheme

/**
 * Main app composable function
 */
@Composable
internal fun App() = AppTheme {
    setSingletonImageLoaderFactory { context ->
        ImageLoader.Builder(context)
            .crossfade(true)
            .build()
    }
    SearchRepositoriesScreen()
}