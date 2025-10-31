package com.example.pasteleriamilsabores.View

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.pasteleriamilsabores.Model.Venta
import com.example.pasteleriamilsabores.ViewModel.BOViewModel // <-- Usamos BOViewModel
import java.text.NumberFormat
import java.util.Locale

import com.example.pasteleriamilsabores.View.DashboardCard
import com.example.pasteleriamilsabores.View.PlaceholderGrafico
import com.example.pasteleriamilsabores.View.VentaRow

@Composable
fun BODashboardScreen(viewModel: BOViewModel) {
    val ventas by viewModel.ventas.collectAsState()
    val formatter = NumberFormat.getCurrencyInstance(Locale("es", "CL"))

    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // --- 1. RESUMEN DE VENTAS (Gráficos Ficticios) ---
        item {
            Text("Resumen de Ventas", style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold))
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Ventas 15 Días
                DashboardCard(
                    title = "Ventas en los últimos 15 días",
                    content = { PlaceholderGrafico(label = "Gráfico de Líneas Ficticio") },
                    modifier = Modifier.weight(1f)
                )

                // Ventas Semestre
                DashboardCard(
                    title = "Ventas primer semestre",
                    content = { PlaceholderGrafico(label = "Gráfico de Barras Ficticio") },
                    modifier = Modifier.weight(1f)
                )
            }
        }

        item {
            Spacer(Modifier.height(8.dp))
            Text("Últimas Ventas Registradas", style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold))
            Spacer(Modifier.height(8.dp))
        }

        // --- 2. TABLA DE VENTAS (Lista) ---
        // CORRECCIÓN: Concatenar monto y fecha como String para crear una clave única
        items(ventas, key = { "${it.montoTotal}-${it.fechaCompra}" }) { venta ->
            VentaRow(venta = venta, formatter = formatter)
        }
    }
}







