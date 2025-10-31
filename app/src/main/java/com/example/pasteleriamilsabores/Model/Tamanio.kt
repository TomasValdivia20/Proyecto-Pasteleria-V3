package com.example.pasteleriamilsabores.Model


//Integramos Nutricion 
data class Tamanio(
    // Gson maneja esto bien. Si 'personas' está ausente, será null (Int?).
    // Si 'descripcion' está ausente, será null (String?).
    val personas: Int? = null,
    val descripcion: String? = null,
    val precio: Int,
    val nutricion: Nutricion
)