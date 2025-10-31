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


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(navController: NavController, viewModel: AuthViewModel) {
    var nombre by remember { mutableStateOf("") }
    var apellido by remember { mutableStateOf("") }
    var rut by remember { mutableStateOf("") }
    var region by remember { mutableStateOf("") }
    var direccion by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    // Control del menu desplegable
    var expanded by remember { mutableStateOf(false) }

    // Lista de regiones de Chile
    val regiones = listOf(
        "Region de Arica y Parinacota",
        "Region de Tarapaca",
        "Region de Antofagasta",
        "Region de Atacama",
        "Region de Coquimbo",
        "Region de Valparaiso",
        "Region Metropolitana",
        "Region del Libertador General Bernardo O'higgins",
        "Region del Maule",
        "Region del Ñuble",
        "Region del Biobio",
        "Region de La Araucania",
        "Region de Los Rios",
        "Region de Los Lagos",
        "Region de Aysen",
        "Region de Magallanes y la Antartica Chilena"
    )

// 1. Contenedor Box para el fondo y el icono 🎨
    Box(
        modifier = Modifier
            .fillMaxSize()
            // Aplicamos el color de fondo PastelCalido
            .background(PastelCalido)
            .padding(all = 20.dp)
    ) {
        // 2. Icono de marca en la esquina superior derecha (TopEnd)
        Image(
            // **IMPORTANTE**: Reemplaza 'R.drawable.logo_pasteleria_mil_sabores' con el ID real de tu imagen
            painter = painterResource(id = R.drawable.logo ), // Cambiado a tu logo
            contentDescription = "Logo de Pastelería Mil Sabores",
            modifier = Modifier
                .align(Alignment.TopEnd) // Posiciona en la esquina superior derecha
                .padding(top = 16.dp, end = 16.dp) // Pequeño margen
                .size(120.dp) // Ajusta el tamaño según sea necesario para tu logo
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(all = 20.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = "Registro", style = MaterialTheme.typography.titleLarge)

            OutlinedTextField(
                value = nombre,
                onValueChange = { nombre = it },
                label = { Text("Nombre") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = apellido,
                onValueChange = { apellido = it },
                label = { Text("Apellido") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = rut,
                onValueChange = { rut = it },
                label = { Text("Rut (12345678-9)") },
                modifier = Modifier.fillMaxWidth()
            )

            // ComboBox (DropDown) para region
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {
                OutlinedTextField(
                    value = region,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Region") },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                    },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                )
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    regiones.forEach { opcion ->
                        DropdownMenuItem(
                            text = { Text(opcion) },
                            onClick = {
                                region = opcion
                                expanded = false
                            }
                        )
                    }
                }
            }

            OutlinedTextField(
                value = direccion,
                onValueChange = { direccion = it },
                label = { Text("Direccion") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Contraseña") },
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = PasswordVisualTransformation()
            )

            Spacer(modifier = Modifier.height(10.dp))

            Button(
                onClick = {
                    val error = validarCampos(nombre, apellido, rut, region, direccion, email, password )
                    if (error != null) {
                        viewModel.mensaje.value=error
                    } else {
                        viewModel.registrar(nombre, apellido, rut, region, direccion, email, password)
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Registrar")
            }

            Text(
                text = viewModel.mensaje.value,
                modifier = Modifier.padding(top = 10.dp)
            )

            TextButton(onClick = { navController.navigate("login") }) {
                Text("¿Ya tienes cuenta? Inicia sesion")
            }
        }
    }}