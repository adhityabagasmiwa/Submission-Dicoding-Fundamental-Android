/*
 * Created by Adhitya Bagas on 29/12/2020
 * Copyright (c) 2020 . All rights reserved.
 */

package com.adhityabagasmiwa.dicodingsubmissionbfaa.data.model

import com.google.gson.annotations.SerializedName

data class SearchUserResponse(

    @field:SerializedName("items")
    val items: ArrayList<UserGithub>

)