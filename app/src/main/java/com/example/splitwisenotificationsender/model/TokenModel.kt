package com.example.splitwisenotificationsender.model

data class TokenModel(
    val userName: String,
    val token: String
){
    override fun toString(): String {
        return userName
    }
}
