package com.wahyuu.japren

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import org.osmdroid.config.Configuration
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import java.text.SimpleDateFormat
import java.util.*

class MapsPresensiActivity : AppCompatActivity() {

    private lateinit var map: MapView
    private lateinit var txtTime: TextView

    private val kampusLat = -7.797068   // Jogja
    private val kampusLng = 110.370529  // Jogja
    private val radiusMeter = 100.0
    private val handler = Handler(Looper.getMainLooper())

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val fine = permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false
        val coarse = permissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false

        if (fine || coarse) {
            setupMap()
        } else {
            Toast.makeText(this, "Izin lokasi ditolak", Toast.LENGTH_SHORT).show()
            setupMap() // map tetap tampil
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Configuration.getInstance().userAgentValue = packageName

        setContentView(R.layout.activity_maps_presensi)

        // Init View
        map = findViewById(R.id.map)
        txtTime = findViewById(R.id.txtTime)

        map.setMultiTouchControls(true)

        startClock()

        // Cek permission lokasi
        if (
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        } else {
            setupMap()
        }
    }

    private fun setupMap() {
        val kampusPoint = GeoPoint(kampusLat, kampusLng)

        map.controller.setZoom(16.0)
        map.controller.setCenter(kampusPoint)

        // Marker Kampus
        val marker = Marker(map)
        marker.position = kampusPoint
        marker.title = "Lokasi Kampus"
        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
        map.overlays.add(marker)
    }

    private fun startClock() {
        handler.post(object : Runnable {
            override fun run() {
                val time = SimpleDateFormat(
                    "HH:mm:ss 'WIB'",
                    Locale.getDefault()
                ).format(Date())
                txtTime.text = time
                handler.postDelayed(this, 1000)
            }
        })
    }

    override fun onResume() {
        super.onResume()
        map.onResume()
    }

    override fun onPause() {
        super.onPause()
        map.onPause()
    }
}
