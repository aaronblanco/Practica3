package com.example.practica3

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class SessionManagerTest {

    @Before
    fun reset() {
        // Aseguramos estado limpio antes de cada test
        SessionManager.isLoggedIn = false
    }

    @Test
    fun `muestra boton eliminar solo cuando hay sesion iniciada (admin)`() {
        // Estado inicial: sin sesión => no se puede eliminar
        assertFalse(SessionManager.isLoggedIn)
        assertFalse(SessionManager.canDeleteProducts())

        // Iniciar sesión => se puede eliminar
        SessionManager.isLoggedIn = true
        assertTrue(SessionManager.canDeleteProducts())

        // Cerrar sesión => no se puede eliminar
        SessionManager.isLoggedIn = false
        assertFalse(SessionManager.canDeleteProducts())
    }

    @Test
    fun `al cerrar sesion se oculta boton eliminar`() {
        // Simular login
        SessionManager.isLoggedIn = true
        assertTrue(SessionManager.canDeleteProducts())

        // Logout
        SessionManager.isLoggedIn = false
        assertFalse(SessionManager.canDeleteProducts())
    }
}

