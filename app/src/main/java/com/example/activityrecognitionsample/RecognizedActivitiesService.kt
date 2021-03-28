package com.example.activityrecognitionsample

import android.app.IntentService
import android.content.Intent
import android.util.Log
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.android.gms.location.ActivityRecognitionResult
import com.google.android.gms.location.DetectedActivity

class RecognizedActivitiesService : IntentService(TAG) {

    companion object {
        private val TAG = RecognizedActivitiesService::class.java.name
    }

    override fun onHandleIntent(receivedIntent: Intent?) {

        val recognitionResult = ActivityRecognitionResult.extractResult(receivedIntent)

        val recognizedActivities = recognitionResult.probableActivities as ArrayList<*>

        for (activity in recognizedActivities) {
            val intent = Intent(MainActivity.BROADCAST_ACTIVITY_RECOGNITION)
            intent.putExtra("type", (activity as DetectedActivity).type)
            intent.putExtra("confidence", (activity as DetectedActivity).confidence)
            LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
        }
    }

}