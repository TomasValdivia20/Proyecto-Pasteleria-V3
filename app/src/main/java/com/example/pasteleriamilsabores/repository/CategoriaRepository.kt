package com.example.pasteleriamilsabores.repository

import android.content.Context
import com.example.pasteleriamilsabores.Model.Categoria
import com.google.gson.Gson // Necesitarás la librería Gson para parsear JSON
import com.google.gson.reflect.TypeToken


class CategoriaRepository() {

    fun obtenerCategoriasDesdeAssets(context: Context, filename: String = "data/categorias.json"): List<Categoria> {
        return try {
            val json = context.assets.open(filename).bufferedReader().use { it.readText() }
            val type = object : TypeToken<List<Categoria>>() {}.type
            Gson().fromJson(json, type)
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }
}