package com.umgmi.traveling

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts

class Registro : ComponentActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private lateinit var storage: StorageReference
    private lateinit var imageUri: Uri

    private lateinit var imagePickerLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inicializa FirebaseAuth, FirebaseDatabase y FirebaseStorage
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference
        storage = FirebaseStorage.getInstance().reference

        // Inicializa el lanzador para seleccionar imágenes
        imagePickerLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val data = result.data
                if (data != null && data.data != null) {
                    imageUri = data.data!!
                }
            }
        }

        setContent {
            RegistroScreen()
        }
    }

    @Composable
    fun RegistroScreen() {
        var nombreState = remember { mutableStateOf(TextFieldValue("")) }
        var correoState = remember { mutableStateOf(TextFieldValue("")) }
        var contraseñaState = remember { mutableStateOf(TextFieldValue("")) }
        var imageSelected by remember { mutableStateOf(false) }

        Scaffold { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = getString(R.string.app_name),
                    style = MaterialTheme.typography.headlineLarge
                )

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = getString(R.string.Crear_Cuenta),
                    style = MaterialTheme.typography.titleLarge
                )

                Spacer(modifier = Modifier.height(20.dp))

                OutlinedTextField(
                    value = nombreState.value,
                    onValueChange = { nombreState.value = it },
                    label = { Text(getString(R.string.Nombre)) },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(20.dp))

                OutlinedTextField(
                    value = correoState.value,
                    onValueChange = { correoState.value = it },
                    label = { Text(getString(R.string.Correo)) },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(20.dp))

                OutlinedTextField(
                    value = contraseñaState.value,
                    onValueChange = { contraseñaState.value = it },
                    label = { Text(getString(R.string.Contraseña)) },
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(autoCorrect = false),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = {
                        // Llama al lanzador de selección de imágenes
                        imagePickerLauncher.launch(Intent().apply {
                            type = "image/*"
                            action = Intent.ACTION_GET_CONTENT
                        })
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Seleccionar Imagen")
                }

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = {
                        val correo = correoState.value.text.trim()
                        val contraseña = contraseñaState.value.text.trim()
                        if (correo.isEmpty() || contraseña.isEmpty() || nombreState.value.text.isEmpty()) {
                            Toast.makeText(this@Registro, "Por favor completa todos los campos", Toast.LENGTH_SHORT).show()
                        } else if (!::imageUri.isInitialized) {
                            Toast.makeText(this@Registro, "Por favor selecciona una imagen", Toast.LENGTH_SHORT).show()
                        } else {
                            registrarUsuario(correo, contraseña, nombreState.value.text.trim())
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = getString(R.string.RegistrarU))
                }

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = getString(R.string.Cuentaex),
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.clickable {
                        startActivity(Intent(this@Registro, MainActivity::class.java))
                        finish()
                    }
                )
            }
        }
    }

    private fun registrarUsuario(correo: String, contraseña: String, nombre: String) {
        auth.createUserWithEmailAndPassword(correo, contraseña)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val userId = auth.currentUser?.uid

                    userId?.let {
                        subirImagen(imageUri, it, correo, nombre, contraseña)
                    } ?: run {
                        Toast.makeText(this, "Error: ID de usuario no encontrado", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this, "Error al registrar: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun subirImagen(uri: Uri, userId: String, correo: String, nombre: String, contraseña: String) {
        val storageRef = storage.child("profile_images/$userId")

        storageRef.putFile(uri)
            .addOnSuccessListener {
                storageRef.downloadUrl.addOnSuccessListener { downloadUrl ->
                    val userData = hashMapOf(
                        "email" to correo,
                        "Nombre" to nombre,
                        "contra" to contraseña,
                        "imageUrl" to downloadUrl.toString() // Guarda la URL de la imagen
                    )

                    // Guarda los datos del usuario en Realtime Database
                    database.child("users").child(userId).setValue(userData)
                        .addOnSuccessListener {
                            Toast.makeText(this, "Registro exitoso y datos guardados", Toast.LENGTH_SHORT).show()
                            val intent = Intent(this, Menu_Principal::class.java)
                            startActivity(intent)
                            finish()
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(this, "Error al guardar datos: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error al subir la imagen: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    @Preview(showBackground = true)
    @Composable
    fun PreviewRegistroScreen() {
        RegistroScreen()
    }
}
