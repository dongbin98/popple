package com.dongbin.popple.data.model.login

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ResponseKakaoProfileAccountDto(
    @SerializedName("email")
    @Expose
    val email: String?,
)