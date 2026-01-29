package com.portfolio.newsapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.portfolio.newsapp.ui.newsdetail.NewsDetailScreen
import com.portfolio.newsapp.ui.newslist.NewsListScreen

sealed class Screen(val route: String) {
    data object NewsList : Screen("news_list")
    data object NewsDetail : Screen("news_detail/{articleId}") {
        fun createRoute(articleId: String) = "news_detail/$articleId"
    }
}
@Composable
fun NewsNavigation(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = Screen.NewsList.route
    ) {
        composable(Screen.NewsList.route) {
            NewsListScreen(
                onArticleClick = { article ->
                    navController.navigate(Screen.NewsDetail.createRoute(article.id))
                }
            )
        }
        
        composable(
            route = Screen.NewsDetail.route,
            arguments = listOf(
                navArgument("articleId") {
                    type = NavType.StringType
                }
            )
        ) {
            NewsDetailScreen(
                onBackClick = {
                    navController.navigateUp()
                }
            )
        }
    }
}
