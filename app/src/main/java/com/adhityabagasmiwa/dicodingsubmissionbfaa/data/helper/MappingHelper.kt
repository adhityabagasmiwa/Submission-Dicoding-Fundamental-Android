/*
 * Created by Adhitya Bagas on 29/12/2020
 * Copyright (c) 2020 . All rights reserved.
 */

package com.adhityabagasmiwa.dicodingsubmissionbfaa.data.helper

import android.database.Cursor
import com.adhityabagasmiwa.dicodingsubmissionbfaa.data.db.DatabaseContract
import com.adhityabagasmiwa.dicodingsubmissionbfaa.data.model.UserGithub

object MappingHelper {

    fun mapCursorToArrayList(userCursor: Cursor?): ArrayList<UserGithub> {

        val userList = ArrayList<UserGithub>()

        userCursor?.apply {
            while (moveToNext()) {
                val id = getInt(getColumnIndexOrThrow(DatabaseContract.UserColumns._ID))
                val username = getString(getColumnIndexOrThrow(DatabaseContract.UserColumns.USERNAME))
                val avatar = getString(getColumnIndexOrThrow(DatabaseContract.UserColumns.AVATAR))
                val type = getString(getColumnIndexOrThrow(DatabaseContract.UserColumns.TYPE))
                userList.add(UserGithub(id, username, avatar, type))
            }
        }
        return userList
    }
}