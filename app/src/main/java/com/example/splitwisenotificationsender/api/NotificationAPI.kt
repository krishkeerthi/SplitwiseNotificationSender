package com.example.splitwisenotificationsender.api

import com.example.splitwisenotificationsender.Constants.Companion.ACCESS_TOKEN
import com.example.splitwisenotificationsender.Constants.Companion.getAccessToken
import com.example.splitwisenotificationsender.model.PushNotification
//import com.google.auth.oauth2.AccessToken
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST

const val endPoint = "./messages:send"
interface NotificationAPI {

    //@Headers("Authorization:Bearer $ACCESS_TOKEN")
    @POST(endPoint)
    suspend fun postNotification(
        @Header("Authorization") token: String, @Body notification: PushNotification
    ) : Response<ResponseBody>
}

// without retrofit
//URL url = new URL(BASE_URL + FCM_SEND_ENDPOINT);
//HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
//httpURLConnection.setRequestProperty("Authorization", "Bearer " + getAccessToken());
//httpURLConnection.setRequestProperty("Content-Type", "application/json; UTF-8");
//return httpURLConnection;

// refer this if doubt
//https://github.com/firebase/quickstart-java/blob/f75816cd181cdaf49401db3b3b52e4f20f629470/messaging/src/main/java/com/google/firebase/quickstart/Messaging.java#L62-L66

// Getting access token
//@Throws(IOException::class)
//private fun getAccessToken(): String? {
//    val googleCredentials: GoogleCredentials = GoogleCredentials
//        .fromStream(FileInputStream("serviceaccount.json"))
//        .createScoped(Arrays.asList(SCOPES))
//    googleCredentials.refreshAccessToken()
//    return googleCredentials.getAccessToken().getTokenValue()
//}