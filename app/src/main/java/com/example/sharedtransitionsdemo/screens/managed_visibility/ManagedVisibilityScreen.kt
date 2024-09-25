@file:OptIn(ExperimentalSharedTransitionApi::class)

package com.example.sharedtransitionsdemo.screens.managed_visibility

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.resizescreenapp.navigation.NavigationDestination
import com.example.sharedtransitionsdemo.ui.composable.AppBar
import com.example.sharedtransitionsdemo.ui.theme.SharedTransitionsDemoTheme
import kotlinx.serialization.Serializable

@Serializable
object ManagedVisibilityDestination : NavigationDestination

@Composable
fun ManagedVisibilityScreen(
    navigateBack: () -> Unit,
) {
    var selectItem by rememberSaveable { mutableStateOf(false) }

    SharedTransitionLayout {
        Scaffold(
            topBar = { AppBar(title = "Manually Managed Visibility", onBackClick = navigateBack) }
        ) { pv ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(pv)
                    .padding(16.dp),
            ) {
                Box(
                    modifier = Modifier
                        // Show the item only if it is not selected.
                        .sharedElementWithCallerManagedVisibility(
                            rememberSharedContentState(key = "visibility"),
                            visible = !selectItem
                        )
                        .size(120.dp)
                        .clip(MaterialTheme.shapes.medium)
                        .background(MaterialTheme.colorScheme.primaryContainer)
                        .clickable { selectItem = !selectItem },
                    contentAlignment = Alignment.Center,
                ) {
                    Text("Item")
                }
                Box(
                    modifier = Modifier
                        // Show the item only if it is selected.
                        // When the visible element is changed, an animated transition will occur.
                        .sharedElementWithCallerManagedVisibility(
                            rememberSharedContentState(key = "visibility"),
                            visible = selectItem
                        )
                        .size(200.dp)
                        .clip(MaterialTheme.shapes.medium)
                        .background(MaterialTheme.colorScheme.primaryContainer)
                        .align(Alignment.Center)
                        .clickable { selectItem = !selectItem },
                    contentAlignment = Alignment.Center,
                ) {
                    Text("Item")
                }
            }
        }
    }
}

@Preview
@Composable
private fun Preview() {
    SharedTransitionsDemoTheme {
        ManagedVisibilityScreen {}
    }
}
