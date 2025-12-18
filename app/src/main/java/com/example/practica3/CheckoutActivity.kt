package com.example.practica3

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CheckoutActivity : AppCompatActivity() {

    private val apiService = ApiClient.retrofit.create(ApiService::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout)

        val textViewOrderSummary: TextView = findViewById(R.id.textViewOrderSummary)
        val buttonConfirmPurchase: Button = findViewById(R.id.buttonConfirmPurchase)

        val cartItems = ShoppingCart.getCart()
        val total = cartItems.sumOf { it.price }

        val summary = StringBuilder()
        cartItems.forEach {
            summary.append("- ${it.productoNombre}: $${it.price}\n")
        }
        summary.append("\nTotal: $$total")

        textViewOrderSummary.text = summary.toString()

        buttonConfirmPurchase.setOnClickListener {
            if (cartItems.isNotEmpty()) {
                val order = Order(products = cartItems)
                apiService.createOrder(order).enqueue(object : Callback<Order> {
                    override fun onResponse(call: Call<Order>, response: Response<Order>) {
                        if (response.isSuccessful) {
                            // Order created, now checkout the cart on the server
                            apiService.checkoutCart().enqueue(object : Callback<Void> {
                                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                                    if (response.isSuccessful) {
                                        Toast.makeText(this@CheckoutActivity, "¡Compra confirmada!", Toast.LENGTH_LONG).show()
                                        ShoppingCart.clearCart()
                                        val intent = Intent(this@CheckoutActivity, MainActivity::class.java)
                                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                                        startActivity(intent)
                                        finish()
                                    } else {
                                        Toast.makeText(this@CheckoutActivity, "Error al vaciar el carrito en el servidor. Error: ${response.code()}", Toast.LENGTH_LONG).show()
                                        Log.e("CHECKOUT_ERROR", "API Error (checkoutCart): ${response.code()}")
                                    }
                                }

                                override fun onFailure(call: Call<Void>, t: Throwable) {
                                    Toast.makeText(this@CheckoutActivity, "Error de conexión al vaciar el carrito.", Toast.LENGTH_LONG).show()
                                    Log.e("CHECKOUT_FAILURE", "API Failure (checkoutCart): ${t.message}")
                                }
                            })
                        } else {
                            Toast.makeText(this@CheckoutActivity, "Error al crear el pedido. Error: ${response.code()}", Toast.LENGTH_LONG).show()
                            Log.e("CHECKOUT_ERROR", "API Error (createOrder): ${response.code()}")
                        }
                    }

                    override fun onFailure(call: Call<Order>, t: Throwable) {
                        Toast.makeText(this@CheckoutActivity, "Error de conexión al crear el pedido.", Toast.LENGTH_LONG).show()
                        Log.e("CHECKOUT_FAILURE", "API Failure (createOrder): ${t.message}")
                    }
                })
            } else {
                Toast.makeText(this, "El carrito está vacío.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
