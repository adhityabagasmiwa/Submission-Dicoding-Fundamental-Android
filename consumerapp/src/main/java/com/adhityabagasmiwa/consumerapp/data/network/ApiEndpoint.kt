/*
 * Created by Adhitya Bagas on 29/12/2020
 * Copyright (c) 2020 . All rights reserved.
 */

package com.adhityabagasmiwa.consumerapp.data.network

import com.adhityabagasmiwa.consumerapp.data.model.UserGithub
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiEndpoint {

    @GET("users/{username}")
    fun getDetailUser(
        @Path("username") username: String?
    ): Call<UserGithub>

    @GET("users/{username}/followers")
    fun getFollower(
        @Path("username") username: String?
    ): Call<ArrayList<UserGithub>>

    @GET("users/{username}/following")
    fun getFollowing(
        @Path("username") username: String?
    ): Call<ArrayList<UserGithub>>

}