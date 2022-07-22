package com.tm968.shoppev4.page.presentation.cart

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.tm968.shoppev4.R
import com.tm968.shoppev4.databinding.CartpageActivityBinding
import com.tm968.shoppev4.page.data.local.AppPreferences
import com.tm968.shoppev4.page.data.model.Cart
import com.tm968.shoppev4.page.data.model.OrderDetailRequest
import com.tm968.shoppev4.page.data.model.OrderRequest
import kotlinx.android.synthetic.main.cartpage_activity.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*

class CartPage_Activity:AppCompatActivity() {
    private lateinit var binding:CartpageActivityBinding
    private val mSecurePreferences:AppPreferences by inject()
    private var listProductCart = mutableListOf<Cart>()
    private var FirstThread: Job? = null
    private var TotalPrice = 0.0
    private var TotalSavePrice = 0.0
    private val CartPageViewModel:CartPageViewModel by viewModel()
    private lateinit var adapter:CartPageAdapter

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = CartpageActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initData()
//        setRecyclerView()
        setupView()
        interactInside()
        interactOutside()


    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun setupView() {

        for (i in 0 until listProductCart.size){
            var k = i + 1
            TotalPrice += when(listProductCart[i].product.sale_price){
                0 -> {listProductCart[i].product.price}
                else -> {listProductCart[i].product.sale_price}
            }
            TotalSavePrice += when(listProductCart[i].product.sale_price){
                0 -> {0}
                else -> {listProductCart[i].product.price - listProductCart[i].product.sale_price}
            }
            for ( j in k until listProductCart.size){
                if ( k >= listProductCart.size){
                    break
                }
                if (listProductCart[i].check == true){
                    continue
                }
                if ( listProductCart[i].product.id == listProductCart[j].product.id){
                    listProductCart[i].count = listProductCart[i].count.plus(1)
                    listProductCart[j].check = true
                }

            }


        }
//        Log.i("AAAA","List Product Cart : ${listProductCart.size.toString()}")
        listProductCart.removeIf { it.check == true }
        adapter = CartPageAdapter(listProductCart)
        binding.rvCart.adapter = adapter
        binding.rvCart.layoutManager = LinearLayoutManager(this@CartPage_Activity)
        val totalPrice = String.format("%,d", TotalPrice.toInt()).replace(',', ' ')
        val totalSavePrice = String.format("%,d", TotalSavePrice.toInt()).replace(',', ' ')

        binding.tvTotalPrice.text = "₫${totalPrice}"
        binding.tvTotalSavePrice.text="₫${totalSavePrice}"

        adapter.onChange = { id, isPlus, position ->
            adapter.changeCount(id, isPlus, position)

            if (isPlus){
                    TotalPrice += if(listProductCart[position].product.sale_price == 0){
                        listProductCart[position].product.price
                    }
                else{
                    listProductCart[position].product.sale_price
                    }
                TotalSavePrice += if(listProductCart[position].product.sale_price == 0){
                    0}
                else {
                    listProductCart[position].product.price - listProductCart[position].product.sale_price
                }


                }
            else {
                TotalPrice -= if(listProductCart[position].product.sale_price == 0){
                    listProductCart[position].product.price
                }
                else{
                    listProductCart[position].product.sale_price
                }
                TotalSavePrice -= if(listProductCart[position].product.sale_price == 0){
                    0}
                else {
                    listProductCart[position].product.price - listProductCart[position].product.sale_price
                }
            }
            val  totalPrice = String.format("%,d", TotalPrice.toInt()).replace(',', ' ')
            val  totalSavePrice = String.format("%,d", TotalSavePrice.toInt()).replace(',', ' ')
            binding.tvTotalPrice.text = "₫${totalPrice}"
            binding.tvTotalSavePrice.text="₫${totalSavePrice}"


            mSecurePreferences.clearCart()
            for (i in 0 until listProductCart.size){
                for (j in 0 until listProductCart[i].count){
                    mSecurePreferences.addProductInCart(listProductCart[i].product)
                }
            }

//            adapter.updateCart = {
//                Log.i("AAAAA","Da Vo Ham Nay")
//                mSecurePreferences.clearCart()
//                for (i in 0 until it.size){
//                    Log.i("AAAA","Gia Tri Hien Tai : $i")
//                    for (j in 0 until it[i].count){
//                        Log.i("AAAA","Gia Tri Hien Tai : ${it[i].count} la $j")
//                        mSecurePreferences.addProductInCart(it[i].product)
//                    }
//                }
//            }
        }

        adapter.updateCart = {
        if (it.size == 0){
            mSecurePreferences.clearCart()
            binding.csNotCart.visibility=View.VISIBLE
            binding.rvCart.visibility=View.GONE
            binding.csPaying.visibility=View.GONE
        }
        }
    }

    private fun interactOutside() {
        binding.btnShopNow.setOnClickListener {
            onBackPressed()
        }
        binding.btnBuy.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            val inflater = layoutInflater
            val dialogLayout = inflater.inflate(R.layout.address_layout, null)
            val etAddress = dialogLayout.findViewById<EditText>(R.id.etAddress)

            with(builder) {
                setTitle("Enter Information")
                setPositiveButton("OK") { dialog, which ->
                    when {
                        etAddress.text.isEmpty() -> {
                            etAddress.error = "Please fill out field!"
                        }
                        else -> {
                            if (listProductCart.size != 0) {
                                val orderDate = Date().time.toString()
                                val userId = mSecurePreferences.getUser().id
                                val listOrderRequestDetail = mutableListOf<OrderDetailRequest>()
                                listProductCart.forEach { element ->
                                    listOrderRequestDetail.add(
                                        OrderDetailRequest(
                                            element.product.id,
                                            element.count
                                        )
                                    )
                                }
//                                Log.i("AAAA", "$userId")
                                if (userId != null) {
                                    val orderRequest = OrderRequest(
                                        userId,
                                        orderDate,
                                        etAddress.text.toString(),
                                        "",
                                        listOrderRequestDetail
                                    )
                                    CartPageViewModel.createOrder(orderRequest)
                                }
                            }
                        }

                    }
                }

                setNegativeButton("Cancel") { dialog, which ->
                    dialog.cancel()
                }
                setView(dialogLayout).show()
            }
        }

        }


    private fun interactInside() {
        FirstThread = lifecycleScope.launchWhenStarted {

            if (mSecurePreferences.getListProductInCart() != null) {
                binding.rvCart.visibility = View.VISIBLE
                binding.csNotCart.visibility = View.GONE
                adapter.refreshData(listProductCart.toMutableList())
                binding.csPaying.visibility = View.VISIBLE
            } else {
                binding.csPaying.visibility = View.GONE
                binding.rvCart.visibility = View.GONE
                binding.csNotCart.visibility = View.VISIBLE
            }





            this.launch {
                CartPageViewModel.success.collect{
                        Toast.makeText(this@CartPage_Activity,"Buy Success! Please wait for your order",Toast.LENGTH_LONG).show()
                    mSecurePreferences.clearCart()
                    finish();
//                    overridePendingTransition(0, 0);
//                    startActivity(getIntent());
//                    overridePendingTransition(0, 0);

                }
            }
            this.launch {
                CartPageViewModel.loading.collect{
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



        }}


    private fun setRecyclerView() {

        adapter = CartPageAdapter(mutableListOf())
        binding.rvCart.adapter = adapter
        binding.rvCart.layoutManager = LinearLayoutManager(this@CartPage_Activity)


    }

    private fun initData() {
        mSecurePreferences.getListProductInCart()?.let {
            for ( i in it){
                listProductCart.add(Cart(1,i,false))
            }
        }
    }
}