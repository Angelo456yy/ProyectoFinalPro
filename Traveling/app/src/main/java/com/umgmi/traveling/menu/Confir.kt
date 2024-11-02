package com.umgmi.traveling.menu

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.firebase.firestore.FirebaseFirestore

class Confir : ComponentActivity() {
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        firestore = FirebaseFirestore.getInstance()

        // Obtener la reserva seleccionada de los extras
        val reserva = intent.getParcelableExtra<ReservaModel>("reserva")

        setContent {
            reserva?.let { ConfirScreen(it) }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun ConfirScreen(reserva: ReservaModel) {
        Scaffold { padding ->
            Column(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(text = "Detalles de la Reserva", style = MaterialTheme.typography.titleLarge)
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = "Correo del reservador: ${reserva.reservadorCorreo ?: "No disponible"}")
                Text(text = "Estado: ${reserva.estado ?: "No especificado"}")
                Text(text = "Lugar: ${reserva.lugar ?: "No especificado"}")
                Text(text = "Nombre: ${reserva.nombre ?: "No especificado"}")

                Spacer(modifier = Modifier.height(32.dp))

                Row {
                    Button(onClick = { manejarAccion(reserva.id, true) }) {
                        Text("Aceptar")
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Button(onClick = { manejarAccion(reserva.id, false) }) {
                        Text("Rechazar")
                    }
                }
            }
        }
    }

    private fun manejarAccion(reservaId: String?, aceptar: Boolean) {
        if (reservaId != null) {
            if (aceptar) {
                // Aquí puedes agregar la lógica para aceptar la reserva (ejemplo: enviar notificación)
                // ... (función para notificaciones push)
                // Mensaje temporal
                Toast.makeText(this, "Reserva aceptada", Toast.LENGTH_SHORT).show()
            } else {
                // Eliminar la reserva de Firestore
                firestore.collection("reservas").document(reservaId)
                    .delete()
                    .addOnSuccessListener {
                        Toast.makeText(this, "Reserva rechazada y eliminada", Toast.LENGTH_SHORT).show()
                        finish() // Regresa a la actividad anterior
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(this, "Error al eliminar la reserva: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
            }
        }
    }
}
