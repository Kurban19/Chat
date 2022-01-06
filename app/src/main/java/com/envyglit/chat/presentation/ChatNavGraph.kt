package com.envyglit.chat.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.envyglit.main.ui.HomeScreen
import com.envyglit.chat.presentation.activities.users.UsersScreen

object MainDestinations {
    const val HOME_ROUTE = "home"
    const val CHAT_ROUTE = "chat"
    const val USERS_ROUTE = "users"
}

@Composable
fun ChatNavGraph(
    navController: NavHostController = rememberNavController(),
    startDestination: String = MainDestinations.HOME_ROUTE
) {

    val actions = remember(navController) { MainActions(navController) }
    val coroutineScope = rememberCoroutineScope()

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(MainDestinations.HOME_ROUTE) {
            HomeScreen(
                navigateToChat = actions.navigateToChat,
                navigateToUsers = actions.navigateToUsers
            )
        }
        composable(MainDestinations.USERS_ROUTE) {
            UsersScreen(navigateHome = actions.navigateToHome)
        }
//        composable(
//            route = "${MainDestinations.ARTICLE_ROUTE}/{$ARTICLE_ID_KEY}",
//            arguments = listOf(navArgument(ARTICLE_ID_KEY) { type = NavType.StringType })
    }

}

class MainActions(navController: NavHostController) {
    val navigateToChat: (String) -> Unit = { chatId: String ->
        navController.navigate("${MainDestinations.CHAT_ROUTE}/$chatId")
    }
    val navigateToHome: () -> Unit = {
        navController.navigate(MainDestinations.HOME_ROUTE)
    }
    val navigateToUsers: () -> Unit = {
        navController.navigate(MainDestinations.USERS_ROUTE)
    }
}
