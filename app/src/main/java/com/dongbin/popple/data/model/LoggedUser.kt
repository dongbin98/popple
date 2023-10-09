package com.dongbin.popple.data.model


data class LoggedUser (
    var id: Int? = null,
    var account: String? = null,
    var name: String? = null,
    var nickName: String? = null,
    var loginType: String? = null,
    var accessToken: String? = null,
) {
    // constructor(): this(null, null, null, null, null, null)

    fun isLogged(): Boolean = id != null

    fun clearUser() {
        id = null
        account = null
        name = null
        nickName = null
        loginType = null
        accessToken = null
    }
}