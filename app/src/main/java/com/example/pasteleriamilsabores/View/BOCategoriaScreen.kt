package com.example.pasteleriamilsabores.View

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.pasteleriamilsabores.ViewModel.BOViewModel
import com.example.pasteleriamilsabores.Model.Categoria // Importar Categoria

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BOCategoriaScreen(viewModel: BOViewModel) {
    val categorias by viewModel.categorias.collectAsState()
    var mostrarFormulario by remember { mutableStateOf(false) }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { mostrarFormulario = !mostrarFormulario }) {
                Icon(Icons.Default.Add, contentDescription = "Agregar Nueva Categoría")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            Text(
                "Gestión de Categorías",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp)
            )

            LazyColumn(
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(categorias, key = { it.id }) { categoria ->
                    BOCategoriaItem(categoria = categoria)
                }
                item { Spacer(Modifier.height(80.dp)) } // Espacio para FAB
            }

            if (mostrarFormulario) {
                Divider(Modifier.padding(vertical = 8.dp))
                BOAgregarCategoriaForm(
                    onGuardar = {
                        viewModel.simularAgregarCategoria()
                        mostrarFormulario = false
                    },
                    onCancelar = { mostrarFormulario = false }
                )
            }
        }
    }
}

// Formulario Placeholder para Categorías
@Composable
fun BOAgregarCategoriaForm(
    onGuardar: () -> Unit,
    onCancelar: () -> Unit
) {
    var nombre by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var imagenUrl by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Agregar Nueva Categoría", style = MaterialTheme.typography.titleLarge)

        OutlinedTextField(value = nombre, onValueChange = { nombre = it }, label = { Text("Nombre Categoría") })
        OutlinedTextField(value = descripcion, onValueChange = { descripcion = it }, label = { Text("Descripción") })
        OutlinedTextField(value = imagenUrl, onValueChange = { imagenUrl = it }, label = { Text("Nombre Imagen (ej: nueva_cat.jpg)") })

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(onClick = onGuardar, modifier = Modifier.weight(1f)) { Text("Guardar Categoría") }
            OutlinedButton(onClick = onCancelar, modifier = Modifier.weight(1f)) { Text("Cancelar") }
        }
    }
}

// Item para mostrar una categoría en la lista
@Composable
fun BOCategoriaItem(categoria: Categoria) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(categoria.nombre, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            categoria.descripcion?.let {
                Text(
                    it,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2,
                    overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                )
            }
            Text(
                "ID: ${categoria.id}, Imagen: ${categoria.imagen}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.outline
            )
        }
    }
}

