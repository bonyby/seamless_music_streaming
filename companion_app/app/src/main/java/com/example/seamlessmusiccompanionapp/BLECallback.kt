package com.example.seamlessmusiccompanionapp

import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.util.Log

class BLECallback : ScanCallback() {
//    private val leDeviceListAdapter = LeDeviceListAdapter()

    override fun onScanFailed(errorCode: Int) {
        super.onScanFailed(errorCode)
        Log.d("seamless proj", "Failed scan :(")
    }

    override fun onScanResult(callbackType: Int, result: ScanResult?) {
        super.onScanResult(callbackType, result)
        Log.d("seamless proj", "SCAN RESULTS!: ${result.toString()}")
    }
}