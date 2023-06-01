package com.dongbin.popple

import android.app.Application
import android.content.pm.PackageManager
import android.os.Build
import com.kakao.sdk.common.KakaoSdk
import com.naver.maps.map.NaverMapSdk
import com.navercorp.nid.NaverIdLoginSDK

class GlobalApplication: Application() {
    init {
        instance = this
    }

    override fun onCreate() {
        super.onCreate()

        /* get metadata for api key */
        val metadata = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            packageManager.getApplicationInfo(
                packageName, PackageManager.ApplicationInfoFlags.of(PackageManager.GET_META_DATA.toLong())
            ).metaData
        } else {
            packageManager.getApplicationInfo(packageName, PackageManager.GET_META_DATA).metaData
        }

        /* Naver Login Module Init */
        val naverSdkId = metadata.getString("com.dongbin.popple.naverSdkId").toString()
        val naverSdkSecret = metadata.getString("com.dongbin.popple.naverSdkSecret").toString()
        val naverSdkName = metadata.getString("com.dongbin.popple.naverSdkName").toString()
        NaverIdLoginSDK.initialize(this, naverSdkId, naverSdkSecret, naverSdkName)

        /* Naver Map Module Init */
        val naverMapSdkId = metadata.getString("com.naver.maps.map.CLIENT_ID").toString()
        NaverMapSdk.getInstance(this).client = NaverMapSdk.NaverCloudPlatformClient(naverMapSdkId)

        /* Kakao Login Module Init */
        val kakaoSdkKey = metadata.getString("com.dongbin.popple.kakaoSdkKey").toString()
        KakaoSdk.init(this, kakaoSdkKey)
    }

    companion object {
        lateinit var instance: GlobalApplication
    }
}