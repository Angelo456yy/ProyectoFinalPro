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
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay

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

                // Espacio para los consejos de viaje
                Spacer(modifier = Modifier.height(16.dp))

                // Sección de consejos y recomendaciones
                val recommendations = listOf(
                    "¡Descubre nuevas culturas! Participa en actividades locales y conoce a la gente.",
                    "Recuerda mantener tu documentación en regla. Siempre ten copias de tu pasaporte y otros documentos importantes.",
                    "Prueba la comida local. ¡Los mejores sabores se encuentran en las calles!",
                    "Lleva siempre una botella de agua reutilizable. Mantente hidratado mientras exploras.",
                    "Haz un presupuesto diario y cúmplelo. Así evitarás sorpresas al final del viaje.",
                    "Usa aplicaciones de mapas para explorar la ciudad. Te ayudarán a no perderte.",
                    "Investiga sobre las costumbres y tradiciones del lugar que visitas.",
                    "No olvides tu cargador portátil. Mantén tus dispositivos cargados para capturar esos momentos especiales.",
                    "Haz una lista de lugares que quieres visitar y planifica tu ruta.",
                    "Conéctate con otros viajeros. Compartir experiencias puede enriquecer tu viaje.",
                    "Visita los mercados locales. Encontrarás productos únicos y precios accesibles.",
                    "Lleva un diario de viaje. Anota tus experiencias y reflexiones.",
                    "Prueba un transporte alternativo, como la bicicleta, para explorar mejor la ciudad.",
                    "Aprovecha las horas menos concurridas para visitar los puntos turísticos.",
                    "Investiga sobre el transporte público local. Te ahorrará tiempo y dinero.",
                    "Si lees esto, inge es porque ya tengo sueño pipipi Att: Angel"
                )

                var currentRecommendation by remember { mutableStateOf(recommendations[0]) }
                LaunchedEffect(Unit) {
                    var index = 0
                    while (true) {
                        delay(30000) // Espera 30 segundos
                        index = (index + 1) % recommendations.size
                        currentRecommendation = recommendations[index]
                    }
                }

                // Cuadro de Consejos de Viaje
                Column(
                    modifier = Modifier
                        .height(200.dp) // Ajusta la altura del cuadro
                        .fillMaxWidth()
                        .padding(16.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(MaterialTheme.colorScheme.primaryContainer),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center// Centrar contenido
                ) {
                    Text(
                        text = "Consejos de Viaje:",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = currentRecommendation,
                        style = MaterialTheme.typography.bodyLarge, // Aumenta el tamaño del texto
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier.padding(horizontal = 16.dp), // Añade un poco de espacio horizontal
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center // Centra el texto
                    )
                }

                // Espacio para el cuadro Beta Tester
                Spacer(modifier = Modifier.height(16.dp)) // Espacio entre los cuadros

                // Cuadro de Beta Tester
                Column(
                    modifier = Modifier
                        .height(200.dp) // Ajusta la altura del cuadro
                        .fillMaxWidth()
                        .padding(16.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(MaterialTheme.colorScheme.secondaryContainer)
                        .padding(16.dp), // Padding dentro del cuadro
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center// Centrar contenido
                ) {
                    Text(
                        text = "Beta Tester: Paga para tener acceso a la IA obten un 35% de descutento a tan solo $5.99.",
                        style = MaterialTheme.typography.bodyLarge, // Aumenta el tamaño del texto
                        color = MaterialTheme.colorScheme.onSecondaryContainer,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center // Centra el texto
                    )
                }

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
                        painter = painterResource(id = R.drawable.bed),
                        onClick = { navigateToBuscarServicio() }
                    )
                    IconButtonWithImage(
                        painter = painterResource(id = R.drawable.message),
                        onClick = { mensage() }
                    )
                    IconButtonWithImage(
                        painter = painterResource(id = R.drawable.check),
                        onClick = { confirmaciones() }
                    )
                }
            }
        }
    }

    private fun confirmaciones() {
        val intent = Intent(this, com.umgmi.traveling.menu.MostrarReserva::class.java)
        startActivity(intent)
    }

    private fun mensage() {
        val intent = Intent(this, com.umgmi.traveling.menu.MostrarChat::class.java)
        startActivity(intent)
    }

    private fun navigateToUsuario() {
        val intent = Intent(this, com.umgmi.traveling.menu.Usuario::class.java)
        startActivity(intent)
    }

    private fun navigateToOfrecerServicio() {
        val intent = Intent(this, com.umgmi.traveling.menu.Servicio::class.java) // Cambia la ruta según tu estructura de paquetes
        startActivity(intent)
    }

    private fun navigateToBuscarServicio() {
        val intent = Intent(this, com.umgmi.traveling.menu.MostrarServicios::class.java)
        startActivity(intent)
    }

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

