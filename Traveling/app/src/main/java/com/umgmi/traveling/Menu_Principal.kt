package com.umgmi.traveling

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseAuth

class Menu_Principal : ComponentActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MenuPrincipalScreen()
        }
    }

    @Composable
    fun MenuPrincipalScreen() {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween // Distribuye los botones
        ) {
            // Botón para cerrar sesión
            Button(onClick = { cerrarSesion() }, modifier = Modifier.padding(end = 16.dp)) {
                Text(text = "Cerrar Sesión")
            }

            // Botón redondo para ver información de usuario
            Button(
                onClick = { verInfoUsuario() },
                modifier = Modifier
                    .size(56.dp) // Ajusta el tamaño del botón
                    .padding(8.dp), // Espacio alrededor del botón
                shape = CircleShape, // Hace que el botón sea redondo
                colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray)
            ) {
                // Ícono dentro del botón redondo
                Icon(Icons.Filled.Person, contentDescription = "Ver información del usuario")
            }
        }
    }

    private fun cerrarSesion() {
        auth.signOut()
        // Redirigir al usuario a la pantalla de carga
        startActivity(Intent(this, Pantalla_De_Carga::class.java))
        finish()
    }

    private fun verInfoUsuario() {
        // Redirigir a la pantalla de información de usuario
        startActivity(Intent(this, UsuarioInfo::class.java))
    }

    @Preview(showBackground = true)
    @Composable
    fun PreviewMenuPrincipalScreen() {
        // Envuelve la previsualización en un tema
        MaterialTheme {
            MenuPrincipalScreen()
        }
    }
}
