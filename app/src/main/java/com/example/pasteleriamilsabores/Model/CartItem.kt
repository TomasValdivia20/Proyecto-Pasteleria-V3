package com.example.pasteleriamilsabores.Model

import java.util.UUID

data class CartItem(
    // ID único para diferenciar instancias del mismo producto (e.g., dos Tortas Selva Negra de diferente tamaño)
    val id: String = UUID.randomUUID().toString(),
    val productoId: Int,
    val nombreProducto: String,
    val imagenProducto: String,
    val tamanioSeleccionado: Tamanio, // El objeto Tamanio con precio y nutrición
    val cantidad: Int = 1
) {
    // Getter para calcular el subtotal de este ítem
    val subtotal: Int
        get() = tamanioSeleccionado.precio * cantidad
}