package com.example.pasteleriamilsabores.View

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.pasteleriamilsabores.ViewModel.BOViewModel
import com.example.pasteleriamilsabores.Model.Producto
import com.example.pasteleriamilsabores.Model.Categoria // Importar Categoria
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BOProductoScreen(viewModel: BOViewModel) {
    val productos by viewModel.productos.collectAsState()
    // Obtenemos las categorías para el ComboBox
    val categorias by viewModel.categorias.collectAsState()
    val formatter = remember { NumberFormat.getCurrencyInstance(Locale("es", "CL")) }

    var mostrarFormulario by remember { mutableStateOf(false) }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { mostrarFormulario = !mostrarFormulario }) {
                Icon(Icons.Default.Add, contentDescription = "Agregar Nuevo Producto")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            Text(
                "Gestión de Productos",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp)
            )

            // La LazyColumn ocupa el espacio restante
            LazyColumn(
                modifier = Modifier.weight(1f), // Ocupa el espacio disponible
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(productos, key = { it.id }) { producto ->
                    BOProductoItem(producto = producto, formatter = formatter)
                }
                // Espacio al final para el FAB
                item { Spacer(Modifier.height(80.dp)) }
            }

            // --- FORMULARIO PLACEHOLDER (Aparece abajo) ---
            if (mostrarFormulario) {
                Divider(Modifier.padding(vertical = 8.dp))
                BOAgregarProductoForm(
                    categorias = categorias, // Pasamos las categorías al formulario
                    onGuardar = {
                        viewModel.simularAgregarProducto() // Llama a la simulación
                        mostrarFormulario = false // Cierra el formulario
                    },
                    onCancelar = { mostrarFormulario = false } // Cierra el formulario
                )
            }
        }
    }
}

// Composable para el formulario (Placeholder)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BOAgregarProductoForm(
    categorias: List<Categoria>,
    onGuardar: () -> Unit,
    onCancelar: () -> Unit
) {
    // Estados para los campos del formulario (solo para UI, no funcionales)
    var nombre by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var imagenUrl by remember { mutableStateOf("") }
    var notas by remember { mutableStateOf("") }
    var categoriaSeleccionada by remember { mutableStateOf<Categoria?>(null) }
    var tamanosJson by remember { mutableStateOf("[]") } // Placeholder para JSON de tamaños

    var expandedCategoria by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()), // Scroll si el formulario es largo
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Agregar Nuevo Producto", style = MaterialTheme.typography.titleLarge)

        OutlinedTextField(value = nombre, onValueChange = { nombre = it }, label = { Text("Nombre del Producto") })
        OutlinedTextField(value = descripcion, onValueChange = { descripcion = it }, label = { Text("Descripción") })
        OutlinedTextField(value = imagenUrl, onValueChange = { imagenUrl = it }, label = { Text("URL Imagen (ej: /assets/img/nuevo.jpg)") })

        // --- ComboBox para Categoría ---
        ExposedDropdownMenuBox(
            expanded = expandedCategoria,
            onExpandedChange = { expandedCategoria = !expandedCategoria }
        ) {
            OutlinedTextField(
                value = categoriaSeleccionada?.nombre ?: "Seleccione Categoría",
                onValueChange = {}, // No editable directamente
                readOnly = true,
                label = { Text("Categoría") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedCategoria) },
                modifier = Modifier.menuAnchor().fillMaxWidth() // Necesario para Dropdown
            )
            ExposedDropdownMenu(
                expanded = expandedCategoria,
                onDismissRequest = { expandedCategoria = false }
            ) {
                categorias.forEach { categoria ->
                    DropdownMenuItem(
                        text = { Text(categoria.nombre) },
                        onClick = {
                            categoriaSeleccionada = categoria
                            expandedCategoria = false
                        }
                    )
                }
            }
        }
        // --- Fin ComboBox ---

        OutlinedTextField(
            value = tamanosJson,
            onValueChange = { tamanosJson = it },
            label = { Text("Tamaños (Formato JSON)") },
            modifier = Modifier.height(100.dp) // Más altura para el JSON
        )
        OutlinedTextField(value = notas, onValueChange = { notas = it }, label = { Text("Notas (Alergénos, etc.)") })

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(onClick = onGuardar, modifier = Modifier.weight(1f)) { Text("Guardar Producto") }
            OutlinedButton(onClick = onCancelar, modifier = Modifier.weight(1f)) { Text("Cancelar") }
        }
    }
}


// BOProductoItem (sin cambios)
@Composable
fun BOProductoItem(producto: Producto, formatter: NumberFormat) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f).padding(end = 8.dp)) {
                Text(producto.nombre, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Text(
                    "Categoría ID: ${producto.categoriaId}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            val precioMinimo = producto.tamaños.minOfOrNull { it.precio } ?: 0
            Text(
                formatter.format(precioMinimo),
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}