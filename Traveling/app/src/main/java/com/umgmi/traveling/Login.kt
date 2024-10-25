package com.umgmi.traveling

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseAuth

class Login : ComponentActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()

        setContent {
            LoginScreen()
        }
    }

    @Composable
    fun LoginScreen() {
        val emailState = remember { mutableStateOf("") }
        val passwordState = remember { mutableStateOf("") }

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
                    text = stringResource(id = R.string.Iniciar_Sesion),
                    style = MaterialTheme.typography.headlineLarge,
                    modifier = Modifier.padding(bottom = 20.dp)
                )

                // Campo de correo
                TextField(
                    value = emailState.value,
                    onValueChange = { emailState.value = it },
                    label = { Text(stringResource(id = R.string.Correo_Login)) },
                    modifier = Modifier.fillMaxWidth()
                )

                // Campo de contraseña
                TextField(
                    value = passwordState.value,
                    onValueChange = { passwordState.value = it },
                    label = { Text(stringResource(id = R.string.Contraseña_Login)) },
                    modifier = Modifier.fillMaxWidth(),
                    visualTransformation = PasswordVisualTransformation() // Para ocultar la contraseña
                )

                // Botón de iniciar sesión
                Button(
                    onClick = { iniciarSesion(emailState.value, passwordState.value) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 20.dp)
                ) {
                    Text(text = stringResource(id = R.string.Boton_Iniciar_Sesion))
                }

                // Texto para crear cuenta
                TextButton(
                    onClick = {
                        val intent = Intent(this@Login, Registro::class.java)
                        startActivity(intent)
                    },
                    modifier = Modifier.padding(top = 20.dp)
                ) {
                    Text(
                        text = stringResource(id = R.string.Crear_Cuenta_Login),
                        color = Color.Blue
                    )
                }
            }
        }
    }

    private fun iniciarSesion(correo: String, contraseña: String) {
        // Validar campos
        if (correo.isEmpty() || contraseña.isEmpty()) {
            Toast.makeText(this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show()
            return
        }

        // Iniciar sesión con Firebase
        auth.signInWithEmailAndPassword(correo, contraseña)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Inicio de sesión exitoso
                    Toast.makeText(this, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, Menu_Principal::class.java)
                    startActivity(intent)
                    finish() // Cierra la actividad de inicio de sesión
                } else {
                    // Si el inicio de sesión falla, muestra un mensaje al usuario
                    Toast.makeText(this, "Error al iniciar sesión: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }

    // Función de vista previa
    @Preview(showBackground = true)
    @Composable
    fun PreviewLoginScreen() {
        LoginScreen()
    }
}
