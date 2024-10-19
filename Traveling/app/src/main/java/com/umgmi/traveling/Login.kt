package com.umgmi.traveling

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Login : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)

        // Ajustar los insets de las ventanas
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Referencia al TextView "Crear Cuenta"
        val textViewCrearCuenta: TextView = findViewById(R.id.textViewCrearCuenta)

        // Configurar el OnClickListener para "Crear Cuenta"
        textViewCrearCuenta.setOnClickListener {
            // Crear un Intent para regresar a la actividad principal
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            // Opcional: cerrar la actividad de inicio de sesión
            finish()
        }
    }
}
