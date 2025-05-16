package com.droidslife.graphqldemo

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.runComposeUiTest
import kotlin.test.Test

@OptIn(ExperimentalTestApi::class)
class SimpleButtonTest {

    @Test
    fun testButtonCounter() = runComposeUiTest {
        setContent {
            var counter by remember { mutableStateOf(0) }
            Column {
                Text(
                    text = "Count: $counter",
                    modifier = Modifier.testTag("counter_text")
                )
                Button(
                    onClick = { counter += 1 },
                    modifier = Modifier.testTag("increment_button")
                ) {
                    Text("Increment")
                }
            }
        }

        // Initial state check
        onNodeWithTag("counter_text").assertTextEquals("Count: 0")
        
        // Click the button once
        onNodeWithTag("increment_button").performClick()
        onNodeWithTag("counter_text").assertTextEquals("Count: 1")
        
        // Click the button two more times
        onNodeWithTag("increment_button").performClick()
        onNodeWithTag("increment_button").performClick()
        onNodeWithTag("counter_text").assertTextEquals("Count: 3")
    }
}