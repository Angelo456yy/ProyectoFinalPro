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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.skydoves.landscapist.glide.GlideImage // Asegúrate de tener esta dependencia para cargar imágenes de Firebase
import com.umgmi.traveling.Menu_Principal
import com.umgmi.traveling.R

data class ServicioModel(
    val nombre: String,
    val tipo: String,
    val lugar: String,
    val pago: String,
    val monto: String,
    val imagenUrl: String
) {
    constructor() : this("", "", "", "", "", "")
}

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

        // Cargar los servicios de Firestore
        LaunchedEffect(Unit) {
            firestore.collection("servicios")
                .get()
                .addOnSuccessListener { documents ->
                    for (document in documents) {
                        val servicio = document.toObject(ServicioModel::class.java)
                        servicios.add(servicio)
                    }
                    loading.value = false // Cambiar el estado de carga
                }
                .addOnFailureListener { exception ->
                    // Manejar el error
                    loading.value = false
                }
        }

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Servicios Disponibles") },
                    navigationIcon = {
                        IconButton(onClick = {
                            // Regresar a Menu_Principal
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
            if (loading.value) {
                // Mostrar un indicador de carga mientras se obtienen los datos
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .padding(padding)
                        .fillMaxSize()
                ) {
                    items(servicios) { servicio ->
                        ServicioCard(servicio)
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }
        }
    }

    @Composable
    fun ServicioCard(servicio: ServicioModel) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .clickable { /* Acción al hacer clic en la tarjeta */ },
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
                // Cargar la imagen con Glide
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