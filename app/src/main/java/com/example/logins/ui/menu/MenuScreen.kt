package com.example.logins.ui.menu

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun MenuScreen(navController: NavController) {

    var showInfoDialog by remember { mutableStateOf(false) }

    Scaffold(
        bottomBar = {
            // Barra inferior con botón de información
            BottomAppBar {
                Spacer(modifier = Modifier.weight(1f))
                IconButton(onClick = { showInfoDialog = true }) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = "Información"
                    )
                }
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(
                    text = "Gestor de Contraseñas",
                    style = MaterialTheme.typography.headlineSmall
                )

                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        navController.navigate("new_password")
                    }
                ) {
                    Text("➕ Nueva contraseña")
                }

                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        navController.navigate("list_passwords")
                    }
                ) {
                    Text(" Consultar contraseñas")
                }
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        navController.navigate("scan_qr")
                    }
                ) {
                    Text(" Lector de código QR")
                }
            }
        }
    }

    if (showInfoDialog) {
        AlertDialog(
            onDismissRequest = { showInfoDialog = false },
            title = { Text("Información de desarrollo") },
            text = {
                Text(
                    "Aplicación desarrollada por:\n\n" +
                            "• Santiago Olucha Sánchez\n" +
                            "• Carlos Fernández Rodriguez"
                )
            },
            confirmButton = {
                TextButton(onClick = { showInfoDialog = false }) {
                    Text("Cerrar")
                }
            }
        )
    }
}
