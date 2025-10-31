package com.example.pasteleriamilsabores.ViewModel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.pasteleriamilsabores.Destinos
import com.example.pasteleriamilsabores.Model.* // Importar todos los modelos
import com.example.pasteleriamilsabores.repository.ProductoRepository
import com.example.pasteleriamilsabores.repository.CategoriaRepository // <-- Importar Repo Categoría
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class BOViewModel(
    private val context: Context,
    private val productoRepo: ProductoRepository = ProductoRepository(),
    private val categoriaRepo: CategoriaRepository = CategoriaRepository()
) : ViewModel() {

    // --- ESTADOS ---
    private val _ventas = MutableStateFlow<List<Venta>>(FakeBackofficeData.ventasRecientes)
    val ventas: StateFlow<List<Venta>> = _ventas
    val ventas15Dias = FakeBackofficeData.ventas15Dias
    val ventasSemestre = FakeBackofficeData.ventasSemestre
    private val _usuario = MutableStateFlow(FakeBackofficeData.usuarioActual)
    val usuario: StateFlow<UsuarioBackoffice> = _usuario
    private val _currentScreen = MutableStateFlow(Destinos.BODASHBOARD) // Estado inicial
    val currentScreen: StateFlow<String> = _currentScreen
    private val _productos = MutableStateFlow<List<Producto>>(emptyList())
    val productos: StateFlow<List<Producto>> = _productos
    private val _usuarios = MutableStateFlow<List<UsuarioBackoffice>>(FakeBackofficeData.usuariosFicticios)
    val usuarios: StateFlow<List<UsuarioBackoffice>> = _usuarios
    private val _categorias = MutableStateFlow<List<Categoria>>(emptyList())
    val categorias: StateFlow<List<Categoria>> = _categorias

    init {
        cargarProductos()
        cargarCategorias()
    }

    private fun cargarProductos() {
        viewModelScope.launch {
            _productos.value = productoRepo.obtenerTodosLosProductos(context)
        }
    }

    private fun cargarCategorias() {
        viewModelScope.launch {
            _categorias.value = categoriaRepo.obtenerCategoriasDesdeAssets(context)
        }
    }

    // --- NAVEGACIÓN INTERNA DEL BACKOFFICE ---
    fun navigateTo(route: String) {
        _currentScreen.value = route
    }

    // --- SIMULACIONES ---
    fun simularAgregarProducto() {
        println("SIMULACIÓN: Agregando un nuevo producto.")
    }

    fun simularActualizarPerfil(nuevoNombre: String, nuevoCorreo: String) {
        println("SIMULACIÓN: Perfil actualizado.")
        _usuario.value = _usuario.value.copy(nombre = nuevoNombre, correo = nuevoCorreo)
    }

    fun simularAgregarCategoria() {
        println("SIMULACIÓN: Agregando una nueva categoría.")
    }
}

// Factory (CORREGIDA)
class BOViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        if (modelClass.isAssignableFrom(BOViewModel::class.java)) {
            // Pasamos ambos repositorios
            return BOViewModel(context.applicationContext, ProductoRepository(), CategoriaRepository()) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class for BOViewModel")
    }
}
