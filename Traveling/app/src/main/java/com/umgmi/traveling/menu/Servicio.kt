package com.umgmi.traveling.menu

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
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
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.util.UUID

class Servicio : ComponentActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private lateinit var storage: FirebaseStorage

    private val pickImageRequest = 1
    private var selectedImageUri: Uri? = null

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
        var expanded by remember { mutableStateOf(false) }
        var isFree by remember { mutableStateOf(true) }
        var amount by remember { mutableStateOf(TextFieldValue("")) }
        var lugar by remember { mutableStateOf(TextFieldValue("")) } // Campo para el lugar

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Text("Ofrecer un Servicio", fontSize = 24.sp) // Aumentar el tamaño del texto

            // Campo para el nombre del servicio
            OutlinedTextField(
                value = servicioNombre,
                onValueChange = { servicioNombre = it },
                label = { Text("Nombre del Servicio") },
                modifier = Modifier.fillMaxWidth()
            )

            // Campo para el lugar del servicio
            OutlinedTextField(
                value = lugar,
                onValueChange = { lugar = it },
                label = { Text("Lugar del Servicio") },
                modifier = Modifier.fillMaxWidth()
            )

            // Selector para el tipo de servicio
            Box {
                TextButton(onClick = { expanded = true }) {
                    Text(servicioTipo)
                }
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    listOf("Alojamiento", "Comida", "Tour").forEach { tipo ->
                        DropdownMenuItem(onClick = {
                            servicioTipo = tipo
                            expanded = false
                        }) {
                            Text(tipo)
                        }
                    }
                }
            }

            // Selección de tipo de pago
            Text("Tipo de Pago:", modifier = Modifier.padding(top = 8.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(top = 8.dp)
            ) {
                RadioButton(
                    selected = isFree,
                    onClick = {
                        isFree = true
                        amount = TextFieldValue("")
                    }
                )
                Text("Gratis")

                Spacer(modifier = Modifier.width(16.dp))

                RadioButton(
                    selected = !isFree,
                    onClick = {
                        isFree = false
                    }
                )
                Text("Monto: ")
                if (!isFree) {
                    OutlinedTextField(
                        value = amount,
                        onValueChange = { amount = it },
                        label = { Text("Ingrese monto") },
                        modifier = Modifier.width(100.dp)
                    )
                }
            }

            // Botón para seleccionar imagen
            Button(onClick = { selectImage() }) {
                Text("Seleccionar Imagen")
            }

            // Mostrar imagen seleccionada
            selectedImageUri?.let { uri ->
                Image(
                    bitmap = MediaStore.Images.Media.getBitmap(contentResolver, uri).asImageBitmap(),
                    contentDescription = null,
                    modifier = Modifier
                        .size(100.dp)
                        .clip(RoundedCornerShape(8.dp))
                )
            }

            // Botón para guardar el servicio
            Button(
                onClick = {
                    guardarServicio(servicioNombre.text, servicioTipo, selectedImageUri, if (isFree) "Gratis" else amount.text, lugar.text)
                },
                modifier = Modifier.padding(top = 16.dp)
            ) {
                Text("Guardar Servicio")
            }
        }
    }

    private fun DropdownMenuItem(onClick: () -> Unit, interactionSource: @Composable () -> Unit) {


    }

    private fun selectImage() {
        val intent = Intent(Intent.ACTION_PICK).apply {
            type = "image/*"
        }
        startActivityForResult(intent, pickImageRequest)
    }

    // Manejar el resultado de la selección de la imagen
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == pickImageRequest && resultCode == RESULT_OK && data != null) {
            selectedImageUri = data.data
        }
    }

    private fun guardarServicio(nombre: String, tipo: String, imageUri: Uri?, payment: String, lugar: String) {
        if (imageUri != null) {
            val storageRef = storage.reference.child("servicios/${UUID.randomUUID()}")
            val uploadTask = storageRef.putFile(imageUri)

            uploadTask.addOnSuccessListener {
                storageRef.downloadUrl.addOnSuccessListener { downloadUrl ->
                    // Guardar en Firestore
                    val servicioData = hashMapOf(
                        "nombre" to nombre,
                        "tipo" to tipo,
                        "imagenUrl" to downloadUrl.toString(),
                        "pago" to payment,
                        "lugar" to lugar // Agregar el lugar a los datos
                    )

                    firestore.collection("servicios")
                        .add(servicioData)
                        .addOnSuccessListener {
                            // Aquí puedes manejar la navegación al menú principal
                            // Por ejemplo, usando un Intent para abrir la actividad del menú principal
                        }
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
