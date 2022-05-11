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

class BLEController(private val context: Context) : MonitorNotifier{
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

//    fun emit(): Boolean {
//        if (ActivityCompat.checkSelfPermission(
//                context,
//                Manifest.permission.BLUETOOTH_ADMIN
//            ) == PackageManager.PERMISSION_GRANTED
//        ) {
//            Log.d("proj", "not callback, but we have bluetooth permissions!")
//            val adapter = BluetoothAdapter.getDefaultAdapter()
//            Log.d("proj", "bonded devices: ${adapter.name}")
//            val bleAdvertiser = adapter.bluetoothLeAdvertiser
////            Log.d("proj", "adapter address: ${bleAdvertiser.)}")
//            handler.postDelayed({
//                bleAdvertiser.stopAdvertising(bleCallback)
//            }, 60000)
//            bleAdvertiser.startAdvertising(advertiseSettings, advertiseData, bleCallback)
//            Log.d("proj", "data: ${advertiseData.serviceData}")
//        } else {
//            Log.d("proj", "no bluetooth permissions")
//            return false
//        }
//
//        return true
//    }

    fun emit(): Boolean {
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.BLUETOOTH_ADMIN
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            val beacon = Beacon.Builder()
                .setId1("13374754-cf6d-4a0f-adf2-f4911ba9ffa6")
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

//    fun emit(): Boolean {
//        val beaconManager =  BeaconManager.getInstanceForApplication(context)
//        val region = Region("all-beacons-region", null, null, null)
//        // Set up a Live Data observer so this Activity can get monitoring callbacks
//        // observer will be called each time the monitored regionState changes (inside vs. outside region)
////        beaconManager.getRegionViewModel(region).regionState.observe(this, monitoringObserver)
//        beaconManager.addMonitorNotifier(this)
//        beaconManager.startMonitoring(region)
//
//        return true
//    }
//
//    val monitoringObserver = Observer { state, _ ->
//        Log.d("proj", "State?: $state")
//    //        if (state == MonitorNotifier.INSIDE) {
////            Log.d("proj", "Detected beacons(s)")
////        }
////        else {
////            Log.d("proj", "Stopped detecting beacons")
////        }
//    }
//
    override fun didEnterRegion(region: Region?) {
        Log.d("proj", "didEnterRegion called")
    }

    override fun didExitRegion(region: Region?) {
        Log.d("proj", "didExitRegion called")
    }

    override fun didDetermineStateForRegion(state: Int, region: Region?) {
        Log.d("proj", "didDetermineStateForRegion called: $region")
    }
}