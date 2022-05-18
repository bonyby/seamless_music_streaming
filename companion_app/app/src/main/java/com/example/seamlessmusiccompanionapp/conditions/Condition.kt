package com.example.seamlessmusiccompanionapp.conditions

interface Condition {
    fun met(): Boolean
    fun setEnabled(state: Boolean)
}