package com.example.seamlessmusiccompanionapp

import android.Manifest
import android.app.Activity
import android.bluetooth.le.AdvertiseSettings
import android.content.Context
import android.content.pm.PackageManager
import android.os.Handler
import android.util.Log
import androidx.core.app.ActivityCompat
import com.example.seamlessmusiccompanionapp.conditions.AuthorizedNetworkCondition
import com.example.seamlessmusiccompanionapp.conditions.Condition
import com.example.seamlessmusiccompanionapp.conditions.MovementCondition
import com.example.seamlessmusiccompanionapp.conditions.SwitchableCondition
import org.altbeacon.beacon.Beacon
import org.altbeacon.beacon.BeaconParser
import org.altbeacon.beacon.BeaconTransmitter
import java.util.*
import kotlin.properties.Delegates

class BLEController(private val context: Activity) {
    var packageUUID: String = ""
    var major: String = "2"
    var minor: String = "1"
    var beaconTx: Int = -59
    var emittingListeners = arrayListOf<(Boolean) -> Unit>()
    var emitting: Boolean by Delegates.observable(false) { _, oldValue, newValue ->
        if (oldValue == newValue) {
            return@observable
        }
        emittingListeners.forEach {
            it(newValue)
        }
    }
        private set

    val switchableCondition = SwitchableCondition()
    private val authorizedNetworkCondition = AuthorizedNetworkCondition()
    private val conditions: Array<Condition> =
        arrayOf(MovementCondition(), switchableCondition, authorizedNetworkCondition)
    private val bleCallback = BLECallback()
    private lateinit var beacon: Beacon
    private val beaconParser =
        BeaconParser().setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24")
    private val beaconTransmitter = BeaconTransmitter(context, beaconParser)
    private val sharedPref = context.getPreferences(Context.MODE_PRIVATE)

    companion object {
        private const val ADVERTISE_MODE_STRING = "advertiseMode"
        private const val ADVERTISE_TX_POWER_STRING = "advertiseTxPower"
        private const val BEACON_MEASURED_TX = "measuredTx"
        private const val PACKAGE_UUID_STRING = "packageUUID"
        private const val REFRESH_FREQUENCY = 1000L
    }

    init {
        // Setup transmitter and beacon settings from sharedPref
        // If no settings exist, use default values
        beaconTransmitter.advertiseMode =
            sharedPref.getInt(ADVERTISE_MODE_STRING, AdvertiseSettings.ADVERTISE_MODE_BALANCED)
        beaconTransmitter.advertiseTxPowerLevel = sharedPref.getInt(
            ADVERTISE_TX_POWER_STRING,
            AdvertiseSettings.ADVERTISE_TX_POWER_MEDIUM
        )
        beaconTx = sharedPref.getInt(BEACON_MEASURED_TX, beaconTx)

        // Fetch packageUUID if present. Generate and save new otherwise
//        sharedPref.edit().remove(PACKAGE_UUID_STRING).apply()
        if (!sharedPref.contains(PACKAGE_UUID_STRING)) {
            packageUUID = UUID.randomUUID().toString()
            with(sharedPref.edit()) {
                putString(PACKAGE_UUID_STRING, packageUUID)
                apply()
            }
        } else {
            packageUUID = sharedPref.getString(PACKAGE_UUID_STRING, null).toString()
        }

        // Initialize beacon
        createBeacon()

        // Create an infinite loop of function calls
        // at fixed interval to check if advertising
        // is permitted or not
        val handler = Handler()
        handler.post(object : Runnable {
            override fun run() {
                update()
                handler.postDelayed(this, REFRESH_FREQUENCY)
            }
        })
    }

    private fun createBeacon() {
        beacon = Beacon.Builder()
            .setId1(packageUUID)
            .setId2(major)
            .setId3(minor)
            .setManufacturer(0x004c)
            .setTxPower(beaconTx)
            .build()
    }

    private fun update() {
        val allowed = checkConditions()

        emitting = if (allowed) emit() else false
    }

    private fun checkConditions(): Boolean {
        var allMet = true
        for (i in conditions.indices) {
            if (!conditions[i].met()) {
                allMet = false
                break
            }
        }

        return allMet
    }

    private fun emit(): Boolean {
        if (beaconTransmitter.isStarted) {
            return true
        }

        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.BLUETOOTH_ADMIN
            ) == PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.BLUETOOTH_ADVERTISE
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            Log.d("proj", "Beacon measured Tx: ${beacon.txPower}")
            beaconTransmitter.startAdvertising(beacon, bleCallback)
        } else {
            return false
        }

        return true
    }

    fun updateAdvertiseMode(mode: Int) {
        if (beaconTransmitter.isStarted) {
            beaconTransmitter.stopAdvertising()
        }
        beaconTransmitter.advertiseMode = mode

        sharedPref.edit().putInt(ADVERTISE_MODE_STRING, mode).apply()
    }

    fun updateAdvertiseTxPower(power: Int) {
        if (beaconTransmitter.isStarted) {
            beaconTransmitter.stopAdvertising()
        }
        beaconTransmitter.advertiseTxPowerLevel = power

        sharedPref.edit().putInt(ADVERTISE_TX_POWER_STRING, power).apply()
    }

    fun updateMeasuredTx(tx: Int) {
        if (beaconTransmitter.isStarted) {
            beaconTransmitter.stopAdvertising()
        }

        beaconTx = tx
        createBeacon()

        sharedPref.edit().putInt(BEACON_MEASURED_TX, tx).apply()
    }

    fun getAdvertiseTxPower(): Int {
        return beaconTransmitter.advertiseTxPowerLevel
    }

    fun getAdvertiseMode(): Int {
        return beaconTransmitter.advertiseMode
    }
}