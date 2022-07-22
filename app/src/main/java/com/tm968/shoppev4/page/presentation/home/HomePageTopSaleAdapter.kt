package com.tm968.shoppev4.page.presentation.home

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tm968.shoppev4.BuildConfig
import com.tm968.shoppev4.databinding.SaleproductItemBinding
import com.tm968.shoppev4.page.data.model.Product
import java.math.RoundingMode

class HomePageTopSaleAdapter(private var ProductList:MutableList<Product>):RecyclerView.Adapter<HomePageTopSaleAdapter.HomePageViewHolder>() {

    var onClick:((item:Product) -> Unit)?= null

    @SuppressLint("NotifyDataSetChanged")
    fun refreshData(newlist:MutableList<Product>){
        ProductList = newlist
        notifyDataSetChanged()
    }

    inner class HomePageViewHolder(private val binding:SaleproductItemBinding):RecyclerView.ViewHolder(binding.root){
        @SuppressLint("SetTextI18n")
        fun bind(item:Product){
            if (item.sale_price.toInt() != 0){
                val salePercent = ((item.sale_price.toDouble()*100)/item.price.toDouble())
                binding.tvPercent.text = "${100 - ((salePercent.toBigDecimal().setScale(0,RoundingMode.UP).toDouble())).toInt()}%"
                val s = String.format("%,d", item.sale_price).replace(',', ' ')
                binding.tvMoneySale.text = "₫${s}"
                Glide.with(itemView)
                    .load(BuildConfig
                        .BASE_URL+"getImage/Products/"+item.image_url)
                    .centerCrop()
                    .into(binding.ivProductImage)

                itemView.setOnClickListener {
                    onClick?.invoke(item)
                }
            }


        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomePageViewHolder {
        val binding=SaleproductItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return HomePageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HomePageViewHolder, position: Int) {
        val item = ProductList[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return ProductList.size
    }
}