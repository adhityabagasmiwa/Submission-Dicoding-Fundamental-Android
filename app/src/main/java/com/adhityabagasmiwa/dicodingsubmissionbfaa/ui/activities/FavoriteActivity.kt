/*
 * Created by Adhitya Bagas on 29/12/2020
 * Copyright (c) 2020 . All rights reserved.
 */

package com.adhityabagasmiwa.dicodingsubmissionbfaa.ui.activities

import android.content.Intent
import android.database.ContentObserver
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.adhityabagasmiwa.dicodingsubmissionbfaa.R
import com.adhityabagasmiwa.dicodingsubmissionbfaa.data.adapter.FavoriteAdapter
import com.adhityabagasmiwa.dicodingsubmissionbfaa.data.db.DatabaseContract.UserColumns.Companion.CONTENT_URI
import com.adhityabagasmiwa.dicodingsubmissionbfaa.data.db.UserHelper
import com.adhityabagasmiwa.dicodingsubmissionbfaa.data.helper.MappingHelper
import com.adhityabagasmiwa.dicodingsubmissionbfaa.data.model.UserGithub
import com.adhityabagasmiwa.dicodingsubmissionbfaa.databinding.ActivityFavoriteBinding
import com.shashank.sony.fancytoastlib.FancyToast
import kotlinx.android.synthetic.main.activity_favorite.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class FavoriteActivity : AppCompatActivity() {

    private lateinit var adapter: FavoriteAdapter
    private lateinit var binding: ActivityFavoriteBinding
    private lateinit var userHelper: UserHelper

    companion object {
        const val EXTRA_STATE = "extra_state"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setActionBar()

        userHelper = UserHelper.getInstance(applicationContext)
        userHelper.open()

        showRecyclerView()
        initContentResolver(savedInstanceState)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        if (menu != null) {
            menu.findItem(R.id.action_fav_menu).isVisible = false
            menu.findItem(R.id.action_share_menu).isVisible = false
            menu.findItem(R.id.action_setting_menu).isVisible = true
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_setting_menu) {
            val mIntent = Intent(this, SettingPreferenceActivity::class.java)
            startActivity(mIntent)
            return true
        }
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initContentResolver(savedInstanceState: Bundle?) {
        val handlerThread = HandlerThread("DataObserver")
        handlerThread.start()
        val handler = Handler(handlerThread.looper)

        val myObserver = object : ContentObserver(handler) {
            override fun onChange(selfChange: Boolean) {
                loadUserAsync()
            }
        }

        contentResolver.registerContentObserver(CONTENT_URI, true, myObserver)

        if (savedInstanceState == null) {
            loadUserAsync()
        } else {
            val list = savedInstanceState.getParcelableArrayList<UserGithub>(EXTRA_STATE)
            if (list != null) {
                adapter.userList = list
            }
        }
    }

    private fun loadUserAsync() {
        GlobalScope.launch(Dispatchers.Main) {
            showLoading(true)
            val deferredUser = async(Dispatchers.IO) {
                val cursor = contentResolver.query(CONTENT_URI, null, null, null, null)
                MappingHelper.mapCursorToArrayList(cursor)
            }
            val user = deferredUser.await()
            if (user.size > 0) {
                adapter.userList = user
                stateSuccess()
            } else {
                adapter.userList = ArrayList()
                stateEmpty()
                FancyToast.makeText(this@FavoriteActivity, resources.getString(R.string.data_favorite), FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show()
            }
            showLoading(false)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList(EXTRA_STATE, adapter.userList)
    }

    private fun showRecyclerView() {
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.setHasFixedSize(true)

        adapter = FavoriteAdapter()

        binding.recyclerView.adapter = adapter
    }

    private fun setActionBar() {
        val actionBarTitle = binding.toolbar
        setSupportActionBar(actionBarTitle)
        actionBarTitle.title = resources.getString(R.string.favorite)
        supportActionBar?.elevation = 0f

        val actionBar = supportActionBar

        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.setHomeButtonEnabled(true)
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    private fun stateSuccess() {
        binding.recyclerView.visibility = View.VISIBLE
    }

    private fun stateEmpty() {
        binding.recyclerView.visibility = View.GONE
        animateEmpty.visibility = View.VISIBLE
    }

    override fun onResume() {
        super.onResume()
        showRecyclerView()
        loadUserAsync()
    }
}