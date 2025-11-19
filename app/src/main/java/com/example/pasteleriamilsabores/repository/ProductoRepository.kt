package com.example.pasteleriamilsabores.repository

import android.content.Context
import com.example.pasteleriamilsabores.Model.Producto
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ProductoRepository() {

    // Función para obtener TODOS los productos desde 'productos.json'
    fun obtenerTodosLosProductos(context: Context, filename: String = "data/productos.json"): List<Producto> {
        return try {
            val json = context.assets.open(filename).bufferedReader().use { it.readText() }
            // TypeToken es necesario para deserializar List<T> con Gson
            val type = object : TypeToken<List<Producto>>() {}.type
            Gson().fromJson(json, type)
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    fun obtenerProductoPorId(context: Context, productoId: Int): Producto? {
        val allProductos = obtenerTodosLosProductos(context) // Reusa la función de cargar todos
        return allProductos.find { it.id == productoId }
    }
}