package com.example.pasteleriamilsabores.Model

//Llamamos la clase tamanio
data class Producto(
    val id: Int,
    val nombre: String,
    val descripcion: String,
    val imagen: String,
    val categoriaId: Int, // CLAVE: Usaremos este ID para filtrar
    val tamaños: List<Tamanio>,
    val notas: String
)