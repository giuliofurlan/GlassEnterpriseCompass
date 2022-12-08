package com.giufu.compass

import android.hardware.SensorManager
import android.os.Bundle
import android.widget.TextView
import com.giufu.compass.model.Azimuth
import com.giufu.compass.model.SensorAccuracy
import com.giufu.compass.utils.CompassListener
import com.giufu.compass.utils.CompassManager
import com.redinput.compassview.CompassView

class MainActivity : BaseActivity(), CompassListener {

    private lateinit var compassView: CompassView
    private lateinit var accuracyTextView: TextView
    private lateinit var sensorManager: SensorManager
    private  lateinit var compassManager: CompassManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        compassManager = CompassManager(sensorManager, this)
        compassView = findViewById<CompassView>(R.id.compassView)
        accuracyTextView = findViewById<TextView>(R.id.accuracyTextView)
    }

    override fun onResume() {
        super.onResume()
        compassManager.registerSensorListener()
    }

    override fun onPause() {
        super.onPause()
        compassManager.unregisterSensorListener()
    }

    override fun onAzimuthChanged(azimuth: Azimuth) {
        compassView.setDegrees(azimuth.degrees, true)
    }

    override fun onAccuracyChanged(sensorAccuracy: SensorAccuracy) {
        accuracyTextView.text = getString(sensorAccuracy.textResourceId)
    }
}