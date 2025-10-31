package com.example.pasteleriamilsabores.ViewModel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.pasteleriamilsabores.Model.Producto
import com.example.pasteleriamilsabores.Model.Tamanio
import com.example.pasteleriamilsabores.repository.ProductoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

// 1. EL VIEWMODEL
class DetalleProductoViewModel (
    private val context: Context,
    private val productoId: Int,
    private val repo: ProductoRepository = ProductoRepository()
) : ViewModel() {

    // Estado para el producto completo (nullable mientras carga)
    private val _producto = MutableStateFlow<Producto?>(null)
    val producto: StateFlow<Producto?> = _producto

    // Estado para el tamaño actualmente seleccionado
    private val _tamanioSeleccionado = MutableStateFlow<Tamanio?>(null)
    val tamanioSeleccionado: StateFlow<Tamanio?> = _tamanioSeleccionado

    init {
        loadProducto()
    }

    private fun loadProducto() {
        viewModelScope.launch {
            val loadedProducto = repo.obtenerProductoPorId(context, productoId)
            _producto.value = loadedProducto

            // Si el producto se carga, preselecciona el primer tamaño, especialmente si solo hay uno.
            if (loadedProducto != null && loadedProducto.tamaños.isNotEmpty()) {
                _tamanioSeleccionado.value = loadedProducto.tamaños.first()
            }
        }
    }

    // Función que la UI llamará al seleccionar un nuevo tamaño en el ComboBox
    fun seleccionarTamanio(tamanio: Tamanio) {
        _tamanioSeleccionado.value = tamanio
    }
}

// 2. LA FACTORY (Necesaria para pasar el ID del producto)
class DetalleProductoViewModelFactory(
    private val context: Context,
    private val productoId: Int
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DetalleProductoViewModel::class.java)) {
            val repository = ProductoRepository()
            return DetalleProductoViewModel(context, productoId, repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}