// View/CompraFinalizadaScreen.kt
package com.example.pasteleriamilsabores.View

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.pasteleriamilsabores.Destinos
import com.example.pasteleriamilsabores.Model.CartItem
import com.example.pasteleriamilsabores.ViewModel.CartViewModel
import com.example.pasteleriamilsabores.ui.theme.Rosa
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CompraFinalizadaScreen(
    navController: NavController,
    cartViewModel: CartViewModel
) {
    // 1. Obtener datos del carrito (snapshots)
    val itemsSnapshot = remember { cartViewModel.items.value }
    val subtotalSnapshot = remember { cartViewModel.subtotalTotal.value }
    val descuentoSnapshot = remember { cartViewModel.descuentoTotal.value }
    val totalSnapshot = remember { cartViewModel.totalPagar.value }
    val codigoAplicadoSnapshot = remember { cartViewModel.descuentoCodigo.value }

    val formatter = remember { NumberFormat.getCurrencyInstance(Locale("es", "CL")) }

    // 2. Limpiar el carrito inmediatamente (simulando que la compra ya se procesó)
    // Esto se ejecuta SÓLO una vez al entrar en la pantalla.
    LaunchedEffect(Unit) {
        cartViewModel.limpiarCarrito()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Confirmación de Compra") }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // --- MENSAJE DE ÉXITO ---
            Text(
                "¡Compra Exitosa!",
                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.ExtraBold),
                color = Rosa
            )
            Spacer(Modifier.height(8.dp))
            Text(
                "¡Gracias por preferirnos! Se le enviará a su correo la boleta de pago, ¡esperemos que disfrute su pedido!",
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(24.dp))

            Divider()
            Spacer(Modifier.height(16.dp))

            // --- RESUMEN DE COMPRA ---
            Text("Resumen del Pedido:", style = MaterialTheme.typography.titleLarge)
            Spacer(Modifier.height(8.dp))

            // Lista de Items comprados (usando el snapshot)
            itemsSnapshot.forEach { item ->
                ItemCompradoRow(item = item, formatter = formatter)
            }

            Spacer(Modifier.height(16.dp))
            Divider()
            Spacer(Modifier.height(8.dp))

            // Totales
            ResumenFinalRow("Subtotal:", subtotalSnapshot, formatter)
            if (descuentoSnapshot > 0) {
                ResumenFinalRow("Descuento (${codigoAplicadoSnapshot ?: ""}):", -descuentoSnapshot, formatter, color = MaterialTheme.colorScheme.error)
            }
            ResumenFinalRow("Total Pagado:", totalSnapshot, formatter, style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.ExtraBold))

            Spacer(Modifier.height(32.dp))

            // --- BOTÓN PARA VOLVER A HOME ---
            Button(
                onClick = {
                    navController.popBackStack(
                        route = Destinos.HOME_SCREEN,
                        inclusive = false // No cerramos la pantalla Home en sí
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Icon(Icons.Default.Home, contentDescription = null, modifier = Modifier.size(24.dp))
                Spacer(Modifier.width(8.dp))
                Text("Volver al Catálogo Principal", style = MaterialTheme.typography.titleMedium)
            }
        }
    }
}

// Componente para mostrar un solo ítem en la factura final
@Composable
fun ItemCompradoRow(item: CartItem, formatter: NumberFormat) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "${item.nombreProducto} (x${item.cantidad})",
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = item.tamanioSeleccionado.personas?.let { "$it personas" } ?: item.tamanioSeleccionado.descripcion ?: "Tamaño",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Text(
            text = formatter.format(item.subtotal),
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.SemiBold
        )
    }
}

// Componente helper para filas de totales
@Composable
fun ResumenFinalRow(label: String, amount: Int, formatter: NumberFormat, color: Color = MaterialTheme.colorScheme.onSurface, style: androidx.compose.ui.text.TextStyle = MaterialTheme.typography.titleMedium) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, style = style, color = color)
        Text(formatter.format(amount), style = style, color = color)
    }
}