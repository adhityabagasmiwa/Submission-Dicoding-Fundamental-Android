/*
 * Created by Adhitya Bagas on 29/12/2020
 * Copyright (c) 2020 . All rights reserved.
 */

package com.adhityabagasmiwa.consumerapp.data.network

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