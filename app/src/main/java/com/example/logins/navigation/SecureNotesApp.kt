package com.example.logins.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.logins.ui.menu.MenuScreen

import com.example.logins.ui.passwords.NewPasswordScreen
import com.example.logins.ui.passwords.PasswordDetailScreen
import com.example.logins.ui.passwords.PasswordListScreen

@Composable
fun SecureNotesApp() {
    val navController = rememberNavController()

    NavHost(navController, startDestination = "menu") {

        composable("menu") {
            MenuScreen(navController)
        }

        composable("new_password") {
            NewPasswordScreen(navController)
        }

        // las siguientes rutas las crearemos luego
        composable("list_passwords") {
            PasswordListScreen(navController)
        }
        composable("password_detail/{id}") { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id")!!
            PasswordDetailScreen(navController, id)
        }
    }

}
