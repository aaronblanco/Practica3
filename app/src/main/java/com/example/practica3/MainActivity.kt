package com.example.practica3

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private lateinit var productAdapter: ProductAdapter
    private lateinit var recyclerView: RecyclerView
    private val apiService = ApiClient.retrofit.create(ApiService::class.java)

    private lateinit var btnAddProduct: MaterialButton
    private lateinit var btnLoginLogout: MaterialButton
    private lateinit var editTextSearch: TextInputEditText
    private lateinit var emptyView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recyclerViewProducts)
        recyclerView.layoutManager = LinearLayoutManager(this)

        btnAddProduct = findViewById(R.id.buttonAddProduct)
        btnLoginLogout = findViewById(R.id.buttonLoginLogout)
        editTextSearch = findViewById(R.id.editTextSearch)
        emptyView = findViewById(R.id.emptyView)

        val btnGoToCart: MaterialButton = findViewById(R.id.buttonGoToCart)
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

        // Listener de búsqueda
        editTextSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val query = s?.toString()?.trim() ?: ""
                if (::productAdapter.isInitialized) {
                    productAdapter.filterByName(query)
                    updateEmptyState()
                }
            }
            override fun afterTextChanged(s: Editable?) {}
        })

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
            updateEmptyState()
        } else {
            emptyView.visibility = View.VISIBLE
        }
    }

    private fun updateEmptyState() {
        val isEmpty = (productAdapter.itemCount == 0)
        emptyView.visibility = if (isEmpty) View.VISIBLE else View.GONE
        recyclerView.visibility = if (isEmpty) View.GONE else View.VISIBLE
    }

    private fun fetchProducts() {
        apiService.getAllProducts().enqueue(object : Callback<List<Product>> {
            override fun onResponse(call: Call<List<Product>>, response: Response<List<Product>>) {
                if (response.isSuccessful) {
                    val productList = response.body()
                    productList?.let {
                        productAdapter = ProductAdapter(it.toMutableList(), this@MainActivity)
                        recyclerView.adapter = productAdapter
                        // Aplicar filtro actual si hay texto
                        val currentQuery = editTextSearch.text?.toString()?.trim().orEmpty()
                        productAdapter.filterByName(currentQuery)
                        updateEmptyState()
                    } ?: run {
                        // Lista nula: estado vacío
                        if (::productAdapter.isInitialized.not()) {
                            emptyView.visibility = View.VISIBLE
                            recyclerView.visibility = View.GONE
                        }
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
