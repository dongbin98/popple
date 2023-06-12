package com.dongbin.popple.ui.login

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dongbin.popple.data.api.provideKakaoApiWithToken

import com.dongbin.popple.data.api.provideNaverApiWithToken
import com.dongbin.popple.data.api.provideUserApi
import com.dongbin.popple.data.dto.login.ResponsePoppleLoginDto
import com.dongbin.popple.data.dto.login.ResponseNaverProfileDto
import com.dongbin.popple.data.dto.login.RequestSsoLoginDto
import com.dongbin.popple.data.dto.login.ResponseKakaoProfileDto
import com.dongbin.popple.data.dto.register.ResponseDuplicateCheckDto
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers

class LoginViewModel() : ViewModel() {

    private val userApi = provideUserApi()

    private val _loginResponse = MutableLiveData<ResponsePoppleLoginDto>()
    var loginResponse: LiveData<ResponsePoppleLoginDto> = _loginResponse

    private val _loginError = MutableLiveData<String>()
    var loginError: LiveData<String> = _loginError

    private val _naverProfile = MutableLiveData<ResponseNaverProfileDto?>()
    var naverProfile: LiveData<ResponseNaverProfileDto?> = _naverProfile

    private val _kakaoProfile = MutableLiveData<ResponseKakaoProfileDto?>()
    var kakaoProfile: LiveData<ResponseKakaoProfileDto?> = _kakaoProfile

    private val _userInfoResponse = MutableLiveData<ResponseDuplicateCheckDto>()
    var userInfoResponse: LiveData<ResponseDuplicateCheckDto> = _userInfoResponse

    @SuppressLint("CheckResult")
    fun login(username: String, password: String) {
        userApi.login(username, password)
            .subscribeOn(AndroidSchedulers.mainThread())
            .doOnError { _loginError.postValue(it.message) }
            .subscribe({
                if (it.accessToken != null)
                    _loginResponse.postValue(it)
            }) {
                _loginError.postValue(it.message)
            }
    }

    @SuppressLint("CheckResult")
    fun ssoLogin(username: String) {
        userApi.ssoLogin(RequestSsoLoginDto(username))
            .subscribeOn(AndroidSchedulers.mainThread())
            .doOnError { _loginError.postValue(it.message) }
            .subscribe({
                if (it.accessToken != null)
                    _loginResponse.postValue(it)
            }) {
                _loginError.postValue(it.message)
            }
    }

    @SuppressLint("CheckResult")
    fun getUserInfo(account: String) {
        userApi.checkAccount(account).subscribeOn(AndroidSchedulers.mainThread())
            .subscribe { _userInfoResponse.postValue(it) }
    }

    fun getNaverProfile(accessToken: String) {
        val disposable = provideNaverApiWithToken(accessToken).getUserProfile()
            .subscribeOn(AndroidSchedulers.mainThread())
            .doOnError { _naverProfile.postValue(null) }
            .subscribe {
                _naverProfile.postValue(it)
            }
    }

    fun getKakaoProfile(accessToken: String) {
        val disposable = provideKakaoApiWithToken(accessToken).getUserProfile()
            .subscribeOn(AndroidSchedulers.mainThread())
            .doOnError { _kakaoProfile.postValue(null) }
            .subscribe {
                _kakaoProfile.postValue(it)
            }
    }
}