package com.umgmi.traveling

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class Usuainfo : ComponentActivity() {
    private lateinit var auth: FirebaseAuth
    private var currentUser: FirebaseUser? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
        currentUser = auth.currentUser

        setContent {
            UsuarioInfoScreen()
        }
    }

    @Composable
    fun UsuarioInfoScreen() {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Center
        ) {
            // Mostrar información del usuario autenticado
            currentUser?.let { user ->
                Text(text = "Nombre: ${user.displayName ?: "No disponible"}")
                Text(text = "Correo: ${user.email ?: "No disponible"}")
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Botón para cerrar sesión
            Button(onClick = { cerrarSesion() }, modifier = Modifier.padding(top = 16.dp)) {
                Text(text = "Cerrar Sesión")
            }
        }
    }

    private fun cerrarSesion() {
        auth.signOut()
        // Redirigir al usuario a la pantalla de carga después de cerrar sesión
        startActivity(Intent(this, Pantalla_De_Carga::class.java))
        finish()
    }

    @Preview(showBackground = true)
    @Composable
    fun PreviewUsuarioInfoScreen() {
        UsuarioInfoScreen()
    }
}
