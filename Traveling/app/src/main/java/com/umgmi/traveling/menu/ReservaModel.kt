package com.umgmi.traveling.menu

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ReservaModel(
    val id: String = "",
    val nombre: String = "",
    val tipo: String = "",
    val lugar: String = "",
    val pago: String = "",
    val monto: String = "",
    val calificacion: Int = 0,
    val reseña: String = "",
    val estado: String = "pendiente",
    val creadorEmail: String = "",  // Correo del creador
    val reservadorCorreo: String = ""  // Correo del reservador
) : Parcelable
