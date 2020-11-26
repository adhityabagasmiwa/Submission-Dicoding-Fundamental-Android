/*
 * Created by Adhitya Bagas on 14/11/2020
 * Copyright (c) 2020 . All rights reserved.
 */

package com.adhityabagasmiwa.dicodingsubmissionbfaa.ui

import android.content.res.TypedArray
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.adhityabagasmiwa.dicodingsubmissionbfaa.R
import com.adhityabagasmiwa.dicodingsubmissionbfaa.data.adapter.UserAdapter
import com.adhityabagasmiwa.dicodingsubmissionbfaa.data.model.User
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var dataUsername: Array<String>
    private lateinit var dataName: Array<String>
    private lateinit var dataAvatar: TypedArray
    private lateinit var dataCompany: Array<String>
    private lateinit var dataLocation: Array<String>
    private lateinit var dataRepository: Array<String>
    private lateinit var dataFollower: Array<String>
    private lateinit var dataFollowing: Array<String>
    private var listUser = arrayListOf<User>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initData()
        addData()
        showRecycleList()
    }

    private fun initData() {
        dataAvatar = resources.obtainTypedArray(R.array.avatar)
        dataUsername = resources.getStringArray(R.array.username)
        dataName = resources.getStringArray(R.array.name)
        dataRepository = resources.getStringArray(R.array.repository)
        dataFollower = resources.getStringArray(R.array.followers)
        dataFollowing = resources.getStringArray(R.array.following)
        dataCompany = resources.getStringArray(R.array.company)
        dataLocation = resources.getStringArray(R.array.location)
    }

    private fun addData(){
        for (position in dataName.indices) {
            val userGithub = User(
                dataAvatar.getResourceId(position, -1),
                dataName[position],
                dataUsername[position],
                dataCompany[position],
                dataLocation[position],
                dataRepository[position],
                dataFollower[position],
                dataFollowing[position]
            )
            listUser.add(userGithub)
        }
    }

    private fun showRecycleList() {
        rv_item_row_data.setHasFixedSize(true)
        rv_item_row_data.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        val listUserAdapter = UserAdapter(this, listUser)
        rv_item_row_data.adapter = listUserAdapter
    }
}