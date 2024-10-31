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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.startActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.skydoves.landscapist.glide.GlideImage
import com.umgmi.traveling.Menu_Principal
import com.umgmi.traveling.R

class Reserva : ComponentActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        setContent {
            ReservaScreen(firestore, auth.currentUser?.email)
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun ReservaScreen(firestore: FirebaseFirestore, userEmail: String?) {
        val servicios = remember { mutableStateListOf<ServicioModel>() }
        val loading = remember { mutableStateOf(true) }
        var selectedRating by remember { mutableStateOf(0) } // Calificación seleccionada
        var reviewText by remember { mutableStateOf("") } // Texto de reseña

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
            Column(modifier = Modifier.padding(padding)) {
                userEmail?.let {
                    Text(
                        text = "Usuario: $it",
                        modifier = Modifier.padding(16.dp),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                if (loading.value) {
                    // Mostrar un indicador de carga mientras se obtienen los datos
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(servicios) { servicio ->
                            ServicioCard(servicio) {
                                // Navegar a la pantalla de reserva cuando se hace clic en la tarjeta
                                DetalleReservaScreen(servicio, selectedRating, reviewText, onRatingChange = { rating ->
                                    selectedRating = rating
                                }, onReviewChange = { review ->
                                    reviewText = review
                                }, onSubmit = { rating, review ->
                                    // Aquí se realiza la reserva y se envían los datos a Firestore
                                    submitReview(firestore, servicio, rating, review)
                                })
                            }
                            Spacer(modifier = Modifier.height(16.dp))
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun ServicioCard(servicio: ServicioModel, onClick: () -> Unit) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .clickable(onClick = { onClick() }),  // Asegúrate de usar clickable(onClick = { onClick() })
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

    @Composable
    fun DetalleReservaScreen(
        servicio: ServicioModel,
        selectedRating: Int,
        reviewText: String,
        onRatingChange: (Int) -> Unit,
        onReviewChange: (String) -> Unit,
        onSubmit: (Int, String) -> Unit
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Reserva para: ${servicio.nombre}", fontSize = 24.sp, fontWeight = FontWeight.Bold)
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
            Text(text = "Tipo: ${servicio.tipo}", fontSize = 20.sp)
            Text(text = "Lugar: ${servicio.lugar}", fontSize = 20.sp)
            Text(text = "Pago: ${servicio.pago}", fontSize = 20.sp)
            Text(text = "Monto: ${servicio.monto}", fontSize = 20.sp)
            Spacer(modifier = Modifier.height(24.dp))

            // RadioButtons para calificación
            Text(text = "Calificación:", fontSize = 20.sp)
            Row {
                for (i in 1..5) {
                    RadioButton(
                        selected = selectedRating == i,
                        onClick = { onRatingChange(i) }
                    )
                    Text(text = "$i", modifier = Modifier.padding(start = 8.dp, end = 16.dp))
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Campo de texto para reseña
            TextField(
                value = reviewText,
                onValueChange = onReviewChange,
                label = { Text("Escribe tu reseña") },
                modifier = Modifier.fillMaxWidth().height(100.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    onSubmit(selectedRating, reviewText)
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Hacer Reserva")
            }
        }
    }

    private fun submitReview(firestore: FirebaseFirestore, servicio: ServicioModel, rating: Int, review: String) {
        val userEmail = auth.currentUser?.email ?: "Usuario anónimo" // Obtener el correo del usuario
        val reviewData = hashMapOf(
            "calificacion" to rating,
            "mensaje" to review,
            "usuario" to userEmail
        )

        firestore.collection("calificacion")
            .add(reviewData)
            .addOnSuccessListener {
                // Manejar el éxito
                println("Reseña enviada con éxito!")
            }
            .addOnFailureListener { e ->
                // Manejar el error
                println("Error al enviar la reseña: ${e.message}")
            }
    }

    @Preview(showBackground = true)
    @Composable
    fun PreviewReserva() {
        val mockService = ServicioModel(
            nombre = "Guía de Montaña",
            tipo = "Aventura",
            lugar = "Parque Nacional",
            pago = "Efectivo",
            monto = "100.00",
            imagenUrl = "https://example.com/image.jpg" // Usa una URL de imagen válida para el preview
        )
        // Muestra la pantalla de detalle de reserva con un servicio de ejemplo
        DetalleReservaScreen(servicio = mockService, selectedRating = 0, reviewText = "", onRatingChange = {}, onReviewChange = {}, onSubmit = { _, _ -> })
    }
}
