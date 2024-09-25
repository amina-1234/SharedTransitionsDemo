@file:OptIn(ExperimentalSharedTransitionApi::class)

package com.example.sharedtransitionsdemo.screens.list_details

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.resizescreenapp.navigation.NavigationDestination
import com.example.sharedtransitionsdemo.LocalSharedTransitionScope
import com.example.sharedtransitionsdemo.getLocalSharedTransitionScope
import com.example.sharedtransitionsdemo.model.Item
import com.example.sharedtransitionsdemo.model.items
import com.example.sharedtransitionsdemo.ui.composable.AppBar
import com.example.sharedtransitionsdemo.ui.theme.SharedTransitionsDemoTheme
import kotlinx.serialization.Serializable

@Serializable
object ListDetailsDestination : NavigationDestination

@Serializable
private object ListContentDestination : NavigationDestination

@Serializable
private data class DetailsContentDestination(
    val itemIndex: Int,
) : NavigationDestination

@Composable
fun ListDetailsScreen(
    navigateBack: () -> Unit,
) {
    val navController = rememberNavController()

    SharedTransitionLayout {
        CompositionLocalProvider(
            // Provide SharedTransitionScope to the entire navigation graph
            LocalSharedTransitionScope provides this
        ) {
            NavHost(
                navController = navController,
                startDestination = ListContentDestination,
            ) {
                composable<ListContentDestination> {
                    ListContent(
                        navigateToDetails = { item ->
                            navController.navigate(DetailsContentDestination(item))
                        },
                        navigateBack = navigateBack,
                    )
                }
                composable<DetailsContentDestination> { navBackStackEntry ->
                    val destination: DetailsContentDestination = navBackStackEntry.toRoute()
                    val item = items.first { it.id == destination.itemIndex }

                    DetailsContent(
                        item = item,
                        navigateBack = {
                            navController.navigateUp()
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun AnimatedContentScope.ListContent(
    navigateToDetails: (Int) -> Unit,
    navigateBack: () -> Unit,
) {
    with(getLocalSharedTransitionScope()) {
        Scaffold(
            topBar = {
                AppBar(
                    modifier = Modifier
                        // Keep top bar always on top during animation
                        .renderInSharedTransitionScopeOverlay(
                            zIndexInOverlay = 1f,
                        )
                        // Make top bar title change when navigating to/from the Details smoother
                        .animateEnterExit(),
                    title = "List",
                    onBackClick = navigateBack
                )
            }
        ) { pv ->
            LazyColumn(
                modifier = Modifier.padding(pv),
                contentPadding = PaddingValues(vertical = 16.dp)
            ) {
                items(items) { item ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp)
                            .clickable { navigateToDetails(item.id) },
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Image(
                            painter = painterResource(id = item.image),
                            contentScale = ContentScale.FillHeight,
                            contentDescription = null,
                            modifier = Modifier
                                .size(86.dp)
                                .clip(CircleShape)
                                .sharedElement(
                                    rememberSharedContentState(key = "image/${item.id}"),
                                    animatedVisibilityScope = this@ListContent,
                                    // To make corners rounded during animated transition
                                    clipInOverlayDuringTransition = OverlayClip(CircleShape)
                                )
                        )
                        Text(
                            text = "Item ${item.id}",
                            modifier = Modifier
//                                .sharedElement(
//                                    rememberSharedContentState(key = "title/${item.id}"),
//                                    animatedVisibilityScope = this@ListContent,
//                                )
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun AnimatedContentScope.DetailsContent(
    item: Item,
    navigateBack: () -> Unit
) {
    with(getLocalSharedTransitionScope()) {
        Scaffold(
            topBar = {
                AppBar(
                    title = "Details",
                    onBackClick = navigateBack
                )
            },
        ) { pv ->
            Column(
                modifier = Modifier.padding(pv),
            ) {
                Image(
                    painter = painterResource(id = item.image),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .sharedElement(
                            rememberSharedContentState(key = "image/${item.id}"),
                            animatedVisibilityScope = this@DetailsContent
                        )
                )
                Text(
                    text = "Item ${item.id}",
                    fontSize = 20.sp,
                    modifier = Modifier
//                        .sharedElement(
//                            rememberSharedContentState(key = "title/${item.id}"),
//                            animatedVisibilityScope = this@DetailsContent,
//                        )
                        .padding(16.dp)
                )
                Text(
                    text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod " +
                            "tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam," +
                            " quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. " +
                            "Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat " +
                            "nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia " +
                            "deserunt mollit anim id est laborum.",
                    maxLines = 5,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }
        }
    }
}

@Preview
@Composable
private fun Preview() {
    SharedTransitionsDemoTheme {
        ListDetailsScreen {}
    }
}