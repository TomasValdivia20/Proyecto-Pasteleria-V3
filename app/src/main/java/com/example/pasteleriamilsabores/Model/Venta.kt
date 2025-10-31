package com.example.pasteleriamilsabores.Model

data class Venta(
    val nombreCliente: String,
    val apellidoCliente: String,
    val direccion: String,
    val region: String,
    val fechaCompra: String,
    val montoTotal: Int
)

