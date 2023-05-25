package com.dongbin.popple.data.api

import com.dongbin.popple.data.model.LoginRequest
import com.dongbin.popple.data.model.LoginResponse
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Headers
import retrofit2.http.POST

interface UserApi {
    // @Headers("Accept: application/json", "content-type: application/json")
    @FormUrlEncoded // FormUrlEncoded의 경우 매개변수를 @Body가 아닌 @Field로 해야한다.
    @POST("api/user/login")
    fun login(
        @Field("username") username: String,
        @Field("password") password: String,
    ): Observable<LoginResponse>
}