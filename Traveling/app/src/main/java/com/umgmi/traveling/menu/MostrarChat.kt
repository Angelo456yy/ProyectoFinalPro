package com.umgmi.traveling.menu

import android.content.Intent
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.umgmi.traveling.Menu_Principal
import com.umgmi.traveling.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

import java.io.Serializable

data class UsuarioModel(
    val id: String = "",
    val nombre: String = "",
    val email: String = "",

) : Serializable


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
                TopAppBar(
                    title = { Text("Usuarios Disponibles") },
                    navigationIcon = {
                        IconButton(onClick = {
                            // Regresar a Menu_Principal
                            val intent = Intent(this@MostrarChat, Menu_Principal::class.java)
                            startActivity(intent)
                        }) {
                            Icon(painter = painterResource(id = R.drawable.home), contentDescription = "Regresar")
                        }
                    },
                    modifier = Modifier.background(MaterialTheme.colorScheme.primary)
                )
            }
        ) { padding ->
            if (loading.value) {
                // Mostrar un indicador de carga mientras se obtienen los datos
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .padding(padding)
                        .fillMaxSize()
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
                .padding(8.dp)
                .clickable {
                    val intent = Intent(context, ChatDetailActivity::class.java).apply {
                        putExtra("usuario", usuario) // Pasa el objeto completo
                    }
                    context.startActivity(intent)
                },
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = "Nombre: ${usuario.nombre}", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = "Email: ${usuario.email}", fontSize = 16.sp)

            }
        }
    }
}
