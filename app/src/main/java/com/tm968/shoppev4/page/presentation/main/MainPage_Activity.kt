package com.tm968.shoppev4.page.presentation.main

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.lifecycleScope
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.tm968.shoppev4.R
import com.tm968.shoppev4.databinding.MainheaderNavBinding
import com.tm968.shoppev4.databinding.MainpageActivityBinding
import com.tm968.shoppev4.page.data.local.AppPreferences
import com.tm968.shoppev4.page.presentation.admin.AdminPage_Activity
import com.tm968.shoppev4.page.presentation.cart.CartPage_Activity
import com.tm968.shoppev4.page.presentation.home.HomePage_Fragment
import com.tm968.shoppev4.page.presentation.login.LoginPage_Activity
import com.tm968.shoppev4.page.presentation.product.AddProductPage_Activity
import com.tm968.shoppev4.page.presentation.product.AllProductPage_Activity
import com.tm968.shoppev4.page.presentation.product.EditProductPage_Activity
import com.tm968.shoppev4.page.presentation.product.ProductSettingPage_Fragment
import com.tm968.shoppev4.page.presentation.search.SearchPage_Fragment
import com.tm968.shoppev4.page.presentation.user.UserPageViewModel
import com.tm968.shoppev4.page.presentation.user.UserPage_Fragment
import com.tm968.shoppev4.page.utils.Keyboard
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainPage_Activity : AppCompatActivity() {
    private lateinit var binding: MainpageActivityBinding
    private val mSecurePreferences:AppPreferences by inject()
    private var FirstThread:Job? = null
    private val LogoutViewModel:UserPageViewModel by viewModel()
    private lateinit var keyboard: Keyboard
    private lateinit var adapter:ViewPagerAdapter
    private lateinit var toggle : ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainpageActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        firstLogin()
        interactInside()
        interactOutside()
    }

    override fun onResume() {
        super.onResume()
        val tvNumberCart: TextView = findViewById(R.id.tvNumberCart)
        val csCartNumber: ConstraintLayout = findViewById(R.id.csCartNumber)
        if (mSecurePreferences.getListProductInCart() == null) {
            csCartNumber.visibility = View.GONE
        } else {
            val count = mSecurePreferences.getListProductInCart()
            csCartNumber.visibility = View.VISIBLE
            tvNumberCart.text = count?.size.toString()
        }
        binding.drawerlayout.closeDrawer(binding.navLeftview)
    }
    private fun firstLogin() {

        val token = mSecurePreferences.getToken()
        if (token == null) {
            var intent: Intent = Intent(this@MainPage_Activity, LoginPage_Activity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
            finish()
        }
    }
    private fun setUpDrawer() {
        // Set Keo tha Ngan Keo
        toggle = ActionBarDrawerToggle(this,binding.drawerlayout,R.string.open,R.string.close)
        binding.drawerlayout.addDrawerListener(toggle)
        toggle.syncState()


        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Set Thong tin user
        val headerView:View = binding.navLeftview.getHeaderView(0)
        val headerBinding: MainheaderNavBinding = MainheaderNavBinding.bind(headerView)

        headerBinding.tvFullname.text = mSecurePreferences.getUser().fullName
        headerBinding.tvEmail.text = mSecurePreferences.getUser().email

        if (mSecurePreferences.getUser().userName.toString() != "admin"){
            binding.navLeftview.menu.findItem(R.id.nav_manageradmin).setVisible(false)}






        // Set Lang nghe su kien cua Nav_LeftView
        binding.navLeftview.setNavigationItemSelectedListener {
            when(it.itemId){
                R.id.nav_AllProduct -> {
                    var intent = Intent(this,AllProductPage_Activity::class.java)
                    startActivity(intent)
                }
                R.id.nav_MyCart -> {
                    var intent = Intent(this, CartPage_Activity::class.java)
                    startActivity(intent)
                }
                R.id.nav_Logout -> {
                    val dgAlert = AlertDialog.Builder(this@MainPage_Activity)
                    dgAlert.setTitle("Logout")
                        .setMessage("Are you sure you want to log out?")
                        .setCancelable(true)
                        .setPositiveButton("Yes", DialogInterface.OnClickListener { dialog, which ->
                            LogoutViewModel.logout()
                        }).setNegativeButton("No", DialogInterface.OnClickListener { dialog, which ->
                            dialog.cancel()
                        }).show()
                }
                R.id.nav_AddProduct -> {
                    var intent = Intent(this,AddProductPage_Activity::class.java)
                    intent.putExtra("data",true)
                    startActivity(intent)
                }
                R.id.nav_EditProduct -> {
                    intent = Intent(this,AdminPage_Activity::class.java)
                    intent.putExtra("data",true)
                    startActivity(intent)
                }
                R.id.nav_ManageOrder -> {
                    intent = Intent(this,AdminPage_Activity::class.java)
                    startActivity(intent)
                }
            }
            true
        }
    }

    private fun setUpTabs() {
        adapter = ViewPagerAdapter(supportFragmentManager)
        adapter.addFragment(HomePage_Fragment(),"Home")
        adapter.addFragment(SearchPage_Fragment(),"Search")
        adapter.addFragment(UserPage_Fragment(),"User")

        binding.viewPager.adapter = adapter
        binding.tabMenu.setupWithViewPager(binding.viewPager)

        binding.tabMenu.getTabAt(0)!!.setIcon(R.drawable.tmhome40)
        binding.tabMenu.getTabAt(1)!!.setIcon(R.drawable.tmsearch40)
        binding.tabMenu.getTabAt(2)!!.setIcon(R.drawable.tmuser64)



    }

    private fun interactInside() {
        FirstThread = lifecycleScope.launchWhenStarted {
            binding.csLoading2.visibility = View.VISIBLE
            binding.tabMenu.visibility = View.GONE
            delay(2000L)
            binding.tabMenu.visibility = View.VISIBLE
            binding.csLoading2.visibility = View.GONE
            setUpTabs()
            setUpDrawer()
            val tvNumberCart: TextView = findViewById(R.id.tvNumberCart)
            val csCartNumber: ConstraintLayout = findViewById(R.id.csCartNumber)
            if (mSecurePreferences.getListProductInCart() == null) {
                csCartNumber.visibility = View.GONE
            } else {
                val count = mSecurePreferences.getListProductInCart()
                csCartNumber.visibility = View.VISIBLE
                tvNumberCart.text = count?.size.toString()
            }




            this.launch {
                LogoutViewModel.success.collect(){
                    if (it as Boolean && it){
                        mSecurePreferences.ClearAllData()
                        var intent = Intent(this@MainPage_Activity,LoginPage_Activity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        startActivity(intent)
                        finish()
                    }
                }
            }

        }
    }

    private fun interactOutside() {
        keyboard=Keyboard(this@MainPage_Activity)

        binding.tabMenu.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (tab != null) {
                    if (tab.position ==  0){
                        binding.csHeaderbar.visibility = View.VISIBLE
                        binding.ivLogo.visibility = View.VISIBLE
                        binding.svSearch.visibility = View.GONE
                    }
                    else if (tab.position == 1){
                        binding.csHeaderbar.visibility = View.VISIBLE
                        binding.ivLogo.visibility = View.GONE
                        binding.svSearch.visibility = View.VISIBLE
                    }
                    else if (tab.position == 2){
                        binding.csHeaderbar.visibility = View.GONE
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

        })
        binding.svSearch.setOnClickListener {

            binding.tabMenu.visibility=View.GONE
            binding.csHeaderbar.visibility=View.GONE
            keyboard.hideSoftKeyboard(this@MainPage_Activity)
            val csHeaderBar:ConstraintLayout = findViewById(R.id.csHeaderbarSearch)
            csHeaderBar.visibility = View.VISIBLE
            val viewpager:ViewPager = findViewById(R.id.viewPagerSearch)
            viewpager.setCurrentItem(1,true)
            val etSearch: EditText = findViewById(R.id.etSearch)
            etSearch.requestFocus()
            etSearch.showKeyboard()


        }
        binding.svSearch.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus){
                binding.tabMenu.visibility=View.GONE
                binding.csHeaderbar.visibility=View.GONE
                keyboard.hideSoftKeyboard(this@MainPage_Activity)
                val csHeaderBar:ConstraintLayout = findViewById(R.id.csHeaderbarSearch)
                csHeaderBar.visibility = View.VISIBLE
                val viewpager:ViewPager = findViewById(R.id.viewPagerSearch)
                viewpager.setCurrentItem(1,true)
                val etSearch: EditText = findViewById(R.id.etSearch)
                etSearch.requestFocus()
                etSearch.showKeyboard()

            }


        }
        binding.ivCart.setOnClickListener {
            var intent = Intent(this@MainPage_Activity,CartPage_Activity::class.java)
            startActivity(intent)
        }


    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)){
            return true
        }
        return super.onOptionsItemSelected(item)
    }
    private fun View.showKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
    }
}


//    private fun View.hideKeyboard() {
//        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
//        imm.hideSoftInputFromWindow(windowToken, 0)
//    }
