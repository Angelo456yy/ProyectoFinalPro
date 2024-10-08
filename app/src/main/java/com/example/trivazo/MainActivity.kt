package com.example.trivazo

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Referencia al TextView
        val textView: TextView = findViewById(R.id.textView)

        // Referencia al botón
        val button: Button = findViewById(R.id.button)

        // Acción al presionar el botón
        button.setOnClickListener {
            Toast.makeText(this, "¡Botón presionado!", Toast.LENGTH_SHORT).show()
            textView.text = "¡Hola Viajeros, nosotros de apoyamos :)!"
        }
    }
}
