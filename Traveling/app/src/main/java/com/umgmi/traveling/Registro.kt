package com.umgmi.traveling

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Registro : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_registro)

        // Ajustar los insets de las ventanas
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Referencia al TextView "Tengo una cuenta"
        val textViewCuentaExistente: TextView = findViewById(R.id.textViewCrearCuenta)

        // Configurar el OnClickListener para "Tengo una cuenta"
        textViewCuentaExistente.setOnClickListener {
            // Crear un Intent para ir a la actividad principal
            val intent = Intent(this, MainActivity::class.java) // Cambiado a Main
            startActivity(intent)
            // Opcional: cerrar la actividad de registro
            finish()
        }

        // Puedes añadir más lógica aquí para el botón de registrar o otros elementos
    }
}
