package com.dongbin.popple.ui.register.seller

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dongbin.popple.data.api.SellerApi
import com.dongbin.popple.data.dto.seller.SellerRegisterRequestDto
import com.dongbin.popple.data.dto.user.UserRegisterRequestDto
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers

class SellerRegisterViewModel(private val api: SellerApi) : ViewModel() {

    private val _nameResponse = MutableLiveData<String>()
    var nameResponse: LiveData<String> = _nameResponse

    private val _registerResponse = MutableLiveData<String>()
    var registerResponse: LiveData<String> = _registerResponse

    @SuppressLint("CheckResult")
    fun checkName(name: String) {
        api.checkName(name)
            .subscribeOn(AndroidSchedulers.mainThread())
            .doOnError { _nameResponse.postValue(it.message) }
            .subscribe ({
                if(name == it.sellerName.toString())
                    _nameResponse.postValue("unavailable")
                else
                    _nameResponse.postValue("available")
            }) {
                _nameResponse.postValue(it.message)
            }
    }

    @SuppressLint("CheckResult")
    fun register(sellerRegisterRequestDto: SellerRegisterRequestDto) {
        api.register(sellerRegisterRequestDto)
            .subscribeOn(AndroidSchedulers.mainThread())
            .doOnError {
                _registerResponse.postValue(it.message)
            }
            .subscribe {
                if (it.id != null) {
                    _registerResponse.postValue(it.name.toString())
                } else {
                    _registerResponse.postValue("register failed")
                }
            }
    }
}