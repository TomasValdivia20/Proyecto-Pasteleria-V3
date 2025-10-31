package com.example.pasteleriamilsabores.Model

//Definimos Nutricion en otro data class para dsp integrarlo a tamanio
data class Nutricion(
    val peso: String,
    val energia: String,
    val porcion: String,
    val proteinas: String,
    val grasas: String,
    val carbohidratos: String,
    val azucares: String,
    val sodio: String
)