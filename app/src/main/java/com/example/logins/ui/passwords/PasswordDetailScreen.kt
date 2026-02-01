package com.example.logins.ui.passwords

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.logins.data.repository.PasswordEntry
import com.example.logins.data.repository.SecureNotesRepository

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasswordDetailScreen(
    navController: NavController,
    id: String
) {
    val context = LocalContext.current
    val repository = remember { SecureNotesRepository(context) }

    var entry by remember { mutableStateOf<PasswordEntry?>(null) }
    var family by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }


    LaunchedEffect(id) {
        val result = repository.getPasswordById(id)
        entry = result

        result?.let {
            family = it.family
            password = it.password
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle de contraseña") }
            )
        }
    ) { padding ->

        if (entry == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = androidx.compose.ui.Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
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
                    modifier = Modifier.fillMaxWidth()
                )

                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        entry?.let {
                            repository.updatePassword(
                                PasswordEntry(it.id, family, password)
                            )
                            navController.popBackStack()
                        }
                    }
                ) {
                    Text("Guardar cambios")
                }

                OutlinedButton(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        entry?.let {
                            repository.deletePassword(it.id)
                            navController.popBackStack()
                        }
                    }
                ) {
                    Text("Eliminar")
                }

                OutlinedButton(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        navController.navigate("menu") {
                            popUpTo("menu") { inclusive = true }
                        }
                    }
                ) {
                    Text("Volver al menú principal")
                }
            }
        }
    }
}
