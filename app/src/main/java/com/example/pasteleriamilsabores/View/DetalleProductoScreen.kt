package com.example.pasteleriamilsabores.View

// Importaciones necesarias
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.pasteleriamilsabores.Model.Tamanio
import com.example.pasteleriamilsabores.ViewModel.DetalleProductoViewModel
import com.example.pasteleriamilsabores.ViewModel.DetalleProductoViewModelFactory
import java.text.NumberFormat
import java.util.Locale

//Para carrito
import com.example.pasteleriamilsabores.ViewModel.CartViewModel
import com.example.pasteleriamilsabores.Model.CartItem
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch
import androidx.compose.material3.SnackbarDuration

// -------------------------------------------------------------------
// PANTALLA PRINCIPAL
// -------------------------------------------------------------------

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetalleProductoScreen(
    productoId: Int, // ID del producto recibido de la navegación
    navController: NavController,
    // Obtén el CartViewModel (será el mismo en toda la app)
    cartViewModel: CartViewModel
) {
    val context = LocalContext.current

    // ESTADOS NECESARIOS PARA EL SNACKBAR (Notificacion de añadido de producto a carrito)
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope() // Scope para lanzar la coroutine que muestra el Snackbar

    // Obtener los viewmodels con la factory
    val viewModel: DetalleProductoViewModel = viewModel(
        factory = DetalleProductoViewModelFactory(context, productoId)
    )

    // Observar los estados (Punto 1)
    val producto by viewModel.producto.collectAsState()
    val tamanioSeleccionado by viewModel.tamanioSeleccionado.collectAsState()
    val formatter = remember { NumberFormat.getCurrencyInstance(Locale("es", "CL")) }

    Box(modifier = Modifier.fillMaxSize()) {
        // Fondo
        Image(
            painter = rememberAsyncImagePainter(model = "file:///android_asset/img/bg.jpg"),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.matchParentSize().alpha(0.3f)
        )

        Scaffold(
            snackbarHost = { SnackbarHost(snackbarHostState) }, //Para añadir el Host de Snackbar al Scaffold
            // ---------------------------------------------
            containerColor = Color.Transparent,
            topBar = {
                TopAppBar(
                    title = { Text(producto?.nombre ?: "Cargando...") },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Volver")
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.85f),
                        titleContentColor = MaterialTheme.colorScheme.onPrimary
                    )
                )
            }
        ) { padding ->
            // Contenedor principal con scroll
            Column(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()) // Habilita el scroll
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (producto == null) {
                    CircularProgressIndicator()
                    Spacer(Modifier.height(16.dp))
                    Text("Cargando detalles...")
                } else {
                    // --- CONTENIDO DEL DETALLE DEL PRODUCTO ---


                    // Imagen Principal
                    val assetPath = producto!!.imagen.replace("/assets/img/", "")
                    val imageUrl = "file:///android_asset/img/$assetPath"

                    Card(
                        modifier = Modifier.fillMaxWidth().height(250.dp),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Image(
                            painter = rememberAsyncImagePainter(model = imageUrl),
                            contentDescription = producto!!.nombre,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    }

                    Spacer(Modifier.height(16.dp))

                    // Nombre y Descripción
                    Text(producto!!.nombre, style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold))
                    Spacer(Modifier.height(8.dp))
                    Text(producto!!.descripcion, style = MaterialTheme.typography.bodyLarge)
                    Spacer(Modifier.height(24.dp))

                    // Selector de Tamaño y Precio
                    TamanioSelector(
                        producto = producto!!,
                        tamanioSeleccionado = tamanioSeleccionado,
                        onTamanioSeleccionado = viewModel::seleccionarTamanio,
                        formatter = formatter
                    )

                    Spacer(Modifier.height(16.dp))

                    // Valores Nutricionales
                    tamanioSeleccionado?.let {
                        NutricionPanel(it, producto!!.notas)
                    }

                    Spacer(Modifier.height(24.dp))

                    // Botón "Agregar al Carrito" (Estético)
                    Button(
                        onClick = {
                            val selectedTamanio = tamanioSeleccionado // El estado del ViewModel
                            val productDetails = producto             // El estado del ViewModel

                            if (selectedTamanio != null && productDetails != null) {
                                val item = CartItem(
                                    productoId = productDetails.id,
                                    nombreProducto = productDetails.nombre,
                                    imagenProducto = productDetails.imagen,
                                    tamanioSeleccionado = selectedTamanio,
                                    cantidad = 1 // Por ahora, añadimos 1 unidad
                                )
                                cartViewModel.addItem(item)

                                scope.launch {
                                    snackbarHostState.showSnackbar(
                                        message = "${productDetails.nombre} (${selectedTamanio.personas?.let { "$it pers." } ?: selectedTamanio.descripcion}) añadido al carrito.",
                                        duration = SnackbarDuration.Short
                                    )
                                }
                                // ------------------------------------
                            }
                        },
                        enabled = tamanioSeleccionado != null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                    ) {
                        Text("Agregar al Carrito", style = MaterialTheme.typography.titleMedium)
                    }
                }
            }
        }
    }
}


