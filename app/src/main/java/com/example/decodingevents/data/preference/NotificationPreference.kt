//package com.example.decodingevents.data.preference
//
//import android.content.Context
//import androidx.work.Constraints
//import androidx.work.NetworkType
//import androidx.work.PeriodicWorkRequest
//import androidx.work.WorkManager
//import com.example.decodingevents.util.NotificationWorker
//import java.util.concurrent.TimeUnit
//
//class NotificationPreference {
//
//    fun startPeriodicTable() {
//        val constraint = Constraints.Builder()
//            .setRequiredNetworkType(NetworkType.CONNECTED)
//            .build()
//        periodicWorkRequest = PeriodicWorkRequest.Builder(NotificationWorker::class.java, 1, TimeUnit.DAYS)
//            .setConstraints(constraint)
//            .build()
//
//        workManager.enqueue(periodicWorkRequest)
//        workManager.getWorkInfoByIdLiveData(periodicWorkRequest.id)
//    }
//
//    fun cancelPeriodicTable() {
//        workManager.cancelWorkById(periodicWorkRequest.id)
//    }
//
//    companion object {
//        @Volatile
//        private var INSTANCE: NotificationPreference? = null
//
//        fun getInstance(periodicWorkRequest: PeriodicWorkRequest, workManager: WorkManager): NotificationPreference {
//            return INSTANCE ?: synchronized(this) {
//                val instance = NotificationPreference(periodicWorkRequest, workManager)
//                INSTANCE = instance
//                instance
//            }
//        }
//    }
//}