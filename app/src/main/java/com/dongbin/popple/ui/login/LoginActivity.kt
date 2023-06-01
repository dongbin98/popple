package com.dongbin.popple.ui.login

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.dongbin.popple.data.api.provideUserApi
import com.dongbin.popple.data.model.login.ResponseKakaoProfileDto
import com.dongbin.popple.data.model.login.ResponseNaverProfileDto
import com.dongbin.popple.data.model.register.RequestRegisterWithKakaoDto
import com.dongbin.popple.data.model.register.RequestRegisterWithNaverDto
import com.dongbin.popple.databinding.ActivityLoginBinding
import com.dongbin.popple.rx.AutoClearedDisposable
import com.dongbin.popple.ui.gps.GpsActivity

import com.dongbin.popple.ui.main.MainActivity
import com.dongbin.popple.ui.register.RegisterViewModel
import com.dongbin.popple.ui.register.RegisterViewModelFactory
import com.dongbin.popple.GlobalApplication
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
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class LoginActivity : AppCompatActivity() {

    private lateinit var loginViewModel: LoginViewModel
    private lateinit var registerViewModel: RegisterViewModel
    private lateinit var binding: ActivityLoginBinding

    private var disposables = AutoClearedDisposable(this) // for RxKotlin

    // 임시 저장할 프로필 정보
    private var responseNaverProfileDto: ResponseNaverProfileDto? = null
    private var responseKakaoProfileDto: ResponseKakaoProfileDto? = null
    private var ssoId: String? = ""  // Naver or Kakao 식별번호

    lateinit var userInfo: GlobalApplication.UserInfo

    private val naverLoginLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            when (result.resultCode) {
                RESULT_OK -> {
                    /*
                    access_token, refresh_token 등등 sharedPreferences(Naver) 저장
                     */
                    Log.i("NaverLogin", "네이버 회원 정보를 불러옵니다")
                    loginViewModel.getNaverProfile(
                        NaverIdLoginSDK.getAccessToken().toString()
                    )   // 네이버 프로필 가져오기
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
                    startWhichActivity()
                }

                RESULT_CANCELED -> {
                    Toast.makeText(
                        this@LoginActivity, "Login Canceled", Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loginViewModel =
            ViewModelProvider(
                this,
                LoginViewModelFactory()
            )[LoginViewModel::class.java]

        registerViewModel =
            ViewModelProvider(
                this,
                RegisterViewModelFactory(provideUserApi())
            )[RegisterViewModel::class.java]

        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )

        initView()

        loginViewModel.naverProfile.observe(this) {
            if (it != null) {
                // 네이버 회원정보를 가져오면 해당 정보로 가입된 계정이 있는지 확인
                Log.i("NaverLogin", "팝플 가입 정보를 불러옵니다")
                responseNaverProfileDto = it
                ssoId = "naver${responseNaverProfileDto!!.response?.id}"
                registerViewModel.checkAccount(ssoId!!)
            }
        }

        loginViewModel.kakaoProfile.observe(this) {
            if (it != null) {
                // 카카오 회원정보를 가져오면 해당 정보로 가입된 계정이 있는지 확인
                Log.i("KakaoLogin", "팝플 가입 정보를 불러옵니다")
                responseKakaoProfileDto = it
                ssoId = "kakao${responseKakaoProfileDto!!.id}"
                registerViewModel.checkAccount(ssoId!!)
            }
        }

        registerViewModel.accountResponse.observe(this) {
            // 고유 id값으로 가입 여부 확인 :: 카카오 -> kakao${id}, 네이버 -> naver${id}
            if (it == "unavailable") {  // 회원등록이 되어있는 경우
                Log.i("SsoLogin", "가입된 정보로 팝플에 로그인합니다")
                loginViewModel.ssoLogin(ssoId.toString())
            }
            else {    // 회원등록이 안되어있는 경우 가입절차
                if (responseNaverProfileDto != null) {  // 네이버로 회원정보를 가져온 경우
                    Log.i("NaverLogin", "팝플 가입을 진행합니다")
                    registerViewModel.registerWithNaver(
                        RequestRegisterWithNaverDto(
                            account = "naver${responseNaverProfileDto!!.response?.id}",
                            name = responseNaverProfileDto!!.response?.name.toString(),
                            nickname = responseNaverProfileDto!!.response?.nickname.toString(),
                            birth = responseNaverProfileDto!!.response?.birthyear.toString() + "-" + responseNaverProfileDto!!.response?.birthday.toString(),
                            login_type = "naver",
                            created_at = LocalDateTime.now()
                                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss.SSS"))
                        )
                    )
                } else if (responseKakaoProfileDto != null) {   // 카카오로 회원정보를 가져온 경우
                    Log.i("KakaoLogin", "팝플 가입을 진행합니다")
                    registerViewModel.registerWithKakao(
                        RequestRegisterWithKakaoDto(
                            account = "kakao${responseKakaoProfileDto!!.id}",
                            name = responseKakaoProfileDto!!.properties?.nickname.toString(),
                            login_type = "kakao",
                            created_at = LocalDateTime.now()
                                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss.SSS"))
                        )
                    )
                }
            }
        }

        registerViewModel.registerResponse.observe(this) {
            if (it == ssoId.toString()) {
                Log.i("SsoLogin", "가입된 정보로 팝플에 로그인합니다")
                loginViewModel.ssoLogin(ssoId.toString())
            }
        }

        loginViewModel.loginResponse.observe(this) {
            Log.i("SSOLogin", "팝플 로그인 성공")
            startWhichActivity()
        }

        loginViewModel.loginError.observe(this) {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onStart() {
        super.onStart()

        responseNaverProfileDto = null
        responseKakaoProfileDto = null
        ssoId = ""

        /* Kakao already Login check */

        /* Naver already Login check */

        /* Google already Login check */
//        val account = GoogleSignIn.getLastSignedInAccount(this)
//        if (account == null) Log.i("GoogleLogin", "로그인 안되어있음")
//        else Intent(this@LoginActivity, MainActivity::class.java).run {
//            startWhichActivity(this@LoginActivity)
//        }
    }

    private fun initView() = with(binding) {
        /* Naver Login */
        btNaverLogin.setOnClickListener {
            loginWithNaver()
        }

        /* Kakao Login */
        btKakaoLogin.setOnClickListener {
            loginWithKakao()
        }

        /* Google Login */
        btGoogleLogin.setOnClickListener {
            loginWithGoogle()
        }

        /* Normal Login */
        btLoginNormal.setOnClickListener {
            loginNormal()
        }

        /* Skip */
        tvLoginSkip.setOnClickListener {
            startWhichActivity()
        }
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
                        loginViewModel.getKakaoProfile(token.accessToken)
                    }, { error ->
                        Log.e("KakaoLogin", "카카오톡 로그인 실패", error)
                    })
            } else {
                UserApiClient.rx.loginWithKakaoAccount(this@LoginActivity)
                    .observeOn(AndroidSchedulers.mainThread()).subscribe({ token ->
                        Log.i("KakaoLogin", "카카오계정 로그인 성공 ${token.accessToken}")
                        loginViewModel.getKakaoProfile(token.accessToken)
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
        NormalLoginDialog(this, this).show()
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

    private fun checkSelfPermission(): Boolean {
        return if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            true
        } else ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun startWhichActivity() {
        if (checkSelfPermission()) {
            // 위치 기반 액세스 동의인 경우 바로 메인 액티비티로 이동
            Intent(this@LoginActivity, MainActivity::class.java).run {
                startActivity(this@run)
            }
        } else {
            // 그 외에는 권한 동의 액티비티로 이동
            Intent(this@LoginActivity, GpsActivity::class.java).run {
                startActivity(this@run)
            }
        }
    }
}
