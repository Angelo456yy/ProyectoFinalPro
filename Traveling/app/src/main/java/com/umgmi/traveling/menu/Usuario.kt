package com.umgmi.traveling.menu

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.umgmi.traveling.Menu_Principal
import com.umgmi.traveling.Pantalla_De_Carga
import com.umgmi.traveling.R

class Usuario : ComponentActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    private var userEmail by mutableStateOf("")
    private var userName by mutableStateOf("")
    private var userPassword by mutableStateOf("")
    private var userLikes by mutableStateOf("Vacío") // Inicializado como "Vacío"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            UsuarioScreen()
        }
        // Inicializa FirebaseAuth y Firestore
        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        // Obtener información del usuario
        fetchUserInfo()
    }

    private fun fetchUserInfo() {
        val user = auth.currentUser
        user?.let {
            userEmail = it.email ?: "No disponible"
            userName = it.displayName ?: "No disponible"

            fetchUserLikes(it.uid)
        }
    }

    private fun fetchUserLikes(userId: String) {
        firestore.collection("users").document(userId).get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    userLikes = document.getString("likes") ?: "Vacío"
                } else {
                    userLikes = "Vacío"
                }
            }
            .addOnFailureListener {
                userLikes = "Error al obtener información"
            }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun UsuarioScreen() {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(
                        text = "Informacion",
                        color = Color.White, // Cambiar el color del texto a blanco
                        fontSize = 30.sp)   },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF6200EE)),
                    actions = {
                        IconButton(onClick = { cerrarSesion() }) {
                            Icon(
                                imageVector = Icons.Default.ExitToApp,
                                contentDescription = "Salir",
                                tint = Color.White)
                        }
                    }
                )
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .padding(16.dp)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Información del Usuario",
                    style = TextStyle(fontSize = 24.sp, color = Color.Blue) // Estilo de letra del título
                )
                Spacer(modifier = Modifier.height(16.dp))

                // Imagen encima del correo
                Image(
                    painter = painterResource(id = R.drawable.usuario),
                    contentDescription = "Imagen del usuario",
                    modifier = Modifier
                        .size(200.dp) // Tamaño de la imagen
                        .padding(bottom = 8.dp) // Espacio entre la imagen y el texto
                )

                // Mostrando la información del usuario
                Text(text = "Correo: $userEmail", style = TextStyle(fontSize = 18.sp))
                Text(text = "Nombre: $userName", style = TextStyle(fontSize = 18.sp))
                Text(text = "Le gusta: $userLikes", style = TextStyle(fontSize = 18.sp))

                Spacer(modifier = Modifier.height(24.dp))

                Button(onClick = { volverAlMenu() }) {
                    Text(text = "Volver al Menú Principal")
                }


                Text(
                    text = "Todos los derechos reservados",
                    modifier = Modifier.padding(top = 16.dp),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }

    private fun volverAlMenu() {
        startActivity(Intent(this, Menu_Principal::class.java))
        finish() // Finaliza la actividad actual
    }

    private fun cerrarSesion() {
        auth.signOut()
        startActivity(Intent(this, Pantalla_De_Carga::class.java))
        finish()
    }

    @Preview(showBackground = true)
    @Composable
    fun PreviewUsuarioScreen() {
        MaterialTheme {
            UsuarioScreen()
        }
    }
}


