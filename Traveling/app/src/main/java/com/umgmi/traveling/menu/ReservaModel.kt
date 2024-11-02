package com.umgmi.traveling.menu

data class ReservaModel(
    val id: String = "",              // ID del documento en Firestore
    val nombre: String = "",          // Nombre del usuario que hizo la reserva
    val servicio: String = "",        // Tipo de servicio reservado
    val confirmado: Boolean = false   // Estado de confirmación de la reserva
)
