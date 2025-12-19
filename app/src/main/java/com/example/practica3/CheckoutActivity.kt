package com.example.practica3

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import java.text.NumberFormat
import java.util.Locale
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CheckoutActivity : AppCompatActivity() {

    private val apiService = ApiClient.retrofit.create(ApiService::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout)

        val textViewOrderSummary: TextView = findViewById(R.id.textViewOrderSummary)
        val buttonConfirmPurchase: MaterialButton = findViewById(R.id.buttonConfirmPurchase)
        val textViewTotalAmount: TextView = findViewById(R.id.textViewTotalAmount)

        val cartItems = ShoppingCart.getCart()
        val total = cartItems.sumOf { it.price }

        val currency = NumberFormat.getCurrencyInstance(Locale.getDefault())

        val summary = StringBuilder()
        cartItems.forEachIndexed { index, item ->
            summary.append("${index + 1}. ${item.productoNombre} - ${currency.format(item.price)}\n")
        }
        if (cartItems.isEmpty()) {
            summary.append("No hay productos en el carrito")
        }

        textViewOrderSummary.text = summary.toString()
        textViewTotalAmount.text = currency.format(total)

        buttonConfirmPurchase.setOnClickListener {
            // Permitimos intentar el pedido sin sesión, pero manejamos la respuesta del backend de forma segura.
            if (cartItems.isNotEmpty()) {
                val order = Order(products = cartItems)
                apiService.createOrder(order).enqueue(object : Callback<Order> {
                    override fun onResponse(call: Call<Order>, response: Response<Order>) {
                        // Detectar contenido no-JSON (por ejemplo, HTML) que suele indicar redirección a login
                        val contentType = response.headers()["content-type"] ?: response.headers()["Content-Type"]
                        if (contentType != null && !contentType.contains("application/json", ignoreCase = true)) {
                            Toast.makeText(this@CheckoutActivity, "El servidor indica que debes iniciar sesión para confirmar la compra.", Toast.LENGTH_LONG).show()
                            Log.e("CHECKOUT_ERROR", "Respuesta no JSON: content-type=$contentType; code=${response.code()}\nEsto suele ser una página HTML de login.")
                            // Opcional: ofrecer login inmediato
                            val intent = Intent(this@CheckoutActivity, LoginActivity::class.java)
                            startActivity(intent)
                            return
                        }

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
