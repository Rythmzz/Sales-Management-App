package com.tm968.shoppev4.page.presentation.home

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tm968.shoppev4.BuildConfig
import com.tm968.shoppev4.databinding.SearchproductItemBinding
import com.tm968.shoppev4.databinding.TopsearchproductItemBinding
import com.tm968.shoppev4.page.data.model.Product

class HomePageTopSearchAdapter(private var ProductList:MutableList<Product>):RecyclerView.Adapter<HomePageTopSearchAdapter.HomePageViewHolder>() {

    var onClick:((item:Product)->Unit)?=null

    @SuppressLint("NotifyDataSetChanged")
    fun refreshData(newList:MutableList<Product>){
        ProductList = newList
        notifyDataSetChanged()
    }

    inner class HomePageViewHolder(private val binding:TopsearchproductItemBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(item:Product){
            if (item.view != 0){
                binding.tvNameProduct.text = item.name
                if (item.sale_price == 0){
                    binding.ivSale.visibility=View.GONE
                    binding.tvSale.visibility = View.GONE
                }
                Glide.with(itemView)
                    .load(BuildConfig.BASE_URL+"getImage/Products/"+item.image_url)
                    .centerCrop()
                    .into(binding.ivProductImage)
                itemView.setOnClickListener {
                    onClick?.invoke(item)
                }
            }
        }
        fun last(){
            binding.llProductDetail.visibility = View.GONE
            binding.ivSale.visibility=View.GONE
            binding.ivTop.visibility=View.GONE
            binding.tvTop.visibility=View.GONE
            binding.llMore.visibility=View.VISIBLE

        }

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomePageViewHolder {
        val binding = TopsearchproductItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return HomePageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HomePageViewHolder, position: Int) {
        val item = ProductList[position]
        if (position < 5){
            holder.bind(item)
        }
        else if (position==5){
            holder.last()
        }

    }

    override fun getItemCount(): Int {
        return ProductList.size
    }

}