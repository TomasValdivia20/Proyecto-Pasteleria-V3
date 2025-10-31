package com.example.pasteleriamilsabores.Utils

import android.util.Patterns

// --- Validar correo, password y rut ---
fun validarCampos(
    nombre: String,
    apellido: String,
    rut: String,
    region: String,
    direccion: String,
    email: String,
    password: String
): String? {
    // Validar nombre
    if (nombre.trim().isEmpty()) {
        return "El nombre no puede estar vacío"
    }

    // Validar apellido
    if (apellido.trim().isEmpty()) {
        return "El apellido no puede estar vacío"
    }

    // Validar RUT chileno
    if (!validarRut(rut)) {
        return "RUT inválido"
    }

    // Validar Region seleccionada
    if (region.trim().isEmpty()) {
        return "Debes seleccionar una región"
    }

    // Validar dirección
    if (direccion.trim().isEmpty()) {
        return "La dirección no puede estar vacía"
    }

    // Validar correo electrónico
    if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
        return "Correo inválido"
    }

    // Validar contraseña
    if (password.length < 6) {
        return "La contraseña debe tener al menos 6 caracteres"
    }



    return null // Todo OK
}


// --- Función para validar RUT chileno ---
fun validarRut(input: String): Boolean {
    val rutLimpio = input.lowercase()
        .replace(Regex("[^0-9k-]"), "") // solo números, guion o k
        .trim()

    if (!Regex("^[0-9]{6,8}-[0-9kK]$").matches(rutLimpio)) return false

    val cuerpo = rutLimpio.substringBefore("-")
    val dvIngresado = rutLimpio.substringAfter("-").first()

    var suma = 0
    var multiplo = 2

    for (i in cuerpo.reversed()) {
        suma += Character.getNumericValue(i) * multiplo
        multiplo = if (multiplo == 7) 2 else multiplo + 1
    }

    val resto = suma % 11
    val dvCalculadoInt = 11 - resto
    val dvEsperado = when (dvCalculadoInt) {
        11 -> '0'
        10 -> 'k'
        else -> dvCalculadoInt.toString().first()
    }

    return dvIngresado == dvEsperado
}