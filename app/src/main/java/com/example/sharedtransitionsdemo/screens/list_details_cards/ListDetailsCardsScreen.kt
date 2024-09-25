@file:OptIn(ExperimentalSharedTransitionApi::class)

package com.example.sharedtransitionsdemo.screens.list_details_cards

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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
object ListDetailsCardsDestination : NavigationDestination

@Serializable
private object ListContentDestination : NavigationDestination

@Serializable
private data class DetailsContentDestination(
    val itemIndex: Int,
) : NavigationDestination

@Composable
fun ListDetailsCardsScreen(
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
            containerColor = MaterialTheme.colorScheme.surfaceContainer,
            topBar = {
                AppBar(
                    modifier = Modifier
                        // Keep top bar always on top during animation
                        .renderInSharedTransitionScopeOverlay(
                            zIndexInOverlay = 1f,
                        )
                        // Make top bar disappear when navigating to/from the Details smoother
                        .animateEnterExit(
                            enter = fadeIn() + slideInVertically { -it },
                            exit = fadeOut() + slideOutVertically { -it }
                        ),
                    title = "List",
                    onBackClick = navigateBack
                )
            }
        ) { pv ->
            LazyVerticalGrid(
                modifier = Modifier.padding(pv),
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(8.dp),
            ) {
                items(items) { item ->
                    Column(
                        modifier = Modifier
                            .padding(8.dp)
                            // Always use sharedBounds instead of sharedElement to animate containers.
                            .sharedBounds(
                                rememberSharedContentState(key = "container/${item.id}"),
                                animatedVisibilityScope = this@ListContent,
                                clipInOverlayDuringTransition = OverlayClip(MaterialTheme.shapes.medium)
                            )
                            .clip(MaterialTheme.shapes.medium)
                            .background(Color.White)
                            .clickable { navigateToDetails(item.id) },
                    ) {
                        Image(
                            painter = painterResource(id = item.image),
                            contentScale = ContentScale.Crop,
                            contentDescription = null,
                            modifier = Modifier
                                .height(180.dp)
                                .fillMaxWidth()
                                .sharedBounds(
                                    rememberSharedContentState(key = "image/${item.id}"),
                                    animatedVisibilityScope = this@ListContent
                                )
                                .clip(MaterialTheme.shapes.medium)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Item ${item.id}",
                            fontSize = 20.sp,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
                        Text(
                            text = "Lorem ipsum dolor sit amet, consectetur adipiscing",
                            fontSize = 14.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
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
            containerColor = MaterialTheme.colorScheme.surfaceContainer
        ) { pv ->
            Column(
                modifier = Modifier
                    .padding(pv)
                    .padding(16.dp)
                    // Always use sharedBounds instead of sharedElement to animate containers.
                    .sharedBounds(
                        rememberSharedContentState(key = "container/${item.id}"),
                        animatedVisibilityScope = this@DetailsContent,
                        clipInOverlayDuringTransition = OverlayClip(MaterialTheme.shapes.medium)
                    ),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Box {
                    Image(
                        painter = painterResource(id = item.image),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .sharedBounds(
                                rememberSharedContentState(key = "image/${item.id}"),
                                animatedVisibilityScope = this@DetailsContent
                            )
                            .clip(MaterialTheme.shapes.medium)
                    )
                    IconButton(onClick = navigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                            contentDescription = null
                        )
                    }
                }
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.White)
                        .padding(16.dp)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    Text(
                        text = "Item ${item.id}",
                        fontSize = 20.sp,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                    Text(
                        text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod " +
                                "tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam," +
                                " quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. " +
                                "Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat " +
                                "nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia " +
                                "deserunt mollit anim id est laborum.",
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            // Prevent the reflow of the text as the screen grows during animated transition
                            .skipToLookaheadSize()
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun Preview() {
    SharedTransitionsDemoTheme {
        ListDetailsCardsScreen {}
    }
}