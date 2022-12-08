package com.giufu.compass.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.giufu.compass.R

enum class SensorAccuracy(@StringRes val textResourceId: Int) {
    NO_CONTACT(R.string.sensor_accuracy_no_contact),
    UNRELIABLE(R.string.sensor_accuracy_unreliable),
    LOW(R.string.sensor_accuracy_low),
    MEDIUM(R.string.sensor_accuracy_medium),
    HIGH(R.string.sensor_accuracy_high)
}
