package com.example.logins.ui.passwords

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.logins.data.repository.SecureNotesRepository

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewPasswordScreen(navController: NavController) {

    val context = LocalContext.current
    val repository = remember { SecureNotesRepository(context) }

    var family by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var error by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Nueva contraseña") })
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            TextField(
                value = family,
                onValueChange = { family = it },
                label = { Text("Familia") },
                modifier = Modifier.fillMaxWidth()
            )

            TextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )

            if (error) {
                Text(
                    text = "Los campos no pueden estar vacíos",
                    color = MaterialTheme.colorScheme.error
                )
            }

            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    if (family.isBlank() || password.isBlank()) {
                        error = true
                    } else {
                        repository.savePassword(family, password)
                        navController.popBackStack()
                    }
                }
            ) {
                Text("Guardar")
            }
        }
    }
}
