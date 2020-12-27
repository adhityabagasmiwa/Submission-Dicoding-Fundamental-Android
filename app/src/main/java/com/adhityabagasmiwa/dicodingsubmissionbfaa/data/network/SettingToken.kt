package com.adhityabagasmiwa.dicodingsubmissionbfaa.data.network

import okhttp3.Interceptor
import okhttp3.Response

class SettingToken(private var token: String) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val requestToken = request.newBuilder()
            .addHeader("Authorization", "token $token")
            .build()

        return chain.proceed(requestToken)
    }
}