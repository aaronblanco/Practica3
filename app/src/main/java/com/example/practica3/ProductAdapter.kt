package com.example.practica3

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.button.MaterialButton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProductAdapter(private val productList: MutableList<Product>, private val context: Context) :
    RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    private val apiService = ApiClient.retrofit.create(ApiService::class.java)

    // Lista filtrada que se muestra
    private val items: MutableList<Product> = productList.toMutableList()

    class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageViewProduct: ImageView = itemView.findViewById(R.id.imageViewProduct)
        val textViewName: TextView = itemView.findViewById(R.id.textViewName)
        val textViewPrice: TextView = itemView.findViewById(R.id.textViewPrice)
        val addToCartButton: MaterialButton = itemView.findViewById(R.id.buttonAddToCart)
        val deleteButton: MaterialButton = itemView.findViewById(R.id.buttonDeleteProduct)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.product_item, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = items[position]
        holder.textViewName.text = product.productoNombre
        holder.textViewPrice.text = "${'$'}${product.price}"

        Glide.with(holder.itemView.context)
            .load(product.imageUrl)
            .placeholder(R.drawable.ic_product_placeholder)
            .error(R.drawable.ic_product_placeholder)
            .into(holder.imageViewProduct)

        holder.addToCartButton.setOnClickListener {
            if (ShoppingCart.addItem(product)) {
                Toast.makeText(context, "${product.productoNombre} añadido al carrito", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "${product.productoNombre} ya está en el carrito", Toast.LENGTH_SHORT).show()
            }
        }

        // Mostrar botón eliminar si se permite eliminar (sesión iniciada)
        holder.deleteButton.visibility = if (SessionManager.canDeleteProducts()) View.VISIBLE else View.GONE
        holder.deleteButton.setOnClickListener {
            val id = product.productoId
            if (id == null) {
                Toast.makeText(context, "Producto sin ID, no se puede eliminar", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            apiService.deleteProduct(id).enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.isSuccessful) {
                        val pos = holder.bindingAdapterPosition
                        if (pos != RecyclerView.NO_POSITION) {
                            // remover de ambas listas
                            val removed = items.removeAt(pos)
                            val originalIndex = productList.indexOfFirst { it.productoId == removed.productoId }
                            if (originalIndex >= 0) {
                                productList.removeAt(originalIndex)
                            }
                            notifyItemRemoved(pos)
                            notifyItemRangeChanged(pos, items.size - pos)
                        }
                        Toast.makeText(context, "Producto eliminado", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, "Error al eliminar: ${response.code()}", Toast.LENGTH_SHORT).show()
                    }
                }
                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Toast.makeText(context, "Error de conexión: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    override fun getItemCount(): Int = items.size

    // Filtrado por nombre
    fun filterByName(query: String) {
        val trimmed = query.trim().lowercase()
        items.clear()
        if (trimmed.isEmpty()) {
            items.addAll(productList)
        } else {
            items.addAll(productList.filter { it.productoNombre?.lowercase()?.contains(trimmed) == true })
        }
        notifyDataSetChanged()
    }
}
