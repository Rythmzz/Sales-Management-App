package com.tm968.shoppev4.page.presentation.search

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tm968.shoppev4.BuildConfig
import com.tm968.shoppev4.databinding.SearchproductItemBinding
import com.tm968.shoppev4.page.data.model.Product

class SearchPageAdapter(private var listProduct:MutableList<Product>):RecyclerView.Adapter<SearchPageAdapter.SearchPageViewHolder>() {

    var onClick:((item:Product)->Unit)? = null

    @SuppressLint("NotifyDataSetChanged")
    fun refreshData(newList:MutableList<Product>) {
        listProduct = newList
        notifyDataSetChanged()
    }

    inner class SearchPageViewHolder(private val binding:SearchproductItemBinding):
        RecyclerView.ViewHolder(binding.root) {
            fun bind(item: Product){
                binding.tvNameProduct.text = item.name
                binding.tvCategory.text = when(item.categoryId){
                    1 -> "Phone"
                    2 -> "Laptop"
                    else -> ""
                }

                Glide.with(itemView)
                    .load(BuildConfig.BASE_URL+"getImage/Products/"+item.image_url).centerCrop()
                    .into(binding.civProductImage)

                itemView.setOnClickListener {
                    onClick?.invoke(item)
                }
            }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchPageViewHolder {
        val binding = SearchproductItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return SearchPageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SearchPageViewHolder, position: Int) {
        val item = listProduct[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return listProduct.size
    }
}