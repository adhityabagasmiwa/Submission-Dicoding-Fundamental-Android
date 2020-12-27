package com.adhityabagasmiwa.dicodingsubmissionbfaa.ui.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager.widget.ViewPager
import com.adhityabagasmiwa.dicodingsubmissionbfaa.R
import com.adhityabagasmiwa.dicodingsubmissionbfaa.data.adapter.SectionPageAdapter
import com.adhityabagasmiwa.dicodingsubmissionbfaa.data.model.UserGithub
import com.adhityabagasmiwa.dicodingsubmissionbfaa.databinding.ActivityDetailBinding
import com.adhityabagasmiwa.dicodingsubmissionbfaa.viewmodel.MainVieModel
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_detail.*

class DetailActivity : AppCompatActivity() {

    private lateinit var mainVieModel: MainVieModel
    private lateinit var binding: ActivityDetailBinding

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

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.setting, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_setting_menu) {
            val mIntent = Intent(this, SettingActivity::class.java)
            startActivity(mIntent)
            return true
        }
        if (item.itemId == R.id.action_share_menu) {
            shareUser()
            return true
        }
        if (item.itemId == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setData() {
        val intent = intent.getParcelableExtra<UserGithub>(EXTRA_USERS_GITHUB)
        getData(intent?.login)
    }

    @SuppressLint("SetTextI18n")
    private fun getData(username: String?) {
        mainVieModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(MainVieModel::class.java)
        mainVieModel.setUserDetail(this, username)
        mainVieModel.getUserDetail().observe(this, Observer { results ->
            Glide.with(this)
                .load(results.avatarUrl)
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .into(imgAvatarDetail)
            binding.tvName.text = results.name
            binding.tvUsername.text = results.login
            binding.tvCompany.text = results.company ?: ""
            binding.tvLocation.text = results.location ?: ""
            binding.tvFollower.text =
                results.followers.toString() + " " + resources.getString(R.string.follower)
            binding.tvFollowing.text =
                results.following.toString() + " " + resources.getString(R.string.following)
            binding.tvRespository.text =
                results.public_repos + " " + resources.getString(R.string.repository)
            showLoading(false)
        })

    }

    @SuppressLint("SetTextI18n")
    private fun setActionBar() {
        supportActionBar?.elevation = 0f

        val actionBar = supportActionBar

        val tv = TextView(applicationContext)
        val typeface = ResourcesCompat.getFont(this, R.font.montserrat_semibold)
        val lp = RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.WRAP_CONTENT,
            RelativeLayout.LayoutParams.WRAP_CONTENT
        )

        tv.layoutParams = lp
        tv.text = resources.getString(R.string.profile)

        tv.textSize = 20f
        tv.setTextColor(Color.WHITE)
        tv.setTypeface(typeface, Typeface.NORMAL)
        actionBar?.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.setHomeButtonEnabled(true)
        actionBar?.customView = tv
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
}