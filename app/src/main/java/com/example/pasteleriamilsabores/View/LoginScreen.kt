package com.example.pasteleriamilsabores.View

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.pasteleriamilsabores.ViewModel.AuthViewModel
//Import color de fondo al Register y icono
import com.example.pasteleriamilsabores.ui.theme.PastelCalido
import com.example.pasteleriamilsabores.R
import com.example.pasteleriamilsabores.Utils.*
import androidx.compose.material3.LocalContentColor // Para el color del icono
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp

// Import color de fondo al Register y icono
import androidx.compose.foundation.Image
import androidx.compose.material3.OutlinedButton

import com.example.pasteleriamilsabores.Destinos



@OptIn(ExperimentalMaterial3Api::class) // Necesario para OutlinedTextField, etc. si usas M3
@Composable
fun LoginScreen(navController: NavController, viewModel: AuthViewModel) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(PastelCalido)
            .padding(20.dp) // Padding general para el Box
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "Logo de la Pastelería Mil Sabores",
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 16.dp, end = 16.dp)
                .size(120.dp)
        )

        // --- COLUMNA PRINCIPAL QUE CENTRA EL CONTENIDO ---
        Column(
            modifier = Modifier
                .fillMaxSize() // Ocupa todo el espacio disponible
                .padding(horizontal = 20.dp), // Padding horizontal para los campos
            verticalArrangement = Arrangement.Center, // Centra verticalmente el contenido
            horizontalAlignment = Alignment.CenterHorizontally // Centra horizontalmente los botones
        ) {
            Text(
                "Inicio de Sesión",
                style = MaterialTheme.typography.headlineMedium, // Un poco más grande
                modifier = Modifier.padding(bottom = 24.dp) // Más espacio abajo
            )

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth() // Ocupa todo el ancho
            )
            Spacer(modifier = Modifier.height(12.dp)) // Espacio entre campos

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Contraseña") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth() // Ocupa todo el ancho
            )

            Spacer(modifier = Modifier.height(20.dp)) // Más espacio antes del botón

            // Botón Principal "Entrar"
            Button(
                onClick = {
                    if (viewModel.login(email, password)) {
                        // Navega limpiando la pila hasta el inicio (si es necesario)
                        navController.navigate("home/$email") {
                            popUpTo(navController.graph.startDestinationId) { inclusive = true }
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(0.8f) // Botón un poco más estrecho
            ) {
                Text("Entrar")
            }

            // Mensaje de feedback del ViewModel
            Text(
                viewModel.mensaje.value,
                modifier = Modifier.padding(top = 10.dp),
                color = MaterialTheme.colorScheme.error // Usar color de error si aplica
            )

            // 🛑 BOTÓN DE ACCESO EMPLEADOS
            Spacer(Modifier.height(24.dp)) // Más espacio antes del botón secundario
            OutlinedButton(
                onClick = {
                    navController.navigate(Destinos.BACKOFFICE_BASE) {
                        popUpTo(Destinos.REGISTER_SCREEN) { inclusive = true }
                    }
                },
                modifier = Modifier.fillMaxWidth(0.8f) // Mismo ancho que el botón "Entrar"
            ) {
                Text("Acceso Empleados (Backoffice)")
            }
            // ----------------------------------------------------
        }
    }
}

