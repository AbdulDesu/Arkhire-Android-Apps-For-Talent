package com.sizdev.arkhirefortalent.networking

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response

class ArkhireHeaderInterceptor(context: Context) : Interceptor {

    private val sharedPref: SharedPreferences =  context.getSharedPreferences("Token", Context.MODE_PRIVATE)

    override fun intercept(chain: Interceptor.Chain): Response = chain.run {
        val token = sharedPref.getString("accToken", null)
        proceed(
                request().newBuilder()
                        .addHeader("Authorization", "Bearer $token")
                        .build()
        )
    }
}