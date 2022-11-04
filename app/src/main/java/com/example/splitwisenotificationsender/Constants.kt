package com.example.splitwisenotificationsender

import java.io.FileInputStream
import java.util.*
import com.google.auth.oauth2.GoogleCredentials
import com.google.gson.annotations.SerializedName
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

class Constants {
    companion object {
        const val BASE_URL = "https://fcm.googleapis.com/v1/projects/splitwise-krish/"
        const val FCM_FULL_URL = "https://fcm.googleapis.com/v1/projects/splitwise-krish/messages:send"
        const val ACCESS_TOKEN =
            "ya29.c.b0AUFJQsHn_E4xNlX6E4KmBeHEalw2kV_YFo1RbuOJPkwgZX5LMGgRoQkOTkzJiJ1VFcKuneeXZKkHhqC6g8IVSIw8sojMw_iHl-IVti67pswLRshY3Yp0lgHFc02R0G6DLQhSSvpedzSg-AKeoQK79qCXLryPAhO8_UVXOqbJvMr-B579J8UVYCIqWMFzXPBetlaIIt7qNNNazO_-OpTSlAMfTbsFl4Q"
        const val CONTENT_TYPE = "application/json"


        private const val MESSAGING_SCOPE = "https://www.googleapis.com/auth/firebase.messaging"
        private val SCOPES = arrayOf(MESSAGING_SCOPE)

        //val NEW_ACCESS_TOKEN = getAccessToken()
//        private fun getAccessToken(): String {
//            val googleCredentials: GoogleCredentials = GoogleCredentials
//                .fromStream(FileInputStream("serviceaccount.json"))
//              //  .fromStream(FileInputStream(File("app/src/main/java/com/example/splitwisenotificationsender/credentials/serviceaccount.json")))
//                .createScoped(SCOPES.toMutableList())
//            googleCredentials.refreshAccessToken()
//            return googleCredentials.accessToken.tokenValue.toString()
//        }

        suspend fun getAccessToken() = withContext(Dispatchers.IO){
            val googleCredentials: GoogleCredentials = GoogleCredentials
                .fromStream(FileInputStream("serviceaccount.json"))
                //  .fromStream(FileInputStream(File("app/src/main/java/com/example/splitwisenotificationsender/credentials/serviceaccount.json")))
                .createScoped(SCOPES.toMutableList())
            googleCredentials.refreshAccessToken()
            googleCredentials.accessToken.tokenValue.toString()
        }
    }
}