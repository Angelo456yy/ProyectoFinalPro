package com.umgmi.traveling

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseAuth

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
    @Composable
    fun RecomendacionCard(titulo: String, descripcion: String) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(8.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Text(text = titulo, style = MaterialTheme.typography.titleLarge)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = descripcion, style = MaterialTheme.typography.bodyMedium)
            }
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
                // Espacio para las recomendaciones
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = "Guía de recomendaciones para el buen viaje!")

                // Contenedor de recomendaciones
                Column(modifier = Modifier.fillMaxWidth()) {
                    RecomendacionCard(
                        titulo = "Planificación",
                        descripcion = "Planifica tu viaje con tiempo incluyendo tu hospedaje, boletos, transporte y un presupuesto general."
                    )
                    RecomendacionCard(
                        titulo = "Documentación",
                        descripcion = "Lleva contigo siempre una identificación vigente."
                    )
                    RecomendacionCard(
                        titulo = "Salud",
                        descripcion = "Lleva contigo siempre un seguro de salud vigente, " +
                                "asi como tu botiquin de primeros auxilios, si tomas algún medicamento preinscrito no olvides de empacarlo!"
                    )
                    RecomendacionCard(
                        titulo = "Seguridad",
                        descripcion = "Nunca compartas información personal como identificaciones o cuentas bancarias a desconocidos. "
                    )
                }

                // Espacio para los iconos
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
                        onClick = { mensage() }
                    )
                    IconButtonWithImage(
                        painter = painterResource(id = R.drawable.bed),
                        onClick = { }
                    )
                }
            }
        }
    }


    private  fun  mensage(){
        val intent = Intent(this, com.umgmi.traveling.menu.MostrarChat::class.java)
        startActivity(intent)
    }


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
        val intent = Intent(this, com.umgmi.traveling.menu.MostrarServicios::class.java)
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