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
import org.altbeacon.beacon.*
import java.nio.charset.Charset
import java.util.*

class BLEController(private val context: Context){
    private val uuid = "ff965a22-aeb2-4be4-b3f7-e9de4dea4a52"
    private val bleCallback = BLECallback()

    fun emit(): Boolean {
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.BLUETOOTH_ADMIN
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            val beacon = Beacon.Builder()
                .setId1(uuid)
                .setId2("1")
                .setId3("2")
                .setManufacturer(0x004c)
                .setTxPower(-59)
                .build()

            val beaconParser = BeaconParser().setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24")
            val beaconTransmitter = BeaconTransmitter(context, beaconParser)
            beaconTransmitter.startAdvertising(beacon, bleCallback)
        } else {
            return false
        }

        return true
    }
}