/*
 * Created by Adhitya Bagas on 20/11/2020
 * Copyright (c) 2020 . All rights reserved.
 */

package com.adhityabagasmiwa.dicodingsubmissionbfaa.data.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.adhityabagasmiwa.dicodingsubmissionbfaa.R
import com.adhityabagasmiwa.dicodingsubmissionbfaa.data.model.User
import com.adhityabagasmiwa.dicodingsubmissionbfaa.ui.UserDetailActivity
import kotlinx.android.synthetic.main.item_user_list.view.*

class UserAdapter internal constructor(private val context: Context, private val listUser: List<User>) :
    RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserAdapter.UserViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_user_list, parent, false)
        return UserViewHolder(view)
    }

    override fun getItemCount(): Int = listUser.count()

    override fun onBindViewHolder(holder: UserAdapter.UserViewHolder, position: Int) {
        holder.bind(listUser[position])

        holder.itemView.setOnClickListener {
            val intent = Intent(context, UserDetailActivity::class.java)
            intent.putExtra(UserDetailActivity.EXTRA_USERS_GITHUB, listUser[position])
            context.startActivity(intent)
        }
    }

    inner class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(listUser: User) {
            with(itemView) {
                tvName.text = listUser.name
                tvUsername.text = listUser.username
                imgAvatarMain.setImageResource(listUser.avatar)
            }
        }
    }
}
