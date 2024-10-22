package com.umgmi.traveling

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class Registro : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference // Instancia de Realtime Database

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro)

        // Inicializa FirebaseAuth y FirebaseDatabase
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference // Inicializa la referencia a la base de datos

        // Obtén referencias de los elementos del layout
        val editTextNombre: EditText = findViewById(R.id.editTextNombre)
        val editTextCorreo: EditText = findViewById(R.id.editTextCorreo)
        val editTextContraseña: EditText = findViewById(R.id.editTextContraseña)
        val buttonRegistrar: Button = findViewById(R.id.buttonRegistrar)
        val textViewCuentaExistente: TextView = findViewById(R.id.textViewCrearCuenta)

        // Maneja el clic para ir a la pantalla de inicio de sesión
        textViewCuentaExistente.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java) // Cambia a la actividad Main
            startActivity(intent)
            finish()
        }

        // Lógica para el botón de registro
        buttonRegistrar.setOnClickListener {
            val correo = editTextCorreo.text.toString().trim()
            val contraseña = editTextContraseña.text.toString().trim()

            // Validación de campos vacíos
            if (correo.isEmpty() || contraseña.isEmpty()) {
                Toast.makeText(this, "Por favor completa todos los campos", Toast.LENGTH_SHORT).show()
            } else {
                registrarUsuario(correo, contraseña, editTextNombre.text.toString().trim())
            }
        }
    }

    // Función para registrar un usuario en Firebase
    private fun registrarUsuario(correo: String, contraseña: String, nombre: String) {
        auth.createUserWithEmailAndPassword(correo, contraseña)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Registro exitoso, guarda datos en Realtime Database
                    val userId = auth.currentUser?.uid
                    val userData = hashMapOf(
                        "email" to correo,
                        "Nombre" to nombre,
                        "contra" to contraseña
                    )

                    userId?.let {
                        database.child("users").child(it).setValue(userData)
                            .addOnSuccessListener {
                                Toast.makeText(this, "Registro exitoso y datos guardados", Toast.LENGTH_SHORT).show()
                                val intent = Intent(this, Menu_Principal::class.java)
                                startActivity(intent)
                                finish()
                            }
                            .addOnFailureListener { e ->
                                Toast.makeText(this, "Error al guardar datos: ${e.message}", Toast.LENGTH_SHORT).show()
                            }
                    } ?: run {
                        Toast.makeText(this, "Error: ID de usuario no encontrado", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    // Si el registro falla, muestra un mensaje de error
                    Toast.makeText(this, "Error al registrar: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }
}
