package com.example.decodingevents.helper

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.text.HtmlCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.decodingevents.R
import com.example.decodingevents.data.di.Injection
import com.example.decodingevents.data.local.entity.Event

class ReminderWork(context: Context, workerParams: WorkerParameters): Worker(context, workerParams) {
    private var _resultStatus: Result? = null
    private val resultStatus get() = _resultStatus!!

    companion object {
        const val NOTIFICATION_ID = 1
        const val CHANNEL_ID = "channel_01"
        const val CHANNEL_NAME = "dicoding channel"
    }

    override fun doWork(): Result {
        return startReminder()
    }

    private fun startReminder(): Result {
        try {
            val eventRepository = Injection.provideRepository(applicationContext)
            var event: Event?
            Handler(Looper.getMainLooper()).post {
                eventRepository.getActiveEvents().observeForever { result ->
                    result?.let { data ->
                        when (data) {
                            is com.example.decodingevents.data.source.Result.Error -> _resultStatus =
                                Result.failure()

                            is com.example.decodingevents.data.source.Result.Loading -> {}
                            is com.example.decodingevents.data.source.Result.Success -> {
                                if (data.data.isNotEmpty()) {
                                    event = data.data[0]
                                    Log.d("ReminderWorker", event.toString())
                                    event?.let {
                                        showNotification(
                                            it.name, HtmlCompat.fromHtml(
                                                it.description,
                                                HtmlCompat.FROM_HTML_MODE_LEGACY
                                            ).toString(), it.link
                                        )
                                    }
                                    _resultStatus = Result.success()
                                }
                            }
                        }
                    }
                }
            }
        } catch (e: Exception) {
            Log.e("ReminderWorker", e.message.toString())
        }

        return resultStatus
    }

    private fun showNotification(name: String, description: String, url: String) {
        val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val intent = Intent().apply {
            data = Uri.parse(url)
        }

        val pendingIntent: PendingIntent = PendingIntent.getActivity(
            applicationContext,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )

        val notification: NotificationCompat.Builder = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notif)
            .setContentTitle(name)
            .setContentText(description)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setContentIntent(pendingIntent)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH)
            notification.setChannelId(CHANNEL_ID)
            notificationManager.createNotificationChannel(channel)
        }
        notificationManager.notify(NOTIFICATION_ID, notification.build())
    }
}