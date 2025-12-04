package com.example.practica3
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    // Get all products
    @GET("/api/products")
    fun getAllProducts(): Call<List<Product>>

    @POST("/api/products")
    fun addProduct(@Body product: Product): Call<Product>

    // Eliminar un producto por su ID
    @DELETE("/api/products/{id}")
    fun deleteProduct(
        @Path("id") id: Long
    ): Call<Void>
}