package com.umgmi.traveling.menu

import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
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
import com.umgmi.traveling.Menu_Principal
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
        var servicioTipo by remember { mutableStateOf("") } // Ahora almacenamos el tipo como String
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
            Text("Ofrecer un Servicio", fontSize = 34.sp)

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

            // Selección de tipo de servicio con RadioButtons
            Text("Tipo de Servicio:", modifier = Modifier.padding(top = 10.dp))
            Row(modifier = Modifier.padding(top = 10.dp)) {
                RadioButton(
                    selected = servicioTipo == "Alojamiento",
                    onClick = { servicioTipo = "Alojamiento" }
                )
                Text("Alojamiento")

                Spacer(modifier = Modifier.width(16.dp))

                RadioButton(
                    selected = servicioTipo == "Comida",
                    onClick = { servicioTipo = "Comida" }
                )
                Text("Comida")

                Spacer(modifier = Modifier.width(16.dp))

                RadioButton(
                    selected = servicioTipo == "Tour",
                    onClick = { servicioTipo = "Tour" }
                )
                Text("Tour")
            }

            // Mostrar el tipo de servicio seleccionado
            if (servicioTipo.isNotEmpty()) {
                Text("Tipo de Servicio Seleccionado: $servicioTipo", modifier = Modifier.padding(top = 8.dp))
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
                        amount = TextFieldValue("") // Resetear monto si es gratis
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

            // Mostrar imagen seleccionada con un espacio adecuado
            Spacer(modifier = Modifier.height(16.dp))
            selectedImageUri?.let { uri ->
                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, uri)
                Image(
                    bitmap = bitmap.asImageBitmap(),
                    contentDescription = null,
                    modifier = Modifier
                        .size(200.dp) // Espacio para la imagen
                        .clip(RoundedCornerShape(8.dp))
                )
            } ?: run {
                Text("No se ha seleccionado ninguna imagen", modifier = Modifier.padding(top = 16.dp))
            }

            // Botón para guardar el servicio
            Button(
                onClick = {
                    val userEmail = auth.currentUser?.email // Obtener el correo del usuario
                    guardarServicio(servicioNombre.text, servicioTipo, selectedImageUri, if (isFree) "Gratis" else amount.text, lugar.text, userEmail)
                },
                modifier = Modifier.padding(top = 16.dp)
            ) {
                Text("Guardar Servicio")
            }
        }
    }

    private fun selectImage() {
        val intent = Intent(Intent.ACTION_PICK).apply {
            type = "image/*"
        }
        startActivityForResult(intent, pickImageRequest)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == pickImageRequest && resultCode == RESULT_OK && data != null) {
            selectedImageUri = data.data
        }
    }

    private fun guardarServicio(nombre: String, tipo: String, imageUri: Uri?, payment: String, lugar: String, userEmail: String?) {
        if (imageUri != null && userEmail != null) {
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
                        "lugar" to lugar,
                        "usuario" to userEmail
                    )

                    firestore.collection("servicios")
                        .add(servicioData)
                        .addOnSuccessListener {
                            // Mostrar mensaje de confirmación
                            Toast.makeText(this, "Servicio guardado exitosamente", Toast.LENGTH_SHORT).show()

                            // Navegar al menú principal
                            startActivity(Intent(this, Menu_Principal::class.java)) // Cambia MenuPrincipal a la actividad que deseas
                            finish()
                        }
                        .addOnFailureListener {
                            Toast.makeText(this, "Error al guardar el servicio", Toast.LENGTH_SHORT).show()
                        }
                }
            }.addOnFailureListener {
                Toast.makeText(this, "Error en la subida de la imagen", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "Seleccione una imagen y asegúrese de estar registrado", Toast.LENGTH_SHORT).show()
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun PreviewServiciosScreen() {
        ServiciosScreen()
    }
}
