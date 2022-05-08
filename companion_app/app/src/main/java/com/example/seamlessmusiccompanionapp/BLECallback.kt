package com.example.seamlessmusiccompanionapp

import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.util.Log

class BLECallback : ScanCallback() {
    override fun onScanFailed(errorCode: Int) {
        super.onScanFailed(errorCode)
        Log.d("bleCallback", "Failed scan :(")
    }

    override fun onScanResult(callbackType: Int, result: ScanResult?) {
        super.onScanResult(callbackType, result)
        Log.d("bleCallback", "SCAN RESULTS!")
    }
}