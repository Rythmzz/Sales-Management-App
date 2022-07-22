package com.tm968.shoppev4.page.presentation.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.tm968.shoppev4.databinding.LoginpageActivityBinding
import com.tm968.shoppev4.page.data.local.AppPreferences
import com.tm968.shoppev4.page.presentation.main.MainPage_Activity
import com.tm968.shoppev4.page.presentation.register.RegisterPage_Activity
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginPage_Activity : AppCompatActivity() {
    private lateinit var binding:LoginpageActivityBinding
    private val loginViewModel: LoginPageViewModel by viewModel()
    private var FirstThread:Job? = null
    private val mSecurePreferences :AppPreferences by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = LoginpageActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        this.supportActionBar?.hide()
        Log.d("AAA",mSecurePreferences.getToken().toString())
        // call Api -> Hung Du Lieu Api -> Luu tru data cua login
        InteractInside()
        // Set Event
        interactOutside()
    }
    private fun InteractInside() {
       FirstThread = lifecycleScope.launchWhenStarted {
            this.launch {
                loginViewModel.success.collect{ result ->
                    if (result is Boolean && result){
//                        Toast.makeText(this@LoginPage_Activity,loginViewModel.user.toString(),Toast.LENGTH_LONG).show()
                            loginViewModel.user?.let { mSecurePreferences.setToken(it.accessToken) }
                            loginViewModel.user?.let { mSecurePreferences.setUserInfo(it) }
                        binding.ivMask.visibility = View.GONE
                        binding.progressLoading.visibility = View.GONE
                        var intent = Intent(this@LoginPage_Activity,MainPage_Activity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        startActivity(intent)
                        finish()
                    }
                    else {
                        Toast.makeText(this@LoginPage_Activity,result.toString(),Toast.LENGTH_LONG).show()
                    }
                }
            }
           this.launch {
               loginViewModel.loading.collect{
                   if (it as Boolean && it){
                       binding.ivMask.visibility = View.VISIBLE
                       binding.progressLoading.visibility = View.VISIBLE
                   }
                   else{
                       Toast.makeText(this@LoginPage_Activity,"Login failed !",Toast.LENGTH_LONG).show()
                       binding.ivMask.visibility = View.GONE
                       binding.progressLoading.visibility = View.GONE
                   }
               }
           }

       }
    }

    private fun interactOutside() {
        binding.tvRegister.setOnClickListener {
            var intent = Intent(this,RegisterPage_Activity::class.java)
            startActivity(intent)
        }


        binding.btnLogin.setOnClickListener {
            val account = binding.etAccount.text.toString().trim()
            val password = binding.etPassword.text.toString()
            when {
                account.isEmpty() -> {
                    binding.etAccount.error = "Account is not valid"
                }
                password.isEmpty() -> {
                    binding.etPassword.error = "Password is not valid"
                }
                else ->{
                    loginViewModel.login(account,password)
                }

            }
        }


    }
}