/*
 * Created by Adhitya Bagas on 20/11/2020
 * Copyright (c) 2020 . All rights reserved.
 */

package com.adhityabagasmiwa.dicodingsubmissionbfaa.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.adhityabagasmiwa.dicodingsubmissionbfaa.R
import com.adhityabagasmiwa.dicodingsubmissionbfaa.data.model.User
import kotlinx.android.synthetic.main.activity_user_detail.*
import kotlinx.android.synthetic.main.item_user_list.tvName
import kotlinx.android.synthetic.main.item_user_list.tvUsername

class UserDetailActivity : AppCompatActivity() {

    private lateinit var btnBack: Button
    private lateinit var btnShare: Button

    companion object {
        const val EXTRA_USERS_GITHUB = "extra_users_github"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_user_detail)

        val user = intent.getParcelableExtra(EXTRA_USERS_GITHUB) as User

        imgAvatar.setImageResource(user.avatar)
        tvName.text = user.name
        tvUsername.text = user.username
        tvRepository.text = user.repository
        tvFollowers.text = user.followers
        tvFollowing.text = user.following
        tvCompany.text = user.company
        tvLocation.text = user.location

        backHome()
        btnShare()
    }

    private fun btnShare() {
        btnShare = findViewById(R.id.btnShare)
        btnShare.setOnClickListener{
            setShare()
        }
    }

    private fun backHome() {
        btnBack = findViewById(R.id.btnBack)
        btnBack.setOnClickListener {
            finish()
        }
    }

    private fun setShare() {
        val s = tvUsername.text.toString()
        val shareIntent = Intent()
        shareIntent.action = Intent.ACTION_SEND
        shareIntent.type="text/plain"
        shareIntent.putExtra(Intent.EXTRA_TEXT, "https://github.com/" + s);
        startActivity(Intent.createChooser(shareIntent,"Share via"))
    }
}