package com.dongbin.popple.data.model.login

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ResponseKakaoProfilePropertiesDto(
    @SerializedName("nickname")
    @Expose
    val nickname: String?,
)