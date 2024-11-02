package com.umgmi.traveling.menu
import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.io.Serializable
@Parcelize

data class ServicioModel(
    val nombre: String = "",
    val tipo: String = "",
    val lugar: String = "",
    val pago: String = "",
    val monto: String = "",
    val imagenUrl: String = "",
    val creadorCorreo: String = "",  // Correo del creador
    var reservadorCorreo: String = "" // Correo de quien reserva (inicialmente vacío)


) : Parcelable {
    constructor() : this("", "", "", "", "", "","","")
}
