package com.example.splitwisenotificationsender

import android.R
import android.content.ContentValues.TAG
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Adapter
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.splitwisenotificationsender.api.RetrofitInstance
import com.example.splitwisenotificationsender.databinding.ActivityMainBinding
import com.example.splitwisenotificationsender.model.Message
import com.example.splitwisenotificationsender.model.NotificationData
import com.example.splitwisenotificationsender.model.PushNotification
import com.example.splitwisenotificationsender.model.TokenModel
import com.google.android.material.snackbar.Snackbar
import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.InputStream


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    val recipients = mutableListOf<TokenModel>()
    var selectedRecipientToken = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // To get token
        getRecipients()

        binding.recipientsSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    selectedRecipientToken = recipients[p2].token
                    Log.d(TAG, "onItemSelected: selected token is $selectedRecipientToken")
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                    TODO("Not yet implemented")
                }
            }

        // send button
        binding.sendButton.setOnClickListener {
            val title = binding.notificationTitleTextView.text.toString()
            val body = binding.notificationTextTextView.text.toString()
            val token = selectedRecipientToken//binding.fcmTokenTextView.text.toString()

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

                    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(binding.root.windowToken, 0)

                    Snackbar.make(binding.root, "Notification sent", Snackbar.LENGTH_SHORT).show()
                }
            } else {
                Snackbar.make(binding.root, "Ensure message is entered", Snackbar.LENGTH_SHORT)
                    .show()
            }
        }

        binding.sendAllButton.setOnClickListener {
            val title = binding.notificationTitleTextView.text.toString()
            val body = binding.notificationTextTextView.text.toString()

            if (title.isNotEmpty() && body.isNotEmpty()) {
                for (recipient in recipients) {
                    val token = recipient.token

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

                        val imm =
                            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                        imm.hideSoftInputFromWindow(binding.root.windowToken, 0)

                        Snackbar.make(binding.root, "Notification sent", Snackbar.LENGTH_SHORT)
                            .show()
                    }
                }
            } else {
                Snackbar.make(binding.root, "Ensure message is entered", Snackbar.LENGTH_SHORT)
                    .show()
            }
        }
    }


    private fun getRecipients() {
        val db = Firebase.firestore
        db.collection("fcm_tokens")
            .get()
            .addOnSuccessListener { result ->

                for (document in result) {
                    Log.d(TAG, "${document.id} => ${document.data}")
                    recipients.add(
                        TokenModel(
                            document.data["username"].toString(),
                            document.data["token"].toString()
                        )
                    )
                }

                val arrayAdapter =
                    ArrayAdapter(this, R.layout.simple_spinner_item, recipients)
                arrayAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
                binding.recipientsSpinner.adapter = arrayAdapter
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents.", exception)
            }
    }

    private fun sendNotification(pushNotification: PushNotification) =
        CoroutineScope(Dispatchers.IO).launch {

            // getting token
            val fis: InputStream = applicationContext.resources.openRawResource(
                applicationContext.resources.getIdentifier(
                    "serviceaccount", "raw",
                    packageName
                )
            )

            val googleCredentials: GoogleCredentials =
                withContext(Dispatchers.IO) {
                    GoogleCredentials
                        .fromStream(fis)
                }
                    //  .fromStream(FileInputStream(File("app/src/main/java/com/example/splitwisenotificationsender/credentials/serviceaccount.json")))
                    .createScoped(mutableListOf("https://www.googleapis.com/auth/firebase.messaging"))
            googleCredentials.refreshAccessToken()

            val token = googleCredentials.refreshAccessToken().tokenValue

            Log.d(TAG, "sendNotification: access token is ${token}")
//            val url = URL(FCM_FULL_URL)
//            val  httpURLConnection = url.openConnection()
//            httpURLConnection.setRequestProperty("Authorization", "Bearer $NEW_ACCESS_TOKEN")
//            httpURLConnection.setRequestProperty("Content-Type", "application/json; UTF-8")
//            httpURLConnection.connect()


            // Sending notification


            // Log.d(TAG, "sendNotification: received access token ${getAccessToken()} \n")
            try {
                Log.d(TAG, "sendNotification: $pushNotification")
                val response =
                    RetrofitInstance.api.postNotification("Bearer $token", pushNotification)
                if (response.isSuccessful) {
                    Log.d(TAG, "sendNotification: response success ${response.raw()}")
                    //Log.d(TAG, "sendNotification: response success ${Gson().toJson(response)}")
                } else {
                    Log.d(
                        TAG,
                        "sendNotification: response failure ${response.isSuccessful}" +
                                "${response.raw()}" +
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
