package com.tm968.shoppev4.page.presentation.product

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.tm968.shoppev4.R
import com.tm968.shoppev4.databinding.AddproductpageActivityBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File

class AddProductPage_Activity:AppCompatActivity() {
    private lateinit var binding:AddproductpageActivityBinding
    private val addProductViewModel:AddProductViewModel by viewModel()
    private var category= mutableListOf<String>()
    private var imagePicker:File? = null
    private var FirstThread:Job? = null
    private var typeofProduct:Int? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        binding = AddproductpageActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setView()
        interactOutside()
        interactInside()
    }

    private fun interactInside() {
        FirstThread = lifecycleScope.launchWhenStarted {
            this.launch {
                addProductViewModel.success.collect {
                    if ( it as Boolean && it){
                        Toast.makeText(this@AddProductPage_Activity,"Create Product Success",Toast.LENGTH_LONG).show()
                        finish()
                    }
                    else {
                        Toast.makeText(this@AddProductPage_Activity,it as String,Toast.LENGTH_LONG).show()
                    }
                }
            }
            this.launch {
                addProductViewModel.loading.collect{
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
        val arrayAdapter = ArrayAdapter(this@AddProductPage_Activity, R.layout.listcategory_item,category)
        binding.actvSelect.setAdapter(arrayAdapter)
    }

    private fun interactOutside() {


        binding.actvSelect.setOnItemClickListener { parent, view, position, id ->
            val item:String = parent.getItemAtPosition(position).toString()
        when(item){
            "Phone" -> typeofProduct = 0
            "Laptop" -> typeofProduct = 1
        }

        }
        binding.ivBack.setOnClickListener {
            onBackPressed()
        }
        binding.btnAdd.setOnClickListener {
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
                imagePicker == null ->{
                    Toast.makeText(this,"Please add photos",Toast.LENGTH_LONG).show()
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
                    addProductViewModel.createProduct(productName,imagePicker!!,price,salePrice,typeofProduct!!,description)
                }

            }
        }
        binding.ivLogoAddImage.setOnClickListener {
            openGalleryForImage()
        }
    }

    private fun openGalleryForImage() {
        var intent = Intent(Intent.ACTION_PICK)
        intent.type ="image/*"
        startActivityForResult(intent,10)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 10 && resultCode == Activity.RESULT_OK){
            binding.ivAddImage.setImageURI(data?.data)
            lifecycleScope.launch(Dispatchers.IO){

                imagePicker = data?.data?.let {
                    addProductViewModel.getImageLocalFile(this@AddProductPage_Activity,it)

                }
//                Log.i("AAAAA"," Image Picker : ${imagePicker.toString()}")
            }
        }
    }
}