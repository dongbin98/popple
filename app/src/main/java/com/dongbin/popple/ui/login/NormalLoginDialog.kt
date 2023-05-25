package com.dongbin.popple.ui.login

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.dongbin.popple.data.api.provideLoginApi
import com.dongbin.popple.databinding.DialogLoginBinding

class NormalLoginDialog(context: Context, private val loginActivity: LoginActivity) :
    Dialog(context) {
    private lateinit var binding: DialogLoginBinding
    private lateinit var viewModel: LoginViewModel
    // private val disposable = AutoClearedDisposable(loginActivity)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DialogLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(
            loginActivity,
            LoginViewModelFactory(provideLoginApi())
        )[LoginViewModel::class.java]
        initDialog()

        binding.btLoginDialog.setOnClickListener {
            viewModel.login(
                binding.etLoginDialogId.text.toString(),
                binding.etLoginDialogPassword.text.toString()
            )
        }

        viewModel.loginResponse.observe(loginActivity) {
            Toast.makeText(context, it.accessToken.toString(), Toast.LENGTH_SHORT).show()
        }

        viewModel.loginError.observe(loginActivity) {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        }
    }

    private fun initDialog() = with(binding) {

        window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        window?.setGravity(Gravity.BOTTOM)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        ivLoginDialogClose.setOnClickListener {
            dismiss()
        }
    }
}