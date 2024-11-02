package com.umgmi.traveling.menu

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
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
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MostrarServiciosScreen(firestore: FirebaseFirestore) {
    val allServicios = remember { mutableStateListOf<ServicioModel>() }
    val displayedServicios = remember { mutableStateListOf<ServicioModel>() }
    val loading = remember { mutableStateOf(true) }
    var query by remember { mutableStateOf("") }

    // Cargar los servicios de Firestore solo una vez
    LaunchedEffect(Unit) {
        firestore.collection("servicios")
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val servicio = document.toObject(ServicioModel::class.java)
                    allServicios.add(servicio)
                }
                displayedServicios.addAll(allServicios)
                loading.value = false
            }
            .addOnFailureListener {
                loading.value = false
            }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Servicios Disponibles") },
                navigationIcon = {
                    IconButton(onClick = {
                        val intent = Intent(LocalContext.current, Menu_Principal::class.java)
                        LocalContext.current.startActivity(intent)
                    }) {
                        Icon(
                            painter = painterResource(id = R.drawable.home),
                            contentDescription = "Regresar"
                        )
                    }
                },
                modifier = Modifier.background(MaterialTheme.colorScheme.primary)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ){
                var active by remember { mutableStateOf(false) }
                // SearchBar composable
                SearchBar(
                    query = query,
                    onQueryChange = { newQuery ->
                        query = newQuery
                        displayedServicios.clear()
                        displayedServicios.addAll(
                            allServicios.filter { servicio ->
                                servicio.nombre.contains(query, ignoreCase = true) ||
                                        servicio.tipo.contains(query, ignoreCase = true) ||
                                        servicio.lugar.contains(query, ignoreCase = true) ||
                                        servicio.pago.contains(query, ignoreCase = true) ||
                                        (query.toIntOrNull()?.let { rangeValue ->
                                            servicio.monto in rangeValue - 50..rangeValue + 50 // Ejemplo de tolerancia de rango
                                        } ?: false)
                            }
                        )
                    },
                    onSearch = {
                        Toast.makeText(LocalContext.current, "Buscando: $query", Toast.LENGTH_SHORT).show()
                    },
                    active = active,
                    onActiveChange = { active = it }
                )

                Spacer(modifier = Modifier.height(16.dp))

                if (loading.value) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(displayedServicios) { servicio ->
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
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable {
                val intent = Intent(context, Reserva::class.java).apply {
                    putExtra("servicio", servicio)
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

@Preview(showBackground = true)
@Composable
fun MostrarServiciosScreenPreview() {
    val firestore = Firebase.firestore
    MostrarServiciosScreen(firestore)
}

