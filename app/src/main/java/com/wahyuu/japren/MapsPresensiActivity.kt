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
import com.google.android.gms.location.*
import com.google.android.material.card.MaterialCardView
import com.google.firebase.database.FirebaseDatabase
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import java.text.SimpleDateFormat
import java.util.*

class MapsPresensiActivity : AppCompatActivity() {

    private lateinit var map: MapView
    private lateinit var txtTime: TextView
    private lateinit var cardStatus: MaterialCardView
    private lateinit var tvStatus: TextView

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    // Lokasi Kampus & Parameter Radius
    private val kampusLat = -7.74175
    private val kampusLng = 110.26378
    private val radiusMeter = 500.0
    private var isInsideRadius = false

    private val handler = Handler(Looper.getMainLooper())

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        if (permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true || permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true) {
            setupMap()
            startLocationUpdates()
        } else {
            Toast.makeText(this, "Izin lokasi ditolak, radius tidak aktif", Toast.LENGTH_LONG).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Konfigurasi OSMDroid
        Configuration.getInstance().apply {
            userAgentValue = packageName
            osmdroidBasePath = filesDir
            osmdroidTileCache = cacheDir
        }

        setContentView(R.layout.activity_maps_presensi)

        // Inisialisasi View
        map = findViewById(R.id.map)
        txtTime = findViewById(R.id.txtTime)
        cardStatus = findViewById(R.id.cardStatus)
        tvStatus = cardStatus.getChildAt(0) as TextView

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        map.setMultiTouchControls(true)
        map.setTileSource(TileSourceFactory.MAPNIK)

        startClock()
        checkPermissionAndSetup()
    }

    private fun checkPermissionAndSetup() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            setupMap()
            startLocationUpdates()
        } else {
            requestPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    private fun setupMap() {
        val kampusPoint = GeoPoint(kampusLat, kampusLng)

        map.controller.apply {
            setZoom(17.0)
            setCenter(kampusPoint)
        }

        Marker(map).apply {
            position = kampusPoint
            title = "Lokasi Kampus"
            setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
            map.overlays.add(this)
        }

        map.invalidate()
    }

    private fun startLocationUpdates() {
        val locationRequest = LocationRequest.Builder(
            Priority.PRIORITY_HIGH_ACCURACY,
            5000
        ).build()

        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.getMainLooper()
            )
        }
    }

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(result: LocationResult) {
            val loc = result.lastLocation ?: return
            val distanceResult = FloatArray(1)
            android.location.Location.distanceBetween(
                loc.latitude,
                loc.longitude,
                kampusLat,
                kampusLng,
                distanceResult
            )
            updateUIStatus(distanceResult[0] <= radiusMeter)
        }
    }

    private fun updateUIStatus(isInside: Boolean) {
        isInsideRadius = isInside
        if (isInside) {
            tvStatus.text = "Klik di sini untuk Presensi"
            cardStatus.setCardBackgroundColor(ContextCompat.getColor(this, R.color.sky_blue))
            cardStatus.setOnClickListener {
                savePresensiToFirebase()
            }
        } else {
            tvStatus.text = "Anda berada di luar area presensi"
            cardStatus.setCardBackgroundColor(ContextCompat.getColor(this, android.R.color.darker_gray))
            cardStatus.setOnClickListener {
                Toast.makeText(this, "Anda masih di luar radius kampus", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun savePresensiToFirebase() {

        // Ambil nama matkul dari Intent
        val matkulDariIntent = intent.getStringExtra("EXTRA_MATKUL") ?: "Umum"

        val prefs = getSharedPreferences("USER_PREF", MODE_PRIVATE)
        val nimUser = prefs.getString("NIM", "00000000")

        val database = FirebaseDatabase.getInstance(
            "https://japren-749fa-default-rtdb.asia-southeast1.firebasedatabase.app/"
        )
        val ref = database.getReference("presensi_log")

        val presensiBaru = Presensi(
            userId = nimUser,
            namaMatkul = matkulDariIntent,   // ✅ tambahan
            status = "Hadir",
            timestamp = System.currentTimeMillis(),
            date = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date()),
            time = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())
        )

        ref.push().setValue(presensiBaru)
            .addOnSuccessListener {
                Toast.makeText(
                    this,
                    "Presensi $matkulDariIntent Berhasil!",
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Gagal: ${e.message}", Toast.LENGTH_LONG).show()
            }
    }

    private fun startClock() {
        handler.post(object : Runnable {
            override fun run() {
                txtTime.text = SimpleDateFormat("HH:mm:ss 'WIB'", Locale.getDefault()).format(Date())
                handler.postDelayed(this, 1000)
            }
        })
    }

    override fun onResume() {
        super.onResume()
        map.onResume()
        startLocationUpdates()
    }

    override fun onPause() {
        super.onPause()
        map.onPause()
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacksAndMessages(null)
    }
}