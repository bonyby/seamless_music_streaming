package com.example.seamlessmusiccompanionapp.conditions

import android.Manifest
import android.content.pm.PackageManager
import com.example.seamlessmusiccompanionapp.MainActivity

class AuthorizedNetworkCondition : Condition {
    private val mainInstance = MainActivity.instance()!!

    companion object {
        const val ACCESS_WIFI_STATE_CODE = 169
    }

    override fun met(): Boolean {
        ensurePermissions()

        return onAuthorizedNetwork()
    }

    private fun onAuthorizedNetwork(): Boolean {
        return true
    }

    private fun ensurePermissions() {
        if (mainInstance.checkSelfPermission(Manifest.permission.ACCESS_WIFI_STATE) != PackageManager.PERMISSION_GRANTED) {
            mainInstance.requestPermissions(arrayOf(Manifest.permission.ACCESS_WIFI_STATE), ACCESS_WIFI_STATE_CODE)
        }
    }
}