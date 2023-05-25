package com.dongbin.popple.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class LoginResponse(
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