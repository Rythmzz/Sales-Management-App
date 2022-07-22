package com.tm968.shoppev4.page.presentation.order

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.tm968.shoppev4.databinding.OrderhistorypageFragmentBinding

import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel


class OrderHistoryPage_Fragment : Fragment() {
    private lateinit var binding: OrderhistorypageFragmentBinding
    private val orderHistoryViewModel: OrderHistoryViewModel by viewModel()
    private var FirstThread :Job? = null
    private lateinit var adapter: OrderHistoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = OrderhistorypageFragmentBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setRecyclerView()
        interactInside()
    }

    private fun setRecyclerView() {
        adapter = OrderHistoryAdapter(mutableListOf())

        binding.rvOrderHistory.adapter = adapter
        val layoutManager = LinearLayoutManager(context)
        layoutManager.orientation = LinearLayoutManager.VERTICAL

        binding.rvOrderHistory.layoutManager = layoutManager
        orderHistoryViewModel.getListOrder()
    }

    private fun interactInside() {
        FirstThread = lifecycleScope.launchWhenStarted {
            this.launch {
                orderHistoryViewModel.success.collect {
                    adapter.refreshData(it.toMutableList())
                }
            }
        }
    }
}