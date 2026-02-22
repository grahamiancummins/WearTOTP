package com.symbolscope.weartotp.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.wear.compose.navigation.SwipeDismissableNavHost
import androidx.wear.compose.navigation.composable
import androidx.wear.compose.navigation.rememberSwipeDismissableNavController
import com.symbolscope.weartotp.presentation.screens.AddSiteScreen
import com.symbolscope.weartotp.presentation.screens.SiteListScreen
import com.symbolscope.weartotp.presentation.screens.TotpCodeScreen
import com.symbolscope.weartotp.presentation.viewmodel.MainViewModel

@Composable
fun AppNavigation() {
    val viewModel: MainViewModel = viewModel()
    val navController = rememberSwipeDismissableNavController()

    SwipeDismissableNavHost(
        navController = navController,
        startDestination = "site_list"
    ) {
        composable("site_list") {
            SiteListScreen(
                viewModel = viewModel,
                onSiteClick = { siteName ->
                    navController.navigate("totp_code/${siteName}")
                },
                onAddClick = {
                    navController.navigate("add_site")
                }
            )
        }
        composable("totp_code/{siteName}") { backStackEntry ->
            val siteName = backStackEntry.arguments?.getString("siteName") ?: ""
            TotpCodeScreen(siteName = siteName, viewModel = viewModel)
        }
        composable("add_site") {
            AddSiteScreen(
                viewModel = viewModel,
                onSave = { navController.popBackStack() }
            )
        }
    }
}
