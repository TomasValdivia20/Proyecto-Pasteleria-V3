package com.example.pasteleriamilsabores.View

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.pasteleriamilsabores.Model.CartItem
import com.example.pasteleriamilsabores.ViewModel.CartViewModel
import java.text.NumberFormat
import java.util.Locale
import com.example.pasteleriamilsabores.Destinos


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
    navController: NavController,
    cartViewModel: CartViewModel = viewModel()
) {
    val items by cartViewModel.items.collectAsState()
    val subtotal by cartViewModel.subtotalTotal.collectAsState()
    val descuento by cartViewModel.descuentoTotal.collectAsState()
    val total by cartViewModel.totalPagar.collectAsState()
    val codigoAplicado by cartViewModel.descuentoCodigo.collectAsState()

    val formatter = remember { NumberFormat.getCurrencyInstance(Locale("es", "CL")) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mi Carrito de Compras") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {

            if (items.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Tu carrito está vacío. ¡Añade algo delicioso!", style = MaterialTheme.typography.titleMedium)
                }
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(items, key = { it.id }) { item ->
                        CartItemRow(item = item, formatter = formatter, onRemove = { cartViewModel.removeItem(item.id) })
                    }

                    item { Divider() }

                    // Sección de Resumen y Descuento
                    item {
                        CompraResumen(
                            subtotal = subtotal,
                            descuento = descuento,
                            total = total,
                            codigoAplicado = codigoAplicado,
                            formatter = formatter,
                            onValidarCodigo = cartViewModel::validarCodigo
                        )
                    }
                }

                // Botones de Checkout al final de la pantalla
                CheckoutButtons(
                    onCompraExitosa = {
                        navController.navigate(Destinos.COMPRA_FINALIZADA_SCREEN) {
                            // Opcional: Elimina el CartScreen de la pila para que 'Volver' no lo muestre
                            popUpTo(Destinos.CART_SCREEN) { inclusive = true }
                        }
                    },
                    onCompraError = {
                        navController.navigate(Destinos.COMPRA_RECHAZADA_SCREEN) {
                            popUpTo(Destinos.CART_SCREEN) { inclusive = true }
                        }
                    }
                )
            }
        }
    }
}

// --- COMPONENTES AUXILIARES DEL CARRO ---

@Composable
fun CartItemRow(item: CartItem, formatter: NumberFormat, onRemove: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Producto e Info
            Column(modifier = Modifier.weight(1f)) {
                Text(item.nombreProducto, style = MaterialTheme.typography.titleMedium)
                Text(
                    item.tamanioSeleccionado.personas?.let { "$it personas" } ?: item.tamanioSeleccionado.descripcion ?: "Único",
                    style = MaterialTheme.typography.bodySmall
                )
                Spacer(Modifier.height(4.dp))
                Text(formatter.format(item.subtotal), style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold))
            }

            // Botón de eliminar
            IconButton(onClick = onRemove) {
                Icon(Icons.Default.Delete, contentDescription = "Eliminar ítem")
            }
        }
    }
}

@Composable
fun CompraResumen(
    subtotal: Int,
    descuento: Int,
    total: Int,
    codigoAplicado: String?,
    formatter: NumberFormat,
    onValidarCodigo: (String) -> String
) {
    var codigoText by remember { mutableStateOf("") }
    var mensajeValidacion by remember { mutableStateOf<String?>(null) }

    // Función para mostrar la fila de resumen
    @Composable
    fun ResumenRow(label: String, amount: Int, color: Color = MaterialTheme.colorScheme.onSurface) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(label, style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold))
            Text(formatter.format(amount), style = MaterialTheme.typography.bodyLarge, color = color)
        }
    }

    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {

        // 1. Descuento
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            OutlinedTextField(
                value = codigoText,
                onValueChange = { codigoText = it },
                label = { Text("Código de Descuento") },
                placeholder = { Text(codigoAplicado ?: "Ingresa código...") },
                modifier = Modifier.weight(1f)
            )
            Spacer(Modifier.width(8.dp))
            Button(
                onClick = { mensajeValidacion = onValidarCodigo(codigoText) },
                enabled = codigoText.isNotBlank()
            ) {
                Text("Aplicar")
            }
        }

        mensajeValidacion?.let {
            Text(it, style = MaterialTheme.typography.bodySmall, color = if (codigoAplicado != null) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error)
        }

        Spacer(Modifier.height(16.dp))

        // 2. Resumen de Totales
        ResumenRow("Subtotal", subtotal)

        if (descuento > 0) {
            ResumenRow("Descuento (10%)", -descuento, color = MaterialTheme.colorScheme.error)
        }

        Divider(Modifier.padding(vertical = 8.dp))

        // Total Final
        ResumenRow("Total a Pagar", total, color = MaterialTheme.colorScheme.primary)
    }
}

@Composable
fun CheckoutButtons(onCompraExitosa: () -> Unit, onCompraError: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        // Botón 1: Compra Exitosa
        Button(
            onClick = onCompraExitosa,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text("Finalizar Compra", style = MaterialTheme.typography.titleMedium)
        }

        Spacer(Modifier.height(8.dp))

        // Botón 2: Error de Compra (Simulación futura)
        OutlinedButton(
            onClick = onCompraError,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text("Simular Error de Pago", style = MaterialTheme.typography.titleSmall)
        }
    }
}