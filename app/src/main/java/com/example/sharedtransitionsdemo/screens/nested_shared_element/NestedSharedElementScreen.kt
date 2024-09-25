@file:OptIn(ExperimentalSharedTransitionApi::class)

package com.example.sharedtransitionsdemo.screens.nested_shared_element

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeightIn
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Create
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.resizescreenapp.navigation.NavigationDestination
import com.example.sharedtransitionsdemo.ui.composable.AppBar
import kotlinx.serialization.Serializable

@Serializable
object NestedSharedElementDestination : NavigationDestination

@Composable
fun NestedSharedElementScreen(
    navigateBack: () -> Unit,
) {
    SharedTransitionLayout {
        var menuExpanded by remember { mutableStateOf(false) }

        Scaffold(
            modifier = Modifier,
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            topBar = { AppBar(title = "Nested Shared Element", onBackClick = navigateBack) },
            floatingActionButton = {
                AnimatedVisibility(
                    visible = menuExpanded,
                    enter = EnterTransition.None,
                    exit = ExitTransition.None
                ) {
                    Box(modifier = Modifier.fillMaxWidth()) {
                        Surface(
                            Modifier
                                .align(Alignment.BottomCenter)
                                .padding(20.dp)
                                .sharedBounds(
                                    rememberSharedContentState(key = "container"),
                                    animatedVisibilityScope = this@AnimatedVisibility
                                )
                                .requiredHeightIn(max = 60.dp),
                            shape = RoundedCornerShape(50),
                        ) {
                            Row(
                                Modifier
                                    .padding(10.dp)
                                    // By using Modifier.skipToLookaheadSize(), we are telling the layout
                                    // system to layout the children of this node as if the animations had
                                    // all finished. This avoid re-laying out the Row with animated width,
                                    // which is desirable here.

                                    // Try removing this modifier and observe the effect.
                                    .skipToLookaheadSize()
                            ) {
                                Icon(
                                    Icons.Outlined.Share,
                                    contentDescription = "Share",
                                    modifier = Modifier.padding(10.dp)
                                )
                                Icon(
                                    Icons.Outlined.Favorite,
                                    contentDescription = "Favorite",
                                    modifier = Modifier.padding(10.dp)
                                )
                                Icon(
                                    Icons.Outlined.DateRange,
                                    contentDescription = "Save to Calendar",
                                    modifier = Modifier.padding(10.dp)
                                )
                                Icon(
                                    Icons.Outlined.Create,
                                    contentDescription = "Create",
                                    tint = Color.White,
                                    modifier = Modifier
                                        .sharedElement(
                                            rememberSharedContentState(key = "icon_background"),
                                            animatedVisibilityScope = this@AnimatedVisibility
                                        )
                                        .background(
                                            MaterialTheme.colorScheme.primary,
                                            RoundedCornerShape(50)
                                        )
                                        .padding(vertical = 10.dp, horizontal = 20.dp)
                                        .sharedElement(
                                            rememberSharedContentState(key = "icon"),
                                            animatedVisibilityScope = this@AnimatedVisibility
                                        )
                                        .clickable { menuExpanded = !menuExpanded }
                                )
                            }
                        }
                    }
                }
                AnimatedVisibility(
                    visible = !menuExpanded,
                    enter = EnterTransition.None,
                    exit = ExitTransition.None
                ) {
                    Box(modifier = Modifier.fillMaxWidth()) {
                        Surface(
                            Modifier
                                .align(Alignment.BottomEnd)
                                .padding(30.dp)
                                .sharedBounds(
                                    rememberSharedContentState(key = "container"),
                                    animatedVisibilityScope = this@AnimatedVisibility,
                                    enter = EnterTransition.None,
                                )
                                .sharedElement(
                                    rememberSharedContentState(key = "icon_background"),
                                    animatedVisibilityScope = this@AnimatedVisibility,
                                )
                                .clickable { menuExpanded = !menuExpanded },
                            shape = RoundedCornerShape(30.dp),
                            color = MaterialTheme.colorScheme.primary
                        ) {
                            Icon(
                                Icons.Outlined.Create,
                                contentDescription = "Create",
                                tint = Color.White,
                                modifier = Modifier
                                    .padding(30.dp)
                                    .size(40.dp)
                                    .sharedElement(
                                        rememberSharedContentState(key = "icon"),
                                        animatedVisibilityScope = this@AnimatedVisibility
                                    )
                            )
                        }
                    }
                }
            },
        ) { pv ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(pv)
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "Main Content")
            }
        }
    }
}

@Preview
@Composable
private fun Preview() {
    NestedSharedElementScreen {}
}