package com.example.pasteleriamilsabores.View

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.pasteleriamilsabores.Model.Venta // <-- Necesario si mueves VentaRow
import java.text.NumberFormat
import java.util.Locale


// Componente Tarjeta para los gráficos
@Composable
fun DashboardCard(title: String, content: @Composable () -> Unit, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.heightIn(min = 200.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(title, style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold))
            Spacer(Modifier.height(12.dp))
            content()
        }
    }
}

// Placeholder para los gráficos
@Composable
fun PlaceholderGrafico(label: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .padding(8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text("[Placeholder: $label]", color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

// Fila para mostrar una venta en la tabla (si se va a reutilizar)
@Composable
fun VentaRow(venta: Venta, formatter: NumberFormat) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerHigh)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("${venta.nombreCliente} ${venta.apellidoCliente}", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Text(venta.region, style = MaterialTheme.typography.bodyMedium)
            }
            Text(venta.direccion, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(Modifier.height(8.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(venta.fechaCompra, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Text(
                    formatter.format(venta.montoTotal),
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}