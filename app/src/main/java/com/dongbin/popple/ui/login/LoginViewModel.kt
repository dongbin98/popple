package com.dongbin.popple.ui.login

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dongbin.popple.data.api.provideKakaoApiWithToken

import com.dongbin.popple.data.api.provideNaverApiWithToken
import com.dongbin.popple.data.api.provideUserApi
import com.dongbin.popple.data.dto.login.PoppleLoginResponseDto
import com.dongbin.popple.data.dto.login.NaverProfileResponseDto
import com.dongbin.popple.data.dto.login.KakaoProfileResponseDto
import com.dongbin.popple.data.dto.user.UserInfoResponseDto
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers

class LoginViewModel() : ViewModel() {

    private val userApi = provideUserApi()

    private val _loginResponse = MutableLiveData<PoppleLoginResponseDto>()
    var loginResponse: LiveData<PoppleLoginResponseDto> = _loginResponse

    private val _loginError = MutableLiveData<String>()
    var loginError: LiveData<String> = _loginError

    private val _naverProfile = MutableLiveData<NaverProfileResponseDto?>()
    var naverProfile: LiveData<NaverProfileResponseDto?> = _naverProfile

    private val _kakaoProfile = MutableLiveData<KakaoProfileResponseDto?>()
    var kakaoProfile: LiveData<KakaoProfileResponseDto?> = _kakaoProfile

    private val _userInfoResponse = MutableLiveData<UserInfoResponseDto>()
    var userInfoResponse: LiveData<UserInfoResponseDto> = _userInfoResponse

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