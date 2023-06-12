package com.dongbin.popple.ui.login

import android.Manifest
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.dongbin.popple.GlobalApplication
import com.dongbin.popple.databinding.DialogLoginBinding
import com.dongbin.popple.ui.gps.GpsActivity
import com.dongbin.popple.ui.main.MainActivity
import com.dongbin.popple.ui.register.RegisterActivity

class NormalLoginDialog(context: Context, private val loginActivity: LoginActivity) :
    Dialog(context) {
    private lateinit var binding: DialogLoginBinding
    private lateinit var viewModel: LoginViewModel
    // private val disposable = AutoClearedDisposable(loginActivity)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DialogLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = LoginViewModel()
        // Dialog에서 ViewModelProvider로 뷰모델 받아오면
        // 액티비티에서의 동작으로 LivaData변화시 observe하는 문제..
//        viewModel = ViewModelProvider(
//            loginActivity,
//            LoginViewModelFactory()
//        )[LoginViewModel::class.java]

        initDialog()

        viewModel.loginResponse.observe(loginActivity) {
            Log.i("Login", "팝플 로그인 성공")
            GlobalApplication.instance.accessToken = it.accessToken
            viewModel.getUserInfo(it.userName.toString())
        }

        viewModel.loginError.observe(loginActivity) {
            Toast.makeText(loginActivity, it, Toast.LENGTH_SHORT).show()
        }

        viewModel.userInfoResponse.observe(loginActivity) {
            Log.i("SSOLogin", "유저 정보 로드")
            GlobalApplication.instance.id = it.id
            GlobalApplication.instance.account = it.account.toString()
            GlobalApplication.instance.name = it.name
            GlobalApplication.instance.nickname = it.nickname.toString()
            GlobalApplication.instance.loginType = it.loginType
            startWhichActivity(loginActivity)
        }
    }

    private fun initDialog() = with(binding) {

        window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        window?.setGravity(Gravity.BOTTOM)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        ivLoginDialogClose.setOnClickListener {
            dismiss()
        }

        tvLoginDialogRegister.setOnClickListener {
            Intent(loginActivity, RegisterActivity::class.java).run {
                this@NormalLoginDialog.context.startActivity(this)
            }
        }

        btLoginDialog.setOnClickListener {
            viewModel.login(
                binding.etLoginDialogId.text.toString(),
                binding.etLoginDialogPassword.text.toString()
            )
        }
    }

    private fun checkSelfPermission(): Boolean {
        return if (ContextCompat.checkSelfPermission(
                loginActivity,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
        ) {
            true
        } else ContextCompat.checkSelfPermission(
            loginActivity,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun startWhichActivity(activity: AppCompatActivity) {
        if(checkSelfPermission()) {
            // 위치 기반 액세스 동의인 경우 바로 메인 액티비티로 이동
            Intent(activity, MainActivity::class.java).run {
                this@NormalLoginDialog.context.startActivity(this)
            }
        } else {
            // 그 외에는 권한 동의 액티비티로 이동
            Intent(activity, GpsActivity::class.java).run {
                this@NormalLoginDialog.context.startActivity(this)
            }
        }
    }
}