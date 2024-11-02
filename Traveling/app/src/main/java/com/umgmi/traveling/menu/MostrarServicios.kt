package com.umgmi.traveling.menu

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.startActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.skydoves.landscapist.glide.GlideImage
import com.umgmi.traveling.Menu_Principal
import com.umgmi.traveling.R

class MostrarServicios : ComponentActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        setContent {
            MostrarServiciosScreen(firestore)
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun MostrarServiciosScreen(firestore: FirebaseFirestore) {
        val servicios = remember { mutableStateListOf<ServicioModel>() }
        val loading = remember { mutableStateOf(true) }
        var filterType by remember { mutableStateOf("Todos") }

        // Función para filtrar los servicios según el tipo seleccionado
        fun filtrarServicios(): List<ServicioModel> {
            return when (filterType) {
                "Gratis" -> servicios.filter { it.monto.toDoubleOrNull() == 0.0 }
                "Con Monto" -> servicios.filter { it.monto.toDoubleOrNull()?.let { monto -> monto > 0.0 } == true }
                else -> servicios
            }
        }

        // Cargar los servicios de Firestore
        LaunchedEffect(Unit) {
            firestore.collection("servicios")
                .get()
                .addOnSuccessListener { documents ->
                    for (document in documents) {
                        val servicio = document.toObject(ServicioModel::class.java)
                        servicios.add(servicio)
                    }
                    loading.value = false
                }
                .addOnFailureListener { exception ->
                    loading.value = false
                }
        }

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Servicios Disponibles") },
                    navigationIcon = {
                        IconButton(onClick = {
                            val intent = Intent(this, Menu_Principal::class.java)
                            startActivity(intent)
                        }) {
                            Icon(painter = painterResource(id = R.drawable.home), contentDescription = "Regresar")
                        }
                    },
                    modifier = Modifier.background(MaterialTheme.colorScheme.primary)
                )
            }
        ) { padding ->
            Column(modifier = Modifier.padding(padding)) {
                // Filtros de botones
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    Button(
                        onClick = {
                            filterType = "Todos"
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (filterType == "Todos") MaterialTheme.colorScheme.primary else Color.Gray
                        )
                    ) {
                        Text("Todos")
                    }
                    Button(
                        onClick = {
                            filterType = "Gratis"
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (filterType == "Gratis") MaterialTheme.colorScheme.primary else Color.Gray
                        )
                    ) {
                        Text("Gratis")
                    }
                    Button(
                        onClick = {
                            filterType = "Con Monto"
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (filterType == "Con Monto") MaterialTheme.colorScheme.primary else Color.Gray
                        )
                    ) {
                        Text("Con Monto")
                    }
                }

                if (loading.value) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 8.dp)
                    ) {
                        items(filtrarServicios()) { servicio ->
                            ServicioCard(servicio)
                            Spacer(modifier = Modifier.height(16.dp))
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun ServicioCard(servicio: ServicioModel) {
        val context = LocalContext.current
        val userEmail = FirebaseAuth.getInstance().currentUser?.email ?: "Correo no disponible"

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .clickable {
                    val intent = Intent(context, Reserva::class.java).apply {
                        putExtra("servicio", servicio.copy(reservadorCorreo = userEmail))
                    }
                    context.startActivity(intent)
                },
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = servicio.nombre, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = "Tipo: ${servicio.tipo}", fontSize = 16.sp)
                Text(text = "Lugar: ${servicio.lugar}", fontSize = 16.sp)
                Text(text = "Pago: ${servicio.pago}", fontSize = 16.sp)
                Text(text = "Monto: ${servicio.monto}", fontSize = 16.sp)
                Text(text = "Creador: ${servicio.creadorEmail}", fontSize = 14.sp, color = Color.Gray)
                Text(text = "Reservador: $userEmail", fontSize = 14.sp, color = Color.Gray)
                Spacer(modifier = Modifier.height(8.dp))
                GlideImage(
                    imageModel = servicio.imagenUrl,
                    contentDescription = "Imagen de ${servicio.nombre}",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .clip(RoundedCornerShape(8.dp))
                )
            }
        }
    }
}
