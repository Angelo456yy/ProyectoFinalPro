package com.umgmi.traveling

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth

class Login : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth // Declaración de FirebaseAuth
    private lateinit var editTextCorreo: EditText
    private lateinit var editTextContraseña: EditText
    private lateinit var buttonIniciarSesion: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)

        // Inicializar FirebaseAuth
        auth = FirebaseAuth.getInstance()

        // Configuración de vista
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Inicialización de los campos
        editTextCorreo = findViewById(R.id.editTextCorreo)
        editTextContraseña = findViewById(R.id.editTextContraseña)
        buttonIniciarSesion = findViewById(R.id.buttonIniciarSesion)

        // Manejo de clics en el botón de iniciar sesión
        buttonIniciarSesion.setOnClickListener {
            iniciarSesion()
        }

        val textViewCrearCuenta: TextView = findViewById(R.id.textViewCrearCuenta)
        textViewCrearCuenta.setOnClickListener {
            // Navegar a la actividad de creación de cuenta
            val intent = Intent(this, Registro::class.java) // Cambia a la actividad de registro
            startActivity(intent)
        }
    }

    private fun iniciarSesion() {
        val correo = editTextCorreo.text.toString().trim()
        val contraseña = editTextContraseña.text.toString().trim()

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
                    val intent = Intent(this, Menu_Principal::class.java) // Cambia a tu actividad principal
                    startActivity(intent)
                    finish() // Cierra la actividad de inicio de sesión
                } else {
                    // Si el inicio de sesión falla, muestra un mensaje al usuario
                    Toast.makeText(this, "Error al iniciar sesión: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }
}
