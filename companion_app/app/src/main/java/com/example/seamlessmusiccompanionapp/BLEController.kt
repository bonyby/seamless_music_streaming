package com.example.seamlessmusiccompanionapp

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.ActivityCompat
import org.altbeacon.beacon.Beacon
import org.altbeacon.beacon.BeaconParser
import org.altbeacon.beacon.BeaconTransmitter
import java.util.*

class BLEController(private val context: Context){
    private val bleCallback = BLECallback()
    private val packageUUID = UUID.randomUUID().toString()

    init {
        // Fetch packageUUID if present. Generate new otherwise
//        val sharedPref = context.getPrefe
    }

    fun emit(): Boolean {
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.BLUETOOTH_ADMIN
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            Log.d("proj", "uuid: $packageUUID")
            val beacon = Beacon.Builder()
                .setId1(packageUUID)
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