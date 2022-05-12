package com.example.seamlessmusiccompanionapp.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.seamlessmusiccompanionapp.MainActivity
import com.example.seamlessmusiccompanionapp.R
import com.example.seamlessmusiccompanionapp.databinding.FragmentSettingsBinding

class SettingsFragment(): Fragment() {
    private var _binding: FragmentSettingsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        val root = binding.root
        val textUuid = binding.uuidLabel
        textUuid.text = getString(R.string.uuid_present_text, MainActivity.instance()?.getPackageUUID())

        return root
    }
}