package com.example.sharedtransitionsdemo.screens.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.resizescreenapp.navigation.NavigationDestination
import com.example.sharedtransitionsdemo.ui.theme.SharedTransitionsDemoTheme
import kotlinx.serialization.Serializable

@Serializable
object MainDestination : NavigationDestination

@Composable
fun MainScreen(
    navigateToListDetails: () -> Unit,
    navigateToListDetailsCards: () -> Unit,
    navigateToSiblingLayouts: () -> Unit,
    navigateToAnimationSpecDemo: () -> Unit,
    navigateToManagedVisibility: () -> Unit,
    navigateToAnimatedVisibility: () -> Unit,
    navigateToNestedSharedElement: () -> Unit,
) {
    Scaffold { pv ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(pv),
            contentAlignment = Alignment.Center,
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Button(onClick = { navigateToListDetails() }) {
                    Text("1. List-Details Example")
                }
                Button(onClick = { navigateToListDetailsCards() }) {
                    Text("2. List-Details Cards Example")
                }
                Button(onClick = { navigateToSiblingLayouts() }) {
                    Text("3. Sibling Layouts")
                }
                Button(onClick = { navigateToAnimationSpecDemo() }) {
                    Text("4. Animation Spec Demo")
                }
                Button(onClick = { navigateToManagedVisibility() }) {
                    Text("5. Manually Managed Visibility")
                }
                Button(onClick = { navigateToAnimatedVisibility() }) {
                    Text("6. Animated Visibility")
                }
                Button(onClick = { navigateToNestedSharedElement() }) {
                    Text("7. Nested Shared Element")
                }
            }
        }
    }
}

@Preview
@Composable
private fun Preview() {
    SharedTransitionsDemoTheme {
        MainScreen(
            navigateToListDetails = {},
            navigateToListDetailsCards = {},
            navigateToSiblingLayouts = {},
            navigateToAnimationSpecDemo = {},
            navigateToManagedVisibility = {},
            navigateToAnimatedVisibility = {},
            navigateToNestedSharedElement = {},
        )
    }
}