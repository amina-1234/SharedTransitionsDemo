@file:OptIn(ExperimentalSharedTransitionApi::class)

package com.example.sharedtransitionsdemo.screens.animated_visibility

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.resizescreenapp.navigation.NavigationDestination
import com.example.sharedtransitionsdemo.model.Item
import com.example.sharedtransitionsdemo.model.items
import com.example.sharedtransitionsdemo.ui.composable.AppBar
import com.example.sharedtransitionsdemo.ui.theme.SharedTransitionsDemoTheme
import kotlinx.serialization.Serializable

@Serializable
object AnimatedVisibilityDestination : NavigationDestination

@Composable
fun AnimatedVisibilityScreen(
    navigateBack: () -> Unit,
) {
    SharedTransitionLayout {
        Scaffold(
            containerColor = MaterialTheme.colorScheme.surfaceContainer,
            topBar = {
                AppBar(
                    title = "Animated Visibility",
                    onBackClick = navigateBack,
                    modifier = Modifier
                        // Keep top bar always on top during animation
                        .renderInSharedTransitionScopeOverlay(
                            zIndexInOverlay = 1f,
                        )
                )
            }
        ) { pv ->
            var selectedItem by remember { mutableStateOf<Item?>(null) }
            val itemShape = MaterialTheme.shapes.medium

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(pv),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(16.dp)
            ) {
                items(items) { item ->
                    // If the item is selected, it's hidden from the list and shown in a separate overlay.
                    // This animated scope animates the item's disappearance/reappearance.
                    AnimatedVisibility(
                        visible = item != selectedItem,
                        enter = fadeIn() + scaleIn(),
                        exit = fadeOut() + scaleOut(),
                        modifier = Modifier.animateItem()
                    ) {
                        Box(
                            modifier = Modifier
                                .sharedBounds(
                                    sharedContentState = rememberSharedContentState("container/${item.id}"),
                                    animatedVisibilityScope = this,
                                    clipInOverlayDuringTransition = OverlayClip(itemShape)
                                )
                                .clip(itemShape)
                                .background(Color.White, itemShape)
                        ) {
                            ItemContent(
                                item = item,
                                modifier = Modifier
                                    .sharedElement(
                                        state = rememberSharedContentState("item/${item.id}"),
                                        animatedVisibilityScope = this@AnimatedVisibility
                                    )
                                    .clickable { selectedItem = item },
                            )
                        }
                    }
                }
            }
            // Overlay with the selected item
            ItemDetailsContent(
                item = selectedItem,
                onConfirmClick = { selectedItem = null }
            )
        }
    }
}

@Composable
private fun SharedTransitionScope.ItemDetailsContent(
    item: Item?,
    onConfirmClick: () -> Unit
) {
    val itemShape = MaterialTheme.shapes.medium
    AnimatedContent(
        modifier = Modifier,
        targetState = item,
        transitionSpec = { fadeIn() togetherWith fadeOut() },
        label = "itemDetailsContent"
    ) { targetItem ->
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            if (targetItem != null) {
                // Color overlay behind the item
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clickable { onConfirmClick() }
                        .background(Color.Black.copy(alpha = 0.5f))
                )
                // The item with additional info
                Column(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .sharedBounds(
                            sharedContentState = rememberSharedContentState(key = "container/${targetItem.id}"),
                            animatedVisibilityScope = this@AnimatedContent,
                            clipInOverlayDuringTransition = OverlayClip(itemShape)
                        )
                        .clip(itemShape)
                        .background(Color.White)
                        .clickable { onConfirmClick() }
                ) {
                    ItemContent(
                        item = targetItem,
                        modifier = Modifier.sharedElement(
                            state = rememberSharedContentState("item/${targetItem.id}"),
                            animatedVisibilityScope = this@AnimatedContent,
                        )
                    )
                    Text(
                        text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore.",
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                    TextButton(
                        modifier = Modifier
                            .align(Alignment.End)
                            .padding(8.dp),
                        onClick = { onConfirmClick() }
                    ) {
                        Text(text = "Ok")
                    }
                }
            }
        }
    }
}

@Composable
private fun ItemContent(
    item: Item,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
    ) {
        Image(
            painter = painterResource(id = item.image),
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp),
            contentScale = ContentScale.Crop,
            contentDescription = null
        )
        Text(
            text = item.title,
            modifier = Modifier
                .wrapContentWidth()
                .padding(16.dp),
            style = MaterialTheme.typography.titleSmall
        )
    }
}

@Preview
@Composable
private fun Preview() {
    SharedTransitionsDemoTheme {
        AnimatedVisibilityScreen {}
    }
}