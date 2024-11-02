package com.umgmi.traveling.menu

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ServicioModel(
    val nombre: String = "",
    val tipo: String = "",
    val lugar: String = "",
    val pago: String = "",
    val monto: String = "",
    val imagenUrl: String = "",
    val creadorEmail: String? = "",  // Correo del creador (opcional)
    var reservadorCorreo: String = "" // Correo de quien reserva (inicialmente vacío)
) : Parcelable {
    // Constructor secundario
    constructor() : this("", "", "", "", "", "", "", "")
}
