package com.umgmi.traveling.menu

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.painterResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.umgmi.traveling.R
import java.util.UUID

class Servicio : ComponentActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private lateinit var storage: FirebaseStorage

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()
        storage = FirebaseStorage.getInstance()

        setContent {
            ServiciosScreen()
        }
    }

    @Composable
    fun ServiciosScreen() {
        var servicioNombre by remember { mutableStateOf(TextFieldValue("")) }
        var servicioTipo by remember { mutableStateOf("Alojamiento") }
        var selectedImageUri by remember { mutableStateOf<Uri?>(null) }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Text("Ofrecer un Servicio")

            // Campo para el nombre del servicio
            OutlinedTextField(
                value = servicioNombre,
                onValueChange = { servicioNombre = it },
                label = { Text("Nombre del Servicio") },
                modifier = Modifier.fillMaxWidth()
            )

            // Selector para el tipo de servicio
            DropdownMenu(
                expanded = true,
                onDismissRequest = { /* TODO */ }
            ) {
                listOf("Alojamiento", "Comida", "Tour").forEach { tipo ->
                    DropdownMenuItem(onClick = { servicioTipo = tipo }) {
                        Text(tipo)
                    }
                }
            }

            // Botón para seleccionar imagen
            Button(onClick = { selectImage() }) {
                Text("Seleccionar Imagen")
            }

            // Mostrar imagen seleccionada
            selectedImageUri?.let { uri ->
                Image(
                    painter = painterResource(id = R.drawable.placeholder_image), // Cambia a tu placeholder
                    contentDescription = null,
                    modifier = Modifier
                        .size(100.dp)
                        .clip(RoundedCornerShape(8.dp))
                )
            }

            // Botón para guardar el servicio
            Button(
                onClick = { guardarServicio(servicioNombre.text, servicioTipo, selectedImageUri) },
                modifier = Modifier.padding(top = 16.dp)
            ) {
                Text("Guardar Servicio")
            }
        }
    }

    private fun selectImage() {
        // Aquí iría la lógica para abrir la galería de imágenes y seleccionar una
    }

    private fun guardarServicio(nombre: String, tipo: String, imageUri: Uri?) {
        if (imageUri != null) {
            val storageRef = storage.reference.child("servicios/${UUID.randomUUID()}")
            val uploadTask = storageRef.putFile(imageUri)

            uploadTask.addOnSuccessListener {
                storageRef.downloadUrl.addOnSuccessListener { downloadUrl ->
                    // Guardar en Firestore
                    val servicioData = hashMapOf(
                        "nombre" to nombre,
                        "tipo" to tipo,
                        "imagenUrl" to downloadUrl.toString()
                    )

                    firestore.collection("servicios")
                        .add(servicioData)
                        .addOnSuccessListener { /* Servicio guardado con éxito */ }
                        .addOnFailureListener { /* Manejo de errores */ }
                }
            }.addOnFailureListener { /* Manejo de errores en la subida */ }
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun PreviewServiciosScreen() {
        ServiciosScreen()
    }
}
