package com.example.seamlessmusiccompanionapp.conditions

class SwitchableCondition : Condition {
    private var isEnabled = true

    override fun met(): Boolean {
        return isEnabled
    }

    override fun setEnabled(state: Boolean) {
        isEnabled = state
    }
}