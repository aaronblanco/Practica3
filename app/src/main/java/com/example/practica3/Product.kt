package com.example.practica3

import com.google.gson.annotations.SerializedName

data class Product(
    @SerializedName("productoId") val id: Long? = null,
    @SerializedName("productoNombre") val name: String,
    @SerializedName("price") val price: Double,
    @SerializedName("image") val image: String // Añade esta línea
)