package com.example.logins.ui.passwords

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.logins.data.repository.PasswordEntry
import com.example.logins.data.repository.SecureNotesRepository
import com.example.logins.ui.auth.BiometricAuth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasswordDetailScreen(
    navController: NavController,
    id: String
) {
    val context = LocalContext.current
    val repository = remember { SecureNotesRepository(context) }

    var isSaved by remember { mutableStateOf(false) }
    var entry by remember { mutableStateOf<PasswordEntry?>(null) }
    var family by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isPasswordVisible by remember { mutableStateOf(false) }
    val biometricAuth = remember { BiometricAuth(context) }
    val buttonColor by animateColorAsState(
        targetValue = if (isSaved) Color(0xFF4CAF50) else MaterialTheme.colorScheme.primary,
        animationSpec = tween(durationMillis = 500), // Medio segundo de transición
        label = "ButtonColorAnimation"
    )

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
                    modifier = Modifier.fillMaxWidth(),
                    // Lógica visual: Si es visible -> Texto plano, Si no -> Puntitos
                    visualTransformation = if (isPasswordVisible) androidx.compose.ui.text.input.VisualTransformation.None
                    else PasswordVisualTransformation(),
                    // El botón del "Ojo"
                    trailingIcon = {
                        val image = if (isPasswordVisible)
                            Icons.Filled.Visibility
                        else Icons.Filled.VisibilityOff

                        IconButton(onClick = {
                            if (isPasswordVisible) {
                                // Si ya se ve, la ocultamos directamente
                                isPasswordVisible = false
                            } else {
                                // SI ESTÁ OCULTA -> PEDIMOS HUELLA
                                biometricAuth.authenticate(
                                    onSuccess = { isPasswordVisible = true },
                                    onError = { /* Opcional: Mostrar Toast de error */ }
                                )
                            }
                        }) {
                            Icon(imageVector = image, contentDescription = "Mostrar password")
                        }
                    }
                )


                Button(
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = buttonColor),
                    onClick = {
                        entry?.let {
                            repository.updatePassword(
                                PasswordEntry(it.id, family, password)
                            )
                            isSaved = true
                        }
                    }
                ) {
                    androidx.compose.animation.Crossfade(targetState = isSaved, label = "TextCrossfade") { saved ->
                        if (saved) {
                            Text("¡Guardado!")
                        } else {
                            Text("Guardar cambios")
                        }
                    }
                }

                LaunchedEffect(isSaved) {
                    if (isSaved) {
                        kotlinx.coroutines.delay(1000)
                        isSaved = false
                        navController.popBackStack() // Volver atrás después del efecto
                    }
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
