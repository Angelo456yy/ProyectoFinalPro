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

class Pantalla_De_Carga : AppCompatActivity() {

    @SuppressLint("MissingInflatedId", "LocalSuppress")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_pantalla_de_carga)


        val imageView = findViewById<ImageView>(R.id.gifImageView)


        Glide.with(this)
            .load(R.drawable.cargas)
            .into(imageView)


        val tiempo = 7000L
        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }, tiempo)
    }
}
