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

    fun settingsSaved(settingsFrag: SettingsFragment) {
        data = AppSettingsDataBuilder()
            .setAdvertiseMode(spinnerIdToAdvertiseMode[settingsFrag.hzSpinner.selectedItemId.toInt()]!!)
            .build()
        mainInstance.updateSettings(data)
    }
}

class AppSettingsData(val advertiseMode: Int) {}

private class AppSettingsDataBuilder {
    private var advertiseMode = AdvertiseSettings.ADVERTISE_MODE_BALANCED

    fun setAdvertiseMode(mode: Int): AppSettingsDataBuilder {
        advertiseMode = mode
        return this
    }

    fun build(): AppSettingsData {
        return AppSettingsData(advertiseMode)
    }
}
