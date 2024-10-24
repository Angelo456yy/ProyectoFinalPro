package com.umgmi.traveling

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowCompat

class Infor : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge() // Habilitar el diseño de borde a borde

        setContent {
            InfoScreen() // Llama a la pantalla de información
        }
    }

    // Habilitar el diseño de borde a borde
    private fun enableEdgeToEdge() {
        WindowCompat.setDecorFitsSystemWindows(window, false) // Para Android 11 y superior
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InfoScreen() {
    var userInfo by remember { mutableStateOf(TextFieldValue()) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Información del Usuario", color = Color.White) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primary)
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
                .background(MaterialTheme.colorScheme.background),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            DisplayLocalGif() // Mostrar el GIF desde los recursos

            Spacer(modifier = Modifier.height(16.dp))

            // Aumentar el tamaño de la fuente y centrar el texto
            Text(
                "Por favor, ingrese su información básica",
                style = MaterialTheme.typography.titleMedium.copy(color = Color.White, fontSize = 20.sp),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Campo grande para información del usuario
            OutlinedTextField(
                value = userInfo.text,
                onValueChange = { userInfo = TextFieldValue(it) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .padding(vertical = 8.dp),
                label = { Text("Escriba aquí su información (intereses, experiencia de viaje, etc.)") },
                maxLines = 5
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Botón para guardar información
            Button(
                onClick = { /* Lógica para guardar información */ },
                modifier = Modifier.padding(vertical = 8.dp)
            ) {
                Text("Guardar Información")
            }
        }
    }
}

// Composable para mostrar el GIF localmente
@Composable
fun DisplayLocalGif() {
    Image(
        painter = painterResource(id = R.drawable.cargas), // Asegúrate de que el nombre coincida con el nombre del archivo
        contentDescription = "GIF de ejemplo",
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewInfoScreen() {
    InfoScreen()
}
