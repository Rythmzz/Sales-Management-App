package com.tm968.shoppev4.page.presentation.product

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.tm968.shoppev4.R
import com.tm968.shoppev4.databinding.ProductsettingpageFragmentBinding
import com.tm968.shoppev4.page.data.model.Product
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProductSettingPage_Fragment : Fragment() {

    private lateinit var binding: ProductsettingpageFragmentBinding
    private lateinit var adapter: ProductSettingAdapter
    private var category= mutableListOf<String>()
    private var FirstThread: Job? = null
    private val AllProductViewModel: AllProductPageViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = ProductsettingpageFragmentBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setView()
        SetRecyclerView()
        interactOutside()
        interactInside()




    }

    private fun interactOutside() {
        binding.ivAddProduct.setOnClickListener {
            var intent = Intent(requireActivity(),AddProductPage_Activity::class.java)
            startActivity(intent)
        }
        adapter.onclick = {
            val intent = Intent(requireActivity(),EditProductPage_Activity::class.java)
            intent.putExtra("DATA",it)
            startActivity(intent)
        }

    }

    private fun interactInside() {
        FirstThread = lifecycleScope.launchWhenStarted {
            this.launch {
                AllProductViewModel.productList.collect {
                    adapter.refreshData(it.toMutableList())
                }
            }
            binding.actvSelect.setOnItemClickListener { parent, view, position, id ->
                val item:String = parent.getItemAtPosition(position).toString()
                AllProductViewModel.getProductList()
//                Toast.makeText(this,"Now click item $item",Toast.LENGTH_LONG).show()
                when (item){
                    "Phone" -> {
                        this.launch {
                            AllProductViewModel.productList.collect{
                                adapter.refreshData((it.toMutableList()).filter { it.categoryId == 1 } as MutableList<Product>)
                            }
                        }
                    }
                    "Laptop" -> {
                        this.launch {
                            AllProductViewModel.productList.collect{
                                adapter.refreshData((it.toMutableList()).filter { it.categoryId == 2 } as MutableList<Product>)
                            }
                        }
                    }
                    "All" -> {
                        this.launch {
                            AllProductViewModel.productList.collect {
                                adapter.refreshData(it.toMutableList())
                            }
                        }
                    }
                }

            }

        }
    }

    private fun setView() {
        category.add("Laptop")
        category.add("Phone")
        category.add("All")
        val arrayAdapter = ArrayAdapter(requireActivity(),R.layout.listcategory_item,category)
        binding.actvSelect.setText("All")
        binding.actvSelect.setAdapter(arrayAdapter)
    }

    private fun SetRecyclerView() {
        adapter= ProductSettingAdapter(mutableListOf())
        binding.rvProductList.layoutManager= GridLayoutManager(requireActivity(),2)
        binding.rvProductList.adapter = adapter
        AllProductViewModel.getProductList()
//        adapter.onclick = {
//            val intent = Intent(this@AllProductPage_Activity, ProductPage_Activity::class.java)
//            intent.putExtra("DATA",it)
//            startActivity(intent)
//        }

    }
}