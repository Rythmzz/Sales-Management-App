package com.tm968.shoppev4.page.presentation.register

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.tm968.shoppev4.databinding.RegisterpageActivityBinding
import com.tm968.shoppev4.page.presentation.main.ViewPagerAdapter
import com.tm968.shoppev4.page.utils.CheckValidData
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class RegisterPage_Activity : AppCompatActivity() {
    private lateinit var binding:RegisterpageActivityBinding
    private val registerViewModel:RegisterPageViewModel by viewModel()
    private var FirstThread: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = RegisterpageActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        this.supportActionBar?.hide()
        InteractInside()
        InteractOutside()
    }

    private fun InteractOutside() {
        binding.ivBack.setOnClickListener{
            onBackPressed()
        }
        binding.btnRegister.setOnClickListener {
            val fullName = binding.etFullName.text.toString()
            val account = binding.etAccount.text.toString()
            val password = binding.etPassword.text.toString()
            val cfpassword = binding.etConfirmPassword.text.toString()
            val email = binding.etEmail.text.toString().trim()
            val phone = binding.etPhoneNumber.text.toString()
            if (fullName.isEmpty() ||
                account.isEmpty() ||
                password.isEmpty() ||
                cfpassword.isEmpty() ||
                email.isEmpty() ||
                phone.isEmpty() )
            {
                Toast.makeText(this,"Please fill out all field.",Toast.LENGTH_LONG).show()
            }
            else if (password != cfpassword){
                binding.etConfirmPassword.error = "Please make sure your passwords match."
            }
            else if (!CheckValidData.isEmailValid(email)){
                binding.etEmail.error = "Please use a valid email address."
            }
            else if (CheckValidData.isEmailValid(email)
                && password == cfpassword
                && !fullName.isEmpty()
                && !account.isEmpty()
                && !phone.isEmpty()){
                registerViewModel.register(account,password,email,phone,fullName)
            }
        }
    }

    private fun InteractInside() {
        FirstThread = lifecycleScope.launchWhenStarted {
            this.launch {
                registerViewModel.success.collect { result ->
                    if (result is Boolean && result){
                    Toast.makeText(this@RegisterPage_Activity,"Register Success",Toast.LENGTH_LONG).show()
                    delay(3000L)
                    onBackPressed()
                    }
                    else {
                        Toast.makeText(this@RegisterPage_Activity,result.toString(),Toast.LENGTH_LONG).show()
                    }
                }

            }

            this.launch {
                registerViewModel.loading.collect {
                    if (it is Boolean && it){
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
}