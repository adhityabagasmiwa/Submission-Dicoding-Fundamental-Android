package com.adhityabagasmiwa.dicodingsubmissionbfaa.data.model

import com.google.gson.annotations.SerializedName

data class SearchUserResponse(

    @field:SerializedName("items")
    val items: ArrayList<UserGithub>

)