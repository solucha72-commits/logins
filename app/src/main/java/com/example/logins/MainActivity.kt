package com.example.logins

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.fragment.app.FragmentActivity
import com.example.logins.navigation.SecureNotesApp

// Usamos FragmentActivity porque la librería biométrica lo exige
class MainActivity : FragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Aquí ya no pedimos huella. Dejamos pasar al usuario.
        // La seguridad la pondremos en las pantallas específicas.
        setContent {
            SecureNotesApp()
        }
    }
}