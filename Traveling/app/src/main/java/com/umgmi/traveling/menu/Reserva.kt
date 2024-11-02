package com.umgmi.traveling.menu

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.skydoves.landscapist.glide.GlideImage

class Reserva : ComponentActivity() {
    private lateinit var firestore: FirebaseFirestore
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        firestore = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

        setContent {
            val servicio = intent.getParcelableExtra<ServicioModel>("servicio")
            val creadorEmail = servicio?.creadorEmail ?: "Correo no disponible"  // El correo del creador
            val reservadorCorreo = auth.currentUser?.email ?: "Correo no disponible"  // Correo del usuario autenticado

            if (servicio != null) {
                MostrarReserva(servicio, creadorEmail, reservadorCorreo)
            } else {
                Text(
                    "Error: Servicio no encontrado",
                    modifier = Modifier.fillMaxSize(),
                    color = Color.Red
                )
            }
        }
    }

    @Composable
    fun MostrarReserva(servicio: ServicioModel, creadorEmail: String, reservadorCorreo: String) {
        var rating by remember { mutableStateOf(0) }
        var review by remember { mutableStateOf("") }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Reserva para: ${servicio.nombre}",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            Text(text = "Tipo: ${servicio.tipo}", fontSize = 18.sp)
            Text(text = "Lugar: ${servicio.lugar}", fontSize = 18.sp)
            Text(text = "Pago: ${servicio.pago}", fontSize = 18.sp)
            Text(text = "Monto: ${servicio.monto}", fontSize = 18.sp)
            Text(text = "Creador: $creadorEmail", fontSize = 14.sp, color = Color.Gray)  // Muestra el correo del creador
            Text(text = "Reservador: $reservadorCorreo", fontSize = 14.sp, color = Color.Gray)

            Spacer(modifier = Modifier.height(16.dp))

            GlideImage(
                imageModel = servicio.imagenUrl,
                contentDescription = "Imagen de ${servicio.nombre}",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(8.dp))
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Calificación
            Text(text = "Calificación:", fontSize = 20.sp, fontWeight = FontWeight.Medium)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                for (i in 1..5) {
                    Button(
                        onClick = { rating = i },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (rating == i) MaterialTheme.colorScheme.primary else Color.Gray
                        )
                    ) {
                        Text(text = "$i", fontSize = 18.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Campo de texto para reseña
            Text(text = "Escribe tu reseña:", fontSize = 20.sp, fontWeight = FontWeight.Medium)
            OutlinedTextField(
                value = review,
                onValueChange = { review = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .background(MaterialTheme.colorScheme.surface),
                placeholder = { Text("Escribe aquí tu reseña") }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Botón de Reservar
            Button(
                onClick = {
                    // Guardar en la colección de reservas
                    val reservaData = mapOf(
                        "nombre" to servicio.nombre,
                        "tipo" to servicio.tipo,
                        "lugar" to servicio.lugar,
                        "pago" to servicio.pago,
                        "monto" to servicio.monto,
                        "calificacion" to rating,
                        "reseña" to review,
                        "estado" to "pendiente",
                        "userEmail" to creadorEmail,  // Guardar como correo del creador
                        "reservadorCorreo" to reservadorCorreo
                    )
                    firestore.collection("reservas").add(reservaData)
                        .addOnSuccessListener {
                            // Aquí puedes agregar una lógica para notificar al usuario que la reserva fue exitosa
                        }
                        .addOnFailureListener { e ->
                            // Aquí puedes manejar errores en la reserva
                        }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Text("Reservar", fontSize = 18.sp)
            }
        }
    }
}
