package com.adhityabagasmiwa.dicodingsubmissionbfaa.data.network

import com.adhityabagasmiwa.dicodingsubmissionbfaa.data.model.SearchUserResponse
import com.adhityabagasmiwa.dicodingsubmissionbfaa.data.model.UserGithub
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiEndpoint {

    @GET("search/users")
    fun getSearchUser(
        @Query("q") q: String?
    ): Call<SearchUserResponse>

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