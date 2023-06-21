package com.dongbin.popple.data.dto.user

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class UserRegisterResponseDto(
    @SerializedName("account")
    @Expose
    val account: String?,

    @SerializedName("name")
    @Expose
    val name: String?,
)