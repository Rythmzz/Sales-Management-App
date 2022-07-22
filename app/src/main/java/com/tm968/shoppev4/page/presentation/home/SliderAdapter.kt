package com.tm968.shoppev4.page.presentation.home

import android.view.LayoutInflater
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.smarteist.autoimageslider.SliderViewAdapter
import com.tm968.shoppev4.databinding.ImagesItemBinding

class SliderAdapter(private var mList: MutableList<String>): SliderViewAdapter<SliderAdapter.SliderAdapterAddImages>() {
    inner class SliderAdapterAddImages(private val binding: ImagesItemBinding):SliderViewAdapter.ViewHolder(binding.root){
        fun bind(item:String){
            Glide.with(itemView.context).load(item).into(binding.ivHeadImage)
        }
    }
    override fun getCount(): Int {
        return mList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup?): SliderAdapterAddImages {
        val binding = ImagesItemBinding.inflate(LayoutInflater.from(parent?.context),parent,false)
        return SliderAdapterAddImages(binding)
    }

    override fun onBindViewHolder(viewHolder: SliderAdapterAddImages?, position: Int) {
        val item = mList[position]
        viewHolder?.bind(item)
    }
}