package com.example.practica3

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton

class CartActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var cartAdapter: CartAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        recyclerView = findViewById(R.id.recyclerViewCart)
        recyclerView.layoutManager = LinearLayoutManager(this)

        cartAdapter = CartAdapter(ShoppingCart.getCart().toMutableList(), this)
        recyclerView.adapter = cartAdapter

        val checkoutButton: MaterialButton = findViewById(R.id.buttonCheckout)
        checkoutButton.setOnClickListener {
            if (ShoppingCart.getCart().isEmpty()) {
                Toast.makeText(this, "El carrito está vacío, añade algún producto", Toast.LENGTH_SHORT).show()
            } else {
                val intent = Intent(this, CheckoutActivity::class.java)
                startActivity(intent)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        // Refresh the adapter to reflect changes in the cart
        cartAdapter.notifyDataSetChanged()
    }
}
