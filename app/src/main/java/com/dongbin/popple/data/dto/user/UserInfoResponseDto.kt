package com.dongbin.popple.data.dto.user

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class UserInfoResponseDto(
    @SerializedName("id")
    @Expose
    val id: Int,

    @SerializedName("account")
    @Expose
    val account: String?,

    @SerializedName("birth")
    @Expose
    val birth: String?,

    @SerializedName("name")
    @Expose
    val name: String,

    @SerializedName("nickname")
    @Expose
    val nickname: String?,

    @SerializedName("login_type")
    @Expose
    val loginType: String,

    @SerializedName("profile_image")
    @Expose
    val profileImage: String,
)