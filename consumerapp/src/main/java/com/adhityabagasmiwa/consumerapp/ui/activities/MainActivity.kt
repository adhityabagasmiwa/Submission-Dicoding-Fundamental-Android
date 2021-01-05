/*
 * Created by Adhitya Bagas on 1/1/2021
 * Copyright (c) 2021 . All rights reserved.
 */

package com.adhityabagasmiwa.consumerapp.ui.activities

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
import com.adhityabagasmiwa.consumerapp.R
import com.adhityabagasmiwa.consumerapp.data.adapter.FavoriteAdapter
import com.adhityabagasmiwa.consumerapp.data.db.DatabaseContract.UserColumns.Companion.CONTENT_URI
import com.adhityabagasmiwa.consumerapp.data.helper.MappingHelper
import com.adhityabagasmiwa.consumerapp.data.model.UserGithub
import com.adhityabagasmiwa.consumerapp.databinding.ActivityMainBinding
import com.shashank.sony.fancytoastlib.FancyToast
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var adapter: FavoriteAdapter
    private lateinit var binding: ActivityMainBinding

    companion object {
        const val EXTRA_STATE = "extra_state"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setActionBar()

        showRecyclerView()
        initContentResolver(savedInstanceState)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        if (menu != null) {
            menu.findItem(R.id.action_setting_menu).isVisible = true
            menu.findItem(R.id.action_share_menu).isVisible = false
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_setting_menu) {
            val mIntent = Intent(this, SettingPreferenceActivity::class.java)
            startActivity(mIntent)
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
                FancyToast.makeText(this@MainActivity, resources.getString(R.string.data_favorite), FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show()
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

