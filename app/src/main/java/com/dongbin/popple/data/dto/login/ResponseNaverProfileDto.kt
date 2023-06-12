package com.dongbin.popple.data.dto.login

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ResponseNaverProfileDto(
    @SerializedName("resultcode")
    @Expose
    val accessToken: String?,

    @SerializedName("message")
    @Expose
    val tokenType: String?,

    @SerializedName("response")
    @Expose
    val response: ResponseNaverProfileResponseDto?,
)