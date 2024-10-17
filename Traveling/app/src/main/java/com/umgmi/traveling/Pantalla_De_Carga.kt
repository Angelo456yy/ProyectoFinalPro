package com.umgmi.traveling

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper // Importa Looper
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide

class Pantalla_De_Carga : AppCompatActivity() {

    @SuppressLint("MissingInflatedId", "LocalSuppress")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_pantalla_de_carga)

        // Inicializar el ImageView
        val imageView = findViewById<ImageView>(R.id.gifImageView)

        // Cargar la imagen usando Glide
        Glide.with(this)
            .load(R.drawable.cargas) // Asegúrate de que el nombre de la imagen sea correcto
            .into(imageView)

        // Manejar el retraso antes de iniciar la actividad principal
        val tiempo = 3000L // Tiempo de espera (3 segundos)
        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, MainActivity::class.java)) // Iniciar la actividad principal
            finish() // Finaliza esta actividad
        }, tiempo)
    }
}
