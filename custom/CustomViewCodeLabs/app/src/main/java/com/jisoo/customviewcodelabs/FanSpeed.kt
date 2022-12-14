package com.jisoo.customviewcodelabs

enum class FanSpeed(val label: Int)
{
    OFF(R.string.fan_off),
    LOW(R.string.fan_low),
    MEDIUM(R.string.fan_medium),
    HIGH(R.string.fan_high);


    fun next() = when(this) {
        OFF -> LOW
        LOW -> MEDIUM
        MEDIUM -> HIGH
        HIGH -> OFF
    }
}

const val RADIUS_OFFSET_LABEL = 30
const val RADIUS_OFFSET_INDICATOR = -35
