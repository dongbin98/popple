package com.dongbin.popple.data.dto.seller

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class SellerInfoResponseDto (
    @SerializedName("id")
    @Expose
    val id: Int,

    @SerializedName("seller_name")
    @Expose
    val sellerName: String?,

    @SerializedName("insta_account")
    @Expose
    val instaAccount: String?,

    @SerializedName("youtube_account")
    @Expose
    val youtubeAccount: String,

    @SerializedName("website_url")
    @Expose
    val websiteUrl: String?,

    @SerializedName("seller_profile_image")
    @Expose
    val sellerProfileImage: String,
)