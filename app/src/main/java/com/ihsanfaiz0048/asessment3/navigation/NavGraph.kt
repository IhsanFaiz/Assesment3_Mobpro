package com.ihsanfaiz0048.asessment3.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.ihsanfaiz0048.asessment3.ui.screen.DetailScreen
import com.ihsanfaiz0048.asessment3.ui.screen.HistoryDetail
import com.ihsanfaiz0048.asessment3.ui.screen.HistoryScreen
//import com.ihsanfaiz0048.asessment3.ui.screen.DetailScreen
import com.ihsanfaiz0048.asessment3.ui.screen.MainScreen

@Composable
fun SetupNavGraph(navController: NavHostController = rememberNavController()){
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ){
        composable (route = Screen.Home.route) {
            MainScreen(navController)
        }
        composable (route = Screen.HistoryScreen.route){
            HistoryScreen(navController)
        }
        composable (
            route = Screen.HistoryDetail.route,
            arguments = listOf(
                navArgument(KEY_ID_MENU) {type = NavType.LongType}
            )
        ){ navBackStackEntry ->
            val id = navBackStackEntry.arguments?.getLong(KEY_ID_MENU) ?: 0L
            HistoryDetail(navController, id)
        }
        composable(
            route = Screen.DetailMenu.route,
            arguments = listOf(

                navArgument(KEY_ID_MENU) {
                    type = NavType.LongType
                },

                navArgument(KEY_ID_ORDER) {
                    type = NavType.StringType
                    nullable = true
                    defaultValue = null
                }
            )
        ) { navBackStackEntry ->

            val idMenu =
                navBackStackEntry
                    .arguments
                    ?.getLong(KEY_ID_MENU)
                    ?: 0L

            val idOrder =
                navBackStackEntry
                    .arguments
                    ?.getString(KEY_ID_ORDER)
                    ?.toLongOrNull()

            DetailScreen(
                navController,
                idMenu,
                idOrder
            )
        }
    }
}