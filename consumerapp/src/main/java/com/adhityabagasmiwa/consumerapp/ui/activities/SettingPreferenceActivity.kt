/*
 * Created by Adhitya Bagas on 29/12/2020
 * Copyright (c) 2020 . All rights reserved.
 */

package com.adhityabagasmiwa.consumerapp.ui.activities

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.adhityabagasmiwa.consumerapp.R
import com.adhityabagasmiwa.consumerapp.databinding.ActivitySettingBinding
import com.adhityabagasmiwa.consumerapp.ui.fragment.PreferenceFragment

class SettingPreferenceActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setActionBar()
        setPreference()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setActionBar() {
        val actionBarTitle = binding.toolbar
        setSupportActionBar(actionBarTitle)

        supportActionBar?.elevation = 0f

        val actionBar = supportActionBar

        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.setHomeButtonEnabled(true)
    }

    private fun setPreference() {
        supportFragmentManager.beginTransaction().add(R.id.settingHolder, PreferenceFragment()).commit()
    }
}