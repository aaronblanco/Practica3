package com.example.practica3

object ShoppingCart {
    private val cart = mutableListOf<Product>()

    fun getCart(): List<Product> {
        return cart
    }

    fun addItem(product: Product): Boolean {
        if (cart.any { it.productoId == product.productoId }) {
            return false
        }
        cart.add(product)
        return true
    }

    fun getProductIds(): List<Int> {
        return cart.mapNotNull { it.productoId }
    }

    fun removeItem(product: Product) {
        cart.remove(product)
    }

    fun clearCart() {
        cart.clear()
    }
}
