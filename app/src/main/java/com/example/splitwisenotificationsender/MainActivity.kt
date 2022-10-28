package com.example.splitwisenotificationsender

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.splitwisenotificationsender.api.RetrofitInstance
import com.example.splitwisenotificationsender.databinding.ActivityMainBinding
import com.example.splitwisenotificationsender.model.PushNotification
import com.example.splitwisenotificationsender.model.Message
import com.example.splitwisenotificationsender.model.NotificationData
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // To get token


        // send button
        binding.sendButton.setOnClickListener {
            val title = binding.notificationTitleTextView.text.toString()
            val body = binding.notificationTextTextView.text.toString()
            val token = binding.fcmTokenTextView.text.toString()

            if (title.isNotEmpty() && body.isNotEmpty() && token.isNotEmpty()) {
                PushNotification(
                    Message(
                        token,
                        NotificationData(
                            body,
                            title
                        )
                    )
                ).also {
                    sendNotification(it)
                }
            }
        }
    }

    private fun sendNotification(pushNotification: PushNotification) =
        CoroutineScope(Dispatchers.IO).launch {

            try {
                Log.d(TAG, "sendNotification: $pushNotification")
                val response = RetrofitInstance.api.postNotification(pushNotification)
                if (response.isSuccessful) {
                    Log.d(TAG, "sendNotification: response success ${Gson().toJson(response)}")
                } else {
                    Log.d(
                        TAG,
                        "sendNotification: response failure ${response.raw()}" +
                                "\n${response.code()}" +
                                "\n${response.headers()}\n ${response.errorBody()} \n${response.message()} \n${
                            response.body().toString()
                        }"
                    )
                }

            } catch (e: Exception) {
                Log.e(TAG, "sendNotification: $e")
            }
        }
}