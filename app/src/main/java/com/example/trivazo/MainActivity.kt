package com.example.trivazo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast

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
            textView.text = "¡Hola niños, bienvenidos a mi app!"
        }
    }
}