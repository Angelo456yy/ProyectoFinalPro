package com.umgmi.traveling

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Registro : AppCompatActivity() {

    // Inicializa la instancia de FirebaseAuth
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro)

        // Instancia de FirebaseAuth
        auth = FirebaseAuth.getInstance()

        // Obtén referencias de los elementos del layout
        val editTextNombre: EditText = findViewById(R.id.editTextNombre)
        val editTextCorreo: EditText = findViewById(R.id.editTextCorreo)
        val editTextContraseña: EditText = findViewById(R.id.editTextContraseña)
        val buttonRegistrar: Button = findViewById(R.id.buttonRegistrar)
        val textViewCuentaExistente: TextView = findViewById(R.id.textViewCrearCuenta)

        // Maneja los insets para el diseño de pantalla completa
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Maneja el clic para la cuenta existente
        textViewCuentaExistente.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        // Lógica para el botón de registro
        buttonRegistrar.setOnClickListener {
            val correo = editTextCorreo.text.toString().trim()
            val contraseña = editTextContraseña.text.toString().trim()

            if (correo.isEmpty() || contraseña.isEmpty()) {
                Toast.makeText(this, "Por favor completa todos los campos", Toast.LENGTH_SHORT).show()
            } else {
                registrarUsuario(correo, contraseña)
            }
        }
    }

    // Función para registrar un usuario en Firebase
    private fun registrarUsuario(correo: String, contraseña: String) {
        auth.createUserWithEmailAndPassword(correo, contraseña)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Registro exitoso, navega a la siguiente actividad o muestra un mensaje
                    Toast.makeText(this, "Registro exitoso", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    // Si el registro falla, muestra un mensaje de error
                    Toast.makeText(this, "Error al registrar: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }
}
