package com.example.pasteleriamilsabores.View

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.pasteleriamilsabores.Destinos
import com.example.pasteleriamilsabores.ViewModel.BOViewModel
import com.example.pasteleriamilsabores.ViewModel.BOViewModelFactory
import kotlinx.coroutines.launch

// Clase de datos (sin cambios)
data class DrawerItem(val route: String, val icon: androidx.compose.ui.graphics.vector.ImageVector, val title: String)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BOBackofficeNavGraph(
    navController: NavController
) {
    val context = LocalContext.current
    val boViewModel: BOViewModel = viewModel(factory = BOViewModelFactory(context))
    val scope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val currentScreenRoute by boViewModel.currentScreen.collectAsState()

    // 🛑 1. DEFINIR drawerItems UNA SOLA VEZ AQUÍ
    val drawerItems = remember { // Usamos remember para que no se recree innecesariamente
        listOf(
            DrawerItem(Destinos.BODASHBOARD, Icons.Default.Dashboard, "Dashboard"),
            DrawerItem(Destinos.BOORDENES, Icons.Default.ReceiptLong, "Órdenes"),
            DrawerItem(Destinos.BOPRODUCTO, Icons.Default.Cake, "Productos"),
            DrawerItem(Destinos.BOCATEGORIA, Icons.Default.Category, "Categorías"),
            DrawerItem(Destinos.BOUSUARIO, Icons.Default.People, "Usuarios"),
            DrawerItem(Destinos.BOREPORTES, Icons.Default.BarChart, "Reportes"),
            DrawerItem(Destinos.BOPERFIL, Icons.Default.Person, "Perfil")
        )
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                // ... (Contenido del DrawerSheet usando drawerItems - sin cambios)
                Spacer(Modifier.height(12.dp))
                Text("Menú Backoffice", style = MaterialTheme.typography.titleLarge, modifier = Modifier.padding(16.dp))
                Divider()
                drawerItems.forEach { item ->
                    NavigationDrawerItem(
                        icon = { Icon(item.icon, contentDescription = null) },
                        label = { Text(item.title) },
                        selected = currentScreenRoute == item.route,
                        onClick = {
                            scope.launch { drawerState.close() }
                            boViewModel.navigateTo(item.route)
                        },
                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                    )
                }
                Spacer(Modifier.weight(1f))
                NavigationDrawerItem( // Botón Cerrar Sesión
                    icon = { Icon(Icons.Default.ExitToApp, contentDescription = null) },
                    label = { Text("Cerrar Sesión") },
                    selected = false,
                    onClick = {
                        navController.navigate(Destinos.LOGIN_SCREEN) {
                            popUpTo(Destinos.BACKOFFICE_BASE) { inclusive = true }
                        }
                    },
                    modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                )
            }
        },
        content = {
            // 🛑 2. PASAR drawerItems A BODashboardContainer
            BODashboardContainer(
                currentScreenRoute = currentScreenRoute,
                onMenuClick = { scope.launch { drawerState.open() } },
                boViewModel = boViewModel,
                drawerItems = drawerItems // <-- Pasamos la lista
            )
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BODashboardContainer(
    currentScreenRoute: String,
    onMenuClick: () -> Unit,
    boViewModel: BOViewModel,
    drawerItems: List<DrawerItem> // <-- Recibe la lista
) {
    // Obtener el título usando la lista recibida
    val title = remember(currentScreenRoute, drawerItems) {
        drawerItems.find { it.route == currentScreenRoute }?.title ?: "Backoffice"
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(title) },
                navigationIcon = {
                    IconButton(onClick = onMenuClick) {
                        Icon(Icons.Default.Menu, contentDescription = "Abrir Menú")
                    }
                }
            )
        }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding)) {
            // El 'when' block sigue igual, llamando a las pantallas correctas
            when (currentScreenRoute) {
                Destinos.BODASHBOARD -> BODashboardScreen(boViewModel)
                Destinos.BOORDENES -> BOOrdenesScreen(boViewModel)
                Destinos.BOPRODUCTO -> BOProductoScreen(boViewModel)
                Destinos.BOCATEGORIA -> BOCategoriaScreen(boViewModel)
                Destinos.BOUSUARIO -> BOUsuarioScreen(boViewModel)
                Destinos.BOREPORTES -> BOReportesScreen(boViewModel)
                Destinos.BOPERFIL -> BOPerfilScreen(boViewModel)
                else -> BODashboardScreen(boViewModel)
            }
        }
    }
}
