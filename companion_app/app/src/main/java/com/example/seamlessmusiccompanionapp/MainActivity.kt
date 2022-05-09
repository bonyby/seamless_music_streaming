package com.example.seamlessmusiccompanionapp

import android.Manifest
import android.R
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.example.seamlessmusiccompanionapp.databinding.ActivityMainBinding
import com.example.seamlessmusiccompanionapp.ui.main.SectionsPagerAdapter
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    companion object {
        private const val BLUETOOTH_PERMISSION_CODE1 = 100
        private const val BLUETOOTH_PERMISSION_CODE2 = 101
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
//        val fab: FloatingActionButton = binding.fab
//
//        fab.setOnClickListener { view ->
//            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                .setAction("Action", null).show()
//        }
        val bleController = BLEController(this)

        val permissions1 = arrayOf(
            Manifest.permission.BLUETOOTH,
            Manifest.permission.BLUETOOTH_ADMIN,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
        if (checkSelfPermission(Manifest.permission.BLUETOOTH_ADMIN) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(permissions1, BLUETOOTH_PERMISSION_CODE1)
        } else {
            bleController.emit()
        }

//        if (checkSelfPermission(Manifest.permission.BLUETOOTH) == PackageManager.PERMISSION_GRANTED) {
//            requestPermissions(permissions2, BLUETOOTH_PERMISSION_CODE2)
//        }

//        if (shouldShowRequestPermissionRationale(Manifest.permission.BLUETOOTH_SCAN)) {
//            showExplanation("Permission needed", "give me bluetooth permission", Manifest.permission.BLUETOOTH_SCAN, BLUETOOTH_PERMISSION_CODE)
//        } else {
//            requestPermissions(permissions, BLUETOOTH_PERMISSION_CODE)
//        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//        val snackbar = Snackbar.make(binding.root, "requestCode: $requestCode", 5000)
//        snackbar.show()
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
        if (requestCode == BLUETOOTH_PERMISSION_CODE1) {
            Log.d("proj", "a bluetooth request")
        }
    }
}