package com.example.decodingevents.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Looper
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.decodingevents.R
import com.example.decodingevents.data.remote.retrofit.ApiConfig
import com.loopj.android.http.AsyncHttpResponseHandler
import com.loopj.android.http.SyncHttpClient
import cz.msebera.android.httpclient.Header

class NotificationWorker(context: Context, workParams: WorkerParameters): Worker(context, workParams) {

    companion object {
        private val TAG = NotificationWorker::class.java.simpleName
        const val NOTIFICATION_ID = 1
        const val CHANNEL_ID = "channel_1"
        const val CHANNEL_NAME = "channel_name"
    }

    private var resultStatus: Result? = null

    override fun doWork(): Result {
        return getActiveEvent()
    }

    private fun getActiveEvent(): Result {
        Looper.prepare()
        val client = SyncHttpClient()
        val url = "https://event-api.dicoding.dev/events?active=-1&limit=1"
        client.post(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray
            ) {
                val result = String(responseBody)
                Log.d(TAG, result)
                try {
                    val response = ApiConfig.getApiService().getNotification("-1", 1)
                    val event = response.event
                    val eventName = event.name
                    val eventSummary = event.summary
                    val eventStart = event.beginTime
                    val eventEnd = event.endTime
                    showNotification(eventName, eventSummary)
                    resultStatus = Result.success()
                }catch (e:Exception) {
                    Log.d(TAG, "Fail")
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?,
                error: Throwable?
            ) {
                Log.d(TAG, "onFailure: Fail.....")
            }

        })
        return resultStatus as Result
    }

    private fun showNotification(eventName: String, eventSummary: String) {
        val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notification: NotificationCompat.Builder = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notif)
            .setContentTitle(eventName)
            .setContentText(eventSummary)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH)
            notification.setChannelId(CHANNEL_ID)
            notificationManager.createNotificationChannel(channel)
        }
        notificationManager.notify(NOTIFICATION_ID, notification.build())
    }
}