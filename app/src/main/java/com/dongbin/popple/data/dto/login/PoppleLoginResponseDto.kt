package com.dongbin.popple.data.dto.login

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class PoppleLoginResponseDto(
    @SerializedName("access_token")
    @Expose
    val accessToken: String?,

    @SerializedName("token_type")
    @Expose
    val tokenType: String?,

    @SerializedName("username")
    @Expose
    val userName: String?,
)