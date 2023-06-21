package com.dongbin.popple.ui.register.user

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dongbin.popple.data.api.UserApi
import com.dongbin.popple.data.dto.user.UserRegisterRequestDto
import com.dongbin.popple.data.dto.user.UserRegisterWithKakaoRequestDto
import com.dongbin.popple.data.dto.user.UserRegisterWithNaverRequestDto
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers

class RegisterViewModel(private val api: UserApi) : ViewModel() {
    private val _accountResponse = MutableLiveData<String>()
    var accountResponse: LiveData<String> = _accountResponse

    private val _nicknameResponse = MutableLiveData<String>()
    var nicknameResponse: LiveData<String> = _nicknameResponse

    private val _registerResponse = MutableLiveData<String>()
    var registerResponse: LiveData<String> = _registerResponse

    @SuppressLint("CheckResult")
    fun checkAccount(account: String) {
        api.checkAccount(account)
            .subscribeOn(AndroidSchedulers.mainThread())
            .doOnError {
                _accountResponse.postValue(it.message)
            }
            .subscribe({
                if(account == it.account.toString())
                    _accountResponse.postValue("unavailable")
                else
                    _accountResponse.postValue("available")
            }) {
                _accountResponse.postValue(it.message)
            }
    }

    @SuppressLint("CheckResult")
    fun checkNickname(nickname: String) {
        api.checkNickname(nickname)
            .subscribeOn(AndroidSchedulers.mainThread())
            .doOnError {
                _nicknameResponse.postValue(it.message)
            }
            .subscribe({
                if(nickname == it.nickname.toString())
                    _nicknameResponse.postValue("unavailable")
                else
                    _nicknameResponse.postValue("available")
            }) {
                _nicknameResponse.postValue(it.message)
            }
    }

    @SuppressLint("CheckResult")
    fun register(userRegisterRequestDto: UserRegisterRequestDto) {
        api.register(userRegisterRequestDto)
            .subscribeOn(AndroidSchedulers.mainThread())
            .doOnError {
                _registerResponse.postValue(it.message)
            }
            .subscribe {
                if (it.account != null) {
                    _registerResponse.postValue(it.account.toString())
                } else {
                    _registerResponse.postValue("register failed")
                }
            }
    }

    @SuppressLint("CheckResult")
    fun registerWithNaver(userRegisterWithNaverRequestDto: UserRegisterWithNaverRequestDto) {
        api.registerWithNaver(userRegisterWithNaverRequestDto)
            .subscribeOn(AndroidSchedulers.mainThread())
            .doOnError {
                _registerResponse.postValue(it.message)
            }
            .subscribe {
                if (it.account != null) {
                    _registerResponse.postValue(it.account.toString())
                } else {
                    _registerResponse.postValue("register failed")
                }
            }
    }

    @SuppressLint("CheckResult")
    fun registerWithKakao(userRegisterWithKakaoRequestDto: UserRegisterWithKakaoRequestDto) {
        api.registerWithKakao(userRegisterWithKakaoRequestDto)
            .subscribeOn(AndroidSchedulers.mainThread())
            .doOnError {
                _registerResponse.postValue(it.message)
            }
            .subscribe {
                if (it.account != null) {
                    _registerResponse.postValue(it.account.toString())
                } else {
                    _registerResponse.postValue("register failed")
                }
            }
    }
}