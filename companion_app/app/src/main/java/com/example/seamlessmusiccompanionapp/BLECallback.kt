package com.example.seamlessmusiccompanionapp

import android.annotation.SuppressLint
import android.bluetooth.le.AdvertiseCallback
import android.bluetooth.le.AdvertiseSettings
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.util.Log

class BLECallback : AdvertiseCallback() {
    override fun onStartFailure(errorCode: Int) {
        super.onStartFailure(errorCode)
        Log.d("proj", "advertisement failure. Error code: $errorCode")
    }

    override fun onStartSuccess(settingsInEffect: AdvertiseSettings?) {
        super.onStartSuccess(settingsInEffect)
        Log.d("proj", "advertisement success: $settingsInEffect")
    }
}