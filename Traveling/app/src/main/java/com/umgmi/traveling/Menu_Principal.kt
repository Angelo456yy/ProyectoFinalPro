package com.umgmi.traveling

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseAuth
import com.umgmi.traveling.menu.Usuario

class Menu_Principal : ComponentActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Inicializa FirebaseAuth
        auth = FirebaseAuth.getInstance()
        setContent {
            MenuPrincipalScreen()
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun MenuPrincipalScreen() {

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Home") },
                    actions = {
                        IconButton(onClick = { cerrarSesion() }) {
                            Icon(imageVector = Icons.Default.ExitToApp, contentDescription = "Salir")
                        }
                    }
                )
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .padding(16.dp)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "Bienvenido a la pantalla principal")

                // Botones en la parte inferior
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {

                    IconButtonWithImage(
                        painter = painterResource(id = R.drawable.usuario),
                        onClick = { navigateToUsuario() }
                    )
                    IconButtonWithImage(
                        painter = painterResource(id = R.drawable.home),
                        onClick = { /* Acción para botón 2 */ }
                    )
                    IconButtonWithImage(
                        painter = painterResource(id = R.drawable.message),
                        onClick = { /* Acción para botón 3 */ }
                    )
                    IconButtonWithImage(
                        painter = painterResource(id = R.drawable.bed),
                        onClick = { /* Acción para botón 4 */ }
                    )
                }
            }
        }
    }

    // Función para navegar a la actividad Usuario
    private fun navigateToUsuario() {
        val intent = Intent(this, com.umgmi.traveling.menu.Usuario::class.java) // Asegúrate de que Usuario esté importado
        startActivity(intent)
    }

    // Composable para un botón con imagen
    @Composable
    fun IconButtonWithImage(painter: Painter, onClick: () -> Unit) {
        Box(
            modifier = Modifier
                .size(80.dp) // Aumenta el tamaño del botón
                .clip(RoundedCornerShape(8.dp)) // Esquinas menos redondeadas
                .clickable(onClick = onClick), // Hace que el Box sea clickeable
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painter,
                contentDescription = null,
                modifier = Modifier.size(48.dp)
            )
        }
    }

    private fun cerrarSesion() {
        auth.signOut()

        startActivity(Intent(this, Pantalla_De_Carga::class.java))
        finish()
    }

    @Preview(showBackground = true)
    @Composable
    fun PreviewMenuPrincipalScreen() {
        MaterialTheme { 
            MenuPrincipalScreen()
        }
    }
}
