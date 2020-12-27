package com.adhityabagasmiwa.dicodingsubmissionbfaa.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.adhityabagasmiwa.dicodingsubmissionbfaa.data.adapter.FollowListAdapter
import com.adhityabagasmiwa.dicodingsubmissionbfaa.data.model.UserGithub
import com.adhityabagasmiwa.dicodingsubmissionbfaa.databinding.FragmentFollowingBinding
import com.adhityabagasmiwa.dicodingsubmissionbfaa.ui.activities.DetailActivity.Companion.EXTRA_USERS_GITHUB
import com.adhityabagasmiwa.dicodingsubmissionbfaa.viewmodel.MainVieModel

class FollowingFragment : Fragment() {

    private lateinit var mainVieModel: MainVieModel
    private lateinit var adapter: FollowListAdapter
    private lateinit var binding: FragmentFollowingBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentFollowingBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setData()
    }

    private fun showRecyclerView() {
        adapter = FollowListAdapter()
        adapter.notifyDataSetChanged()
        binding.recyclerViewFollow.setHasFixedSize(true)
        binding.recyclerViewFollow.layoutManager = LinearLayoutManager(activity)
        binding.recyclerViewFollow.adapter = adapter
    }

    private fun setData() {
        val intent = activity!!.intent.getParcelableExtra<UserGithub>(EXTRA_USERS_GITHUB)
        getData(intent?.login)
        showRecyclerView()
    }

    private fun getData(username: String?) {
        mainVieModel = ViewModelProvider(
            activity!!,
            ViewModelProvider.NewInstanceFactory()
        ).get(MainVieModel::class.java)
        mainVieModel.setFollowingList(activity!!, username)
        mainVieModel.getFollowingList().observe(activity!!, Observer { results ->
            adapter.setData(results)
        })
    }

}