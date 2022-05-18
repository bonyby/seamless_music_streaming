package com.example.seamlessmusiccompanionapp.ui.main

import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import android.widget.TextView
import androidx.appcompat.widget.SwitchCompat
import androidx.fragment.app.Fragment
import com.example.seamlessmusiccompanionapp.BLEController
import com.example.seamlessmusiccompanionapp.MainActivity
import com.example.seamlessmusiccompanionapp.conditions.SwitchableCondition
import com.example.seamlessmusiccompanionapp.databinding.FragmentMainBinding
import org.w3c.dom.Text
import kotlin.properties.Delegates

class MainFragment() : Fragment() {
    private var _binding: FragmentMainBinding? = null

    companion object {
        private const val EMITTING_BACKGROUND_COLOR = "#FF86E675"
        private const val EMITTING_TEXT_COLOR = "#005E00"
        private const val NOT_EMITTING_BACKGROUND_COLOR = "#FFE16D6D"
        private const val NOT_EMITTING_TEXT_COLOR = "#FF4E0808"
    }

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    lateinit var textMain: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        val root = binding.root
        textMain = binding.mainLabel

        val mainInstance = MainActivity.instance()!!
        val bleController = mainInstance.bleController
        val switchableCondition: SwitchableCondition? = bleController.switchableCondition
        bleController.emittingListeners.add { state -> updateEmittingVisuals(state) }
        updateEmittingVisuals(bleController.emitting) // ensure that the visuals are created no matter what

        val emittingSwitch = binding.emittingSwitch
        emittingSwitch.isChecked = true
        emittingSwitch.setOnCheckedChangeListener { _, state ->
            switchableCondition?.setEnabled(
                state
            )
        }

        return root
    }

    private fun updateEmittingVisuals(state: Boolean) {
        Log.d("proj", "update emitting visuals called. State: $state")
        textMain.text = if (state) "EMITTING" else "NOT EMITTING"
        textMain.setTextColor(Color.parseColor(if (state) EMITTING_TEXT_COLOR else NOT_EMITTING_TEXT_COLOR))
        textMain.setBackgroundColor(Color.parseColor(if (state) EMITTING_BACKGROUND_COLOR else NOT_EMITTING_BACKGROUND_COLOR))
    }
}