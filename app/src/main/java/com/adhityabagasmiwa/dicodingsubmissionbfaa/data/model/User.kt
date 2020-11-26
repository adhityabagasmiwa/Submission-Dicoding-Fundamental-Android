/*
 * Created by Adhitya Bagas on 21/11/2020
 * Copyright (c) 2020 . All rights reserved.
 */

package com.adhityabagasmiwa.dicodingsubmissionbfaa.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User(
    var avatar: Int,
    var name: String,
    var username: String,
    var location: String,
    var company: String,
    var repository: String,
    var followers: String,
    var following: String
) : Parcelable