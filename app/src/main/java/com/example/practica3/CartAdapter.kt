package com.example.practica3

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class CartAdapter(
    private val cartItems: List<Product>,
    private val context: Context
) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cart_item, parent, false)
        return CartViewHolder(view)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val product = cartItems[position]
        holder.bind(product)
    }

    override fun getItemCount(): Int = cartItems.size

    inner class CartViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val productName: TextView = itemView.findViewById(R.id.textViewProductName)
        private val productPrice: TextView = itemView.findViewById(R.id.textViewProductPrice)
        private val productImage: ImageView = itemView.findViewById(R.id.imageViewProduct)
        private val removeFromCartButton: Button = itemView.findViewById(R.id.buttonRemoveFromCart)

        fun bind(product: Product) {
            productName.text = product.title
            productPrice.text = "$${product.price}"

            Glide.with(context)
                .load(product.image)
                .into(productImage)

            removeFromCartButton.setOnClickListener {
                ShoppingCart.removeItem(product)
                notifyDataSetChanged() // This is not the most efficient way, but it's simple for this case.
            }
        }
    }
}
