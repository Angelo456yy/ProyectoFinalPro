package com.umgmi.traveling.menu

import java.io.Serializable

data class ServicioModel(
    val nombre: String,
    val tipo: String,
    val lugar: String,
    val pago: String,
    val monto: String,
    val imagenUrl: String
) : Serializable {
    constructor() : this("", "", "", "", "", "")
}
