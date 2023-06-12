package com.dongbin.popple.data.dto.register

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ResponseDuplicateCheckDto(
    @SerializedName("id")
    @Expose
    val id: Int,

    @SerializedName("account")
    @Expose
    val account: String?,

    @SerializedName("name")
    @Expose
    val name: String,

    @SerializedName("nickname")
    @Expose
    val nickname: String?,

    @SerializedName("login_type")
    @Expose
    val loginType: String
)