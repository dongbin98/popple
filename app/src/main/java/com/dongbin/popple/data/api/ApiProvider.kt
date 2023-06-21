package com.dongbin.popple.data.api

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

fun provideUserApi(): UserApi = Retrofit.Builder()
    .baseUrl("http://10.0.2.2:8000/api/user/")
    .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
    .addConverterFactory(GsonConverterFactory.create())
    .client(OkHttpClient())
    .build()
    .create(UserApi::class.java)

fun provideSellerApi(): SellerApi = Retrofit.Builder()
    .baseUrl("http://10.0.2.2:8000/api/seller/")
    .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
    .addConverterFactory(GsonConverterFactory.create())
    .client(OkHttpClient())
    .build()
    .create(SellerApi::class.java)

fun provideUserApiWithToken(accessToken: String): UserApi = Retrofit.Builder()
    .baseUrl("http://10.0.2.2:8000/api/user/")
    .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
    .addConverterFactory(GsonConverterFactory.create())
    .client(accessTokenClient(accessToken))
    .build()
    .create(UserApi::class.java)

fun provideNaverApiWithToken(accessToken: String): NaverApi = Retrofit.Builder()
    .baseUrl("https://openapi.naver.com/v1/nid/")
    .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
    .addConverterFactory(GsonConverterFactory.create())
    .client(accessTokenClient(accessToken))
    .build()
    .create(NaverApi::class.java)

fun provideKakaoApiWithToken(accessToken: String): KakaoApi = Retrofit.Builder()
    .baseUrl("https://kapi.kakao.com/v2/user/")
    .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
    .addConverterFactory(GsonConverterFactory.create())
    .client(accessTokenClient(accessToken))
    .build()
    .create(KakaoApi::class.java)

fun accessTokenClient(accessToken: String) = OkHttpClient.Builder().addInterceptor(
    Interceptor { chain ->
        val request = chain.request().newBuilder()
            .addHeader("Authorization", "Bearer $accessToken")
            .build()
        chain.proceed(request)
    }).build()