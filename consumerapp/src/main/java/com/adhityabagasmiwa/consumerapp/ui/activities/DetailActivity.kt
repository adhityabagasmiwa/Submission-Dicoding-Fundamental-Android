/*
 * Created by Adhitya Bagas on 1/1/2021
 * Copyright (c) 2021 . All rights reserved.
 */

package com.adhityabagasmiwa.consumerapp.ui.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.database.sqlite.SQLiteConstraintException
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.contentValuesOf
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager.widget.ViewPager
import com.adhityabagasmiwa.consumerapp.R
import com.adhityabagasmiwa.consumerapp.data.adapter.SectionPageAdapter
import com.adhityabagasmiwa.consumerapp.data.db.DatabaseContract
import com.adhityabagasmiwa.consumerapp.data.db.DatabaseContract.UserColumns.Companion.CONTENT_URI
import com.adhityabagasmiwa.consumerapp.data.model.UserGithub
import com.adhityabagasmiwa.consumerapp.databinding.ActivityDetailBinding
import com.adhityabagasmiwa.consumerapp.viewmodel.MainVieModel
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.android.material.tabs.TabLayout
import com.shashank.sony.fancytoastlib.FancyToast
import kotlinx.android.synthetic.main.activity_detail.*

class DetailActivity : AppCompatActivity() {

    private lateinit var mainVieModel: MainVieModel
    private lateinit var binding: ActivityDetailBinding
    private var statusFavoriteUser: Boolean = false
    private lateinit var uriWithId: Uri

    companion object {
        const val EXTRA_USERS_GITHUB = "extra_users_github"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setActionBar()
        setData()
        showViewPager()
        showLoading(true)

        btnAddUserFavorite()
        checkDataOnDB()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        if (menu != null) {
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
        if (item.itemId == R.id.action_share_menu) {
            shareUser()
            return true
        }
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setData() {
        val intent = intent.getParcelableExtra<UserGithub>(EXTRA_USERS_GITHUB)
        getData(intent?.login)
    }

    @SuppressLint("SetTextI18n")
    private fun getData(username: String?) {
        val actionBarTitle = binding.toolbar
        setSupportActionBar(actionBarTitle)

        mainVieModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(MainVieModel::class.java)
        mainVieModel.setUserDetail(this, username)
        mainVieModel.getUserDetail().observe(this, { results ->
            if (results != null) {
                Glide.with(this)
                    .load(results.avatarUrl)
                    .placeholder(R.drawable.github)
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                    .into(imgAvatarDetail)
                binding.tvName.text = results.name
                binding.tvUsername.text = results.login
                binding.tvCompany.text = results.company ?: ""
                binding.tvLocation.text = results.location ?: ""
                binding.tvFollower.text = results.followers.toString() + " " + resources.getString(R.string.follower)
                binding.tvFollowing.text = results.following.toString() + " " + resources.getString(R.string.following)
                binding.tvRespository.text = results.public_repos.toString() + " " + resources.getString(R.string.repository)
                actionBarTitle.title = results.name
                showLoading(false)
            }
        })
    }

    private fun setActionBar() {
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

    private fun showViewPager() {
        val sectionsPagerAdapter = SectionPageAdapter(this, supportFragmentManager)
        val viewPager: ViewPager = binding.viewPager
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = binding.tabsLayout
        tabs.setupWithViewPager(viewPager)

        supportActionBar?.elevation = 0f
    }

    private fun shareUser() {
        val s = binding.tvUsername.text
        val url = "https://github.com/"
        val shareIntent = Intent()
        shareIntent.action = Intent.ACTION_SEND
        shareIntent.type = "text/plain"
        shareIntent.putExtra(Intent.EXTRA_TEXT, url + s)
        startActivity(Intent.createChooser(shareIntent, "Share via"))
    }

    private fun btnAddUserFavorite() {
        setStatusFavoriteUser(statusFavoriteUser)
        fabAdd.setOnClickListener {
            if (!statusFavoriteUser) {
                statusFavoriteUser = !statusFavoriteUser
                addUserToFavorite()
                setStatusFavoriteUser(statusFavoriteUser)
            } else {
                statusFavoriteUser = !statusFavoriteUser
                removeUserFromFavorite()
                setStatusFavoriteUser(statusFavoriteUser)
            }
        }
    }

    private fun addUserToFavorite() {
        try {
            val user = intent.getParcelableExtra<UserGithub>(EXTRA_USERS_GITHUB)
            val id = user?.id.toString()
            val username = user?.login.toString()
            val avatar = user?.avatarUrl.toString()
            val type = user?.type.toString()

            val values = contentValuesOf(
                DatabaseContract.UserColumns._ID to id,
                DatabaseContract.UserColumns.USERNAME to username,
                DatabaseContract.UserColumns.AVATAR to avatar,
                DatabaseContract.UserColumns.TYPE to type
            )

            contentResolver.insert(CONTENT_URI, values)

            FancyToast.makeText(this, resources.getString(R.string.added_favorite), FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, false).show()
        } catch (e: SQLiteConstraintException) {
            e.printStackTrace()
        }
    }

    private fun removeUserFromFavorite() {
        try {
            val user = intent.getParcelableExtra<UserGithub>(EXTRA_USERS_GITHUB)
            val id = user?.id.toString()

            uriWithId = Uri.parse("$CONTENT_URI/$id")
            contentResolver.delete(uriWithId, null, null)

            FancyToast.makeText(this, resources.getString(R.string.removed_favorite), FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, false).show()
        } catch (e: SQLiteConstraintException) {
            e.printStackTrace()
        }
    }

    private fun setStatusFavoriteUser(statusFavoriteUser: Boolean) {
        if (statusFavoriteUser) {
            binding.fabAdd.setImageResource(R.drawable.ic_favorite_24)
        } else {
            binding.fabAdd.setImageResource(R.drawable.ic_favorite_border_24)
        }
    }

    @SuppressLint("Recycle")
    private fun checkDataOnDB() {
        val user = intent.getParcelableExtra<UserGithub>(EXTRA_USERS_GITHUB)
        val id = user?.id.toString()

        uriWithId = Uri.parse("$CONTENT_URI/$id")
        val cursor = contentResolver.query(uriWithId, null, "${DatabaseContract.UserColumns._ID} = ?", null, null)

        if (cursor?.moveToNext()!!) {
            statusFavoriteUser = true
            setStatusFavoriteUser(statusFavoriteUser)
        }
    }
}