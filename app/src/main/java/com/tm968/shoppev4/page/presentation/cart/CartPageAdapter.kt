package com.tm968.shoppev4.page.presentation.cart

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tm968.shoppev4.BuildConfig
import com.tm968.shoppev4.databinding.CartItemBinding
import com.tm968.shoppev4.page.data.model.Cart
import com.tm968.shoppev4.page.data.model.Product
import java.math.RoundingMode

class CartPageAdapter(private var listProductCart:MutableList<Cart>):RecyclerView.Adapter<CartPageAdapter.CartViewHolder>() {

    var onChange:((id:Int,isPlus:Boolean,position:Int)->Unit)? = null
    var updateCart:((listproductcart:MutableList<Cart>) -> Unit )? = null


    @SuppressLint("NotifyDataSetChanged")
    fun refreshData(newList:MutableList<Cart>){
        listProductCart=newList
        notifyDataSetChanged()
    }

    fun changeCount(id:Int,isPlus: Boolean,position: Int){
        val productCount = listProductCart.find {
            id == it.product.id
        }
        if (isPlus == true){
            if (productCount?.count != null){
            productCount.count += 1
            }
        }
        else {
            if (productCount?.count != null){
                productCount.count -= 1
            }
        }
        if (productCount?.count == 0){
            listProductCart.remove(productCount)
        }
        updateCart?.invoke(listProductCart)
        notifyItemChanged(position)
    }

    inner class CartViewHolder(private val binding:CartItemBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(item:Cart,position: Int){
            if (item.product.sale_price == 0){

                binding.tvPrice.visibility = View.GONE
                val s = String.format("%,d", item.product.price).replace(',', ' ')
                binding.tvSalePrice.text = "₫${s}"
            }
            else {
                val s = String.format("%,d", item.product.sale_price).replace(',', ' ')
                val p = String.format("%,d", item.product.price).replace(',', ' ')
                binding.tvPrice.text = "₫${p}"
                binding.tvSalePrice.text = "₫${s}"
            }

            binding.tvNumberProduct.text = item.count.toString()
            binding.tvNameProduct.text = item.product.name
            binding.tvCategory.text = when(item.product.categoryId){
                1 -> "Phone"
                2 -> "Laptop"
                else -> ""
            }

            Glide.with(itemView).load(
                BuildConfig
                    .BASE_URL+"getImage/Products/"+item.product.image_url).centerCrop().into(binding.ivProductImage)
            binding.ivPlus.setOnClickListener {

                onChange?.invoke(item.product.id,true,position)

            }
            binding.ivMinius.setOnClickListener {
                onChange?.invoke(item.product.id,false,position )

            }
        }





    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val binding = CartItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return CartViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val item = listProductCart[position]
        holder.bind(item,position)

    }

    override fun getItemCount(): Int {
         return listProductCart.size
    }
}