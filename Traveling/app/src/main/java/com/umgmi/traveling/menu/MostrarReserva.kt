package com.umgmi.traveling.menu

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.umgmi.traveling.Menu_Principal
import com.umgmi.traveling.R

class MostrarReserva : ComponentActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        setContent {
            MostrarReservasScreen(firestore)
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun MostrarReservasScreen(firestore: FirebaseFirestore) {
        val reservas = remember { mutableStateListOf<ReservaModel>() }
        val loading = remember { mutableStateOf(true) }

        // Obtener reservas desde Firestore
        LaunchedEffect(Unit) {
            val userEmail = auth.currentUser?.email ?: return@LaunchedEffect
            loading.value = true // Iniciar carga

            // Consultar reservas donde el creadorEmail es el usuario autenticado
            firestore.collection("reservas")
                .whereEqualTo("creadorEmail", userEmail) // Filtrar por creadorEmail
                .get()
                .addOnSuccessListener { result ->
                    for (document in result.documents) {
                        val reserva = document.toObject(ReservaModel::class.java)?.copy(id = document.id) // Copia con id
                        reserva?.let { reservas.add(it) }
                    }

                    // Consultar reservas donde el reservadorCorreo es el usuario autenticado
                    firestore.collection("reservas")
                        .whereEqualTo("reservadorCorreo", userEmail) // Filtrar por reservadorCorreo
                        .get()
                        .addOnSuccessListener { result2 ->
                            for (document in result2.documents) {
                                val reserva = document.toObject(ReservaModel::class.java)?.copy(id = document.id) // Copia con id
                                reserva?.let { reservas.add(it) }
                            }
                            loading.value = false // Cambiar el estado de carga
                        }
                        .addOnFailureListener { e -> loading.value = false }
                }
                .addOnFailureListener { e -> loading.value = false }
        }


        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Reservas") },
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
            if (loading.value) {
                // Mostrar un indicador de carga
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .padding(padding)
                        .fillMaxSize()
                ) {
                    items(reservas) { reserva ->
                        ReservaCard(reserva)
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }
        }
    }

    @Composable
    fun ReservaCard(reserva: ReservaModel) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .clickable {
                    // Navegar a DetallesReserva y pasar la reserva
                    val intent = Intent(this@MostrarReserva, Confir::class.java)
                    intent.putExtra("reserva", reserva) // Asegúrate de que ReservaModel implemente Parcelable
                    startActivity(intent)
                },
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
<<<<<<< HEAD
                // Muestra el correo del usuario que realizó la reserva
=======
>>>>>>> 15ea89259456cc677318d201fe43902ccb06a3c7
                Text(text = "Correo del reservador: ${reserva.reservadorCorreo ?: "No disponible"}", fontSize = 16.sp)
                Text(text = "Estado: ${reserva.estado ?: "No especificado"}", fontSize = 16.sp)
                Text(text = "Lugar: ${reserva.lugar ?: "No especificado"}", fontSize = 16.sp)
                Text(text = "Nombre: ${reserva.nombre ?: "No especificado"}", fontSize = 16.sp)
                Text(text = "¡Reserva realizada con éxito!", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}
