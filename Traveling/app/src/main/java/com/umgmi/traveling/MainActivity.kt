package com.umgmi.traveling

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainScreen()
        }
    }

    @Composable
    fun MainScreen() {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Text(
                text = "Traveling", // Cambia esto por el nombre real de tu aplicación
                fontSize = 34.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF6200EA), // Puedes cambiar el color del texto
                modifier = Modifier.padding(bottom = 20.dp) // Espaciado debajo del texto
            )
            // Imagen
            Image(
                painter = painterResource(id = R.drawable.avion),
                contentDescription = stringResource(id = R.string.image_description),
                modifier = Modifier
                    .size(400.dp)
                    .padding(bottom = 30.dp)
            )


            // Botón de Login
            Button(
                onClick = {
                    val intent = Intent(this@MainActivity, Login::class.java)
                    startActivity(intent)
                },
                modifier = Modifier
                    .width(200.dp)
                    .height(60.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF6200EA) // Fondo morado
                )
            ) {
                Text(text = stringResource(id = R.string.Login), color = Color.White) // Texto blanco
            }

            // Botón de Register
            Button(
                onClick = {
                    val intent = Intent(this@MainActivity, Registro::class.java)
                    startActivity(intent)
                },
                modifier = Modifier
                    .width(200.dp)
                    .height(75.dp)
                    .padding(top = 15.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF6200EA) // Fondo morado
                )
            ) {
                Text(text = stringResource(id = R.string.Register), color = Color.White) // Texto blanco
            }
        }
    }

    // Función de vista previa
    @Preview(showBackground = true)
    @Composable
    fun PreviewMainScreen() {
        MainScreen()
    }
}
