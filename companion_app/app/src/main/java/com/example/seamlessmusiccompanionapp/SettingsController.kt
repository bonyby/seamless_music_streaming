package com.example.seamlessmusiccompanionapp

import android.bluetooth.le.AdvertiseSettings
import com.example.seamlessmusiccompanionapp.ui.main.SettingsFragment

class SettingsController() {
    var data: AppSettingsData = AppSettingsDataBuilder().build()

    private var mainInstance = MainActivity.instance()!!
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

    fun settingsSaved(settingsFrag: SettingsFragment) {
        data = AppSettingsDataBuilder()
            .setAdvertiseMode(spinnerIdToAdvertiseMode[settingsFrag.hzSpinner.selectedItemId.toInt()]!!)
            .setAdvertiseTxPower(spinnerIdToEmitPower[settingsFrag.txSpinner.selectedItemId.toInt()]!!)
            .setMeasuredTx(Integer.parseInt(settingsFrag.measuredtext.text.toString()))
            .build()
        mainInstance.updateSettings(data)
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
