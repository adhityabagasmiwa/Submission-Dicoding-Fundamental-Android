/*
 * Created by Adhitya Bagas on 29/12/2020
 * Copyright (c) 2020 . All rights reserved.
 */

package com.adhityabagasmiwa.consumerapp.data.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.adhityabagasmiwa.consumerapp.R
import com.adhityabagasmiwa.consumerapp.data.model.UserGithub
import com.adhityabagasmiwa.consumerapp.databinding.ItemUserListBinding
import com.adhityabagasmiwa.consumerapp.ui.activities.DetailActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy

class FollowListAdapter : RecyclerView.Adapter<FollowListAdapter.FollowViewHolder>() {

    private val followList = ArrayList<UserGithub>()

    fun setData(items: ArrayList<UserGithub>) {
        followList.clear()
        followList.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FollowViewHolder {
        val mView = LayoutInflater.from(parent.context).inflate(R.layout.item_user_list, parent, false)
        return FollowViewHolder(mView)
    }

    override fun onBindViewHolder(holder: FollowViewHolder, position: Int) {
        holder.bind(followList[position])

        holder.itemView.setOnClickListener {
            val mContext = holder.itemView.context
            val intent = Intent(mContext, DetailActivity::class.java)
            intent.putExtra(DetailActivity.EXTRA_USERS_GITHUB, followList[position])
            mContext.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = followList.size

    inner class FollowViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = ItemUserListBinding.bind(itemView)
        fun bind(userGithub: UserGithub) {
            with(itemView) {
                Glide.with(context)
                    .load(userGithub.avatarUrl)
                    .placeholder(R.drawable.github)
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                    .into(binding.imgAvatarMain)
                binding.tvUsername.text = userGithub.login
                binding.tvType.text = userGithub.type
            }
        }
    }
}