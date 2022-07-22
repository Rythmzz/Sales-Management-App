package com.tm968.shoppev4.page.presentation.product

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tm968.shoppev4.BuildConfig
import com.tm968.shoppev4.databinding.ProductItemBinding
import com.tm968.shoppev4.page.data.model.Product
import java.math.RoundingMode

class ProductSettingAdapter(private var listProduct: MutableList<Product>):RecyclerView.Adapter<ProductSettingAdapter.ProductSettingViewHolder>() {
    var onclick:((item:Product)->Unit)? = null

    @SuppressLint("NotifyDataSetChanged")
    fun refreshData(newList:MutableList<Product>){
        listProduct = newList
        notifyDataSetChanged()
    }
    inner class ProductSettingViewHolder(private val binding: ProductItemBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(item:Product){
            if (item.sale_price == 0){
                binding.ivSale.visibility = View.GONE
                binding.tvSale.visibility = View.GONE

                binding.ivSalePercent.visibility= View.GONE
                binding.tvPercent.visibility= View.GONE
                binding.tvNumber.visibility= View.GONE

                val s = String.format("%,d", item.price).replace(',', ' ')
                binding.tvMoneySale.text = "₫${s}"
            }
            else {val salePercent = ((item.sale_price.toDouble()*100)/item.price.toDouble())
                binding.tvPercent.text = "${100 - ((salePercent.toBigDecimal().setScale(0,
                    RoundingMode.UP).toDouble())).toInt()}%"
                val s = String.format("%,d", item.sale_price).replace(',', ' ')
                binding.tvMoneySale.text = "₫${s}"
            }
            binding.tvNameProduct.text = item.name
            binding.tvViewSearch.visibility = View.GONE
            binding.ivEditProduct.visibility = View.VISIBLE

            Glide.with(itemView).load(
                BuildConfig
                    .BASE_URL+"getImage/Products/"+item.image_url).centerCrop().into(binding.ivProductImage)

            itemView.setOnClickListener {
                onclick?.invoke(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductSettingViewHolder {
        val binding = ProductItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ProductSettingViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductSettingViewHolder, position: Int) {
        val item = listProduct[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return listProduct.size
    }

}