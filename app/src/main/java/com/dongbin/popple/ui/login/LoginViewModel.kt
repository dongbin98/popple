package com.dongbin.popple.ui.login

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import android.util.Patterns

import com.dongbin.popple.R
import com.dongbin.popple.data.api.UserApi
import com.dongbin.popple.data.model.LoginRequest
import com.dongbin.popple.data.model.LoginResponse
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel(private val api: UserApi) : ViewModel() {

    private val _loginResponse =  MutableLiveData<LoginResponse>()
    var loginResponse: LiveData<LoginResponse> = _loginResponse

    private val _loginError =  MutableLiveData<String>()
    var loginError: LiveData<String> = _loginError

    @SuppressLint("CheckResult")
    fun login(id: String, password: String) {
        api.login(id, password)
            .subscribeOn(AndroidSchedulers.mainThread())
            .subscribe ({response ->
                if(response.accessToken != null)
                    _loginResponse.postValue(response)
            }) {
                _loginError.postValue(it.message)
            }
    }
}