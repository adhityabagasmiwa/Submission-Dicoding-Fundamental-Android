/*
 * Created by Adhitya Bagas on 20/11/2020
 * Copyright (c) 2020 . All rights reserved.
 */

package com.adhityabagasmiwa.dicodingsubmissionbfaa.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.adhityabagasmiwa.dicodingsubmissionbfaa.R

class SplashScreenActivity : AppCompatActivity() {

    private lateinit var handler: Handler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.hide()

        setContentView(R.layout.activity_splash_screen)

        handler = Handler()
        handler.postDelayed({
            val intent = Intent(this@SplashScreenActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }, 5000)
    }
}