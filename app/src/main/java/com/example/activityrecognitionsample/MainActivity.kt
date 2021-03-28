package com.example.activityrecognitionsample

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.android.gms.location.DetectedActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var broadcastReceiver: BroadcastReceiver? = null

    companion object {
        val BROADCAST_ACTIVITY_RECOGNITION = "activity_recognition"
        internal val DETECTION_INTERVAL_IN_MILLISECONDS: Long = 1000
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        broadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                if (intent.action == MainActivity.BROADCAST_ACTIVITY_RECOGNITION) {
                    val type = intent.getIntExtra("type", -1)
                    val confidence = intent.getIntExtra("confidence", 0)
                    handleUserActivity(type, confidence)
                }
            }
        }

        val intent = Intent(this@MainActivity, BackgroundRecognizedActivitiesService::class.java)
        startService(intent)
    }

    private fun handleUserActivity(type: Int, confidence: Int) {
        var activityType = "Activity Unknown"

        when (type) {
            DetectedActivity.STILL -> {
                activityType = "Still"
            }
            DetectedActivity.ON_FOOT -> {
                activityType = "On Foot"
            }
            DetectedActivity.WALKING -> {
                activityType = "Walking"
            }
            DetectedActivity.RUNNING -> {
                activityType = "Running"
            }
            DetectedActivity.IN_VEHICLE -> {
                activityType = "In Vehicle"
            }
            DetectedActivity.ON_BICYCLE -> {
                activityType = "On Bicycle"
            }
            DetectedActivity.TILTING -> {
                activityType = "Tilting"
            }
            DetectedActivity.UNKNOWN -> {
                activityType = "Unknown Activity"
            }
        }

        Log.e("Activity Type:", activityType)
        Log.e("Activity Confidence:", confidence.toString())

        if (confidence > 70) {
            txt_type?.text = activityType
            txt_confidence?.text = "Confidence: " + confidence
        }
    }

    override fun onResume() {
        super.onResume()
        broadcastReceiver?.let {
            LocalBroadcastManager.getInstance(this).registerReceiver(
                it,
                IntentFilter(BROADCAST_ACTIVITY_RECOGNITION)
            )
        }
    }

    override fun onPause() {
        super.onPause()
        broadcastReceiver?.let{
            LocalBroadcastManager.getInstance(this).unregisterReceiver(it)
        }
    }
}
