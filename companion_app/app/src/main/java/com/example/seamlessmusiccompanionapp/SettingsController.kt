package com.example.seamlessmusiccompanionapp

import android.bluetooth.le.AdvertiseSettings
import android.content.pm.PackageManager
import com.example.seamlessmusiccompanionapp.ui.main.SettingsFragment
import android.Manifest
import android.content.Context
import android.net.wifi.WifiManager
import android.util.Log

class SettingsController() {
    var data: AppSettingsData = AppSettingsDataBuilder().build()

    private var mainInstance = MainActivity.instance()!!
    private val bleController = mainInstance.bleController
    private val wifiManager = mainInstance.getSystemService(Context.WIFI_SERVICE) as WifiManager
    private val authorizedNetworks = mutableListOf<String>()
    private val spinnerIdToAdvertiseMode = mapOf(
        0 to AdvertiseSettings.ADVERTISE_MODE_LOW_POWER,
        1 to AdvertiseSettings.ADVERTISE_MODE_BALANCED,
        2 to AdvertiseSettings.ADVERTISE_MODE_LOW_LATENCY
    )

    private val spinnerIdToEmitPower = mapOf(
        0 to AdvertiseSettings.ADVERTISE_TX_POWER_ULTRA_LOW,
        1 to AdvertiseSettings.ADVERTISE_TX_POWER_LOW,
        2 to AdvertiseSettings.ADVERTISE_TX_POWER_MEDIUM,
        3 to AdvertiseSettings.ADVERTISE_TX_POWER_HIGH
    )

    companion object {
        const val MOVEMENT_CONDITION_CODE = 0
        const val NETWORK_CONDITION_CODE = 1
    }

    init {
        Log.d("proj", "authorizedNetworks: $authorizedNetworks")
    }

    fun settingsSaved(settingsFrag: SettingsFragment) {
        data = AppSettingsDataBuilder()
            .setAdvertiseMode(spinnerIdToAdvertiseMode[settingsFrag.hzSpinner.selectedItemId.toInt()]!!)
            .setAdvertiseTxPower(spinnerIdToEmitPower[settingsFrag.txSpinner.selectedItemId.toInt()]!!)
            .setMeasuredTx(Integer.parseInt(settingsFrag.measuredtext.text.toString()))
            .build()
        mainInstance.updateSettings(data)
    }

    private fun wifiAvailable(): Boolean {
        if (mainInstance.checkSelfPermission(Manifest.permission.ACCESS_WIFI_STATE) != PackageManager.PERMISSION_GRANTED) {
            return false
        }

        if (!wifiManager.isWifiEnabled) {
            return false
        }

        return true
    }

    fun authorizeNetwork(): Boolean {
        if (!wifiAvailable()) {
            return false
        }

        val curWifi = wifiManager.connectionInfo

        if (authorizedNetworks.contains(curWifi.ssid)) {
            return false
        }

        authorizedNetworks.add(curWifi.ssid)

        return true
    }

    fun onAuthorizedNetwork(): Boolean {
        if (!wifiAvailable()) {
            return false
        }

        val curWifi = wifiManager.connectionInfo
        return authorizedNetworks.contains(curWifi.ssid)
    }

    fun setConditionEnabled(code: Int, isEnabled: Boolean) {
        val cond = bleController.conditions[code]
        cond.setEnabled(isEnabled)
    }
}

class AppSettingsData(val advertiseMode: Int, val advertiseTxPower: Int, val measuredTx: Int) {}

private class AppSettingsDataBuilder {
    private var advertiseMode = AdvertiseSettings.ADVERTISE_MODE_BALANCED
    private var advertiseTxPower = AdvertiseSettings.ADVERTISE_TX_POWER_MEDIUM
    private var measuredTx = -59

    fun setAdvertiseMode(mode: Int): AppSettingsDataBuilder {
        advertiseMode = mode
        return this
    }

    fun setAdvertiseTxPower(power: Int): AppSettingsDataBuilder {
        advertiseTxPower = power
        return this
    }

    fun setMeasuredTx(tx: Int): AppSettingsDataBuilder {
        measuredTx = tx
        return this
    }

    fun build(): AppSettingsData {
        return AppSettingsData(advertiseMode, advertiseTxPower, measuredTx)
    }
}
