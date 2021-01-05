/*
 * Created by Adhitya Bagas on 29/12/2020
 * Copyright (c) 2020 . All rights reserved.
 */

package com.adhityabagasmiwa.consumerapp.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.adhityabagasmiwa.consumerapp.data.adapter.FollowListAdapter
import com.adhityabagasmiwa.consumerapp.data.model.UserGithub
import com.adhityabagasmiwa.consumerapp.databinding.FragmentFollowingBinding
import com.adhityabagasmiwa.consumerapp.ui.activities.DetailActivity.Companion.EXTRA_USERS_GITHUB
import com.adhityabagasmiwa.consumerapp.viewmodel.MainVieModel


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
        val intent = activity?.intent?.getParcelableExtra<UserGithub>(EXTRA_USERS_GITHUB)
        getData(intent?.login)
        showRecyclerView()
    }

    private fun getData(username: String?) {
        mainVieModel = ViewModelProvider(requireActivity(), ViewModelProvider.NewInstanceFactory()).get(MainVieModel::class.java)
        mainVieModel.setFollowingList(requireActivity(), username)
        mainVieModel.getFollowingList().observe(requireActivity(), { results ->
            adapter.setData(results)
        })
    }
}