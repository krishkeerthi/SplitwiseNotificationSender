package com.example.splitwisenotificationsender.model

data class PushNotification(
    val data: NotificationData,
    val fcmToken: String
)
