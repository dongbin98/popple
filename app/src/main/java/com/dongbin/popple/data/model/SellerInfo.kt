package com.dongbin.popple.data.model


data class SellerInfo (
    var id: Int? = null,
    var seller_name: String? = null,
    var category: MutableList<String> = mutableListOf(),
    var instaAccount: String? = null,
    var youtubeAccount: String? = null,
    var websiteUrl: String? = null,
    var sellerProfileImage: String? = null,
) {
    fun isReadyToSave(): Boolean {
        return id != null && seller_name != null
    }
}