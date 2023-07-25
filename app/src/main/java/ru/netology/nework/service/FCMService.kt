package ru.netology.nework.service

import android.Manifest
import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import dagger.hilt.android.AndroidEntryPoint
import ru.netology.nework.R
import ru.netology.nework.auth.AppAuth
import javax.inject.Inject
import kotlin.random.Random

@SuppressLint("MissingFirebaseInstanceTokenRefresh")
@AndroidEntryPoint
class FCMService : FirebaseMessagingService() {
    private val channelId = "remote"
    private val gson = Gson()

    @Inject
    lateinit var auth: AppAuth

    override fun onCreate() {
        super.onCreate()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.channel_remote_name)
            val descriptionText = getString(R.string.channel_remote_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(channelId, name, importance).apply {
                description = descriptionText
            }
            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
    }

    override fun onMessageReceived(message: RemoteMessage) {
        try {
            println(message.data["content"])
            val push = gson.fromJson(message.data["content"], Push::class.java)
            handlePush(push)
        } catch (e: JsonSyntaxException) {
            Log.e("FCMService", "can't not parse: ${message.data["content"]}")
        }
    }

    private fun handlePush(push: Push) {
        val notification = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.baseline_event_24)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentText(push.content)
            .build()

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        NotificationManagerCompat.from(this)
            .notify(Random.nextInt(100_000), notification)
    }

    data class Push(val recipiendId: Long?, val content: String)
}
