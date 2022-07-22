package com.tm968.shoppev4.page.presentation.user

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import com.tm968.shoppev4.R
import com.tm968.shoppev4.databinding.UserpageFragmentBinding
import com.tm968.shoppev4.page.data.local.AppPreferences
import com.tm968.shoppev4.page.presentation.admin.AdminPage_Activity
import org.koin.android.ext.android.inject

class UserPage_Fragment : Fragment() {

    private val mSecurePreferences:AppPreferences by inject()
    private lateinit var binding:UserpageFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = UserpageFragmentBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setInitData()
        interactOutside()



    }

    private fun interactOutside() {
        binding.ivAdminMode.setOnClickListener {
            var intent = Intent(requireActivity(),AdminPage_Activity::class.java)
            startActivity(intent)
        }



//        val builder = AlertDialog.Builder(requireActivity())
//        val inflater = layoutInflater
//        val dialogLayout = inflater.inflate(R.layout.address_layout, null)
//        val editText = dialogLayout.findViewById<EditText>(R.id.etAddress)
//
//        binding.ivEditName.setOnClickListener {
//            with(builder) {
//                setTitle("Enter Information")
//                setPositiveButton("OK") { dialog, which ->
//                    when {
//                        editText.text.isEmpty() -> {
//                            editText.error = "Please fill out field!"
//                        }
//                        else -> {
//                            binding.tvName.text = editText.text
//                        }
//                    }
//
//
//                }
//                setNegativeButton("Cancel"){
//                    dialog,which-> dialog.cancel()
//                }
//                setView(dialogLayout)
//                show()
//            }
//        }
//        binding.ivEditBio.setOnClickListener {
//            with(builder) {
//                setTitle("Enter Information")
//                setPositiveButton("OK") { dialog, which ->
//                    when {
//                        editText.text.isEmpty() -> {
//                            editText.error = "Please fill out field!"
//                        }
//                        else -> {
//                            binding.tvBio.text = editText.text
//                        }
//                    }
//
//
//                }
//                setNegativeButton("Cancel"){
//                        dialog,which-> dialog.cancel()
//                }
//                setView(dialogLayout)
//                show()
//            }
//        }
    }

    private fun setInitData() {
        if (mSecurePreferences.getUser().userName.toString() != "admin"){
            binding.mdAdminMode.visibility = View.GONE
            binding.csAdminMode.visibility = View.GONE
        }
        binding.tvUsernameTitle.text = mSecurePreferences.getUser().fullName
        binding.tvUsernameTitle.isAllCaps = true
        binding.tvUsername.text = mSecurePreferences.getUser().userName
        binding.tvPermission.text = when(mSecurePreferences.getUser().userName){
            "admin" -> "Admin"
            else -> "Customer"
        }
        binding.tvName.text = mSecurePreferences.getUser().fullName
        binding.tvBio.text = "Set up now"
        binding.tvSex.text = "Set up now"
        binding.tvPhone.text = mSecurePreferences.getUser().phoneNumber
        binding.tvEmail.text = mSecurePreferences.getUser().email

    }

}