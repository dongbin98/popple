package com.dongbin.popple.data.dto.register

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ResponseRegisterDto(
    @SerializedName("account")
    @Expose
    val account: String?,

    @SerializedName("name")
    @Expose
    val name: String?,
)