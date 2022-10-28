package com.example.splitwisenotificationsender.model

import com.google.gson.annotations.SerializedName

data class Message (

    @SerializedName("token" ) var token : String? = null,
    @SerializedName("data"  ) var data  : NotificationData?

)