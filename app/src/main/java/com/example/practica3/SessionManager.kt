package com.example.practica3

object SessionManager {
    var isLoggedIn: Boolean = false
    // Función pura para decidir si se puede eliminar productos (actualmente: solo si hay sesión iniciada)
    fun canDeleteProducts(): Boolean = isLoggedIn
}
