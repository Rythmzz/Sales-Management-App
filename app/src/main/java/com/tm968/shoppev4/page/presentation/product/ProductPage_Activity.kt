package com.tm968.shoppev4.page.presentation.product

import android.content.Intent
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.securepreferences.SecurePreferences
import com.tm968.shoppev4.BuildConfig
import com.tm968.shoppev4.R
import com.tm968.shoppev4.databinding.ProductpageActivityBinding
import com.tm968.shoppev4.page.data.local.AppPreferences
import com.tm968.shoppev4.page.data.model.Product
import com.tm968.shoppev4.page.presentation.cart.CartPageViewModel
import com.tm968.shoppev4.page.presentation.cart.CartPage_Activity
import com.tm968.shoppev4.page.presentation.main.MainPage_Activity
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.math.RoundingMode

class ProductPage_Activity:AppCompatActivity() {
    private val mSecurePreferences:AppPreferences by inject()
    private lateinit var binding:ProductpageActivityBinding
    private var data:Product? = null
    private var count:Int = 1
    private val editviewProduct:EditProductViewModel by viewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ProductpageActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initData()
        setupView()
        interactOutside()

    }



    private fun interactOutside() {

        binding.ivBack.setOnClickListener {
            onBackPressed()
        }
        binding.ivPlus.setOnClickListener {
            if (count == 1 ){
                binding.ivMinius.visibility = View.VISIBLE
            }
            count++
            binding.tvNumberProduct.text = count.toString()
        }
        binding.ivMinius.setOnClickListener {
                count--
                binding.tvNumberProduct.text = count.toString()

           if (count == 1){
               binding.ivMinius.visibility = View.GONE
           }
        }
        binding.ivAddCart.setOnClickListener {
            for (i in 0 until count) {
                data?.let { it1 -> mSecurePreferences.addProductInCart(it1) }
            }
            Toast.makeText(this@ProductPage_Activity,"Add Cart Success !",Toast.LENGTH_LONG).show()
            finish()
        }
        binding.btnBuyNow.setOnClickListener {
            for (i in 0 until count) {
                data?.let { it1 -> mSecurePreferences.addProductInCart(it1) }
            }
            var intent = Intent(this@ProductPage_Activity,CartPage_Activity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
            finish()


        }
    }

    private fun setupView() {
        binding.ivMinius.visibility = View.GONE
        if (data?.sale_price == 0){
            binding.tvMoneyStrike.visibility = View.GONE
//            binding.csSale.visibility=View.GONE

            val s = String.format("%,d", data?.price).replace(',', ' ')
            binding.tvMoneySale.text = "₫${s}"
        }
        else {
//            val salePercent = ((data?.sale_price!!.toDouble()*100)/ data?.price!!.toDouble())
//            binding.tvPercentProduct.text = "${100 - ((salePercent.toBigDecimal().setScale(0,
//                RoundingMode.UP).toDouble())).toInt()}"
            val sp = String.format("%,d",data?.sale_price).replace(',', ' ')
            binding.tvMoneySale.text = "₫${sp}"
            val p = String.format("%,d",data?.price).replace(',', ' ')
            binding.tvMoneyStrike.text = "₫${p}"
        }
        binding.tvNameProduct.text = data?.name.toString()
        binding.tvDescriptionProduct.text=data?.description.toString()
        binding.tvDescriptionProduct.movementMethod = ScrollingMovementMethod()
        Glide.with(binding.ivProductImage).load(BuildConfig.BASE_URL+"getImage/Products/"+data?.image_url).centerCrop().into(binding.ivProductImage)

    }

    private fun initData() {
        data = intent.getParcelableExtra("DATA")
        editviewProduct.editProduct(data!!.id,
            data!!.name,data!!.image_url,data!!.price.toDouble(),data!!.sale_price.toDouble(),data!!.categoryId,data!!.view+1,data!!.description)
    }

}