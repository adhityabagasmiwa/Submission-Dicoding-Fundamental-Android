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
import android.widget.SearchView
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.adhityabagasmiwa.dicodingsubmissionbfaa.R
import com.adhityabagasmiwa.dicodingsubmissionbfaa.data.adapter.UserGithubAdapter
import com.adhityabagasmiwa.dicodingsubmissionbfaa.databinding.ActivityMainBinding
import com.adhityabagasmiwa.dicodingsubmissionbfaa.viewmodel.MainVieModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var mainViewModel: MainVieModel
    private lateinit var adapter: UserGithubAdapter
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setActionBar()
        setData()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.setting, menu)
        if (menu != null) {
            menu.findItem(R.id.action_share_menu).isVisible = false
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_setting_menu) {
            val mIntent = Intent(this, SettingActivity::class.java)
            startActivity(mIntent)
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showRecyclerView() {
        adapter = UserGithubAdapter()
        adapter.notifyDataSetChanged()
        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter

        getData()
    }

    private fun showSearchData() {
        svUser.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }

            override fun onQueryTextSubmit(query: String?): Boolean {
                stateQuery()
                query?.let { searchUser(it) }
                return false
            }
        })
    }

    private fun searchUser(username: String) {
        showLoading(true)
        mainViewModel.setUser(this, username)
    }

    private fun setData() {
        showRecyclerView()
        showSearchData()
    }

    private fun getData() {
        mainViewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(MainVieModel::class.java)
        mainViewModel.getUser().observe(this, Observer { results ->
            if ((results != null) && (results.size != 0)) {
                adapter.setData(results)
                showLoading(false)
                stateSuccess()
            } else {
                showLoading(false)
                stateFailure()
            }
        })
    }

    @SuppressLint("SetTextI18n")
    private fun setActionBar() {
        val actionBar = supportActionBar

        val tv = TextView(applicationContext)
        val typeface = ResourcesCompat.getFont(this, R.font.montserrat_semibold)
        val lp = RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.MATCH_PARENT,
            RelativeLayout.LayoutParams.WRAP_CONTENT
        )

        tv.layoutParams = lp
        tv.text = "Github User"

        tv.textSize = 20f
        tv.setTextColor(Color.WHITE)
        tv.setTypeface(typeface, Typeface.NORMAL)
        if (actionBar != null) {
            actionBar.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
            actionBar.customView = tv
        }
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
        animateSearch.visibility = View.GONE
        animateNotResult.visibility = View.GONE
    }

    private fun stateFailure() {
        binding.recyclerView.visibility = View.GONE
        animateSearch.visibility = View.GONE
        animateNotResult.visibility = View.VISIBLE
    }

    private fun stateQuery() {
        binding.recyclerView.visibility = View.GONE
        animateSearch.visibility = View.GONE
        animateNotResult.visibility = View.GONE
    }
}