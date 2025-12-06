package com.example.practica3

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
            summary.append("- ${it.title}: $${it.price}\n")
        }
        summary.append("\nTotal: $$total")

        textViewOrderSummary.text = summary.toString()

        buttonConfirmPurchase.setOnClickListener {
            if (cartItems.isNotEmpty()) {
                val order = Order(products = cartItems)
                apiService.createOrder(order).enqueue(object : Callback<Order> {
                    override fun onResponse(call: Call<Order>, response: Response<Order>) {
                        if (response.isSuccessful) {
                            Toast.makeText(this@CheckoutActivity, "Purchase confirmed!", Toast.LENGTH_LONG).show()
                            ShoppingCart.clearCart()
                            finish()
                        } else {
                            Toast.makeText(this@CheckoutActivity, "Failed to create order. Error: ${response.code()}", Toast.LENGTH_LONG).show()
                            Log.e("CHECKOUT_ERROR", "API Error: ${response.code()}")
                        }
                    }

                    override fun onFailure(call: Call<Order>, t: Throwable) {
                        Toast.makeText(this@CheckoutActivity, "Failed to connect to the server.", Toast.LENGTH_LONG).show()
                        Log.e("CHECKOUT_FAILURE", "API Failure: ${t.message}")
                    }
                })
            } else {
                Toast.makeText(this, "Your cart is empty.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
