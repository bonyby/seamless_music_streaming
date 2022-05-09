package com.example.seamlessmusiccompanionapp

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.le.AdvertiseData
import android.bluetooth.le.AdvertiseSettings
import android.content.Context
import android.content.pm.PackageManager
import android.os.Handler
import android.os.ParcelUuid
import android.util.Log
import androidx.core.app.ActivityCompat
import java.nio.charset.Charset
import java.util.*

class BLEController(private val context: Context) {
    private val uuid = "ff965a22-aeb2-4be4-b3f7-e9de4dea4a52"
    private val pUuid = ParcelUuid(UUID.fromString(uuid))
    private val handler = Handler()
    private val bleCallback = BLECallback()
    private val advertiseSettings = AdvertiseSettings.Builder()
        .setAdvertiseMode(AdvertiseSettings.ADVERTISE_MODE_LOW_LATENCY)
        .setConnectable(true)
        .setTxPowerLevel(AdvertiseSettings.ADVERTISE_TX_POWER_HIGH)
        .build()
    private val advertiseData = AdvertiseData.Builder()
        .setIncludeDeviceName(true)
//        .setIncludeTxPowerLevel(true)
//        .addServiceUuid(pUuid)
//        .addServiceData(pUuid, "Data".getBytes(Charset.forName("UTF-8")))
//        .addServiceData(pUuid, "test".toByteArray())
        .build()

    fun emit(): Boolean {
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.BLUETOOTH_ADMIN
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            Log.d("proj", "not callback, but we have bluetooth permissions!")
            val adapter = BluetoothAdapter.getDefaultAdapter()
            Log.d("proj", "bonded devices: ${adapter.name}")
            val bleAdvertiser = adapter.bluetoothLeAdvertiser
//            Log.d("proj", "adapter address: ${bleAdvertiser.)}")
            handler.postDelayed({
                bleAdvertiser.stopAdvertising(bleCallback)
            }, 60000)
            bleAdvertiser.startAdvertising(advertiseSettings, advertiseData, bleCallback)
            Log.d("proj", "data: ${advertiseData.serviceData}")
        } else {
            Log.d("proj", "no bluetooth permissions")
            return false
        }

        return true
    }
}