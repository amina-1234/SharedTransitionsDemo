@file:OptIn(ExperimentalSharedTransitionApi::class)

package com.example.sharedtransitionsdemo.screens.animation_spec_demo

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.BoundsTransform
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.ArcMode
import androidx.compose.animation.core.ExperimentalAnimationSpecApi
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.resizescreenapp.navigation.NavigationDestination
import com.example.sharedtransitionsdemo.LocalSharedTransitionScope
import com.example.sharedtransitionsdemo.R
import com.example.sharedtransitionsdemo.ui.composable.AppBar
import com.example.sharedtransitionsdemo.ui.theme.SharedTransitionsDemoTheme
import kotlinx.serialization.Serializable

@Serializable
object AnimationSpecDemoDestination : NavigationDestination

@Composable
fun AnimationSpecDemoScreen(
    navigateBack: () -> Unit,
) {
    var showDetails by remember {
        mutableStateOf(false)
    }
    SharedTransitionLayout {
        CompositionLocalProvider(
            LocalSharedTransitionScope provides this
        ) {
            Scaffold(
                topBar = { AppBar(title = "Animation Spec Demo", onBackClick = navigateBack) }
            ) { pv ->
                AnimatedContent(
                    modifier = Modifier
                        .padding(pv)
                        .padding(16.dp),
                    targetState = showDetails,
                    label = "resizeModeTransition"
                ) { targetState ->
                    if (!targetState) {
                        MainContent(
                            onShowDetailsClick = {
                                showDetails = true
                            },
                            animatedVisibilityScope = this@AnimatedContent,
                            sharedTransitionScope = this@SharedTransitionLayout
                        )
                    } else {
                        DetailsContent(
                            onBackClick = {
                                showDetails = false
                            },
                            animatedVisibilityScope = this@AnimatedContent,
                            sharedTransitionScope = this@SharedTransitionLayout
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalAnimationSpecApi::class)
@Composable
private fun MainContent(
    onShowDetailsClick: () -> Unit,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope
) {
    with(sharedTransitionScope) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(MaterialTheme.shapes.medium)
                .background(MaterialTheme.colorScheme.surfaceContainer)
                .sharedBounds(
                    rememberSharedContentState(key = "animation_spec_container"),
                    animatedVisibilityScope = animatedVisibilityScope,
                    enter = fadeIn(
                        tween(
                            boundsAnimationDurationMillis,
                            easing = FastOutSlowInEasing
                        )
                    ),
                    exit = fadeOut(
                        tween(
                            boundsAnimationDurationMillis,
                            easing = FastOutSlowInEasing
                        )
                    ),
                    // To change the animation spec used for the size and position movement.
                    // Here to set up a uniform duration for all transition animations = `boundsAnimationDurationMillis`
                    boundsTransform = boundsTransform,
                    // To make corners rounded during animated transition
                    clipInOverlayDuringTransition = OverlayClip(MaterialTheme.shapes.medium),
                    // Use RemeasureToBounds to re-layout child during bounds transition for fluid continuity
                    resizeMode = SharedTransitionScope.ResizeMode.RemeasureToBounds
                )
                .clickable { onShowDetailsClick() }
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.img),
                contentDescription = null,
                modifier = Modifier
                    .sharedElement(
                        rememberSharedContentState(key = "animation_spec_image"),
                        animatedVisibilityScope = animatedVisibilityScope,
                        // To change the animation spec used for the size and position movement.
                        // Here to set up a uniform duration for all transition animations = `boundsAnimationDurationMillis`
                        boundsTransform = boundsTransform
                    )
                    .size(100.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
            /*
                To change the animation spec used for the size and position movement.
                Here to set up a uniform duration for all transition animations
                and to make the text move in a semi-circular arc around the image.
             */
            val textBoundsTransform = BoundsTransform { initialBounds, targetBounds ->
                keyframes {
                    durationMillis = boundsAnimationDurationMillis
                    initialBounds at 0 using ArcMode.ArcBelow using FastOutSlowInEasing
                    targetBounds at boundsAnimationDurationMillis
                }
            }
            Text(
                text = "Item",
                fontSize = 21.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.sharedBounds(
                    rememberSharedContentState(key = "animation_spec_title"),
                    animatedVisibilityScope = animatedVisibilityScope,
                    boundsTransform = textBoundsTransform
                )
            )
        }
    }
}

@OptIn(ExperimentalAnimationSpecApi::class)
@Composable
private fun DetailsContent(
    onBackClick: () -> Unit,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope
) {
    with(sharedTransitionScope) {
        Column(
            modifier = Modifier
                .clip(MaterialTheme.shapes.medium)
                .background(MaterialTheme.colorScheme.primaryContainer)
                .sharedBounds(
                    rememberSharedContentState(key = "animation_spec_container"),
                    animatedVisibilityScope = animatedVisibilityScope,
                    enter = fadeIn(
                        tween(
                            durationMillis = boundsAnimationDurationMillis,
                            easing = FastOutSlowInEasing
                        )
                    ),
                    exit = fadeOut(
                        tween(
                            durationMillis = boundsAnimationDurationMillis,
                            easing = FastOutSlowInEasing
                        )
                    ),
                    // To change the animation spec used for the size and position movement.
                    // Here to set up a uniform duration for all transition animations = `boundsAnimationDurationMillis`
                    boundsTransform = boundsTransform,
                    // To make corners rounded during animated transition
                    clipInOverlayDuringTransition = OverlayClip(MaterialTheme.shapes.medium),
                    // Use RemeasureToBounds to re-layout child during bounds transition for fluid continuity
                    resizeMode = SharedTransitionScope.ResizeMode.RemeasureToBounds
                )
                .clickable { onBackClick() }
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.img),
                contentDescription = null,
                modifier = Modifier
                    .sharedElement(
                        rememberSharedContentState(key = "animation_spec_image"),
                        animatedVisibilityScope = animatedVisibilityScope,
                        // To change the animation spec used for the size and position movement.
                        // Here to set up a uniform duration for all transition animations = `boundsAnimationDurationMillis`
                        boundsTransform = boundsTransform
                    )
                    .size(200.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
            /*
                To change the animation spec used for the size and position movement.
                Here to set up a uniform duration for all transition animations
                and to make the text move in a semi-circular arc around the image.
             */
            val textBoundsTransform = BoundsTransform { initialBounds, targetBounds ->
                keyframes {
                    durationMillis = boundsAnimationDurationMillis
                    initialBounds at 0 using ArcMode.ArcBelow using FastOutSlowInEasing
                    targetBounds at boundsAnimationDurationMillis
                }
            }
            Text(
                text = "Item",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.sharedBounds(
                    rememberSharedContentState(key = "animation_spec_title"),
                    animatedVisibilityScope = animatedVisibilityScope,
                    boundsTransform = textBoundsTransform
                )
            )
            Text(
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Curabitur sit amet lobortis velit. " +
                        "Lorem ipsum dolor sit amet, consectetur adipiscing elit." +
                        " Curabitur sagittis, lectus posuere imperdiet facilisis, nibh massa " +
                        "molestie est, quis dapibus orci ligula non magna. Pellentesque rhoncus " +
                        "hendrerit massa quis ultricies. Curabitur congue ullamcorper leo, at maximus",
            )
        }
    }
}

private val boundsTransform = BoundsTransform { _: Rect, _: Rect ->
    tween(durationMillis = boundsAnimationDurationMillis, easing = FastOutSlowInEasing)
}
private const val boundsAnimationDurationMillis = 500

@Preview
@Composable
private fun Preview() {
    SharedTransitionsDemoTheme {
        AnimationSpecDemoScreen {}
    }
}
