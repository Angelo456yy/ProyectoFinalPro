package com.umgmi.traveling.menu

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.umgmi.traveling.Menu_Principal
import java.util.UUID

class Servicio : ComponentActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private lateinit var storage: FirebaseStorage
    private lateinit var getImageResult: ActivityResultLauncher<Intent> // Declarar el lanzador

    private val pickImageRequest = 1
    private var selectedImageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inicializar el ActivityResultLauncher
        getImageResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                selectedImageUri = result.data?.data
            }
        }

        // Inicializar Firebase
        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()
        storage = FirebaseStorage.getInstance()

        // Verificar permisos para acceder a la galería
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), pickImageRequest)
        } else {
            // Establecer el contenido de la pantalla si ya se tienen los permisos
            setContent {
                ServiciosScreen()
            }
        }
    }

    @Composable
    fun ServiciosScreen() {
        var servicioNombre by remember { mutableStateOf(TextFieldValue("")) }
        var servicioTipo by remember { mutableStateOf(TextFieldValue("")) }
        var isFree by remember { mutableStateOf(true) }
        var amount by remember { mutableStateOf(TextFieldValue("")) }
        var lugar by remember { mutableStateOf(TextFieldValue("")) }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Text("Ofrecer un Servicio", fontSize = 24.sp)

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

            // Campo para el tipo de servicio
            OutlinedTextField(
                value = servicioTipo,
                onValueChange = { servicioTipo = it },
                label = { Text("Tipo de Servicio (escribe aquí)") },
                modifier = Modifier.fillMaxWidth()
            )

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
                        amount = TextFieldValue("") // Resetear monto si se selecciona gratis
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
                // Convertir la URI a un bitmap y mostrar la imagen
                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, uri)
                Image(
                    bitmap = bitmap.asImageBitmap(),
                    contentDescription = null,
                    modifier = Modifier
                        .size(200.dp) // Espacio para la imagen
                        .clip(RoundedCornerShape(8.dp))
                )
            } ?: run {
                // Mostrar un mensaje cuando no se ha seleccionado ninguna imagen
                Text("No se ha seleccionado ninguna imagen", modifier = Modifier.padding(top = 16.dp))
            }

            // Botón para guardar el servicio
            Button(
                onClick = {
                    val userEmail = auth.currentUser?.email // Obtener el correo del usuario
                    guardarServicio(servicioNombre.text, servicioTipo.text, selectedImageUri, if (isFree) "Gratis" else amount.text, lugar.text, userEmail)
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
        getImageResult.launch(intent) // Usar el lanzador
    }

    private fun guardarServicio(nombre: String, tipo: String, imageUri: Uri?, payment: String, lugar: String, userEmail: String?) {
        val userId = auth.currentUser?.uid
        if (userId == null) {
            Toast.makeText(this, "Usuario no autenticado", Toast.LENGTH_SHORT).show()
            return
        }

        if (imageUri == null) {
            Toast.makeText(this, "Seleccione una imagen", Toast.LENGTH_SHORT).show()
            return
        }

        val documentId = UUID.randomUUID().toString()
        val storageRef = storage.reference.child("servicios/$userId/$documentId") // Ruta para guardar la imagen

        // Subir la imagen
        val uploadTask = storageRef.putFile(imageUri)

        uploadTask.addOnSuccessListener {
            // Obtener la URL de la imagen subida
            storageRef.downloadUrl.addOnSuccessListener { downloadUrl ->
                // Guardar el servicio en Firestore
                val servicioData = hashMapOf(
                    "nombre" to nombre,
                    "tipo" to tipo,
                    "imagenUrl" to downloadUrl.toString(),
                    "pago" to payment,
                    "lugar" to lugar,
                    "usuarioEmail" to userEmail
                )
                firestore.collection("servicios")
                    .document(documentId)
                    .set(servicioData)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Servicio guardado con éxito", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, Menu_Principal::class.java)) // Navega al menú principal
                        finish()
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "Error al guardar el servicio intente mas tarde", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, Menu_Principal::class.java)) // Navega al menú principal
                        finish()
                    }
            }
        }.addOnFailureListener {
            Toast.makeText(this, "Formato invalido", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, Menu_Principal::class.java)) // Navega al menú principal
            finish()
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun PreviewServiciosScreen() {
        ServiciosScreen()
    }
}
