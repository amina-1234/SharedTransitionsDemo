package com.example.sharedtransitionsdemo.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.resizescreenapp.navigation.NavigationDestination
import com.example.sharedtransitionsdemo.screens.animated_visibility.AnimatedVisibilityDestination
import com.example.sharedtransitionsdemo.screens.animated_visibility.AnimatedVisibilityScreen
import com.example.sharedtransitionsdemo.screens.animation_spec_demo.AnimationSpecDemoDestination
import com.example.sharedtransitionsdemo.screens.animation_spec_demo.AnimationSpecDemoScreen
import com.example.sharedtransitionsdemo.screens.list_details.ListDetailsDestination
import com.example.sharedtransitionsdemo.screens.list_details.ListDetailsScreen
import com.example.sharedtransitionsdemo.screens.list_details_cards.ListDetailsCardsDestination
import com.example.sharedtransitionsdemo.screens.list_details_cards.ListDetailsCardsScreen
import com.example.sharedtransitionsdemo.screens.main.MainDestination
import com.example.sharedtransitionsdemo.screens.main.MainScreen
import com.example.sharedtransitionsdemo.screens.managed_visibility.ManagedVisibilityDestination
import com.example.sharedtransitionsdemo.screens.managed_visibility.ManagedVisibilityScreen
import com.example.sharedtransitionsdemo.screens.nested_shared_element.NestedSharedElementDestination
import com.example.sharedtransitionsdemo.screens.nested_shared_element.NestedSharedElementScreen
import com.example.sharedtransitionsdemo.screens.sibling_layouts.SiblingLayoutsDestination
import com.example.sharedtransitionsdemo.screens.sibling_layouts.SiblingLayoutsScreen

@Composable
fun AppHavHost(
    navController: NavHostController = rememberNavController(),
    startDestination: NavigationDestination,
) {
    NavHost(navController = navController, startDestination = startDestination) {
        composable<MainDestination> {
            MainScreen(
                navigateToListDetails = {
                    navController.navigate(ListDetailsDestination)
                },
                navigateToListDetailsCards = {
                    navController.navigate(ListDetailsCardsDestination)
                },
                navigateToSiblingLayouts = {
                    navController.navigate(SiblingLayoutsDestination)
                },
                navigateToAnimationSpecDemo = {
                    navController.navigate(AnimationSpecDemoDestination)
                },
                navigateToManagedVisibility = {
                    navController.navigate(ManagedVisibilityDestination)
                },
                navigateToAnimatedVisibility = {
                    navController.navigate(AnimatedVisibilityDestination)
                },
                navigateToNestedSharedElement = {
                    navController.navigate(NestedSharedElementDestination)
                }
            )
        }

        composable<ListDetailsDestination> {
            ListDetailsScreen(
                navigateBack = { navController.popBackStack() }
            )
        }

        composable<ListDetailsCardsDestination> {
            ListDetailsCardsScreen(
                navigateBack = { navController.popBackStack() }
            )
        }

        composable<SiblingLayoutsDestination> {
            SiblingLayoutsScreen(
                navigateBack = { navController.popBackStack() }
            )
        }

        composable<AnimationSpecDemoDestination> {
            AnimationSpecDemoScreen(
                navigateBack = { navController.popBackStack() }
            )
        }

        composable<ManagedVisibilityDestination> {
            ManagedVisibilityScreen(
                navigateBack = { navController.popBackStack() }
            )
        }

        composable<AnimatedVisibilityDestination> {
            AnimatedVisibilityScreen(
                navigateBack = { navController.popBackStack() }
            )
        }

        composable<NestedSharedElementDestination> {
            NestedSharedElementScreen(
                navigateBack = { navController.popBackStack() }
            )
        }
    }
}