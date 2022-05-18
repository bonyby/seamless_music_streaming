package com.example.seamlessmusiccompanionapp.conditions

import android.content.Context
import android.util.Log
import com.example.seamlessmusiccompanionapp.MainActivity
import android.Manifest
import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.hardware.*
import java.util.Calendar
import android.os.Handler
import android.widget.Toast
import com.example.seamlessmusiccompanionapp.BuildConfig
import com.google.android.gms.location.*

class MovementCondition : Condition {
    private val mainInstance: MainActivity = MainActivity.instance()!!
    private var moving = false
    private val timeoutHandler = Handler()
    private var lastAllowed: Long = Calendar.getInstance().timeInMillis
    private var timeoutPreventionActive = false

    companion object {
        private const val ACCELERATION_THRESHOLD =
            0.1 // Threshold for filtering measurement errors when still
        private const val TIMEOUT_INTERVAL = 5000 // In milliseconds
    }

    init {
        val sensorManager = mainInstance.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        val sensor = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION)
        val eventListener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent?) {
                val values = event?.values!!
                val x = if (values[0] >= ACCELERATION_THRESHOLD) values[0] else 0f
                val y = if (values[1] >= ACCELERATION_THRESHOLD) values[1] else 0f
                val z = if (values[2] >= ACCELERATION_THRESHOLD) values[2] else 0f

                moving =
                    (x + y + z > 0) // If any movement on any axis set moving to true, else false
            }

            override fun onAccuracyChanged(p0: Sensor?, p1: Int) {}
        }
        sensorManager.registerListener(eventListener, sensor, Int.MAX_VALUE)
    }

    override fun met(): Boolean {
        // Bail out fast if currently emitting to prevent timeout
        if (timeoutPreventionActive) {
            return true
        }

        // Prevent timeout if it's been too long
        // since last time device moved
        if (Calendar.getInstance().timeInMillis >= lastAllowed + TIMEOUT_INTERVAL) {
            Log.d("proj", "Preventing timeout")
            timeoutPreventionActive = true

            timeoutHandler.postDelayed({
                lastAllowed = Calendar.getInstance().timeInMillis; timeoutPreventionActive = false
                Log.d("proj", "stopping timeout prevention")
            }, 500) // Reset timeout prevention after 0.5 seconds

            return true
        }

        if (moving) {
            Log.d("proj", "allowed. Updating lastAllowed: ${Calendar.getInstance().timeInMillis}")
            lastAllowed = Calendar.getInstance().timeInMillis
        }

        return moving
    }

//    private fun ensureActivityRecognitionPermissions() {
//        if (mainInstance.checkSelfPermission(Manifest.permission.ACTIVITY_RECOGNITION) != PackageManager.PERMISSION_GRANTED) {
//            mainInstance.requestPermissions(
//                arrayOf(Manifest.permission.ACTIVITY_RECOGNITION),
//                MainActivity.ACTIVITY_RECOGNITION_CODE
//            )
//        }
//    }
}