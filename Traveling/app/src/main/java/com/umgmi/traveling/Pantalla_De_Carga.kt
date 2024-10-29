package com.umgmi.traveling

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class Pantalla_De_Carga : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pantalla_de_carga)

        val imageView = findViewById<ImageView>(R.id.gifImageView)

        // Carga la imagen de carga
        Glide.with(this)
            .load(R.drawable.cargas)
            .into(imageView)

        // Tiempo de espera para la pantalla de carga usando corutinas
        lifecycleScope.launch {
            delay(7000L)
            irAlaActividadPrincipal() // Cambia a la actividad principal
        }
    }

    // Función para ir a la actividad principal
    private fun irAlaActividadPrincipal() {
        startActivity(Intent(this, MainActivity::class.java))
        finish() // Cierra la pantalla de carga
    }
}
