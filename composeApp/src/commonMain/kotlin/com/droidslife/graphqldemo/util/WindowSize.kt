package com.droidslife.graphqldemo.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.runtime.remember

/**
 * Window size class for responsive UI
 */
enum class WindowWidthSizeClass {
    COMPACT, // Phone (< 600dp)
    MEDIUM,  // Tablet/small laptop (600dp - 840dp)
    EXPANDED // Large tablet/desktop (> 840dp)
}

/**
 * Composable that provides the current window width size class
 */
@Composable
fun WindowSize(content: @Composable (WindowWidthSizeClass) -> Unit) {
    BoxWithConstraints {
        // Calculate the window width size class directly based on maxWidth
        // This ensures it's recalculated whenever the constraints change
        val windowWidthSizeClass = when {
            maxWidth < 600.dp -> WindowWidthSizeClass.COMPACT
            maxWidth < 840.dp -> WindowWidthSizeClass.MEDIUM
            else -> WindowWidthSizeClass.EXPANDED
        }

        content(windowWidthSizeClass)
    }
}
