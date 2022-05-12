package com.example.seamlessmusiccompanionapp.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        val root = binding.root
        val mainInstance = MainActivity.instance()!!
        val settingsController = mainInstance.settingsController

        val saveButton = binding.saveSettingsButton
        saveButton.setOnClickListener { settingsController.settingsSaved(this) }

        // Initial setup of view
        val textUuid = binding.uuidLabel
        textUuid.text = getString(R.string.uuid_present_text, mainInstance.getPackageUUID())

        hzSpinner = binding.emitHzSpinner
        ArrayAdapter.createFromResource(mainInstance, R.array.emit_hz_array, android.R.layout.simple_spinner_item)
            .also { adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                hzSpinner.adapter = adapter
            }

        return root
    }
}