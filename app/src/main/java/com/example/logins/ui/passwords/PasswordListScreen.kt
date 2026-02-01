package com.example.logins.ui.passwords

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.logins.data.repository.PasswordEntry
import com.example.logins.data.repository.SecureNotesRepository

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasswordListScreen(navController: NavController) {

    val context = LocalContext.current
    val repository = remember { SecureNotesRepository(context) }

    var passwords by remember { mutableStateOf<List<PasswordEntry>>(emptyList()) }

    LaunchedEffect(Unit) {
        passwords = repository.getPasswords()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Consultar contraseñas") }
            )
        }
    ) { padding ->

        if (passwords.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text("No hay contraseñas guardadas")
            }
        } else {
            Column(
                modifier = Modifier
                    .padding(padding)
                    .padding(16.dp)
                    .fillMaxSize()
            ) {

                // Cabecera de columnas
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Familia",
                        style = MaterialTheme.typography.labelLarge
                    )
                    Text(
                        text = "Password",
                        style = MaterialTheme.typography.labelLarge
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Listado
                LazyColumn(
                    modifier = Modifier.weight(1f)
                ) {
                    items(passwords) { entry ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    navController.navigate("password_detail/${entry.id}")
                                }
                                .padding(vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(entry.family)
                            Text("******")
                        }
                        Divider()
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Botón volver al menú
                Button(
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
