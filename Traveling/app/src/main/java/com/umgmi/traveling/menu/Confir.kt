package com.umgmi.traveling.menu

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class Confir : ComponentActivity() {

    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        firestore = Firebase.firestore

        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ConfirmationScreen(firestore)
                }
            }
        }
    }
}

@Composable
fun ConfirmationScreen(firestore: FirebaseFirestore) {
    var reservas by remember { mutableStateOf<List<ReservaModel>>(emptyList()) }
    var loading by remember { mutableStateOf(true) }

    // Obtener reservas pendientes de Firestore
    LaunchedEffect(Unit) {
        firestore.collection("reservas")
            .whereEqualTo("confirmado", false) // Filtrar reservas no confirmadas
            .get()
            .addOnSuccessListener { snapshot ->
                reservas = snapshot.documents.map { doc ->
                    ReservaModel(
                        id = doc.id,
                        nombre = doc.getString("nombre") ?: "",
                        servicio = doc.getString("servicio") ?: "",
                        confirmado = doc.getBoolean("confirmado") ?: false
                    )
                }
                loading = false
            }
            .addOnFailureListener {
                loading = false
            }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (loading) {
            CircularProgressIndicator() // Mostrar cargando mientras se obtienen datos
        } else if (reservas.isEmpty()) {
            Text("No hay reservas pendientes")
        } else {
            reservas.forEach { reserva ->
                ReservaItem(reserva, firestore)
            }
        }
    }
}

@Composable
fun ReservaItem(reserva: ReservaModel, firestore: FirebaseFirestore) {
    var confirmationState by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .border(1.dp, MaterialTheme.colorScheme.outline)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Reserva para: ${reserva.nombre}", style = MaterialTheme.typography.headlineSmall)
        Text(text = "Servicio: ${reserva.servicio}")

        Spacer(modifier = Modifier.height(16.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            Button(
                onClick = {
                    confirmationState = "Reserva Confirmada"
                    saveConfirmation(firestore, reserva.id, true)
                },
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Text("Confirmar")
            }

            Button(
                onClick = {
                    confirmationState = "Reserva Rechazada"
                    saveConfirmation(firestore, reserva.id, false)
                },
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
            ) {
                Text("Rechazar")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = confirmationState,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

// Función para guardar la confirmación o el rechazo en Firestore
fun saveConfirmation(firestore: FirebaseFirestore, reservaId: String, isConfirmed: Boolean) {
    firestore.collection("reservas").document(reservaId)
        .update("confirmado", isConfirmed)
        .addOnSuccessListener {
            // Aquí podrías mostrar un mensaje o realizar una acción adicional
        }
        .addOnFailureListener {
            // Manejar error
        }
}

