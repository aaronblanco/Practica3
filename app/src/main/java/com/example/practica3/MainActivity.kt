package com.example.practica3

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
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

    private lateinit var btnAddProduct: Button
    private lateinit var btnLoginLogout: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recyclerViewProducts)
        recyclerView.layoutManager = LinearLayoutManager(this)

        btnAddProduct = findViewById(R.id.buttonAddProduct)
        btnLoginLogout = findViewById(R.id.buttonLoginLogout)

        val btnGoToCart = findViewById<Button>(R.id.buttonGoToCart)
        btnGoToCart.setOnClickListener {
            val intent = Intent(this, CartActivity::class.java)
            startActivity(intent)
        }

        btnAddProduct.setOnClickListener {
            val intent = Intent(this, AddProductActivity::class.java)
            startActivity(intent)
        }

        btnLoginLogout.setOnClickListener {
            if (SessionManager.isLoggedIn) {
                // Logout
                apiService.logout().enqueue(object : Callback<Void> {
                    override fun onResponse(call: Call<Void>, response: Response<Void>) {
                        SessionManager.isLoggedIn = false
                        updateUI()
                        Toast.makeText(this@MainActivity, "Sesión cerrada", Toast.LENGTH_SHORT).show()
                    }

                    override fun onFailure(call: Call<Void>, t: Throwable) {
                        // Even if logout fails, log out locally
                        SessionManager.isLoggedIn = false
                        updateUI()
                        Toast.makeText(this@MainActivity, "Error de conexión al cerrar sesión", Toast.LENGTH_SHORT).show()
                    }
                })
            } else {
                // Login
                val intent = Intent(this@MainActivity, LoginActivity::class.java)
                startActivity(intent)
            }
        }

        val textViewWhereAreWe: TextView = findViewById(R.id.textViewWhereAreWe)
        textViewWhereAreWe.setOnClickListener {
            val intent = Intent(this, MapActivity::class.java)
            startActivity(intent)
        }

        fetchProducts()
        updateUI()
    }

    override fun onResume() {
        super.onResume()
        fetchProducts()
        updateUI()
    }

    private fun updateUI() {
        if (SessionManager.isLoggedIn) {
            btnAddProduct.visibility = View.VISIBLE
            btnLoginLogout.text = "Cerrar Sesión"
        } else {
            btnAddProduct.visibility = View.GONE
            btnLoginLogout.text = "Iniciar Sesión"
        }
        // Refrescar la lista para que los items actualicen la visibilidad del botón Eliminar
        if (::productAdapter.isInitialized) {
            productAdapter.notifyDataSetChanged()
        }
    }

    private fun fetchProducts() {
        apiService.getAllProducts().enqueue(object : Callback<List<Product>> {
            override fun onResponse(call: Call<List<Product>>, response: Response<List<Product>>) {
                if (response.isSuccessful) {
                    val productList = response.body()
                    productList?.let {
                        productAdapter = ProductAdapter(it.toMutableList(), this@MainActivity)
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
