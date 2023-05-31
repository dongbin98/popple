package com.dongbin.popple.data.model.register

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ResponseDuplicateCheckDto(
    @SerializedName("account")
    @Expose
    val account: String?,

    @SerializedName("nickname")
    @Expose
    val nickname: String?,
)