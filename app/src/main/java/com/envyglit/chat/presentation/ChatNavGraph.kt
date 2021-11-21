package com.envyglit.chat.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.envyglit.chat.Application
import com.envyglit.chat.presentation.activities.main.HomeScreen
import com.envyglit.chat.presentation.activities.main.HomeViewModel

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
                navigateToChat = actions.navigateToChat
            )
        }
        composable(MainDestinations.USERS_ROUTE) {
//            val interestsViewModel: InterestsViewModel = viewModel(
//                factory = InterestsViewModel.provideFactory(appContainer.interestsRepository)
//            )
//            InterestsScreen(
//                interestsViewModel = interestsViewModel,
//                openDrawer = openDrawer
//            )
        }
//        composable(
//            route = "${MainDestinations.ARTICLE_ROUTE}/{$ARTICLE_ID_KEY}",
//            arguments = listOf(navArgument(ARTICLE_ID_KEY) { type = NavType.StringType })
//        ) { backStackEntry ->
//            // ArticleVM obtains the articleId via backStackEntry.arguments from SavedStateHandle
//            val articleViewModel: ArticleViewModel = viewModel(
//                factory = ArticleViewModel.provideFactory(
//                    postsRepository = appContainer.postsRepository,
//                    owner = backStackEntry,
//                    defaultArgs = backStackEntry.arguments
//                )
//            )
//            ArticleScreen(
//                articleViewModel = articleViewModel,
//                onBack = actions.upPress
//            )
//        }
    }

}

class MainActions(navController: NavHostController) {
    val navigateToChat: (String) -> Unit = { postId: String ->
        navController.navigate("${MainDestinations.CHAT_ROUTE}/$postId")
    }
    val upPress: () -> Unit = {
        navController.navigateUp()
    }
}
