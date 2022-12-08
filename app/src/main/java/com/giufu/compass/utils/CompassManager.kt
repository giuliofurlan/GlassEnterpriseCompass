package com.giufu.compass.utils

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import com.giufu.compass.model.*

class CompassManager(sensorManager: SensorManager, compassListener: CompassListener): SensorEventListener {
    private val sensorManager: SensorManager
    private val compassListener: CompassListener?
    private val observableSensorAccuracy = ObservableSensorAccuracy(SensorAccuracy.NO_CONTACT)


    init {
        this.sensorManager = sensorManager
        this.compassListener = compassListener
    }

    fun registerSensorListener() {
        val rotationVectorSensor: Sensor? = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR)
        if (rotationVectorSensor == null) {
            println("Rotation vector sensor not available")
            return
        }

        val success = sensorManager.registerListener(this, rotationVectorSensor,
            SensorManager.SENSOR_DELAY_FASTEST
        )
        if (!success) {
            println("Could not enable rotation vector sensor")
            return
        }
    }

    fun unregisterSensorListener() {
        sensorManager.unregisterListener(this)
    }

    override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {
        when (sensor.type) {
            Sensor.TYPE_ROTATION_VECTOR -> setSensorAccuracy(accuracy)
            else -> println("Unexpected accuracy changed event of type ${sensor.type}")
        }
    }

    private fun setSensorAccuracy(accuracy: Int) {
        val sensorAccuracy = adaptSensorAccuracy(accuracy)
        compassListener?.onAccuracyChanged(sensorAccuracy)
        setSensorAccuracy(sensorAccuracy)
    }

    private fun setSensorAccuracy(sensorAccuracy: SensorAccuracy) {
        observableSensorAccuracy.set(sensorAccuracy)
    }

    private fun adaptSensorAccuracy(accuracy: Int): SensorAccuracy {
        return when (accuracy) {
            SensorManager.SENSOR_STATUS_NO_CONTACT -> SensorAccuracy.NO_CONTACT
            SensorManager.SENSOR_STATUS_UNRELIABLE -> SensorAccuracy.UNRELIABLE
            SensorManager.SENSOR_STATUS_ACCURACY_LOW -> SensorAccuracy.LOW
            SensorManager.SENSOR_STATUS_ACCURACY_MEDIUM -> SensorAccuracy.MEDIUM
            SensorManager.SENSOR_STATUS_ACCURACY_HIGH -> SensorAccuracy.HIGH
            else -> {
                SensorAccuracy.NO_CONTACT
            }
        }
    }

    override fun onSensorChanged(event: SensorEvent) {
        when (event.sensor.type) {
            Sensor.TYPE_ROTATION_VECTOR -> updateCompass(event)
            else -> println("Unexpected sensor changed event of type ${event.sensor.type}")
        }
    }

    private fun updateCompass(event: SensorEvent) {
        val rotationVector = RotationVector(event.values[0], event.values[1], event.values[2])
        val azimuth = MathUtils.calculateAzimuth(rotationVector, DisplayRotation.ROTATION_0)
        compassListener?.onAzimuthChanged(azimuth)
    }
}

interface CompassListener {
    fun onAzimuthChanged(azimuth: Azimuth)
    fun onAccuracyChanged(sensorAccuracy: SensorAccuracy)
}