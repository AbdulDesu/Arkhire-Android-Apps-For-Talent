package com.sizdev.arkhirefortalent.networking

import android.content.Context
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class ApiClient {
    companion object {

        private const val BASE_URL = "http://54.82.81.23:911/"
        private var retrofit: Retrofit? = null

        private fun provideHttpLoggingInterceptor() = run {
            HttpLoggingInterceptor().apply {
                apply { level = HttpLoggingInterceptor.Level.BODY }
            }
        }


        fun getApiClient(context: Context): Retrofit? {

            if (retrofit == null) {
                val okHttpClient = OkHttpClient.Builder()
                        .addInterceptor(provideHttpLoggingInterceptor())
                        .addInterceptor(HeaderInterceptor(context))
                        .connectTimeout(1, TimeUnit.MINUTES)
                        .readTimeout(1, TimeUnit.MINUTES)
                        .writeTimeout(1, TimeUnit.MINUTES)
                        .build()

                retrofit = Retrofit.Builder()
                        .baseUrl(BASE_URL)
                        .client(okHttpClient)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build()
            }
            return retrofit

        }
    }
}