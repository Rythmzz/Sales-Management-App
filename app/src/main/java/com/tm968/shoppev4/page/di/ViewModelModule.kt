package com.tm968.shoppev4.page.di

import com.tm968.shoppev4.page.presentation.order.OrderHistoryViewModel
import com.tm968.shoppev4.page.presentation.cart.CartPageViewModel
import com.tm968.shoppev4.page.presentation.home.HomePageViewModel
import com.tm968.shoppev4.page.presentation.login.LoginPageViewModel
import com.tm968.shoppev4.page.presentation.product.AddProductViewModel
import com.tm968.shoppev4.page.presentation.product.AllProductPageViewModel
import com.tm968.shoppev4.page.presentation.product.EditProductViewModel
import com.tm968.shoppev4.page.presentation.register.RegisterPageViewModel
import com.tm968.shoppev4.page.presentation.search.SearchPageViewModel
import com.tm968.shoppev4.page.presentation.user.UserPageViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel {
        LoginPageViewModel(get())
    }
    viewModel {
        RegisterPageViewModel(get())
    }
    viewModel{
        UserPageViewModel(get())
    }
    viewModel {
        HomePageViewModel(get())
    }
    viewModel{
        SearchPageViewModel(get())
    }
    viewModel {
        AllProductPageViewModel(get())
    }
    viewModel{
        CartPageViewModel(get())
    }
    viewModel {
        OrderHistoryViewModel(get())
    }
    viewModel{
        AddProductViewModel(get())
    }
    viewModel{
        EditProductViewModel(get())
    }
}