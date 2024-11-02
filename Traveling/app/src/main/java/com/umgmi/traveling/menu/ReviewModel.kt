package com.umgmi.traveling.menu

data class ReviewModel(
    val id: String = "",            // ID del documento en Firestore
    val nombre: String = "",        // Nombre del usuario que deja la reseña
    val correo: String = "",        // Correo del usuario que deja la reseña
    val calificacion: Int = 0,      // Calificación (1 a 5)
    val reseña: String = "",        // Texto de la reseña
    val fecha: String = ""          // Fecha en que se dejó la reseña (puede ser en formato String)
)
