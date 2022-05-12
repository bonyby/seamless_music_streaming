package com.example.seamlessmusiccompanionapp

import android.Manifest
import android.app.Activity
import android.bluetooth.le.AdvertiseSettings
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.ActivityCompat
import org.altbeacon.beacon.Beacon
import org.altbeacon.beacon.BeaconParser
import org.altbeacon.beacon.BeaconTransmitter
import java.util.*

class BLEController(private val context: Activity){
    var packageUUID: String

    private val bleCallback = BLECallback()
    private var advertiseMode = AdvertiseSettings.ADVERTISE_MODE_BALANCED
    private lateinit var beacon: Beacon
    private val beaconParser = BeaconParser().setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24")
    private val beaconTransmitter = BeaconTransmitter(context, beaconParser)


    companion object {
        private const val PACKAGE_UUID_STRING = "packageUUID"
    }

    init {
        beaconTransmitter.advertiseMode = advertiseMode

        // Fetch packageUUID if present. Generate and save new otherwise
        val sharedPref = context.getPreferences(Context.MODE_PRIVATE)
        if(!sharedPref.contains(PACKAGE_UUID_STRING)) {
            packageUUID = UUID.randomUUID().toString()
            with(sharedPref.edit()) {
                putString(PACKAGE_UUID_STRING, packageUUID)
                apply()
            }
        } else {
            packageUUID = sharedPref.getString(PACKAGE_UUID_STRING, null).toString()
        }

        // Initialize beacon
        beacon = Beacon.Builder()
            .setId1(packageUUID)
            .setId2("1")
            .setId3("2")
            .setManufacturer(0x004c)
            .setTxPower(-59)
            .build()
    }

    fun emit(): Boolean {
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.BLUETOOTH_ADMIN
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            beaconTransmitter.startAdvertising(beacon, bleCallback)
        } else {
            return false
        }

        return true
    }

    fun updateAdvertiseMode(mode: Int) {
        beaconTransmitter.stopAdvertising()
        beaconTransmitter.advertiseMode = mode

        emit()
    }
}