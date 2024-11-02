package com.umgmi.traveling.menu

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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
        var lugarServicio by remember { mutableStateOf(TextFieldValue("")) }
        var servicioTipo by remember { mutableStateOf("Alojamiento") }
        var tipoPago by remember { mutableStateOf("Gratis") }
        var monto by remember { mutableStateOf(TextFieldValue("")) }
        var selectedImageUri by remember { mutableStateOf<Uri?>(null) }

        val imagePickerLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.GetContent(),
            onResult = { uri: Uri? -> selectedImageUri = uri }
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Ofrecer un Servicio", style = MaterialTheme.typography.headlineMedium)

            OutlinedTextField(
                value = servicioNombre,
                onValueChange = { servicioNombre = it },
                label = { Text("Nombre del Servicio") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = lugarServicio,
                onValueChange = { lugarServicio = it },
                label = { Text("Lugar del Servicio") },
                modifier = Modifier.fillMaxWidth()
            )

            // Tipo de Servicio
            Text("Tipo de Servicio:")
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                listOf("Alojamiento", "Comida", "Tour").forEach { tipo ->
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(
                            selected = servicioTipo == tipo,
                            onClick = { servicioTipo = tipo }
                        )
                        Text(tipo)
                    }
                }
            }

            // Tipo de Pago
            Text("Tipo de Pago:")
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(
                        selected = tipoPago == "Gratis",
                        onClick = { tipoPago = "Gratis" }
                    )
                    Text("Gratis")
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(
                        selected = tipoPago == "Monto",
                        onClick = { tipoPago = "Monto" }
                    )
                    Text("Monto:")
                }
            }

            if (tipoPago == "Monto") {
                OutlinedTextField(
                    value = monto,
                    onValueChange = { monto = it },
                    label = { Text("Monto") },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Button(onClick = { imagePickerLauncher.launch("image/*") }) {
                Text("Seleccionar Imagen")
            }

            selectedImageUri?.let { uri ->
                Text("Imagen seleccionada: ${uri.lastPathSegment}")
            } ?: Text("No se ha seleccionado ninguna imagen")

            Button(
                onClick = {
                    guardarServicio(
                        servicioNombre.text,
                        servicioTipo,
                        lugarServicio.text,
                        tipoPago,
                        monto.text,
                        selectedImageUri
                    )
                },
                modifier = Modifier.padding(top = 16.dp)
            ) {
                Text("Guardar Servicio")
            }
        }
    }

    private fun guardarServicio(nombre: String, tipo: String, lugar: String, pago: String, monto: String, imageUri: Uri?) {
        val user = auth.currentUser
        val userEmail = user?.email ?: "Correo no disponible" // Manejo del correo

        if (imageUri != null) {
            val storageRef = storage.reference.child("servicios/${UUID.randomUUID()}")
            val uploadTask = storageRef.putFile(imageUri)

            uploadTask.addOnSuccessListener {
                storageRef.downloadUrl.addOnSuccessListener { downloadUrl ->
                    // Guardar en Firestore
                    val servicioData = hashMapOf(
                        "nombre" to nombre,
                        "tipo" to tipo,
                        "lugar" to lugar,
                        "pago" to pago,
                        "monto" to if (pago == "Monto") monto else "0",
                        "imagenUrl" to downloadUrl.toString(),
                        "creadorEmail" to userEmail // Guardar el correo del creador
                    )

                    firestore.collection("servicios")
                        .add(servicioData)
                        .addOnSuccessListener {
                            Log.d("Firestore", "Servicio guardado con éxito")
                        }
                        .addOnFailureListener { e ->
                            Log.e("Firestore", "Error al guardar servicio: ${e.message}")
                        }
                }
            }.addOnFailureListener { e ->
                Log.e("Firestore", "Error al subir imagen: ${e.message}")
            }
        } else {
            val servicioData = hashMapOf(
                "nombre" to nombre,
                "tipo" to tipo,
                "lugar" to lugar,
                "pago" to pago,
                "monto" to if (pago == "Monto") monto else "0",
                "creadorEmail" to userEmail // Agrega el correo del creador
            )

            firestore.collection("servicios")
                .add(servicioData)
                .addOnSuccessListener {
                    Log.d("Firestore", "Servicio guardado con éxito")
                }
                .addOnFailureListener { e ->
                    Log.e("Firestore", "Error al guardar servicio: ${e.message}")
                }
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun PreviewServiciosScreen() {
        ServiciosScreen()
    }
}
