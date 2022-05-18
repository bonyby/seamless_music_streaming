package com.example.seamlessmusiccompanionapp.conditions

class SwitchableCondition : Condition {
    private var permitted = true

    override fun met(): Boolean {
        return permitted
    }

    fun setPermitted(state: Boolean) {
        permitted = state
    }
}