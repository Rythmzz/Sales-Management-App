package com.tm968.shoppev4.page.presentation.home

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.tm968.shoppev4.databinding.HomepageFragmentBinding
import com.tm968.shoppev4.page.data.model.Product
import com.tm968.shoppev4.page.presentation.product.ProductPage_Activity
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomePage_Fragment : Fragment() {
    private lateinit var binding:HomepageFragmentBinding
    private lateinit var imageList: MutableList<String>
    private lateinit var adapterSale: HomePageTopSaleAdapter
    private lateinit var adapterTopSearch: HomePageTopSearchAdapter
    private val homeViewModel:HomePageViewModel by viewModel()
    private var firstThread: Job? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = HomepageFragmentBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setSlideImage()
        setRecyclerView()
        interactInside()
//        interactOutside()


    }

    private fun interactOutside() {
        TODO("Not yet implemented")
    }

    private fun interactInside() {
        firstThread = lifecycleScope.launchWhenStarted {
            this.launch {
                homeViewModel.ProductList.collect{
                    adapterSale.refreshData(it.toMutableList())

                }
            }
            this.launch {
                homeViewModel.ProductListbyView.collect{
                    var x = it.toMutableList()
                    adapterTopSearch.refreshData(x.subList(0,6) as MutableList<Product>)

                }
            }





        }
    }

    private fun setRecyclerView() {
        val layoutManager = LinearLayoutManager(context)
        layoutManager.orientation = LinearLayoutManager.HORIZONTAL

        val layoutManager1 = LinearLayoutManager(context)
        layoutManager1.orientation = LinearLayoutManager.HORIZONTAL

        adapterSale = HomePageTopSaleAdapter(mutableListOf())
        binding.rvProductSale.adapter = adapterSale
        binding.rvProductSale.layoutManager = layoutManager

        adapterTopSearch = HomePageTopSearchAdapter(mutableListOf())
        binding.rvProductViewSearch.adapter = adapterTopSearch
        binding.rvProductViewSearch.layoutManager=layoutManager1


        adapterSale.onClick = {
            var intent = Intent(context,ProductPage_Activity::class.java)
            intent.putExtra("DATA",it)
            startActivity(intent)
        }
        adapterTopSearch.onClick = {
            var intent = Intent(context,ProductPage_Activity::class.java)
            intent.putExtra("DATA",it)
            startActivity(intent)
        }

    }

    private fun setSlideImage() {
        imageList = mutableListOf()
        imageList.add("https://image-us.eva.vn/upload/2-2021/images/2021-06-04/mua-sam-tren-shopee-voi-don-tri-gia-hang-trieu-nhung-chi-thanh-toan-hang-tram-nghin-1-1622807901-631-width600height348.jpg")
        imageList.add("https://cf.shopee.vn/file/cb405c9036ea39ddfbe9b5844a5b0603")
        imageList.add("https://i.ytimg.com/vi/xw2byVg8ehE/maxresdefault.jpg")
        imageList.add("https://magiamgialientuc.com/wp-content/uploads/2019/11/shopee-ng%C3%A0y-h%E1%BB%99i-mua-s%E1%BA%AFm-11-11-l%E1%BB%9Bn-nh%E1%BA%A5t-n%C4%83m.png")
        imageList.add("https://media1.nguoiduatin.vn/media/vuong-thi-thao/2017/11/10/anh-2---a.png")

        binding.isImageSlider.setSliderAdapter(SliderAdapter(imageList))
        binding.isImageSlider.startAutoCycle()

        homeViewModel.getProductList()
        homeViewModel.getProductListShowView()



    }

}