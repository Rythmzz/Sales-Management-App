package com.tm968.shoppev4.page.presentation.search

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.google.android.material.tabs.TabLayout
import com.tm968.shoppev4.R
import com.tm968.shoppev4.databinding.SearchpageFragmentBinding
import com.tm968.shoppev4.page.presentation.main.ViewPagerAdapter
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchPage_Fragment : Fragment() {

    private lateinit var binding:SearchpageFragmentBinding
    private val SearchViewModel :SearchPageViewModel by viewModel()
    private var FirstThread:Job? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = SearchpageFragmentBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setViewPager()
//        interactInside()
//        interactOutside()


    }

    private fun interactOutside() {

    }

    private fun interactInside() {
        FirstThread = lifecycleScope.launchWhenStarted {
            this.launch {
                SearchViewModel.searchtype.collect{

                }
            }
        }
    }

    private fun setViewPager() {
        val adapter = ViewPagerAdapter(childFragmentManager)
        adapter.addFragment(SearchPage1_ChildFragment(),"Search 1")
        adapter.addFragment(SearchPage2_ChildFragment(),"Search 2")
        binding.viewPagerSearch.adapter = adapter



    }

}