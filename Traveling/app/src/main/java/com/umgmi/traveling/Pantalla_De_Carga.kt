package com.umgmi.traveling

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth

class Pantalla_De_Carga : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    @SuppressLint("MissingInflatedId", "LocalSuppress")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_pantalla_de_carga)

        // Inicializa FirebaseAuth
        auth = FirebaseAuth.getInstance()

        val imageView = findViewById<ImageView>(R.id.gifImageView)

        // Carga la imagen de carga
        Glide.with(this)
            .load(R.drawable.cargas)
            .into(imageView)

        // Tiempo de espera para la pantalla de carga
        val tiempo = 7000L
        Handler(Looper.getMainLooper()).postDelayed({
            verificarInicioSesion() // Verifica el estado de inicio de sesión
        }, tiempo)
    }

    // Función para verificar el estado de inicio de sesión
    private fun verificarInicioSesion() {
        // Comprueba si el usuario está autenticado
        val currentUser = auth.currentUser
        if (currentUser != null) {
            // Usuario está autenticado, ir a la actividad principal
            startActivity(Intent(this, MainActivity::class.java))
        } else {
            // Usuario no está autenticado, ir a la actividad de registro
            startActivity(Intent(this, Registro::class.java))
        }
        finish() // Cierra la pantalla de carga
    }
}
