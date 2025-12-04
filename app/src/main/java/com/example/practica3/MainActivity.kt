package com.example.practica3

import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private lateinit var productAdapter: ProductAdapter
    private lateinit var recyclerView: RecyclerView
    private val apiService = ApiClient.retrofit.create(ApiService::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // configurar RecyclerView
        recyclerView = findViewById(R.id.recyclerViewProducts)
        recyclerView.layoutManager = LinearLayoutManager(this)
        // Botón ADD PRODUCT
        val btnAdd = findViewById<Button>(R.id.buttonAddProduct)
        btnAdd.setOnClickListener {
            val newProduct = Product(
                id = null,
                name = "Nuevo producto",
                price = 99.99,
                image = "https://example.com/image.jpg"
            )
            apiService.addProduct(newProduct)
                .enqueue(object : Callback<Product> {
                    override fun onResponse(call: Call<Product>, response: Response<Product>) {
                        if (response.isSuccessful) {
                            fetchProducts()
                        } else {
                            Log.e("ADD_PRODUCT", "Error: ${response.code()}")
                        }
                    }
                    override fun onFailure(call: Call<Product>, t: Throwable) {
                        Log.e("ADD_PRODUCT", "Failure: ${t.message}")
                    }
                })
        }
        // traer los datos del API
        fetchProducts()
    }
    private fun fetchProducts() {

        apiService.getAllProducts().enqueue(object : Callback<List<Product>> {
            override fun onResponse(
                call: Call<List<Product>>,
                response: Response<List<Product>>
            ) {
                if (response.isSuccessful) {
                    val productList = response.body()
                    Log.d("API_RESPONSE", "Productos: $productList")
                    productList?.let {
                        // Initialize the adapter with the product list
                        productAdapter = ProductAdapter(it)
                        recyclerView.adapter = productAdapter
                    }
                } else {
                    Log.e("API_ERROR", "Error code: ${response.code()}")
                }
            }
            override fun onFailure(call: Call<List<Product>>, t: Throwable) {
                Log.e("API_ERROR", "Failure: ${t.message}")
            }
        })
    }
}