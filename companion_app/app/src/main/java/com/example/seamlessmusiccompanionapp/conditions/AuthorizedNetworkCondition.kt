package com.example.seamlessmusiccompanionapp.conditions

import android.Manifest
import android.content.pm.PackageManager
import com.example.seamlessmusiccompanionapp.MainActivity
import com.example.seamlessmusiccompanionapp.SettingsController

class AuthorizedNetworkCondition : Condition {
    private var isEnabled = true
    private val mainInstance = MainActivity.instance()!!
    private var settingsController: SettingsController? = null

    companion object {
        const val ACCESS_WIFI_STATE_CODE = 169
    }

    override fun met(): Boolean {
        if (!isEnabled) {
            return true
        }

        ensurePermissions()

        return onAuthorizedNetwork()
    }

    override fun setEnabled(state: Boolean) {
        isEnabled = state
    }

    private fun onAuthorizedNetwork(): Boolean {
        if (settingsController == null) {
            settingsController = mainInstance.settingsController
        }

        return settingsController!!.onAuthorizedNetwork()
    }

    private fun ensurePermissions() {
        if (mainInstance.checkSelfPermission(Manifest.permission.ACCESS_WIFI_STATE) != PackageManager.PERMISSION_GRANTED) {
            mainInstance.requestPermissions(
                arrayOf(Manifest.permission.ACCESS_WIFI_STATE),
                ACCESS_WIFI_STATE_CODE
            )
        }
    }
}