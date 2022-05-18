package com.example.seamlessmusiccompanionapp.ui.main

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.seamlessmusiccompanionapp.MainActivity
import com.example.seamlessmusiccompanionapp.R
import com.example.seamlessmusiccompanionapp.databinding.FragmentSettingsBinding

class SettingsFragment() : Fragment() {
    private var _binding: FragmentSettingsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    lateinit var hzSpinner: Spinner
    lateinit var txSpinner: Spinner
    lateinit var measuredtext: EditText

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        val root = binding.root
        val mainInstance = MainActivity.instance()!!
        val settingsController = mainInstance.settingsController
        val bleController = mainInstance.bleController

        val saveButton = binding.saveSettingsButton
        saveButton.setOnClickListener { settingsController.settingsSaved(this) }

        //// Initial setup of view
        val textUuid = binding.uuidLabel
        textUuid.text = getString(R.string.uuid_present_text, mainInstance.getPackageUUID())

        // Advertise mode
        hzSpinner = binding.emitHzSpinner
        ArrayAdapter.createFromResource(mainInstance, R.array.emit_hz_array, android.R.layout.simple_spinner_item)
            .also { adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                hzSpinner.adapter = adapter
            }
        hzSpinner.setSelection(bleController.getAdvertiseMode()) // advertise mode is int in {0,1,2} so matches with index

        // Emission power
        txSpinner = binding.emitTxPowerSpinner
        ArrayAdapter.createFromResource(mainInstance, R.array.emit_tx_power_array, android.R.layout.simple_spinner_item)
            .also { adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                txSpinner.adapter = adapter
            }
        txSpinner.setSelection(bleController.getAdvertiseTxPower()) // advertise tx power is int in {0,1,2,3} so matches with index

        // Measured tx
        measuredtext = binding.measuredPowerEditText
        measuredtext.setText(bleController.beaconTx.toString())

        // Authorize Network button
        val authButton = binding.authorizeButton
        authButton.setOnClickListener {
            val result = settingsController.authorizeNetwork()
            Toast.makeText(mainInstance, if (result) "Network Authorized" else "Could not authorize. Network might already be authorized", Toast.LENGTH_LONG).show()
        }

        return root
    }
}