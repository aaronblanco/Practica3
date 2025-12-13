package com.example.practica3

import com.google.gson.annotations.SerializedName

data class Order(
    @SerializedName("products") val products: List<Product>
)
