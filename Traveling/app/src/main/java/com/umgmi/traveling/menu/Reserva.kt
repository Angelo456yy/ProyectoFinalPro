// Reserva.kt
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
import com.google.firebase.firestore.FirebaseFirestore
import com.skydoves.landscapist.glide.GlideImage

class Reserva : ComponentActivity() {
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        firestore = FirebaseFirestore.getInstance()

        setContent {
            val servicio = intent.getSerializableExtra("servicio") as? ServicioModel
            if (servicio != null) {
                MostrarReserva(servicio)
            } else {
                Text("Error: Servicio no encontrado", modifier = Modifier.fillMaxSize(), color = Color.Red)
            }
        }
    }

    @Composable
    fun MostrarReserva(servicio: ServicioModel) {
        var rating by remember { mutableStateOf(0) }
        var review by remember { mutableStateOf("") }
        val userEmail = "usuario@example.com" // Aquí debes obtener el correo del usuario que está haciendo la reserva

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
                    firestore.collection("reservas").add(
                        mapOf(

                            "nombre" to servicio.nombre,
                            "tipo" to servicio.tipo,
                            "lugar" to servicio.lugar,
                            "pago" to servicio.pago,
                            "monto" to servicio.monto,
                            "calificacion" to rating,
                            "reseña" to review,
                            "estado" to "pendiente", // Asegúrate de incluir este campo
                            "correo" to userEmail // Guarda el correo del usuario
                        )
                    )
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
