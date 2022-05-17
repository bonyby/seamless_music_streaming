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
import android.widget.Toast
import com.example.seamlessmusiccompanionapp.BuildConfig
import com.google.android.gms.location.*

class MovementCondition : Condition {
    private val mainInstance: MainActivity = MainActivity.instance()!!

    override fun met(): Boolean {
        ensureActivityRecognitionPermissions()

        return false
    }

    private fun ensureActivityRecognitionPermissions() {
        if (mainInstance.checkSelfPermission(Manifest.permission.ACTIVITY_RECOGNITION) != PackageManager.PERMISSION_GRANTED) {
            mainInstance.requestPermissions(
                arrayOf(Manifest.permission.ACTIVITY_RECOGNITION),
                MainActivity.ACTIVITY_RECOGNITION_CODE
            )
        }
    }
}