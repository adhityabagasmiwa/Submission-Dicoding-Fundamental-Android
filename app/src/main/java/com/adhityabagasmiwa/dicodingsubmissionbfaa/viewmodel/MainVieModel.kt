/*
 * Created by Adhitya Bagas on 29/12/2020
 * Copyright (c) 2020 . All rights reserved.
 */

package com.adhityabagasmiwa.dicodingsubmissionbfaa.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.adhityabagasmiwa.dicodingsubmissionbfaa.R
import com.adhityabagasmiwa.dicodingsubmissionbfaa.data.model.SearchUserResponse
import com.adhityabagasmiwa.dicodingsubmissionbfaa.data.model.UserGithub
import com.adhityabagasmiwa.dicodingsubmissionbfaa.data.network.ApiService
import com.shashank.sony.fancytoastlib.FancyToast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainVieModel : ViewModel() {

    private val userList = MutableLiveData<ArrayList<UserGithub>>()
    private val userDetail = MutableLiveData<UserGithub>()
    private val followingList = MutableLiveData<ArrayList<UserGithub>>()
    private val followerList = MutableLiveData<ArrayList<UserGithub>>()

    fun setUser(context: Context, query: String?) {
        ApiService.endpoint.getSearchUser(query).enqueue(object : Callback<SearchUserResponse> {

            override fun onResponse(
                call: Call<SearchUserResponse>,
                response: Response<SearchUserResponse>
            ) {
                try {
                    val result = response.body()
                    if (result != null) {
                        val userItem: ArrayList<UserGithub> = result.items
                        userList.postValue(userItem)
                    }
                } catch (e: Exception) {
                    Log.d("Exception", e.message.toString())
                    FancyToast.makeText(context, context.resources.getString(R.string.failed_data), FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show()
                }
            }

            override fun onFailure(call: Call<SearchUserResponse>, t: Throwable) {
                Log.d("onFailure", t.message.toString())
                FancyToast.makeText(context, context.resources.getString(R.string.check_internet), FancyToast.LENGTH_SHORT, FancyToast.WARNING, false).show()
            }
        })
    }

    fun setUserDetail(context: Context, username: String?) {

        ApiService.endpoint.getDetailUser(username).enqueue(object : Callback<UserGithub> {

            override fun onResponse(call: Call<UserGithub>, response: Response<UserGithub>) {
                try {
                    val result = response.body()
                    if (result != null) {
                        userDetail.value = result
                    }
                } catch (e: Exception) {
                    Log.d("Exception", e.message.toString())
                    FancyToast.makeText(context, context.resources.getString(R.string.failed_data), FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show()
                }
            }

            override fun onFailure(call: Call<UserGithub>, t: Throwable) {
                Log.d("onFailure", t.message.toString())
                FancyToast.makeText(context, context.resources.getString(R.string.check_internet), FancyToast.LENGTH_SHORT, FancyToast.WARNING, false).show()
            }
        })
    }

    fun setFollowingList(context: Context, username: String?) {

        ApiService.endpoint.getFollowing(username)
            .enqueue(object : Callback<ArrayList<UserGithub>> {

                override fun onResponse(
                    call: Call<ArrayList<UserGithub>>,
                    response: Response<ArrayList<UserGithub>>
                ) {
                    try {
                        val result = response.body()
                        result?.let {
                            followingList.postValue(it)
                        }
                    } catch (e: Exception) {
                        Log.d("Exception", e.message.toString())
                        FancyToast.makeText(context, context.resources.getString(R.string.failed_data), FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show()
                    }
                }

                override fun onFailure(call: Call<ArrayList<UserGithub>>, t: Throwable) {
                    Log.d("onFailure", t.message.toString())
                    FancyToast.makeText(context, context.resources.getString(R.string.check_internet), FancyToast.LENGTH_SHORT, FancyToast.WARNING, false).show()
                }
            })
    }

    fun setFollowerList(context: Context, username: String?) {

        ApiService.endpoint.getFollower(username).enqueue(object : Callback<ArrayList<UserGithub>> {

            override fun onResponse(
                call: Call<ArrayList<UserGithub>>,
                response: Response<ArrayList<UserGithub>>
            ) {
                try {
                    val result = response.body()
                    result?.let {
                        followerList.postValue(it)
                    }
                } catch (e: Exception) {
                    Log.d("Exception", e.message.toString())
                    FancyToast.makeText(context, context.resources.getString(R.string.failed_data), FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show()
                }
            }

            override fun onFailure(call: Call<ArrayList<UserGithub>>, t: Throwable) {
                Log.d("onFailure", t.message.toString())
                FancyToast.makeText(context, context.resources.getString(R.string.check_internet), FancyToast.LENGTH_SHORT, FancyToast.WARNING, false).show()
            }
        })
    }

    fun getUser(): LiveData<ArrayList<UserGithub>> {
        return userList
    }

    fun getUserDetail(): LiveData<UserGithub> {
        return userDetail
    }

    fun getFollowingList(): LiveData<ArrayList<UserGithub>> {
        return followingList
    }

    fun getFollowerList(): LiveData<ArrayList<UserGithub>> {
        return followerList
    }
}