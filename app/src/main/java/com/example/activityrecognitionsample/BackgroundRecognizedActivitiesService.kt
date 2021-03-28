package com.example.activityrecognitionsample

import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import com.google.android.gms.location.ActivityRecognitionClient

class BackgroundRecognizedActivitiesService : Service() {
    private val TAG = BackgroundRecognizedActivitiesService::class.java.name
    private var mIntent: Intent? = null
    private var pendingIntent: PendingIntent? = null
    private var activityRecognitionClient: ActivityRecognitionClient? = null
    internal var mBinder: IBinder = LocalBinder()

    inner class LocalBinder : Binder() {
        val service: BackgroundRecognizedActivitiesService
            get() = this@BackgroundRecognizedActivitiesService
    }

    override fun onCreate() {
        super.onCreate()
        activityRecognitionClient = ActivityRecognitionClient(this)
        mIntent = Intent(this, RecognizedActivitiesService::class.java)
        mIntent?.let {
            pendingIntent =
                PendingIntent.getService(this, 1, mIntent!!, PendingIntent.FLAG_UPDATE_CURRENT)
            startActivityUpdates()
        }
    }

    override fun onBind(intent: Intent): IBinder? {
        return mBinder
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        return START_STICKY
    }

    private fun startActivityUpdates() {
        val activityUpdates = activityRecognitionClient?.requestActivityUpdates(
            MainActivity.DETECTION_INTERVAL_IN_MILLISECONDS,
            pendingIntent
        )

        activityUpdates?.addOnSuccessListener {
            Log.e(TAG, "Start Activity Updates Success")
        }

        activityUpdates?.addOnFailureListener {
            Log.e(TAG, "Start Activity Updates Fail")
        }
    }

    fun stopActivityUpdates() {
        val activityUpdates = activityRecognitionClient?.removeActivityUpdates(
            pendingIntent
        )

        activityUpdates?.addOnSuccessListener {
            Log.e(TAG, "Stop Activity Updates Success")
        }

        activityUpdates?.addOnFailureListener {
            Log.e(TAG, "Stop Activity Updates Fail")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        stopActivityUpdates()
    }

}