package com.example.splitwisenotificationsender.model

import com.google.gson.annotations.SerializedName

data class PushNotification (

    @SerializedName("message" ) var message : Message?

)