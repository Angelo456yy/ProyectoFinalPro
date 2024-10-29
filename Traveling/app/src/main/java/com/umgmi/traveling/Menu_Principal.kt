package com.umgmi.traveling

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
<<<<<<< HEAD
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
=======
import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
>>>>>>> Angel
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
<<<<<<< HEAD
import androidx.compose.ui.graphics.Color
=======
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
>>>>>>> Angel
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
<<<<<<< HEAD
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
=======
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

                // Espacio para los nuevos botones
                Spacer(modifier = Modifier.height(16.dp))

                // Botones para ofrecer y buscar servicio
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    // Botón para ofrecer servicio
                    Button(
                        onClick = { navigateToOfrecerServicio() },
                        modifier = Modifier.weight(1f).padding(4.dp)
                    ) {
                        Text("Ofrecer Servicio")
                    }

                    // Botón para buscar servicio
                    Button(
                        onClick = { navigateToBuscarServicio() },
                        modifier = Modifier.weight(1f).padding(4.dp)
                    ) {
                        Text("Buscar Servicio")
                    }
                }

                // Espacio para los iconos que ya tenías
                Spacer(modifier = Modifier.height(16.dp))

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
        val intent = Intent(this, com.umgmi.traveling.menu.Usuario::class.java)
        startActivity(intent)
    }

    // Función para navegar a la actividad de ofrecer servicio
    private fun navigateToOfrecerServicio() {
        val intent = Intent(this, com.umgmi.traveling.menu.Servicio::class.java) // Cambia la ruta según tu estructura de paquetes
        startActivity(intent)
    }

    // Función para navegar a la actividad de buscar servicio (aquí debes implementar la clase o composable)
    private fun navigateToBuscarServicio() {
        // Aquí debes implementar la navegación a la actividad donde los usuarios puedan buscar servicios
        // val intent = Intent(this, com.umgmi.traveling.menu.BuscarServicio::class.java)
        // startActivity(intent)
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
>>>>>>> Angel
        }
    }

    private fun cerrarSesion() {
        auth.signOut()
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
<<<<<<< HEAD
        // Envuelve la previsualización en un tema
=======
>>>>>>> Angel
        MaterialTheme {
            MenuPrincipalScreen()
        }
    }
}
