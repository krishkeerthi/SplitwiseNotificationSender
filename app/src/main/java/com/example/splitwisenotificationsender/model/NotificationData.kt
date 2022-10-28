package com.example.splitwisenotificationsender.model

import com.google.gson.annotations.SerializedName


data class NotificationData (

    @SerializedName("body"  ) var body  : String? = null,
    @SerializedName("title" ) var title : String? = null,
    @SerializedName("key_1" ) var key1  : String? = null,
    @SerializedName("key_2" ) var key2  : String? = null

)