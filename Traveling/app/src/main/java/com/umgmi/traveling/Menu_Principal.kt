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
        // Inicializa FirebaseAuth
        auth = FirebaseAuth.getInstance()

        // Composición del contenido de la pantalla
        Button(onClick = { cerrarSesion() }, modifier = Modifier.padding(16.dp)) {
            Text(text = "Cerrar Sesión")
        }
    }

    private fun cerrarSesion() {
        auth.signOut()
        // Redirigir al usuario a la pantalla de carga
        startActivity(Intent(this, Pantalla_De_Carga::class.java))
        finish()
    }

    @Preview(showBackground = true)
    @Composable
    fun PreviewMenuPrincipalScreen() {
        MenuPrincipalScreen()
    }
}
