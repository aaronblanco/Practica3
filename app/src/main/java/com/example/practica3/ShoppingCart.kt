package com.example.practica3

object ShoppingCart {
    private val cart = mutableListOf<Product>()

    fun getCart(): List<Product> {
        return cart
    }

    fun addItem(product: Product) {
        cart.add(product)
    }

    fun removeItem(product: Product) {
        cart.remove(product)
    }

    fun clearCart() {
        cart.clear()
    }
}
