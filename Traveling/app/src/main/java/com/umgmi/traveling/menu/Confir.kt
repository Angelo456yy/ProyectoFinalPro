package com.umgmi.traveling.menu

import android.os.Bundle
import android.util.Log
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
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.RemoteMessage

class Confir : ComponentActivity() {
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        firestore = FirebaseFirestore.getInstance()

        // Obtener la reserva seleccionada de los extras
        val reserva = intent.getParcelableExtra<ReservaModel>("reserva")

        // Suponiendo que tienes el ID del usuario disponible, reemplaza "userId" con el ID real
        val userId = "userId" // Cambia esto por la forma en que obtienes el ID del usuario

        // Llama a la función para guardar el token en Firestore
        guardarTokenEnFirestore(userId)

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
                    Button(onClick = { manejarAccion(reserva, true) }) {
                        Text("Aceptar")
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Button(onClick = { manejarAccion(reserva, false) }) {
                        Text("Rechazar")
                    }
                }
            }
        }
    }

    private fun manejarAccion(reserva: ReservaModel, aceptar: Boolean) {
        if (aceptar) {
            // Actualizamos el estado de la reserva a "Aceptada"
            reserva.estado = "Aceptada" // Cambia el estado directamente en el objeto
            Toast.makeText(this, "Reserva aceptada", Toast.LENGTH_SHORT).show()

            // Enviar notificación al reservador
            enviarNotificacion(reserva.reservadorCorreo ?: "", "Reserva Aceptada", "Tu reserva ha sido aceptada.")
        } else {
            // Eliminar la reserva de Firestore
            eliminarReserva(reserva.reservadorCorreo ?: "")
        }
    }

    private fun eliminarReserva(reservadorCorreo: String) {
        // Eliminar la reserva usando el correo del reservador
        firestore.collection("reservas")
            .whereEqualTo("reservadorCorreo", reservadorCorreo)
            .get()
            .addOnSuccessListener { result ->
                for (document in result.documents) {
                    // Eliminar cada documento que coincida
                    document.reference.delete()
                }
                Toast.makeText(this, "Reserva rechazada y eliminada", Toast.LENGTH_SHORT).show()
                // Enviar notificación de rechazo
                enviarNotificacion(reservadorCorreo, "Reserva Rechazada", "Tu reserva ha sido rechazada.")
                finish() // Regresar a la actividad anterior
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error al eliminar la reserva: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun enviarNotificacion(correo: String, titulo: String, mensaje: String) {
        val notificationMessage = RemoteMessage.Builder(correo) // Cambia esto según la implementación de envío en tu servidor
            .setMessageId(System.currentTimeMillis().toString())
            .addData("titulo", titulo)
            .addData("mensaje", mensaje)
            .build()

        // Aquí puedes enviar la notificación usando Firebase Cloud Messaging
        FirebaseMessaging.getInstance().send(notificationMessage)
    }

    private fun guardarTokenEnFirestore(userId: String) {
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val token = task.result
                firestore.collection("usuarios").document(userId)
                    .update("token", token) // Asegúrate de tener un campo 'token' en el documento del usuario
                    .addOnSuccessListener {
                        Log.d("Token", "Token guardado exitosamente")
                    }
                    .addOnFailureListener { e ->
                        Log.e("Token", "Error al guardar el token: ${e.message}")
                    }
            }
        }
    }
}
