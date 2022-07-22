package com.tm968.shoppev4.page.presentation.search

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.tm968.shoppev4.R
import com.tm968.shoppev4.databinding.Searchpage2ChildfragmentBinding
import com.tm968.shoppev4.page.presentation.home.HomePageTopSaleAdapter
import com.tm968.shoppev4.page.presentation.product.ProductPage_Activity
import com.tm968.shoppev4.page.utils.Keyboard
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel


class SearchPage2_ChildFragment : Fragment() {
    private lateinit var binding:Searchpage2ChildfragmentBinding
    private lateinit var keyboard: Keyboard
    private lateinit var adapterSearch: SearchPageAdapter
    private var FirstThread:Job? = null
    private val searchViewModel:SearchPageViewModel by viewModel()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = Searchpage2ChildfragmentBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setRecyclerView()
        interactOutside()
        interactInside()
    }

    private fun setRecyclerView() {
        val layoutManager = LinearLayoutManager(context)
        layoutManager.orientation = LinearLayoutManager.VERTICAL

        adapterSearch = SearchPageAdapter(mutableListOf())
        binding.rvProductSearch.adapter = adapterSearch
        binding.rvProductSearch.layoutManager = layoutManager

        adapterSearch.onClick = {
            var intent = Intent(context,ProductPage_Activity::class.java)
            intent.putExtra("DATA",it)
            binding.etSearch.hideKeyboard()
            startActivity(intent)
        }
    }

    private fun interactInside() {
        FirstThread = lifecycleScope.launchWhenStarted {
            this.launch {
                searchViewModel.searchtype.collect{
                    adapterSearch.refreshData(it.toMutableList())
                }
            }
        }
    }

    private fun interactOutside() {


        binding.etSearch.addTextChangedListener {
            searchViewModel.SearchProduct(it.toString())
        }


       binding.ivBack.setOnClickListener {
           
           val tabmenu:TabLayout = requireActivity().findViewById(R.id.tabMenu)
           val headbar:ConstraintLayout = requireActivity().findViewById(R.id.csHeaderbar)
           val viewPager:ViewPager = requireActivity().findViewById(R.id.viewPagerSearch)
            
           binding.etSearch.hideKeyboard()

           tabmenu.visibility = View.VISIBLE
           headbar.visibility = View.VISIBLE
           binding.csHeaderbarSearch.visibility = View.GONE

           viewPager.setCurrentItem(0,true)


       }
    }
        private fun View.hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }



}