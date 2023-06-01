package com.dongbin.popple.data.model.login

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ResponseKakaoProfileDto(
    @SerializedName("id")   // 동일인 식별 정보
    @Expose
    val id: String?,

    @SerializedName("properties")
    @Expose
    val properties: ResponseKakaoProfilePropertiesDto?,

    @SerializedName("kakao_account")
    @Expose
    val kakaoAccount: ResponseKakaoProfileAccountDto?,
)