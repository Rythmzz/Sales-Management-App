package com.tm968.shoppev4.page.presentation.product

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.tm968.shoppev4.R
import com.tm968.shoppev4.databinding.AllproductpageActivityBinding
import com.tm968.shoppev4.page.data.model.Product
import com.tm968.shoppev4.page.utils.Sample
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class AllProductPage_Activity:AppCompatActivity() {
    private lateinit var binding:AllproductpageActivityBinding
    private var category= mutableListOf<String>()
    private lateinit var adapter:AllProductPageAdapter
    private var FirstThread:Job? = null
    private val AllProductViewModel:AllProductPageViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = AllproductpageActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setView()
        SetRecyclerView()
        interactInside()
        interactOutside()
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

    private fun SetRecyclerView() {
        adapter=AllProductPageAdapter(mutableListOf())
        binding.rvProductList.layoutManager=GridLayoutManager(this@AllProductPage_Activity,2)
        binding.rvProductList.adapter = adapter


        AllProductViewModel.getProductList()
        adapter.onclick = {
            val intent = Intent(this@AllProductPage_Activity,ProductPage_Activity::class.java)
            intent.putExtra("DATA",it)
            startActivity(intent)
        }

    }

    private fun interactOutside() {

        binding.ivBack.setOnClickListener {
            onBackPressed()
        }

    }

    private fun setView() {
        category.add("Laptop")
        category.add("Phone")
        category.add("All")
        val arrayAdapter = ArrayAdapter(this,R.layout.listcategory_item,category)
        binding.actvSelect.setText("All")
        binding.actvSelect.setAdapter(arrayAdapter)



    }
}