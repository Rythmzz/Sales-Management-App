package com.tm968.shoppev4.page.presentation.product

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.tm968.shoppev4.R
import com.tm968.shoppev4.databinding.EditproductpageActivityBinding
import com.tm968.shoppev4.page.data.model.Product
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class EditProductPage_Activity: AppCompatActivity() {
    private var category= mutableListOf<String>()
    private var data:Product? = null
    private lateinit var binding:EditproductpageActivityBinding
    private var typeofProduct:Int? = null
    private val editProductViewModel:EditProductViewModel by viewModel()
    private var FirstThread:Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = EditproductpageActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initData()
        setView()
        interactInside()
        interactOutside()
    }

    private fun interactInside() {
        FirstThread = lifecycleScope.launchWhenStarted {
            this.launch {
                editProductViewModel.success.collect {
                    if(it as Boolean && it){
                        Toast.makeText(this@EditProductPage_Activity,"Update Success",Toast.LENGTH_LONG).show()
                        finish()
                    }
                    else {
                        Toast.makeText(this@EditProductPage_Activity,it.toString(),Toast.LENGTH_LONG).show()
                    }
                }
                editProductViewModel.loading.collect{
                    if (it){
                        binding.ivMask.visibility = View.VISIBLE
                        binding.progressLoading.visibility = View.VISIBLE
                    }
                    else {
                        binding.ivMask.visibility = View.GONE
                        binding.progressLoading.visibility=View.GONE
                    }
                }
            }
            this.launch {
                editProductViewModel.loading.collect{
                    if (it){
                        binding.ivMask.visibility = View.VISIBLE
                        binding.progressLoading.visibility = View.VISIBLE
                    }
                    else {
                        binding.ivMask.visibility = View.GONE
                        binding.progressLoading.visibility = View.GONE
                    }
                }
            }
        }
    }

    private fun setView() {
        category.add("Laptop")
        category.add("Phone")
        val arrayAdapter = ArrayAdapter(this, R.layout.listcategory_item,category)

        binding.etProductName.setText(data?.name.toString())
        binding.etPrice.setText(data?.price.toString())
        binding.etSalePrice.setText(data?.sale_price.toString())
        binding.etDescription.setText(data?.description.toString())
        when (data?.categoryId.toString()){
        "1" -> {binding.actvSelect.setText("Phone")}
            "2" ->  {binding.actvSelect.setText("Laptop")}
        }
        binding.actvSelect.setAdapter(arrayAdapter)
    }

    private fun interactOutside() {
        binding.ivBack.setOnClickListener {
            onBackPressed()
        }
        binding.actvSelect.setOnItemClickListener { parent, view, position, id ->
            val item:String = parent.getItemAtPosition(position).toString()
            when(item){
                "Phone" -> typeofProduct = 1
                "Laptop" -> typeofProduct = 2
            }
        }
        binding.btnUpdate.setOnClickListener {
            val productName = binding.etProductName.text.toString()
            val price = binding.etPrice.text.toString()
            val salePrice = binding.etSalePrice.text.toString()
            val description = binding.etDescription.text.toString()

            when {
                productName.isEmpty() -> {
                    binding.etProductName.error = "Name is not valid"
                }
                price.isEmpty() -> {
                    binding.etPrice.error = "Price is not valid"
                }
                typeofProduct == null ->{
                    binding.tilDropMenu.error = "Please Select Category"
                }
                salePrice.isEmpty() ->{
                    binding.etSalePrice.error = "Sale Price is not valid"
                }
                description.isEmpty() ->{
                    binding.etDescription.error = "Description is not valid"
                }
                (price.toInt() < salePrice.toInt() && salePrice.isNotEmpty()) ->{
                    binding.etSalePrice.error = "Promotion price must be higher than original price"
                }
                else ->{

                    editProductViewModel.editProduct(data!!.id,productName,data!!.image_url,price.toDouble(),salePrice.toDouble(),
                        typeofProduct!!.toInt(),data!!.view,description)
                }

            }
        }
    }

    private fun initData() {
        data = intent.getParcelableExtra("DATA")
//        Log.i("AAAA",data?.name.toString())
    }
}