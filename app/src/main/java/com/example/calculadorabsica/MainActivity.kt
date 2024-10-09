package com.example.calculadorabsica

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Habilitar el modo Edge-to-edge para aprovechar toda la pantalla.
        enableEdgeToEdge()

        // Establecer el layout de la actividad.
        setContentView(R.layout.activity_main)

        // Obtener referencias a los elementos de la vista
        val num1 = findViewById<EditText>(R.id.num1)
        val num2 = findViewById<EditText>(R.id.num2)
        val btnSumar = findViewById<Button>(R.id.btn_sumar)
        val btnRestar = findViewById<Button>(R.id.btn_restar)
        val txtResultado = findViewById<TextView>(R.id.txt_resultado)

        // Lógica para la suma
        btnSumar.setOnClickListener {
            val numero1 = num1.text.toString().toDoubleOrNull()
            val numero2 = num2.text.toString().toDoubleOrNull()

            if (numero1 != null && numero2 != null) {
                val resultado = numero1 + numero2
                txtResultado.text = "Resultado: $resultado"
            } else {
                txtResultado.text = "Introduce números válidos"
            }
        }

        // Lógica para la resta
        btnRestar.setOnClickListener {
            val numero1 = num1.text.toString().toDoubleOrNull()
            val numero2 = num2.text.toString().toDoubleOrNull()

            if (numero1 != null && numero2 != null) {
                val resultado = numero1 - numero2
                txtResultado.text = "Resultado: $resultado"
            } else {
                txtResultado.text = "Introduce números válidos"
            }
        }

        // Ajustar padding para las barras del sistema (como la barra de estado o de navegación)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}
