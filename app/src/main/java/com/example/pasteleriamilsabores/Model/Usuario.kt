package com.example.pasteleriamilsabores.Model

data class Usuario(
    val nombre: String,
    val apellido: String,
    val rut: String,
    val region: String,
    val direccion: String,
    val email: String,
    val password: String
)