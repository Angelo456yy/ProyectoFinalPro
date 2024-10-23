package com.umgmi.traveling

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseAuth

class UsuarioInfo : ComponentActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            UsuarioInfoScreen()
        }
    }

    @Composable
    fun UsuarioInfoScreen(userDisplayName: String? = null, userEmail: String? = null) {
        // Mostrar la información del usuario
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Nombre: ${userDisplayName ?: "Nombre no disponible"}")
            Text(text = "Correo: ${userEmail ?: "Correo no disponible"}")
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun PreviewUsuarioInfoScreen() {
        // Envuelve la previsualización en un tema y provee datos simulados
        MaterialTheme {
            UsuarioInfoScreen(userDisplayName = "Usuario Simulado", userEmail = "simulado@example.com")
        }
    }
}
