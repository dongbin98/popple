package com.dongbin.popple.ui.login

import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.WindowManager
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.dongbin.popple.data.api.provideLoginApi
import com.dongbin.popple.databinding.ActivityLoginBinding
import com.dongbin.popple.rx.AutoClearedDisposable

import com.dongbin.popple.ui.main.MainActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import com.kakao.sdk.user.rx
import com.navercorp.nid.NaverIdLoginSDK
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single

class LoginActivity : AppCompatActivity() {

    private lateinit var loginViewModel: LoginViewModel
    private lateinit var binding: ActivityLoginBinding

    private var disposables = AutoClearedDisposable(this) // for RxKotlin

    private var naverToken: String? = ""
    private val naverLoginLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            when (result.resultCode) {
                RESULT_OK -> {
                    naverToken = NaverIdLoginSDK.getAccessToken()
                    Intent(this@LoginActivity, MainActivity::class.java).run {
                        startActivity(this@run)
                    }
                }

                RESULT_CANCELED -> {
                    val errorCode = NaverIdLoginSDK.getLastErrorCode().code
                    val errorDescription = NaverIdLoginSDK.getLastErrorDescription()

                    Log.e("NaverLogin", "errorCode = $errorCode")
                    Log.e("NaverLogin", "errorDescription = $errorDescription")
                    Toast.makeText(
                        this@LoginActivity,
                        "errorCode: $errorCode`n errorDescription: $errorDescription",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

    private lateinit var gso: GoogleSignInOptions
    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private val googleLoginLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            when (result.resultCode) {
                RESULT_OK -> {
                    val data: Intent? = result.data
                    val task: Task<GoogleSignInAccount> =
                        GoogleSignIn.getSignedInAccountFromIntent(data)
                    handleGoogleAccount(task)
                    Intent(this@LoginActivity, MainActivity::class.java).run {
                        startActivity(this@run)
                    }
                }

                RESULT_CANCELED -> {
                    Toast.makeText(
                        this@LoginActivity, "Login Canceled", Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

    private var kakaoToken: String? = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loginViewModel =
            ViewModelProvider(this, LoginViewModelFactory(provideLoginApi()))[LoginViewModel::class.java]

        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )

        /* Naver Login */
        binding.btNaverLogin.setOnClickListener {
            loginWithNaver()
        }

        /* Kakao Login */
        binding.btKakaoLogin.setOnClickListener {
            loginWithKakao()
        }

        /* Google Login */
        binding.btGoogleLogin.setOnClickListener {
            loginWithGoogle()
        }

        /* Normal Login */
        binding.btLoginNormal.setOnClickListener {
            loginNormal()
        }

        /* Skip */
        binding.tvLoginSkip.setOnClickListener {
            Intent(this@LoginActivity, MainActivity::class.java).run {
                startActivity(this@run)
            }
        }
    }

    override fun onStart() {
        super.onStart()

        /* Google already Login check */
//        val account = GoogleSignIn.getLastSignedInAccount(this)
//        if (account == null) Log.i("GoogleLogin", "로그인 안되어있음")
//        else Intent(this@LoginActivity, MainActivity::class.java).run {
//            startActivity(this@run)
//        }
    }

    // https://developers.naver.com/docs/login/android/android.md
    private fun loginWithNaver() {
        // NaverIdLoginSDK.authenticate 직접 실행
        NaverIdLoginSDK.authenticate(this, naverLoginLauncher)
        // 배포 시 false 처리
        NaverIdLoginSDK.showDevelopersLog(true)
    }

    // https://developers.kakao.com/docs/latest/ko/kakaologin/android#kakaologin
    private fun loginWithKakao() {
        // for rx
        disposables.add(
            // 카카오톡 설치 확인
            if ((UserApiClient.instance.isKakaoTalkLoginAvailable(this@LoginActivity))) {
                UserApiClient.rx.loginWithKakaoTalk(this@LoginActivity)
                    .observeOn(AndroidSchedulers.mainThread())
                    .onErrorResumeNext { error ->
                        // 의도적인 로그인 취소 시 카카오계정 로그인 시도 없이 로그인 취소로 처리
                        if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                            Single.error(error)
                        } else {
                            // 카카오톡에 연결된 계정이 없는 경우, 카카오계정으로 로그인 시도
                            UserApiClient.rx.loginWithKakaoAccount(this@LoginActivity)
                        }
                    }.subscribe({ token ->
                        Log.i("KakaoLogin", "카카오톡 로그인 성공 ${token.accessToken}")
                        kakaoToken = token.accessToken
                        Intent(this@LoginActivity, MainActivity::class.java).run {
                            startActivity(this@run)
                        }
                    }, { error ->
                        Log.e("KakaoLogin", "카카오톡 로그인 실패", error)
                    })
            } else {
                UserApiClient.rx.loginWithKakaoAccount(this@LoginActivity)
                    .observeOn(AndroidSchedulers.mainThread()).subscribe({ token ->
                        Log.i("KakaoLogin", "카카오계정 로그인 성공 ${token.accessToken}")
                        kakaoToken = token.accessToken
                        Intent(this@LoginActivity, MainActivity::class.java).run {
                            startActivity(this@run)
                        }
                    }, { error ->
                        Log.e("KakaoLogin", "카카오계정 로그인 실패", error)
                    })
            }
        )
    }

    // https://developers.google.com/identity/sign-in/android/sign-in?hl=ko
    private fun loginWithGoogle() {
        gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail().build()
        // mGoogleSignInClient = GoogleSignIn.getClient(this@LoginActivity, gso)
        mGoogleSignInClient = this@LoginActivity.let { GoogleSignIn.getClient(it, gso) }
        googleLoginLauncher.launch(mGoogleSignInClient.signInIntent)
    }

    private fun loginNormal() {
        val dialog = NormalLoginDialog(this@LoginActivity, this).show()
    }

    /*private fun logoutWithKakao() {
        UserApiClient.instance.logout { error ->
            if (error != null) {
                Log.e("KakaoLogout", error.toString())
            } else {
                Log.i("KakaoLogout", "로그아웃 성공")
            }
        }
    }

    private fun unlinkWithKakao() {
        UserApiClient.instance.unlink { error ->
            if (error != null) {
                Log.e("KakaoLogout", error.toString())
            } else {
                Log.i("KakaoLogout", "연결 끊기 성공")
            }
        }
    }*/

    private fun handleGoogleAccount(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)
            val email = account?.email.toString()
            val familyName = account?.familyName.toString()
            Log.i("GoogleLogin", "email=$email")
        } catch (e: ApiException) {
            Log.e("GoogleLogin", "failed! code=${e.statusCode}")
        }
    }

    private fun checkPermission() {

    }
}

/**
 * Extension function to simplify setting an afterTextChanged action to EditText components.
 */
fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(editable: Editable?) {
            afterTextChanged.invoke(editable.toString())
        }

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
    })
}