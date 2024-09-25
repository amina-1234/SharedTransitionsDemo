@file:OptIn(ExperimentalSharedTransitionApi::class)

package com.example.sharedtransitionsdemo.screens.sibling_layouts

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
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
object SiblingLayoutsDestination : NavigationDestination

@Serializable
private object HomeDestination : NavigationDestination

@Serializable
private data class DetailsDestination(
    val itemId: Int,
) : NavigationDestination

@Composable
fun SiblingLayoutsScreen(
    navigateBack: () -> Unit,
) {
    val navController = rememberNavController()

    SharedTransitionLayout {
        CompositionLocalProvider(
            LocalSharedTransitionScope provides this
        ) {
            NavHost(
                navController = navController,
                startDestination = HomeDestination
            ) {
                composable<HomeDestination> {
                    HomeContent(
                        navigateToDetails = { itemId ->
                            navController.navigate(DetailsDestination(itemId))
                        },
                        navigateBack = { navigateBack() }
                    )
                }
                composable<DetailsDestination> { backStackEntry ->
                    val detailsDestination: DetailsDestination = backStackEntry.toRoute()
                    val item = items.first { it.id == detailsDestination.itemId }

                    DetailsContent(
                        item = item,
                        navigateBack = { navController.navigateUp() }
                    )
                }
            }
        }
    }
}

@Composable
fun AnimatedContentScope.HomeContent(
    navigateToDetails: (Int) -> Unit,
    navigateBack: () -> Unit
) {
    Scaffold(
        topBar = {
            AppBar(title = "Sibling Layouts", onBackClick = navigateBack)
        }
    ) { pv ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(pv)
        ) {
            var notifySiblings by rememberSaveable {
                mutableStateOf(false)
            }

            with(getLocalSharedTransitionScope()) {
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp)
                ) {
                    items(items) { item ->
                        Image(
                            painterResource(id = item.image),
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .padding(8.dp)
                                .sharedBounds(
                                    rememberSharedContentState(key = item.id),
                                    animatedVisibilityScope = this@HomeContent,
                                    /**
                                     * By default, sharedBounds() and sharedElement() don't notify
                                     * the parent container of any size changes as the layout transitions.
                                     *
                                     * In order to propagate size changes to the parent container
                                     * as it transitions, change the placeHolderSize parameter
                                     * to PlaceHolderSize.animatedSize. Doing so causes the item to grow or shrink.
                                     * All other items in the layout respond to the change.
                                     */
                                    placeHolderSize = if (notifySiblings) {
                                        SharedTransitionScope.PlaceHolderSize.animatedSize
                                    } else {
                                        SharedTransitionScope.PlaceHolderSize.contentSize
                                    }
                                )
                                .clickable { navigateToDetails(item.id) }
                                .size(width = 120.dp, height = 180.dp)
                                .clip(MaterialTheme.shapes.medium)

                        )
                    }
                }
            }
            Text(
                text = "Other Items",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(16.dp)
            )
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp)
            ) {
                items(items) { item ->
                    Image(
                        painterResource(id = item.image),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(width = 220.dp, height = 180.dp)
                            .padding(8.dp)
                            .clip(MaterialTheme.shapes.medium)
                    )
                }
            }

            Row(
                modifier = Modifier.padding(vertical = 16.dp, horizontal = 32.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text("Notify sibling layouts of changes to shared element size")
                Checkbox(
                    checked = notifySiblings,
                    onCheckedChange = { notifySiblings = !notifySiblings })
            }
        }
    }
}


@Composable
fun AnimatedContentScope.DetailsContent(
    item: Item,
    navigateBack: () -> Unit
) {
    with(getLocalSharedTransitionScope()) {
        Scaffold(
            modifier = Modifier.clickable { navigateBack() },
            topBar = {
                AppBar(title = "Item ${item.id}", onBackClick = navigateBack)
            },
        ) { pv ->
            Image(
                painterResource(id = item.image),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .sharedBounds(
                        rememberSharedContentState(key = item.id),
                        animatedVisibilityScope = this@DetailsContent,
                        placeHolderSize = SharedTransitionScope.PlaceHolderSize.animatedSize
                    )
                    .clip(MaterialTheme.shapes.medium)
                    .fillMaxSize()
                    .padding(pv)
            )
        }
    }
}

@Preview
@Composable
private fun Preview() {
    SharedTransitionsDemoTheme {
        SiblingLayoutsScreen {}
    }
}