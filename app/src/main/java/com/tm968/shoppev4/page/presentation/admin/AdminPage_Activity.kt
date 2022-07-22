package com.tm968.shoppev4.page.presentation.admin

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.tabs.TabLayout
import com.tm968.shoppev4.R
import com.tm968.shoppev4.databinding.AdminpageActivityBinding
import com.tm968.shoppev4.page.presentation.main.ViewPagerAdapter
import com.tm968.shoppev4.page.presentation.order.OrderHistoryPage_Fragment
import com.tm968.shoppev4.page.presentation.product.ProductSettingPage_Fragment

class AdminPage_Activity : AppCompatActivity(){
    private lateinit var binding:AdminpageActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = AdminpageActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpTabs()
        setView()
        interactOutside()
    }

    private fun interactOutside() {
        binding.ivBack.setOnClickListener {
            onBackPressed()
        }
        binding.tabMenu.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (tab != null) {
                    if (tab.position == 0) {
                        binding.tvTitle.text = "Order Management"
                    } else if (tab.position == 1) {
                        binding.tvTitle.text = "Setting Product"
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }


    })
    }
    private fun setView() {
        var intent:Intent = getIntent()
        val x =  intent.getBooleanExtra("data",false)
        if (x){
            binding.viewPagerAdmin.setCurrentItem(1,true)
            binding.tvTitle.text = "Setting Product"
        }
        else {
            binding.tvTitle.text = "Order Management"
        }
    }

    private fun setUpTabs() {
        val adapter = ViewPagerAdapter(supportFragmentManager)

        adapter.addFragment(OrderHistoryPage_Fragment(),"History")
        adapter.addFragment(ProductSettingPage_Fragment(),"Setting")

        binding.viewPagerAdmin.adapter = adapter
        binding.tabMenu.setupWithViewPager(binding.viewPagerAdmin)

        binding.tabMenu.getTabAt(0)!!.setIcon(R.drawable.tmorderlist40)
        binding.tabMenu.getTabAt(1)!!.setIcon(R.drawable.tmeditproduct40)

    }
}