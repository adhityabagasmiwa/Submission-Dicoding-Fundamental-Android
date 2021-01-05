/*
 * Created by Adhitya Bagas on 29/12/2020
 * Copyright (c) 2020 . All rights reserved.
 */

package com.adhityabagasmiwa.dicodingsubmissionbfaa.data.adapter


import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.adhityabagasmiwa.dicodingsubmissionbfaa.R
import com.adhityabagasmiwa.dicodingsubmissionbfaa.data.model.UserGithub
import com.adhityabagasmiwa.dicodingsubmissionbfaa.databinding.ItemUserListBinding
import com.adhityabagasmiwa.dicodingsubmissionbfaa.ui.activities.DetailActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy

class FavoriteAdapter : RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder>() {

    var userList = ArrayList<UserGithub>()
        set(mData) {
            if (mData.size > 0) {
                this.userList.clear()
            }
            this.userList.addAll(mData)
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val mView = LayoutInflater.from(parent.context).inflate(R.layout.item_user_list, parent, false)
        return FavoriteViewHolder(mView)
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        holder.bind(userList[position])

        holder.itemView.setOnClickListener {
            val mContext = holder.itemView.context
            val intent = Intent(mContext, DetailActivity::class.java)
            intent.putExtra(DetailActivity.EXTRA_USERS_GITHUB, userList[position])
            mContext.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = userList.size

    inner class FavoriteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
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