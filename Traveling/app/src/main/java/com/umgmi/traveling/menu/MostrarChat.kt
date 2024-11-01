package com.umgmi.traveling.menu

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

data class UsuarioModel(
    val id: String = "",
    val nombre: String = "",
    val email: String = "",
)

class MostrarChat : ComponentActivity() {
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        database = FirebaseDatabase.getInstance().reference

        setContent {
            MostrarUsuariosScreen(database)
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun MostrarUsuariosScreen(database: DatabaseReference) {
        val usuarios = remember { mutableStateListOf<UsuarioModel>() }
        val loading = remember { mutableStateOf(true) }

        // Cargar los usuarios de Realtime Database
        LaunchedEffect(Unit) {
            database.child("users").addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    usuarios.clear()
                    for (userSnapshot in snapshot.children) {
                        val usuario = userSnapshot.getValue(UsuarioModel::class.java)
                        usuario?.let { usuarios.add(it) }
                    }
                    loading.value = false // Cambiar el estado de carga
                }

                override fun onCancelled(error: DatabaseError) {
                    // Manejar el error
                    loading.value = false
                }
            })
        }

        Scaffold(
            topBar = {
                TopAppBar(title = { Text("Usuarios Disponibles") })
            }
        ) { padding ->
            if (loading.value) {
                // Mostrar un indicador de carga mientras se obtienen los datos
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else {
                LazyColumn(
                    modifier = Modifier.padding(padding).fillMaxSize()
                ) {
                    items(usuarios) { usuario ->
                        UsuarioCard(usuario)
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }
        }
    }

    @Composable
    fun UsuarioCard(usuario: UsuarioModel) {
        val context = LocalContext.current
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    val intent = Intent(context, ChatDetailActivity::class.java).apply {
                        putExtra("USER_EMAIL", usuario.email) // Pasar el correo del usuario
                    }
                    context.startActivity(intent)
                },
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = "Nombre: ${usuario.nombre}")
                Text(text = "Email: ${usuario.email}")
            }
        }
    }
}
