package com.tm968.shoppev4.page.presentation.order

import android.annotation.SuppressLint
import android.text.method.ScrollingMovementMethod
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tm968.shoppev4.databinding.OrderItemBinding
import com.tm968.shoppev4.page.data.model.Order
import java.text.SimpleDateFormat

class OrderHistoryAdapter(private var listOrder:MutableList<Order>):RecyclerView.Adapter<OrderHistoryAdapter.OrderHistoryViewHolder>() {

    @SuppressLint("NotifyDataSetChanged")
    fun refreshData(newList: MutableList<Order>){
        listOrder = newList
        notifyDataSetChanged()
    }

    inner class OrderHistoryViewHolder(private val binding:OrderItemBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(item:Order){
            binding.tvOrderCode.text = item.orderCode
            var TotalPrice = 0
            var count = 0
            for (i in 0 until item.orderDetails.size){
                TotalPrice += item.orderDetails[i].Price!!.toInt()
                count++
            }
            val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
            val formatter = SimpleDateFormat("dd.MM.yyyy HH:mm")
            val output: String = formatter.format(parser.parse(item.orderDate))

            val sp = String.format("%,d",TotalPrice).replace(',', ' ')

            binding.tvAddress.movementMethod = ScrollingMovementMethod()
            binding.tvProductCount.text=count.toString()
            binding.tvPrice.text = "$sp VNĐ"
            binding.tvOrderDate.text=output
            binding.tvAddress.text = item.addressDelivery
            binding.tvUserId.text = item.userId.toString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderHistoryViewHolder {
        val binding = OrderItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return OrderHistoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OrderHistoryViewHolder, position: Int) {
        val item = listOrder[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return listOrder.size
    }

}