// -------------------------------------------------------------------
// COMPONENTES AUXILIARES
// -------------------------------------------------------------------

@Composable
fun TamanioSelector(
    producto: com.example.pasteleriamilsabores.Model.Producto,
    tamanioSeleccionado: Tamanio?,
    onTamanioSeleccionado: (Tamanio) -> Unit,
    formatter: NumberFormat
) {
    val tieneMultiplesTamanos = producto.tamaños.size > 1

    var expanded by remember { mutableStateOf(false) }

    fun formatTamanio(t: Tamanio): String {
        val sizeText = t.personas?.let { "$it personas" } ?: t.descripcion ?: "Tamaño único"
        return "$sizeText - ${formatter.format(t.precio)}"
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        Text("Tamaño y Precio:", style = MaterialTheme.typography.titleLarge)
        Spacer(Modifier.height(8.dp))

        if (tieneMultiplesTamanos) {
            // Caso 1: Múltiples Tamaños (ComboBox)
            Box(modifier = Modifier.fillMaxWidth().wrapContentSize(Alignment.TopStart)) {
                OutlinedTextField(
                    value = tamanioSeleccionado?.let { formatTamanio(it) } ?: "Seleccione un tamaño",
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Seleccionar Tamaño") },
                    trailingIcon = {
                        Icon(
                            Icons.Filled.ArrowDropDown,
                            "dropdown",
                            Modifier.clickable { expanded = true }
                        )
                    },
                    modifier = Modifier.fillMaxWidth()
                )
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    producto.tamaños.forEach { tamanio ->
                        DropdownMenuItem(
                            text = { Text(formatTamanio(tamanio)) },
                            onClick = {
                                onTamanioSeleccionado(tamanio)
                                expanded = false
                            }
                        )
                    }
                }
            }
        } else {
            // Caso 2: Tamaño Único
            val tamanio = producto.tamaños.firstOrNull()
            if (tamanio != null) {
                // Preselección manual para asegurar el estado si el ViewModel no lo hizo
                LaunchedEffect(Unit) { onTamanioSeleccionado(tamanio) }

                Text(
                    text = formatTamanio(tamanio),
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.primary
                )
            } else {
                Text("Precio no disponible", style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}

@Composable
fun NutricionPanel(tamanio: Tamanio, notas: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Información Nutricional (${tamanio.nutricion.peso})", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
            Divider(Modifier.padding(vertical = 8.dp))

            val nutricion = tamanio.nutricion

            @Composable
            fun NutriRow(label: String, value: String) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(label, style = MaterialTheme.typography.bodyMedium)
                    Text(value, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold))
                }
            }

            NutriRow("Energía Total:", nutricion.energia)
            NutriRow("Proteínas:", nutricion.proteinas)
            NutriRow("Grasas:", nutricion.grasas)
            NutriRow("Carbohidratos:", nutricion.carbohidratos)
            NutriRow("Azúcares:", nutricion.azucares)
            NutriRow("Sodio:", nutricion.sodio)

            Spacer(Modifier.height(16.dp))

            // Notas
            Text(notas, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}