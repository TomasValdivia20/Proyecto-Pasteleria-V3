package com.example.pasteleriamilsabores.ViewModel
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.pasteleriamilsabores.Model.FakeDatabase
import com.example.pasteleriamilsabores.Model.Usuario

class AuthViewModel : ViewModel() {

    var mensaje = mutableStateOf("")
    var usuarioActual = mutableStateOf<String?>(null)

    fun registrar(nombre: String, apellido: String, rut: String, region: String, direccion: String, email: String, password: String) {
        val nuevo = Usuario(nombre, apellido, rut, region, direccion, email, password)
        if (FakeDatabase.registrar(nuevo)) {
            mensaje.value = "Registro exitoso ✅"
        } else {
            mensaje.value = "El usuario ya existe ❌"
        }
    }

    fun login(email: String, password: String): Boolean {
        return if (FakeDatabase.login(email, password)) {
            usuarioActual.value = email
            mensaje.value = "Inicio de sesión exitoso 🎉"
            true
        } else {
            mensaje.value = "Credenciales inválidas ❌"
            false
        }
    }
}