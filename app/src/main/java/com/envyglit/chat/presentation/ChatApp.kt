package com.envyglit.chat.presentation

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.envyglit.chat.presentation.theme.ChatTheme
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.launch

@Composable
fun ChatApp() {

    ChatTheme {
        ProvideWindowInsets {
            val systemUiController = rememberSystemUiController()
            val darkIcons = MaterialTheme.colors.isLight
            SideEffect {
                systemUiController.setSystemBarsColor(Color.Transparent, darkIcons = darkIcons)
            }

            val navController = rememberNavController()
            val coroutineScope = rememberCoroutineScope()
            // This top level scaffold contains the app drawer, which needs to be accessible
            // from multiple screens. An event to open the drawer is passed down to each
            // screen that needs it.
            val scaffoldState = rememberScaffoldState()

            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route ?: MainDestinations.HOME_ROUTE
            Scaffold(
                scaffoldState = scaffoldState,
                drawerContent = {
                    AppDrawer(
                        currentRoute = currentRoute,
                        navigateToHome = { navController.navigate(MainDestinations.HOME_ROUTE) },
                        navigateToInterests = { navController.navigate(MainDestinations.INTERESTS_ROUTE) },
                        closeDrawer = { coroutineScope.launch { scaffoldState.drawerState.close() } }
                    )
                }
            ) {
                ChatNavGraph(
                    navController = navController,
                    scaffoldState = scaffoldState
                )
            }
        }
    }
}
