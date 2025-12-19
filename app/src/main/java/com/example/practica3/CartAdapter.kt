package com.example.practica3

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.button.MaterialButton

class CartAdapter(
    private val cartItems: MutableList<Product>,
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
        private val removeFromCartButton: MaterialButton = itemView.findViewById(R.id.buttonRemoveFromCart)

        fun bind(product: Product) {
            productName.text = product.productoNombre
            productPrice.text = "${'$'}${product.price}"

            Glide.with(context)
                .load(product.imageUrl)
                .placeholder(R.drawable.ic_product_placeholder)
                .error(R.drawable.ic_product_placeholder)
                .centerCrop()
                .into(productImage)

            removeFromCartButton.setOnClickListener {
                val pos = bindingAdapterPosition
                if (pos != RecyclerView.NO_POSITION) {
                    val productToRemove = cartItems[pos]
                    ShoppingCart.removeItem(productToRemove)
                    cartItems.removeAt(pos)
                    notifyItemRemoved(pos)
                    notifyItemRangeChanged(pos, cartItems.size - pos)
                }
            }
        }
    }
}
