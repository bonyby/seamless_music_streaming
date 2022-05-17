package com.example.seamlessmusiccompanionapp

import android.Manifest
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_IMMUTABLE
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.example.seamlessmusiccompanionapp.databinding.ActivityMainBinding
import com.example.seamlessmusiccompanionapp.ui.main.SectionsPagerAdapter
import com.google.android.gms.location.ActivityRecognition
import com.google.android.gms.location.ActivityRecognitionClient
import com.google.android.gms.location.ActivityTransitionRequest
import com.google.android.material.tabs.TabLayout


class MainActivity : AppCompatActivity() {
    private lateinit var client: ActivityRecognitionClient
    private lateinit var binding: ActivityMainBinding
    lateinit var bleController: BLEController
    lateinit var settingsController: SettingsController
    private lateinit var activityController: ActivityController


    companion object {
        private var instance: MainActivity? = null
        private const val BLUETOOTH_PERMISSION_CODE = 100 // Arbitrary number
        const val TRANSITIONS_RECEIVER_ACTION =
            BuildConfig.APPLICATION_ID + "TRANSITIONS_RECEIVER_ACTION"
        const val ACTIVITY_RECOGNITION_CODE = 20 // Arbitrary number

        fun instance(): MainActivity? {
            return instance
        }
    }

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        instance = this
        bleController = BLEController(this)
        settingsController = SettingsController()
        client = ActivityRecognition.getClient(this)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager)
        val viewPager: ViewPager = binding.viewPager
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = binding.tabs
        tabs.setupWithViewPager(viewPager)

        val permissions1 = arrayOf(
            Manifest.permission.BLUETOOTH,
            Manifest.permission.BLUETOOTH_ADMIN,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_BACKGROUND_LOCATION,
            Manifest.permission.BLUETOOTH_SCAN,
            Manifest.permission.BLUETOOTH_ADVERTISE
        )
        if (checkSelfPermission(Manifest.permission.BLUETOOTH_ADMIN) != PackageManager.PERMISSION_GRANTED || checkSelfPermission(
                Manifest.permission.BLUETOOTH_ADVERTISE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(permissions1, BLUETOOTH_PERMISSION_CODE)
        }
    }

    override fun onStart() {
        super.onStart()

        activityController = ActivityController()
//        registerReceiver(activityController, IntentFilter(TRANSITIONS_RECEIVER_ACTION))
        val transitions = activityController.getTransitions()
        Log.d("proj", "transitions: $transitions")
        val request = ActivityTransitionRequest(transitions)

        client
            .requestActivityTransitionUpdates(request, getPendingIntent())
            .addOnSuccessListener { Log.d("proj", "Request success") }
            .addOnFailureListener { Log.d("proj", "Request failure") }
    }

    private fun getPendingIntent(): PendingIntent {
//        val intent = Intent(TRANSITIONS_RECEIVER_ACTION, null, this, ActivityController::class.java)
        val intent = Intent(this, activityController::class.java)
        return PendingIntent.getBroadcast(this, 166, intent, PendingIntent.FLAG_UPDATE_CURRENT)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//        Log.d(
//            "proj",
//            "-------\n permissions length: ${permissions.size} \n grantResults length: ${grantResults.size}"
//        )
//        for (element in permissions) {
//            Log.d("proj", "permission: $element")
//        }
//
//        for (i in grantResults.indices) {
//            Log.d("proj", "grant $i: ${grantResults[i]}")
//        }
//        if (requestCode == BLUETOOTH_PERMISSION_CODE) {
//            Log.d("proj", "a bluetooth request")
//        }
    }

    fun updateSettings(data: AppSettingsData) {
        Log.d("proj", "settings updated")
        bleController.updateAdvertiseMode(data.advertiseMode)
        bleController.updateAdvertiseTxPower(data.advertiseTxPower)
        bleController.updateMeasuredTx(data.measuredTx)
    }

    // Getters & setters
    fun getPackageUUID(): String {
        return "${
            bleController.packageUUID.replace(
                "-",
                ""
            )
        }-${bleController.major}-${bleController.minor}"
    }
}