package com.example.seamlessmusiccompanionapp

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Handler
import android.util.Log
import androidx.core.app.ActivityCompat

class BLEController(private val context: Context) {
    private val handler = Handler()
    private val bleCallback = BLECallback()

    fun emit(): Boolean {
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.BLUETOOTH_SCAN
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            Log.d("bleCallback", "not callback, but we have bluetooth permissions!")
            val bleScanner = BluetoothAdapter.getDefaultAdapter().bluetoothLeScanner
            handler.postDelayed({
                bleScanner.stopScan(bleCallback)
            }, 10000)
            bleScanner.startScan(bleCallback)
        }

        return true
    }
}