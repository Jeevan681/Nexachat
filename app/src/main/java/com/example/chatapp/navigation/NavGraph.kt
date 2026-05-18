package com.example.chatapp.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.example.chatapp.navigation.Routes

@Composable
fun NavGraph() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Routes.LOGIN) {
        composable(Routes.LOGIN) {
            LoginScreen(
                goToRegister = { navController.navigate(Routes.REGISTER) },
                goToHome = { navController.navigate(Routes.HOME) }
            )
        }

        composable(Routes.REGISTER) {
            RegisterScreen(
                goToLogin = { navController.popBackStack() },
                goToHome = {
                    navController.navigate(Routes.HOME) { popUpTo(Routes.LOGIN) { inclusive = true } }
                }
            )
        }

        composable(Routes.HOME) {
            HomeScreen(
                goToChat = { uid, name -> navController.navigate("${Routes.CHAT}/$uid/$name") },
                goToUsers = { navController.navigate(Routes.USERS) },
                goToLogin = { navController.navigate(Routes.LOGIN) }
            )
        }

        composable(Routes.USERS) {
            UsersScreen(
                goToChat = { uid, name -> navController.navigate("${Routes.CHAT}/$uid/$name") }
            )
        }

        composable(
            route = "${Routes.CHAT}/{uid}/{name}",
            arguments = listOf(
                navArgument("uid") { type = NavType.StringType },
                navArgument("name") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val uid = backStackEntry.arguments?.getString("uid").orEmpty()
            val name = backStackEntry.arguments?.getString("name").orEmpty()
            ChatScreen(receiverId = uid, receiverName = name)
        }
    }
}