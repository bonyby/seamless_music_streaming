package com.example.seamlessmusiccompanionapp

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.example.seamlessmusiccompanionapp.databinding.ActivityMainBinding
import com.example.seamlessmusiccompanionapp.ui.main.SectionsPagerAdapter
import com.google.android.material.tabs.TabLayout
import java.util.*


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    companion object {
        private const val BLUETOOTH_PERMISSION_CODE = 100
    }

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager)
        val viewPager: ViewPager = binding.viewPager
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = binding.tabs
        tabs.setupWithViewPager(viewPager)

        val bleController = BLEController(this)

        val permissions1 = arrayOf(
            Manifest.permission.BLUETOOTH,
            Manifest.permission.BLUETOOTH_ADMIN,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_BACKGROUND_LOCATION
        )
        if (checkSelfPermission(Manifest.permission.BLUETOOTH_ADMIN) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(permissions1, BLUETOOTH_PERMISSION_CODE)
        } else {
            bleController.emit()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        Log.d(
            "proj",
            "-------\n permissions length: ${permissions.size} \n grantResults length: ${grantResults.size}"
        )
        for (element in permissions) {
            Log.d("proj", "permission: $element")
        }

        for (i in grantResults.indices) {
            Log.d("proj", "grant $i: ${grantResults[i]}")
        }
        if (requestCode == BLUETOOTH_PERMISSION_CODE) {
            Log.d("proj", "a bluetooth request")
        }
    }
}